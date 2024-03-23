package com.lms.sqlfather.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.lms.exception.BusinessException;
import com.lms.lmscommon.constant.StatusConstant;
import com.lms.lmscommon.model.entity.InterfaceInfo;
import com.lms.lmscommon.model.enums.MethodEnum;
import com.lms.lmscommon.service.InnerInterfaceInfoService;
import com.lms.sqlfather.service.InterfaceInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;


public class InnerInterfaceInfoServiceImpl implements InnerInterfaceInfoService {

    @Resource
    private InterfaceInfoService interfaceInfoService;
    @Override
    public InterfaceInfo getInterfaceInfo(String url, Integer method) {
       BusinessException.throwIf(StringUtils.isEmpty(url));
       BusinessException.throwIfNot(validMethod(method));
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("url", url);
        queryWrapper.eq("method", method);
        queryWrapper.eq("delete_flag", StatusConstant.NOT_DELETED).eq("status",1);
        return interfaceInfoService.getOne(queryWrapper);
    }
    private boolean validMethod(Integer method){
        List<Integer> values = MethodEnum.getValues();
        return !ObjectUtils.isEmpty(method)&&values.contains(method);
    }
}
