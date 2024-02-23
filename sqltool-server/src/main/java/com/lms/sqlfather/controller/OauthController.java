package com.lms.sqlfather.controller;


import cn.dev33.satoken.stp.StpUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.lms.lmscommon.model.vo.user.UserVO;
import com.lms.sqlfather.annotation.IgnoreLog;
import com.lms.sqlfather.thridparty.adapt.LoginOauthAdapter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


/**
 * 三方登录
 * @author lms2000
 * @since 2024-01-30
 */
@Controller

@RequestMapping("/oauth")
@Api(value = "三方认证管理")
public class OauthController {


    @Resource
    private LoginOauthAdapter loginOauthAdapter;



    /**
     * 微信登录
     *
     * @param code
     * @param state
     * @param request
     * @return
     */
    @GetMapping("/wechat")
    @ApiOperationSupport(order = 1)
    @IgnoreLog
    @ApiOperation(value = " 微信登录")
    public String loginForWechat(@RequestParam("code") String code, @RequestParam("state") String state, HttpServletRequest request) {
        UserVO userVO = loginOauthAdapter.loginByWechat(code, state, request);
        if(!ObjectUtils.isEmpty(userVO)){
            StpUtil.login(userVO.getId());
            return "redirect:http://localhost:8000/";
        }
        return "redirect:http://localhost:8000/user/login";
    }

    /**
     * gitee登录
     *
     * @param code
     * @param state
     * @param request
     * @return
     */
    @GetMapping("/gitee")
    @ApiOperationSupport(order =2)
    @IgnoreLog
    @ApiOperation(value = " gitee登录")
    public String loginForGitee(@RequestParam("code") String code, @RequestParam("state") String state, HttpServletRequest request) {
        UserVO userVO = loginOauthAdapter.loginByGitee(code, state, request);
        if (!ObjectUtils.isEmpty(userVO)) {
            StpUtil.login(userVO.getId());
            return "redirect:http://localhost:8000/";
        }
        return "redirect:http://localhost:8000/user/login";
    }
}
