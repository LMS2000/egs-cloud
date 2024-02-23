package com.lms.sqlfather.service.impl.facade;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lms.contants.HttpCode;
import com.lms.lmscommon.common.DeleteRequest;
import com.lms.lmscommon.constant.UserConstant;
import com.lms.lmscommon.meta.Meta;
import com.lms.lmscommon.model.dto.generator.GeneratorEditRequest;
import com.lms.lmscommon.model.dto.generator.GeneratorQueryRequest;
import com.lms.lmscommon.model.entity.Generator;
import com.lms.lmscommon.model.entity.User;
import com.lms.lmscommon.model.vo.generator.GeneratorVO;
import com.lms.lmscommon.model.vo.user.UserVO;
import com.lms.redis.RedisCache;
import com.lms.lmscommon.common.BusinessException;
import com.lms.sqlfather.service.GeneratorService;
import com.lms.sqlfather.service.GeneratorServiceFacade;
import com.lms.sqlfather.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GeneratorServiceFacadeImpl implements GeneratorServiceFacade {


    private final GeneratorService generatorService;
    private final UserService userService;
    private final RedisCache redisCache;

    @Autowired
    public GeneratorServiceFacadeImpl(GeneratorService generatorService, @Qualifier("userServiceImpl") UserService userService, RedisCache redisCache) {
        this.generatorService = generatorService;
        this.userService = userService;
        this.redisCache = redisCache;
    }

    @Override
    public Boolean deleteGenerator(DeleteRequest deleteRequest, Long uid) {
        Long id = deleteRequest.getId();
        User loginUser = userService.getById(uid);
        // 判断是否存在
        Generator oldGenerator = generatorService.getById(id);
        BusinessException.throwIf(oldGenerator == null, HttpCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldGenerator.getUserId().equals(uid) && !UserConstant.ADMIN_ROLE.equals(loginUser.getUserRole())) {
            throw new BusinessException(HttpCode.NO_AUTH_ERROR);
        }
        return generatorService.removeById(id);
    }

    @Override
    public GeneratorVO getGeneratorVO(Generator generator, Long userId) {
        GeneratorVO generatorVO = GeneratorVO.objToVo(generator);
        // 1. 关联查询用户信息
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        generatorVO.setUser(userVO);
        return generatorVO;
    }

    @Override
    public Page<GeneratorVO> getGeneratorVOPage(Page<Generator> generatorPage) {
        List<Generator> generatorList = generatorPage.getRecords();
        Page<GeneratorVO> generatorVOPage = new Page<>(generatorPage.getCurrent(), generatorPage.getSize(), generatorPage.getTotal());
        if (CollUtil.isEmpty(generatorList)) {
            return generatorVOPage;
        }
        // 1. 关联查询用户信息
        Set<Long> userIdSet = generatorList.stream().map(Generator::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 填充信息
        List<GeneratorVO> generatorVOList = generatorList.stream().map(generator -> {
            GeneratorVO generatorVO = GeneratorVO.objToVo(generator);
            Long userId = generator.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            generatorVO.setUser(userService.getUserVO(user));
            return generatorVO;
        }).collect(Collectors.toList());
        generatorVOPage.setRecords(generatorVOList);
        return generatorVOPage;
    }

    @Override
    public Page<GeneratorVO> listGeneratorVOByPage(GeneratorQueryRequest generatorQueryRequest) {
        long current = generatorQueryRequest.getCurrent();
        long size = generatorQueryRequest.getPageSize();
        // 限制爬虫
        BusinessException.throwIf(size>20,HttpCode.PARAMS_ERROR);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Page<Generator> generatorPage = generatorService.page(new Page<>(current, size),
                generatorService.getQueryWrapper(generatorQueryRequest));
        stopWatch.stop();
        log.info("查询生成器：" + stopWatch.getTotalTimeMillis());
        stopWatch = new StopWatch();
        stopWatch.start();
        Page<GeneratorVO> generatorVOPage = this.getGeneratorVOPage(generatorPage);
        stopWatch.stop();
        log.info("查询关联数据：" + stopWatch.getTotalTimeMillis());
        return generatorVOPage;
    }

    @Override
    public Page<GeneratorVO> listGeneratorVOByPageFast(GeneratorQueryRequest generatorQueryRequest) {
        long current = generatorQueryRequest.getCurrent();
        long size = generatorQueryRequest.getPageSize();
        // 优先从缓存读取
//        String cacheKey = getPageCacheKey(generatorQueryRequest);
//        Object cacheValue = redisCache.getCacheObject(cacheKey);
//        if (cacheValue != null) {
//            return (Page<GeneratorVO>) cacheValue;
//        }

        // 限制爬虫
        BusinessException.throwIf(size>20,HttpCode.PARAMS_ERROR);
        QueryWrapper<Generator> queryWrapper = generatorService.getQueryWrapper(generatorQueryRequest);
        queryWrapper.select("id",
                "name",
                "description",
                "tags",
                "picture",
                "status",
                "user_id",
                "create_time",
                "update_time"
        );
        Page<Generator> generatorPage = generatorService.page(new Page<>(current, size), queryWrapper);
        Page<GeneratorVO> generatorVOPage = this.getGeneratorVOPage(generatorPage);
        // 写入缓存
//        redisCache.setCacheObject(cacheKey, generatorVOPage);
        return generatorVOPage;
    }

    @Override
    public Page<GeneratorVO> listMyGeneratorVOByPage(GeneratorQueryRequest generatorQueryRequest) {
        Long loginId = Long.parseLong((String) StpUtil.getLoginId());
        User loginUser = userService.getById(loginId);
        generatorQueryRequest.setUserId(loginUser.getId());
        long current = generatorQueryRequest.getCurrent();
        long size = generatorQueryRequest.getPageSize();
        // 限制爬虫
        BusinessException.throwIf(size>20,HttpCode.PARAMS_ERROR);
        Page<Generator> generatorPage = generatorService.page(new Page<>(current, size),
                generatorService.getQueryWrapper(generatorQueryRequest));
        return this.getGeneratorVOPage(generatorPage);
    }

    @Override
    public Boolean editGenerator(GeneratorEditRequest generatorEditRequest) {
        Generator generator = new Generator();
        BeanUtils.copyProperties(generatorEditRequest, generator);
        List<String> tags = generatorEditRequest.getTags();
        generator.setTags(JSONUtil.toJsonStr(tags));
        Meta.FileConfig fileConfig = generatorEditRequest.getFileConfig();
        generator.setFileConfig(JSONUtil.toJsonStr(fileConfig));
        Meta.ModelConfig modelConfig = generatorEditRequest.getModelConfig();
        generator.setModelConfig(JSONUtil.toJsonStr(modelConfig));

        // 参数校验
        generatorService.validGenerator(generator, false);
        Long loginId = Long.parseLong((String) StpUtil.getLoginId());
        User loginUser = userService.getById(loginId);
        long id = generatorEditRequest.getId();
        // 判断是否存在
        Generator oldGenerator = generatorService.getById(id);
        BusinessException.throwIf(oldGenerator == null,HttpCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldGenerator.getUserId().equals(loginUser.getId()) && !UserConstant.ADMIN_ROLE.equals(loginUser.getUserRole())) {
            throw new BusinessException(HttpCode.NO_AUTH_ERROR);
        }
        return generatorService.updateById(generator);
    }
}
