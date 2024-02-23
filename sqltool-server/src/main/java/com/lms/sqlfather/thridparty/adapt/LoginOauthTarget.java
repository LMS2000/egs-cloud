package com.lms.sqlfather.thridparty.adapt;

import com.lms.lmscommon.model.vo.user.UserVO;

import javax.servlet.http.HttpServletRequest;

public interface LoginOauthTarget {

    /**
     * gitee方式登录
     * @param code
     * @param state
     * @return
     */
    UserVO loginByGitee(String code, String state, HttpServletRequest request);

    /**
     * 微信登录方式
     * @param code
     * @param state
     * @return
     */
    UserVO loginByWechat(String code, String state, HttpServletRequest request);

    /**
     * QQ登录方式
     * @param params
     * @return
     */
    String loginByQQ(String... params);



}
