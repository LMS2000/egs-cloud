package com.lms.sqlfather.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.lms.contants.HttpCode;
import com.lms.exception.BusinessException;
import com.lms.lmscommon.common.DeleteRequest;
import com.lms.lmscommon.constant.EmailConstant;
import com.lms.lmscommon.constant.UserConstant;
import com.lms.lmscommon.model.dto.email.SendEmailRequest;
import com.lms.lmscommon.model.dto.user.*;
import com.lms.lmscommon.model.entity.User;
import com.lms.lmscommon.model.vo.user.UserVO;
import com.lms.lmscommon.utils.CreateImageCode;
import com.lms.redis.RedisCache;
import com.lms.result.EnableResponseAdvice;
import com.lms.result.ResultData;
import com.lms.sqlfather.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.io.IOException;
import java.util.List;

/**
 * 用户控制类
 *
 * @author lms2000
 * @since 2024-01-30
 */
@RestController
@RequestMapping("/user")
@EnableResponseAdvice
@Api(value = "用户管理")
@AllArgsConstructor
public class UserController {


    private final UserService userService;


    private final RedisCache redisCache;






    /**
     * 判断找回密码的邮箱验证码是否正确,如果在数据库中找不到的话就返回false
     *
     * @param userEmailCodeRequest
     * @return
     */
    @PostMapping("/valid/findback")
    @ApiOperationSupport(order =1)
    @ApiOperation(value = "判断找回密码的邮箱验证码是否正确,如果在数据库中找不到的话就返回false")
    public ResultData checkEmailCodeForFindBack(@RequestBody @Valid UserEmailCodeRequest userEmailCodeRequest) {
        return userService.validFindback(userEmailCodeRequest);
    }


    /**
     * 上传头像
     * @param file
     * @return
     */
    @PostMapping("/upload")
    @SaCheckLogin
    @ApiOperationSupport(order =2)
    @ApiOperation(value = "上传头像")
    public String uploadAvatar(MultipartFile file) {
        Long loginId = Long.parseLong((String) StpUtil.getLoginId());
        return userService.uploadUserAvatar(file, loginId);
    }

    /**
     * 用邮箱来设置密码
     *
     * @param userFindBackPasswordRequest
     * @return
     */
    @PostMapping("/update/password")
    @SaCheckLogin
    @ApiOperationSupport(order =3)
    @ApiOperation(value = "用邮箱来设置密码")
    public Boolean findBackNewPassword(@Validated @RequestBody  UserFindBackPasswordRequest userFindBackPasswordRequest) {
        return userService.updatePasswordByFindBack(userFindBackPasswordRequest);
    }

    /**
     * 验证码
     * 0 为注册    1 为邮箱验证
     *
     * @param response
     * @param request
     * @param type
     * @throws IOException
     */
    @GetMapping(value = "/checkCode")
    @ApiOperationSupport(order =4)
    @ApiOperation(value = "图片验证码 0 为注册    1 为邮箱验证")
    public void checkCode(HttpServletResponse response, HttpServletRequest request, Integer type) throws IOException {
        CreateImageCode vCode = new CreateImageCode(130, 38, 5, 10);
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        String code = vCode.getCode();
        HttpSession session = request.getSession();
        if (type == null || type == 0) {
            session.setAttribute(EmailConstant.CHECK_CODE_KEY, code);
        } else {
            session.setAttribute(EmailConstant.CHECK_CODE_KEY_EMAIL, code);
        }
        vCode.write(response.getOutputStream());
    }


    /**
     * type 0 为注册 1 为找回密码
     *
     * @param session
     * @return
     */
    @PostMapping("/sendEmailCode")
    @ApiOperationSupport(order =5)
    @ApiOperation(value = "type 0 为注册 1 为找回密码")
    public Boolean sendEmailCode(HttpSession session, @Validated  @RequestBody  SendEmailRequest sendEmailRequest) {
        String code = sendEmailRequest.getCode();
        try {
            if (!code.equalsIgnoreCase((String) session.getAttribute(EmailConstant.CHECK_CODE_KEY_EMAIL))) {
                throw new BusinessException(HttpCode.PARAMS_ERROR, "图片验证码不正确");
            }
            return userService.sendEmailCode(sendEmailRequest);
        } finally {
            session.removeAttribute(EmailConstant.CHECK_CODE_KEY_EMAIL);
        }
    }


    /**
     * 注册
     *
     * @param userRegisterRequest
     * @return
     */

    @PostMapping("/register")
    @ApiOperationSupport(order =6)
    @ApiOperation(value = "注册")
    public Long registerUser(@Validated @RequestBody(required = true) UserRegisterRequest userRegisterRequest, HttpSession session) {

        try {
            String email = userRegisterRequest.getEmail();
            String code = redisCache.getCacheObject(EmailConstant.EMAIIL_HEADER + 0 + "_" + email);
            BusinessException.throwIf(StrUtil.isEmpty(code) || !code.equals(userRegisterRequest.getEmailCode()), HttpCode.PARAMS_ERROR, "邮箱验证码错误");
            return userService.userRegister(userRegisterRequest);
        } finally {
            redisCache.deleteObject(EmailConstant.EMAIIL_HEADER + 0 + "_" + session.getId());
        }
    }

    /**
     * 登录
     *
     * @param userLoginRequest
     * @param request
     * @return
     */
    @PostMapping("/login")
    @ApiOperationSupport(order =7)
    @ApiOperation(value = "登录")
    public UserVO userLogin(@Validated @RequestBody  UserLoginRequest userLoginRequest, HttpServletRequest request) {
        try {
            //校验码校验
            String trueCode = (String) request.getSession().getAttribute(EmailConstant.CHECK_CODE_KEY);
            String code = userLoginRequest.getCode();
            BusinessException.throwIf(StrUtil.isEmpty(trueCode) || !trueCode.equals(code), HttpCode.PARAMS_ERROR, "图片校验码不正确");
            UserVO userVO = userService.userLogin(userLoginRequest);
            StpUtil.login(userVO.getId());
            return userVO;
            //脱敏
        } finally {
            request.getSession().removeAttribute(EmailConstant.CHECK_CODE_KEY);
        }
    }

    /**
     * 注销
     *
     * @return
     */
    @PostMapping("/logout")
    @ApiOperationSupport(order =8)
    @ApiOperation(value = "注销")
    public void logout() {
        StpUtil.logout();
    }


    /**
     * 获取当前用户
     *
     * @return
     */
    @GetMapping("/get/login")
    @ApiOperationSupport(order =9)
    @ApiOperation(value = "获取当前用户")
    public UserVO getCurrentUser() {
        Long loginId = Long.parseLong((String) StpUtil.getLoginId());
        return userService.getUserVO(userService.getById(loginId));
    }

    /**
     * 添加用户
     *
     * @param userAddRequest
     * @return
     */
    @PostMapping("/add")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    @ApiOperationSupport(order =10)
    @ApiOperation(value = "添加用户")
    public Long add(@Validated @RequestBody UserAddRequest userAddRequest) {
        return userService.addUser(userAddRequest);
    }

    /**
     * 删除用户
     *
     *
     * @param deleteRequest
     * @return
     */
    @DeleteMapping("/delete")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    @ApiOperationSupport(order =11)
    @ApiOperation(value = "删除用户")
    public Boolean delete(@Validated @RequestBody  DeleteRequest deleteRequest) {
        BusinessException.throwIf(deleteRequest == null || deleteRequest.getId() <= 0);
        return userService.removeById(deleteRequest.getId());
    }


    /**
     * 修改
     *
     * @param userUpdateRequest
     * @return
     */
    @PostMapping("/update/current")
    @ApiOperationSupport(order =12)
    @ApiOperation(value = "当前用户修改")
    @SaCheckLogin
    public Boolean updateCurrentUser(@Validated @RequestBody  UserUpdateRequest userUpdateRequest) {
        Long loginId = Long.parseLong((String) StpUtil.getLoginId());
        userUpdateRequest.setId(loginId);
        return userService.updateCurrentUser(userUpdateRequest);
    }

    /**
     * 更新用户
     *
     * @param userUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    @ApiOperationSupport(order =13)
    @ApiOperation(value = "修改用户")
    public Boolean updateUser(@Validated @RequestBody  UserUpdateRequest userUpdateRequest) {
        BusinessException.throwIf(userUpdateRequest == null || userUpdateRequest.getId() == null);
        User user = new User();
        BeanUtils.copyProperties(userUpdateRequest, user);
        return userService.updateById(user);
    }

    @PostMapping("/flush/keys")
    @ApiOperation("刷新用户的公钥和私钥")
    @SaCheckLogin
    @ApiOperationSupport(order =14)
    public Boolean  changeAkAndSk(){
        Long loginId = Long.parseLong((String) StpUtil.getLoginId());
        return userService.flushKeys(loginId);
    }
    /**
     * 获取用户列表
     *
     * @param userQueryRequest
     * @return
     */
    @GetMapping("/list")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    @ApiOperationSupport(order =15)
    @ApiOperation(value = "获取用户列表")
    public List<UserVO> listUser(UserQueryRequest userQueryRequest) {
        return userService.listUserVO(userQueryRequest);
    }

    /**
     * 根据 id 获取用户
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    @ApiOperationSupport(order =16)
    @ApiOperation(value = "根据 id 获取用户")
    public UserVO getUserById(@Positive(message = "id不合法") Long id) {
        User user = userService.getById(id);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }


    /**
     * 获取用户分页列表
     *
     * @param userQueryRequest
     * @return
     */

    @GetMapping("/list/page")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    @ApiOperationSupport(order =17)
    @ApiOperation(value = "获取用户分页列表")
    public Page<UserVO> page(UserQueryRequest userQueryRequest) {
       return userService.pageUserVO(userQueryRequest);
    }
}
