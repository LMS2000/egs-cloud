package com.lms.sqlfather.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lms.lmscommon.model.entity.User;
import com.lms.lmscommon.model.vo.user.UserVO;
import com.lms.lmscommon.service.InnerUserService;
import com.lms.sqlfather.service.UserService;



import javax.annotation.Resource;

import static com.lms.lmscommon.model.factory.UserFactory.USER_CONVERTER;


public class InnerUserServiceImpl implements InnerUserService {

    @Resource(name = "userServiceImpl")
    private UserService userService;
    @Override
    public UserVO getInvokeUser(String accessKey) {
        return USER_CONVERTER.toUserVo(userService
                .getOne(new QueryWrapper<User>().eq("access_key",accessKey)));
    }
}
