package com.lms.sqlfather.service.impl.facade;

import cn.dev33.satoken.stp.StpUtil;
import com.lms.contants.HttpCode;
import com.lms.lmscommon.common.DeleteRequest;
import com.lms.lmscommon.constant.UserConstant;
import com.lms.lmscommon.model.entity.Dict;
import com.lms.lmscommon.model.entity.User;
import com.lms.lmscommon.common.BusinessException;
import com.lms.sqlfather.service.DictService;
import com.lms.sqlfather.service.DictServiceFacade;
import com.lms.sqlfather.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class DictServiceFacadeImpl implements DictServiceFacade {


    private final UserService userService;


    private  final DictService dictService;

    @Autowired
    public DictServiceFacadeImpl(@Qualifier(value = "userServiceImpl") UserService userService, DictService dictService, DictService dictService1) {
        this.userService = userService;
        this.dictService = dictService1;
    }


    @Override
    public Boolean deleteDict(DeleteRequest deleteRequest) {
        Long loginId =Long.parseLong((String) StpUtil.getLoginId());
        User loginUser = userService.getById(loginId);
        long id = deleteRequest.getId();
        // 判断是否存在
        Dict oldDict = dictService.getById(id);
        BusinessException.throwIf(oldDict == null,HttpCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        BusinessException.throwIf(!oldDict.getUserId().equals(loginId) && !UserConstant.ADMIN_ROLE.equals(loginUser.getUserRole()),
                HttpCode.NO_AUTH_ERROR);
        return dictService.removeById(id);
    }
}
