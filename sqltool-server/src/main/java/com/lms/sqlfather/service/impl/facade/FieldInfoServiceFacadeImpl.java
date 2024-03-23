package com.lms.sqlfather.service.impl.facade;

import com.lms.exception.BusinessException;
import com.lms.lmscommon.common.DeleteRequest;
import com.lms.lmscommon.constant.UserConstant;
import com.lms.lmscommon.model.entity.FieldInfo;
import com.lms.lmscommon.model.vo.user.UserVO;
import com.lms.sqlfather.service.FieldInfoService;
import com.lms.sqlfather.service.FieldInfoServiceFacade;
import com.lms.sqlfather.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class FieldInfoServiceFacadeImpl implements FieldInfoServiceFacade {

    private final UserService userService;
    private final FieldInfoService fieldInfoService;

    @Autowired
    public FieldInfoServiceFacadeImpl(@Qualifier(value = "userServiceImpl") UserService userService, FieldInfoService fieldInfoService) {
        this.userService = userService;
        this.fieldInfoService = fieldInfoService;
    }

    @Override
    public Boolean deleteFieldInfo(DeleteRequest deleteRequest,Long uid) {
        UserVO loginUser = userService.getUserVO(userService.getById(uid));
        long id = deleteRequest.getId();
        // 判断是否存在
        FieldInfo oldFieldInfo = fieldInfoService.getById(id);
        BusinessException.throwIf(oldFieldInfo == null);
        // 仅本人或管理员可删除
        BusinessException.throwIf(!oldFieldInfo.getUserId().equals(uid) && !UserConstant.ADMIN_ROLE.equals(loginUser.getUserRole()));
        return fieldInfoService.removeById(id);
    }
}
