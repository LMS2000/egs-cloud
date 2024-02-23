package com.lms.sqlfather.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.lms.contants.HttpCode;
import com.lms.lmscommon.constant.SqlConstant;
import com.lms.lmscommon.model.dto.field.FieldInfoAddRequest;
import com.lms.lmscommon.model.dto.field.FieldInfoQueryRequest;
import com.lms.lmscommon.model.entity.FieldInfo;
import com.lms.lmscommon.model.enums.ReviewStatusEnum;
import com.lms.lmscommon.model.vo.field.FieldInfoVO;
import com.lms.sqlfather.core.GeneratorFacade;
import com.lms.sqlfather.core.builder.SqlBuilder;
import com.lms.sqlfather.core.schema.TableSchema;
import com.lms.lmscommon.common.BusinessException;
import com.lms.sqlfather.mapper.FieldInfoMapper;
import com.lms.sqlfather.service.FieldInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static com.lms.lmscommon.model.factory.FieldFactory.FIELD_CONVERTER;

@Service
public class FieldInfoServiceImpl extends ServiceImpl<FieldInfoMapper, FieldInfo> implements FieldInfoService {
    private final static Gson GSON = new Gson();
    @Override
    public void validAndHandleFieldInfo(FieldInfo fieldInfo, boolean add) {
        BusinessException.throwIf(fieldInfo == null);
        String content = fieldInfo.getContent();
        String name = fieldInfo.getName();
        Integer reviewStatus = fieldInfo.getReviewStatus();
        // 创建时，所有参数必须非空
        BusinessException.throwIf(add && StringUtils.isAnyBlank(name, content));
        BusinessException.throwIf(StringUtils.isNotBlank(name) && name.length() > 30);
        if (StringUtils.isNotBlank(content)) {

            BusinessException.throwIf(content.length() > 20000);
            // 校验字段内容
            try {
                TableSchema.Field field = GSON.fromJson(content, TableSchema.Field.class);
                GeneratorFacade.validField(field);
                // 填充 fieldName
                fieldInfo.setFieldName(field.getFieldName());
            } catch (Exception e) {
                throw new BusinessException(HttpCode.PARAMS_ERROR, "内容格式错误");
            }
        }
        BusinessException.throwIf(reviewStatus != null && !ReviewStatusEnum.getValues().contains(reviewStatus));
    }

    @Override
    public Long addField(FieldInfoAddRequest fieldInfoAddRequest) {
        FieldInfo fieldInfo = new FieldInfo();
        BeanUtils.copyProperties(fieldInfoAddRequest, fieldInfo);
        // 校验
        validAndHandleFieldInfo(fieldInfo, true);
        Long loginId =Long.parseLong((String) StpUtil.getLoginId());
        fieldInfo.setUserId(loginId);
        boolean result = this.save(fieldInfo);
        BusinessException.throwIf(!result);
        return fieldInfo.getId();

    }

    @Override
    public Page<FieldInfoVO> pageMyAddFieldInfo(FieldInfoQueryRequest fieldInfoQueryRequest, Long uid) {
        fieldInfoQueryRequest.setUserId(uid);
        long current = fieldInfoQueryRequest.getCurrent();
        long size = fieldInfoQueryRequest.getPageSize();
        // 限制爬虫
        BusinessException.throwIf(size > 20);
        Page<FieldInfo> fieldInfoPage = this.page(new Page<>(current, size),
                getQueryWrapper(fieldInfoQueryRequest));
        List<FieldInfoVO> fieldInfoVOList = FIELD_CONVERTER.toListFieldVO(fieldInfoPage.getRecords());
        Page<FieldInfoVO> fieldInfoVOPage=new Page<>(current,size);
        fieldInfoVOPage.setRecords(fieldInfoVOList);
        fieldInfoVOPage.setTotal(fieldInfoVOList.size());
        return fieldInfoVOPage;
    }

    @Override
    public Page<FieldInfoVO> pageMyFieldInfo(FieldInfoQueryRequest fieldInfoQueryRequest, Long uid) {
        long current = fieldInfoQueryRequest.getCurrent();
        long size = fieldInfoQueryRequest.getPageSize();
        // 限制爬虫
        BusinessException.throwIf(size > 20);
        QueryWrapper<FieldInfo> queryWrapper = getQueryWrapper(fieldInfoQueryRequest);
        queryWrapper.eq("user_id", uid)
                .or()
                .eq("review_status", ReviewStatusEnum.PASS.getValue());
        Page<FieldInfo> fieldInfoPage = this.page(new Page<>(current, size), queryWrapper);
        List<FieldInfoVO> fieldInfoVOList = FIELD_CONVERTER.toListFieldVO(fieldInfoPage.getRecords());
        Page<FieldInfoVO> fieldInfoVOPage=new Page<>(current,size);
        fieldInfoVOPage.setRecords(fieldInfoVOList);
        fieldInfoVOPage.setTotal(fieldInfoVOList.size());
        return fieldInfoVOPage;
    }

    @Override
    public List<FieldInfoVO> listMyFieldInfo(FieldInfoQueryRequest fieldInfoQueryRequest, Long uid) {
        FieldInfo fieldInfoQuery = new FieldInfo();
        if (fieldInfoQueryRequest != null) {
            BeanUtils.copyProperties(fieldInfoQueryRequest, fieldInfoQuery);
        }
        // 先查询所有审核通过的
        fieldInfoQuery.setReviewStatus(ReviewStatusEnum.PASS.getValue());
        QueryWrapper<FieldInfo> queryWrapper = getQueryWrapper(fieldInfoQueryRequest);
        final String[] fields = new String[]{"id", "name"};
        queryWrapper.select(fields);
        List<FieldInfo> fieldInfoList = this.list(queryWrapper);
        // 再查所有本人的
        try {
            fieldInfoQuery.setReviewStatus(null);
            fieldInfoQuery.setUserId(uid);
            queryWrapper = new QueryWrapper<>(fieldInfoQuery);
            queryWrapper.select(fields);
            fieldInfoList.addAll(this.list(queryWrapper));
        } catch (Exception e) {
            // 未登录
        }
        // 根据 id 去重
        List<FieldInfo> resultList = fieldInfoList.stream().collect(Collectors.collectingAndThen(
                Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(FieldInfo::getId))), ArrayList::new));
        return FIELD_CONVERTER.toListFieldVO(resultList);
    }

    @Override
    public Page<FieldInfoVO> pageFieldInfo(FieldInfoQueryRequest fieldInfoQueryRequest) {
        long current = fieldInfoQueryRequest.getCurrent();
        long size = fieldInfoQueryRequest.getPageSize();
        // 限制爬虫
        BusinessException.throwIf(size>20);
        Page<FieldInfo> fieldInfoPage = this.page(new Page<>(current, size),
                getQueryWrapper(fieldInfoQueryRequest));
        List<FieldInfoVO> fieldInfoVOList = FIELD_CONVERTER.toListFieldVO(fieldInfoPage.getRecords());
        Page<FieldInfoVO> fieldInfoVOPage=new Page<>(current,size);
        fieldInfoVOPage.setRecords(fieldInfoVOList);
        fieldInfoVOPage.setTotal(fieldInfoVOList.size());
        return fieldInfoVOPage;
    }

    @Override
    public List<FieldInfoVO> listFieldInfo(FieldInfoQueryRequest fieldInfoQueryRequest) {
        return FIELD_CONVERTER.toListFieldVO( this.list(getQueryWrapper(fieldInfoQueryRequest)));
    }

    @Override
    public String generateCreateSql(Long id) {
        FieldInfo byId = this.getById(id);
        BusinessException.throwIf(byId == null);
        TableSchema.Field field = GSON.fromJson(byId.getContent(), TableSchema.Field.class);
        SqlBuilder sqlBuilder = new SqlBuilder();
        return sqlBuilder.buildCreateFieldSql(field);
    }

    /**
     * 获取查询包装类
     *
     * @param fieldInfoQueryRequest
     * @return
     */
    private QueryWrapper<FieldInfo> getQueryWrapper(FieldInfoQueryRequest fieldInfoQueryRequest) {
        BusinessException.throwIf(fieldInfoQueryRequest == null,HttpCode.PARAMS_ERROR,
                "请求参数为空");
        FieldInfo fieldInfoQuery = new FieldInfo();
        BeanUtils.copyProperties(fieldInfoQueryRequest, fieldInfoQuery);
        String searchName = fieldInfoQueryRequest.getSearchName();
        String sortField = fieldInfoQueryRequest.getSortField();
        String sortOrder = fieldInfoQueryRequest.getSortOrder();
        String name = fieldInfoQuery.getName();
        String content = fieldInfoQuery.getContent();
        String fieldName = fieldInfoQuery.getFieldName();
        // name、fieldName、content 需支持模糊搜索
        fieldInfoQuery.setName(null);
        fieldInfoQuery.setFieldName(null);
        fieldInfoQuery.setContent(null);
        QueryWrapper<FieldInfo> queryWrapper = new QueryWrapper<>(fieldInfoQuery);
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        queryWrapper.like(StringUtils.isNotBlank(fieldName), "field_name", fieldName);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        // 同时按 name、fieldName 搜索
        if (StringUtils.isNotBlank(searchName)) {
            queryWrapper.like("name", searchName).or().like("field_name", searchName);
        }
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField), sortOrder.equals(SqlConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }
}
