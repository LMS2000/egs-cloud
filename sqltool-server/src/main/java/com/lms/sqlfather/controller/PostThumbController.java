package com.lms.sqlfather.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.lms.contants.HttpCode;
import com.lms.lmscommon.model.dto.postthumb.PostThumbAddRequest;
import com.lms.lmscommon.model.vo.user.UserVO;
import com.lms.lmscommon.common.BusinessException;
import com.lms.result.EnableResponseAdvice;
import com.lms.sqlfather.service.PostThumbService;
import com.lms.sqlfather.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 帖子点赞控制类
 * @author lms2000
 * @since 2024-01-30
 */
@RestController
@RequestMapping("/post/thumb")
@Slf4j
@Api(value = "帖子点赞")
@EnableResponseAdvice
public class PostThumbController {

    @Resource
    private PostThumbService PostThumbService;

    @Resource(name = "userServiceImpl")
    private UserService userService;

    /**
     * 点赞 / 取消点赞
     *
     * @param postThumbAddRequest
     * @return resultNum 本次点赞变化数
     */
    @PostMapping()
    @SaCheckLogin
    @ApiOperationSupport(order =1)
    @ApiOperation(value = "点赞 / 取消点赞")
    public Integer doThumb( @Validated @RequestBody PostThumbAddRequest postThumbAddRequest) {
        // 登录才能点赞
        Long loginId =Long.parseLong((String) StpUtil.getLoginId());
        final  UserVO loginUser = userService.getUserVO(userService.getById(loginId));
        Long generatorId = postThumbAddRequest.getGeneratorId();
        return PostThumbService.doPostThumb(generatorId, loginUser);
    }

}
