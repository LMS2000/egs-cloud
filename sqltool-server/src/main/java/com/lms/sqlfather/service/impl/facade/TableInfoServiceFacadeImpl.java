package com.lms.sqlfather.service.impl.facade;

import cn.dev33.satoken.stp.StpUtil;
import com.lms.contants.HttpCode;
import com.lms.lmscommon.common.DeleteRequest;
import com.lms.lmscommon.constant.UserConstant;
import com.lms.lmscommon.model.entity.TableInfo;
import com.lms.lmscommon.model.vo.user.UserVO;
import com.lms.lmscommon.common.BusinessException;
import com.lms.sqlfather.service.TableInfoService;
import com.lms.sqlfather.service.TableInfoServiceFacade;
import com.lms.sqlfather.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


/**
 * @author lms2000
 */
@Service
public class TableInfoServiceFacadeImpl implements TableInfoServiceFacade {


    private final UserService userService;
    private final TableInfoService tableInfoService;

    @Autowired
    public TableInfoServiceFacadeImpl(@Qualifier("userServiceImpl") UserService userService, TableInfoService tableInfoService) {
        this.userService = userService;
        this.tableInfoService = tableInfoService;
    }

    @Override
    public Boolean deleteFieldInfo(DeleteRequest deleteRequest, Long uid) {
        TableInfo byId = tableInfoService.getById(deleteRequest.getId());
        BusinessException.throwIf(byId == null);
        Long loginId = Long.parseLong((String) StpUtil.getLoginId());
        UserVO loginUser = userService.getUserVO(userService.getById(loginId));
        BusinessException.throwIf(!loginId.equals(byId.getUserId()) && !UserConstant.ADMIN_ROLE.equals(loginUser.getUserRole()),
                HttpCode.NO_AUTH_ERROR);
        return tableInfoService.removeById(byId.getId());
    }
}
