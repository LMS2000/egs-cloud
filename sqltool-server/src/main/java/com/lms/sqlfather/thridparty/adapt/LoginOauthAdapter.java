package com.lms.sqlfather.thridparty.adapt;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lms.contants.HttpCode;
import com.lms.exception.BusinessException;
import com.lms.lmscommon.model.entity.User;
import com.lms.lmscommon.model.vo.user.UserVO;

import com.lms.sqlfather.config.GiteeProperties;
import com.lms.sqlfather.config.WechatProperties;
import com.lms.sqlfather.service.UserService;
import com.lms.sqlfather.utils.HttpClientUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;

import static com.lms.lmscommon.constant.UserConstant.SALT;
import static com.lms.lmscommon.constant.UserConstant.USER_LOGIN_STATE;
import static com.lms.lmscommon.model.factory.UserFactory.USER_CONVERTER;



/**
 * @author lms2000
 * @since 2024-01-30
 * 三方登录的适配器类，通过继承复用UserService的 register方法和login方法  以及实现目标角色实现三方登录功能的扩充
 *  By《贯穿设计模式》
 */
@Component
@Slf4j
@AllArgsConstructor
public class LoginOauthAdapter implements LoginOauthTarget {




    private final GiteeProperties giteeProperties;


    private final WechatProperties wechatProperties;

    private final WxMpService wxMpService;

    private final UserService userService;




    @Override
    public UserVO loginByGitee(String code, String state, HttpServletRequest request) {

        //进行state判断，state的值是前端与后端商定好的
        // 前端将state传给Gitee平台,Gitee平台回传state给回调接口
        BusinessException.throwIf(!giteeProperties.getGiteeState().equals(state),
                HttpCode.OPERATION_ERROR,"无效的state");
        // 请求Gitee平台获取Token,并携带code
        String tokenUrl=giteeProperties.getTokenUrl().concat(code);
        JSONObject tokenResponse= HttpClientUtils.execute(tokenUrl, HttpMethod.POST);
        String token=String.valueOf(tokenResponse.get("access_token"));

        //如果获取的token为null就报错
        BusinessException.throwIf(tokenResponse.containsKey("error"));
        //请求用户信息
        String userUrl=giteeProperties.getUserUrl().concat(token);
        JSONObject userInfoResponse=HttpClientUtils.execute(userUrl,HttpMethod.GET);
        //获取用户信息，userName添加前缀GITEE@，密码保持与userName一致。
        String userName=giteeProperties.getUserPrefix().concat(String.valueOf(userInfoResponse.get("name")));
        String avatarUrl = (String) userInfoResponse.get("avatar_url");

        return autoRegisterGiteeAndLogin(userName,avatarUrl,request);
    }

     //自动注册和登录功能，此处体现了方法的”复用“
    private UserVO autoRegisterGiteeAndLogin(String userName,String avatarUrl, HttpServletRequest request){
        //如果第三方账号已经登录过了，则直接登录


        User user = userService.getOne(new QueryWrapper<User>().eq("username", userName));
        if(ObjectUtils.isEmpty(user)){ //如果第一次登录
            String accessKey = DigestUtil.md5Hex(SALT + userName + RandomUtil.randomNumbers(5));
            String secretKey = DigestUtil.md5Hex(SALT + userName + RandomUtil.randomNumbers(8));
            user=new User();
            user.setNickname(userName);
            user.setUserAvatar(avatarUrl);
            user.setUsername(userName);
            user.setUserPassword(userName);
            user.setAccessKey(accessKey);
            user.setSecretKey(secretKey);
            userService.save(user);
        }
//        UserVO userVO=new UserVO();
//        BeanUtils.copyProperties(user,userVO);
        return USER_CONVERTER.toUserVo(user);
        // 3. 记录用户的登录态
        //查找有没有当前gitee记录在数据库中
        //没有就插入
    }
    @Override
    public UserVO loginByWechat(String code,String state, HttpServletRequest request) {

        BusinessException.throwIf(!wechatProperties.getState().equals(state),
                HttpCode.OPERATION_ERROR,"无效的state");

        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = null;
        try {
            wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
            WxMpUser wxMpUser = wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, code);
            String openId = wxMpUser.getOpenId();
            String avatarUrl = wxMpUser.getHeadImgUrl();
            String nickname = "WECHAT@".concat(wxMpUser.getNickname());
            log.info("【微信网页授权】openId={}", openId);
            return autoRegisterWechatAndLogin(nickname,avatarUrl,openId,request);
        } catch (WxErrorException e) {
            log.info("【微信网页授权】{}", e);
            // 统一抛出异常
            throw new BusinessException(HttpCode.OPERATION_ERROR);
        }
        // 自动注册和登录功能，此处体现了方法的”复用“

    }


    private UserVO autoRegisterWechatAndLogin(String userName,String avatarUrl,String openId, HttpServletRequest request) {
        User user = userService.getOne(new QueryWrapper<User>().eq("user_name", userName));
        if(ObjectUtils.isEmpty(user)){ //如果第一次登录
            String accessKey = DigestUtil.md5Hex(SALT + userName + RandomUtil.randomNumbers(5));
            String secretKey = DigestUtil.md5Hex(SALT + userName + RandomUtil.randomNumbers(8));
            user=new User();
            user.setNickname(userName);
            user.setUserAvatar(avatarUrl);
            user.setUsername(userName);
            user.setUserPassword(userName);
            user.setAccessKey(accessKey);
            user.setSecretKey(secretKey);
            user.setOpenId(openId);
            userService.save(user);
        }
        UserVO userVO = USER_CONVERTER.toUserVo(user);
        // 3. 记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, userVO);
        //查找有没有当前gitee记录在数据库中
        //没有就插入
        return userVO;
    }

    @Override
    public String loginByQQ(String... params) {
        return null;
    }

}
