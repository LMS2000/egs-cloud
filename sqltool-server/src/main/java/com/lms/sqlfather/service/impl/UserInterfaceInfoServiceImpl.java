package com.lms.sqlfather.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lms.exception.BusinessException;
import com.lms.lmscommon.constant.StatusConstant;
import com.lms.lmscommon.model.dto.userInterfaceInfo.PageUserInterfaceInfoRequest;
import com.lms.lmscommon.model.entity.InterfaceInfo;
import com.lms.lmscommon.model.entity.UserInterfaceInfo;
import com.lms.lmscommon.model.enums.InterfaceInfoStatusEnum;
import com.lms.lmscommon.model.enums.MethodEnum;
import com.lms.lmscommon.model.vo.interfaceInfo.InterfaceInfoVO;
import com.lms.lmscommon.model.vo.userInterfaceInfo.UserInterfaceInfoVO;
import com.lms.lmscommon.utils.MybatisUtils;
import com.lms.sqlfather.mapper.UserInterfaceInfoMapper;
import com.lms.sqlfather.service.InterfaceInfoService;
import com.lms.sqlfather.service.UserInterfaceInfoService;
import com.lms.sqlfather.service.UserService;

import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.lms.lmscommon.model.factory.InterfaceInfoFactory.INTERFACE_CONVERTER;
import static com.lms.lmscommon.model.factory.UserInterfaceFactory.USER_INTERFACE_CONVERTER;


/**
 *  用户接口管理
 */
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
    implements UserInterfaceInfoService {

    @Resource
    private InterfaceInfoService interfaceInfoService;


    @Resource(name = "userServiceImpl")
    private UserService userService;


    @Override
    public Boolean saveUserInterface(Long uid, Long interfaceId) {
        BusinessException.throwIfNot(MybatisUtils.existCheck(interfaceInfoService, Map.of("id",interfaceId)));
        BusinessException.throwIf(MybatisUtils.existCheck(this,Map.of("user_id",uid,"interface_info_id",interfaceId)));
      return   this.save(UserInterfaceInfo.builder().interfaceInfoId(interfaceId).userId(uid).leftNum(100).build());

    }

    @Override
    public Page<UserInterfaceInfoVO> pageUseInterfaceInfo(PageUserInterfaceInfoRequest pageInterfaceInfoDto) {
        Integer pageSize = pageInterfaceInfoDto.getPageSize();
        Integer pageNum = pageInterfaceInfoDto.getPageNum();
        Integer method = pageInterfaceInfoDto.getMethod();
        String name = pageInterfaceInfoDto.getName();
        Integer status = pageInterfaceInfoDto.getStatus();

        //获取所需要的接口信息和用户信息的map，然后遍历的时候根据记录的id注入信息

//        List<InterfaceInfo>  list = interfaceInfoService.list(new QueryWrapper<InterfaceInfo>().eq(validMethod(method), "method", method)
//                    .like(!StringUtils.isEmpty(name), "name", name));
//
//        List<Long> interfaceIds = list.stream().map(InterfaceInfo::getId).collect(Collectors.toList());
//        List<InterfaceInfoVO> interfaceInfoVos = INTERFACE_CONVERTER.toListInterfaceInfoVO(list);
//        Map<Long, InterfaceInfoVO> interfaceInfoVoMap = interfaceInfoVos.stream().collect(Collectors.toMap(InterfaceInfoVO::getId, Function.identity()));
//        Map<Long, UserVO> userVoMap = innerUserService.listUserInfo().stream().collect(Collectors.toMap(UserVO::getId, Function.identity()));
//        if(ObjectUtils.isEmpty(interfaceIds)){
//            return new Page<>();
//        }
//        Page<UserInterfaceInfo>  userInterfaceInfoPage = this.page(new Page<>(pageNum, pageSize), new QueryWrapper<UserInterfaceInfo>().eq(validStatus(status), "status", status)
//                .eq("delete_flag", CommonConstants.NOT_DELETED).in("interface_info_id",interfaceIds));
//
//        List<UserInterfaceInfo> records = userInterfaceInfoPage.getRecords();
//        List<UserInterfaceInfoVO> userInterfaceInfoVOs = USER_INTERFACE_CONVERTER.toListUserInterfaceVo(records);
//        List<UserInterfaceInfoVO> collect = userInterfaceInfoVOs.stream().map(userInterfaceInfoVO -> {
//            userInterfaceInfoVO.setInterfaceInfoVO(interfaceInfoVoMap.getOrDefault(userInterfaceInfoVO.getInterfaceInfoId(), null));
//            userInterfaceInfoVO.setUserVo(userVoMap.getOrDefault(userInterfaceInfoVO.getUserId(), null));
//            return userInterfaceInfoVO;
//        }).collect(Collectors.toList());
//        Page<UserInterfaceInfoVO> userInterfaceInfoVOPage=new Page<>(pageNum,pageSize,userInterfaceInfoPage.getTotal());
//        userInterfaceInfoVOPage.setRecords(collect);
//
//        return userInterfaceInfoVOPage;
        return new Page<>();
    }

    @Override
    public Page<UserInterfaceInfoVO> pageCurrentInterfaceInfo(Long uid, PageUserInterfaceInfoRequest pageInterfaceInfoDto) {

        Integer pageSize = pageInterfaceInfoDto.getPageSize();
        Integer pageNum = pageInterfaceInfoDto.getPageNum();
        Integer method = pageInterfaceInfoDto.getMethod();
        String name = pageInterfaceInfoDto.getName();
        Integer status = pageInterfaceInfoDto.getStatus();

        //获取所需要的接口信息和用户信息的map，然后遍历的时候根据记录的id注入信息
        List<InterfaceInfo> list = interfaceInfoService.list(new QueryWrapper<InterfaceInfo>().eq(validMethod(method), "method", method)
                .like(!StringUtils.isEmpty(name), "name", name));
        List<InterfaceInfoVO> interfaceInfoVos = INTERFACE_CONVERTER.toListInterfaceVO(list);
        Map<Long, InterfaceInfoVO> interfaceInfoVoMap = interfaceInfoVos.stream().collect(Collectors.toMap(InterfaceInfoVO::getId, Function.identity()));

        Page<UserInterfaceInfo> userInterfaceInfoPage = this.page(new Page<>(pageNum, pageSize), new QueryWrapper<UserInterfaceInfo>().eq(validStatus(status), "status", status)
                .eq("delete_flag", StatusConstant.NOT_DELETED).eq("user_id",uid));
        List<UserInterfaceInfo> records = userInterfaceInfoPage.getRecords();
        List<UserInterfaceInfoVO> userInterfaceInfoVOs = USER_INTERFACE_CONVERTER.toListUserInterfaceVO(records);
        List<UserInterfaceInfoVO> collect = userInterfaceInfoVOs.stream().map(userInterfaceInfoVO -> {
            userInterfaceInfoVO.setInterfaceInfoVo(interfaceInfoVoMap.getOrDefault(userInterfaceInfoVO.getInterfaceInfoId(), null));
            return userInterfaceInfoVO;
        }).collect(Collectors.toList());
        Page<UserInterfaceInfoVO> userInterfaceInfoVOPage=new Page<>(pageNum,pageSize,userInterfaceInfoPage.getTotal());
        userInterfaceInfoVOPage.setRecords(collect);
        return userInterfaceInfoVOPage;
    }

    private boolean validMethod(Integer method){
        List<Integer> values = MethodEnum.getValues();
        return !ObjectUtils.isEmpty(method)&&values.contains(method);
    }

    private boolean validStatus(Integer status){
        List<Integer> values = InterfaceInfoStatusEnum.getValues();
        return  !ObjectUtils.isEmpty(status)&&values.contains(status);
    }

}




