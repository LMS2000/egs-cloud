package com.lms.sqlfather.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lms.lmscommon.model.dto.email.SendEmailRequest;
import com.lms.lmscommon.model.dto.user.*;
import com.lms.lmscommon.model.entity.User;
import com.lms.lmscommon.model.vo.user.UserVO;
import com.lms.result.ResultData;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户服务
 *
 */
public interface UserService extends IService<User> {




    Page<UserVO> pageUserVO(UserQueryRequest userQueryRequest);
    /**
     * 获取用户列表
     * @param userQueryRequest
     * @return
     */
    List<UserVO> listUserVO(UserQueryRequest userQueryRequest);

    /**
     * 修改当前用户信息
     * @param userUpdateRequest
     * @return
     */
    Boolean updateCurrentUser(UserUpdateRequest userUpdateRequest);
    /**
     * 管理员手动添加用户
     * @param userAddRequest
     * @return
     */
    Long addUser(UserAddRequest userAddRequest);

    /**
     * 发送邮箱验证码
     * @param sendEmailRequest
     * @return
     */
    Boolean sendEmailCode(SendEmailRequest sendEmailRequest);

    /**
     *  根据邮箱和邮箱验证码去修改密码
     * @param userFindBackPasswordRequest
     * @return
     */
    Boolean updatePasswordByFindBack(UserFindBackPasswordRequest userFindBackPasswordRequest);

    /**
     *  找回密码时，校验邮箱和邮箱验证码
     * @param userEmailCodeRequest
     * @return
     */
    ResultData validFindback(UserEmailCodeRequest userEmailCodeRequest);

    String uploadUserAvatar(MultipartFile file,Long uid);
    /**
     * 用户注册
     *
     * @return 新用户 id
     */
    Long userRegister(UserRegisterRequest userRegisterRequest);

    /**
     * 用户登录
     * @param userLoginRequest
     * @return 脱敏后的用户信息
     */
    UserVO userLogin(UserLoginRequest userLoginRequest);

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    UserVO getLoginUser(HttpServletRequest request);

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    boolean isAdmin(HttpServletRequest request);

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    boolean userLogout(HttpServletRequest request);

    String sendEmail(String email, Integer type);



    Boolean resetPasswordForFindBack(String email ,String password,String checkPassword);
    /**
     * 获取当前登录用户（允许未登录）
     *
     * @param request
     * @return
     */
    UserVO getLoginUserPermitNull(HttpServletRequest request);
    /**
     * 获取脱敏的用户信息
     *
     * @param userList
     * @return
     */
    List<UserVO> getUserVO(List<User> userList);

    /**
     * 获取脱敏的用户信息
     *
     * @param user
     * @return
     */
    UserVO getUserVO(User user);



}
