package com.lms.sqlfather.aop;

import cn.dev33.satoken.stp.StpInterface;
import com.lms.lmscommon.model.entity.User;
import com.lms.sqlfather.service.UserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
@Component
public class SaTokenInterceptor implements StpInterface {

    @Resource(name = "userServiceImpl")
    private UserService userService;
    @Override
    public List<String> getPermissionList(Object o, String s) {
        return null;
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        User user = userService.getById(Long.parseLong((String)loginId));
        return Collections.singletonList(user.getUserRole());
    }
}
