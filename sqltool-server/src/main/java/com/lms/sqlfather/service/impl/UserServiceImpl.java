package com.lms.sqlfather.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lms.contants.HttpCode;
import com.lms.exception.BusinessException;
import com.lms.lmscommon.constant.CommonConstant;
import com.lms.lmscommon.constant.EmailConstant;
import com.lms.lmscommon.constant.FileConstant;
import com.lms.lmscommon.constant.UserConstant;
import com.lms.lmscommon.model.dto.email.SendEmailRequest;
import com.lms.lmscommon.model.dto.email.SysSettingsRequest;
import com.lms.lmscommon.model.dto.user.*;
import com.lms.lmscommon.model.entity.User;
import com.lms.lmscommon.model.enums.RoleEnum;
import com.lms.lmscommon.model.vo.user.UserVO;

import com.lms.lmscommon.utils.MybatisUtils;
import com.lms.lmscommon.utils.StringTools;
import com.lms.redis.RedisCache;
import com.lms.result.ResultData;
import com.lms.sqlfather.client.OssClient;
import com.lms.sqlfather.config.AppConfig;
import com.lms.sqlfather.mapper.UserMapper;
import com.lms.sqlfather.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.lms.lmscommon.constant.FileConstant.ONE_M;
import static com.lms.lmscommon.constant.UserConstant.*;
import static com.lms.lmscommon.model.factory.UserFactory.USER_CONVERTER;


@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    private final JavaMailSender javaMailSender;


    /**
     * 邮件发送的相关信息
     */
    private final AppConfig appConfig;


    private final OssClient ossClient;

    private final RedisCache redisCache;


    @Override
    public Boolean flushKeys(Long id) {
        User user = getById(id);
        String username = user.getUsername();
        String accessKey = DigestUtil.md5Hex(SALT + username + RandomUtil.randomNumbers(5));
        String secretKey = DigestUtil.md5Hex(SALT + username + RandomUtil.randomNumbers(8));
        user = new User();
        user.setId(id);
        user.setAccessKey(accessKey);
        user.setSecretKey(secretKey);
        return updateById(user);
    }

    @Override
    public Page<UserVO> pageUserVO(UserQueryRequest userQueryRequest) {

        long current = 1;
        long size = 10;
        User userQuery = new User();
        if (userQueryRequest != null) {
            BeanUtils.copyProperties(userQueryRequest, userQuery);
            current = userQueryRequest.getCurrent();
            size = userQueryRequest.getPageSize();
        }
        String username = userQueryRequest.getUsername();
        String userRole = userQueryRequest.getUserRole();
        Integer gender = userQueryRequest.getGender();
        QueryWrapper<User> wrapper = new QueryWrapper<User>().like(StringUtils.isNotEmpty(username), "username", username)
                .eq(StrUtil.isNotEmpty(userRole) && ObjectUtil.isNotEmpty(RoleEnum.getEnumByValue(userRole)), "user_role", userRole)
                .eq(ObjectUtil.isNotEmpty(gender), "gender", gender);

        Page<User> userPage = this.page(new Page<>(current, size), wrapper);
        Page<UserVO> userVOPage = new PageDTO<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal());
        List<UserVO> userVOList = USER_CONVERTER.toListUserVo(userPage.getRecords());
        userVOPage.setRecords(userVOList);
        return userVOPage;
    }

    @Override
    public List<UserVO> listUserVO(UserQueryRequest userQueryRequest) {

        String username = userQueryRequest.getUsername();
        String userRole = userQueryRequest.getUserRole();
        Integer gender = userQueryRequest.getGender();
        List<User> userList = this.list(new QueryWrapper<User>().like(StringUtils.isNotEmpty(username), "user_name", username)
                .eq(StrUtil.isNotEmpty(userRole) && ObjectUtil.isNotEmpty(RoleEnum.getEnumByValue(userRole)), "user_role", userRole)
                .eq(ObjectUtil.isNotEmpty(gender), "gender", gender));
        return USER_CONVERTER.toListUserVo(userList);
    }

    @Override
    public Boolean updateCurrentUser(UserUpdateRequest userUpdateRequest) {
        User user = new User();
        //防止恶意修改
        user.setUserRole("");
        List<String> tags = userUpdateRequest.getTags();
        if(CollUtil.isNotEmpty(tags)){
            user.setTags(JSONObject.toJSONString(tags));
        }
        BeanUtils.copyProperties(userUpdateRequest, user);
        return this.updateById(user);

    }

    @Override
    public Long addUser(UserAddRequest userAddRequest) {
        User user = new User();
        BeanUtils.copyProperties(userAddRequest, user);
        user.setUserPassword(UserConstant.INIT_PASSWORD);
        boolean result = this.save(user);
        BusinessException.throwIf(!result, HttpCode.OPERATION_ERROR);
        return user.getId();
    }

    @Override
    public Boolean sendEmailCode(SendEmailRequest sendEmailRequest) {
        String email = sendEmailRequest.getEmail();
        Integer type = sendEmailRequest.getType();
        //检查redis中是否有相同的邮箱
        boolean hasCode = redisCache.getCacheObject(EmailConstant.EMAIIL_HEADER + type + "_" + email) != null;
        BusinessException.throwIf(hasCode, HttpCode.PARAMS_ERROR, "重复发送邮件");
        String emailCode = sendEmail(email, type);
        //设置15分钟的失效时间
        redisCache.setCacheObject(EmailConstant.EMAIIL_HEADER + type + "_" + email, emailCode, 15, TimeUnit.MINUTES);
        return true;
    }

    @Override
    public Boolean updatePasswordByFindBack(UserFindBackPasswordRequest userFindBackPasswordRequest) {

        String checkPassword = userFindBackPasswordRequest.getCheckPassword();
        String email = userFindBackPasswordRequest.getEmail();
        String userPassword = userFindBackPasswordRequest.getUserPassword();
        String emailCode = userFindBackPasswordRequest.getEmailCode();
        try {
            String code = redisCache.getCacheObject(EmailConstant.EMAIIL_HEADER + "1_" + email);
            BusinessException.throwIf(!emailCode.equals(code), HttpCode.PARAMS_ERROR, "验证码不符");

            return this.resetPasswordForFindBack(email, userPassword, checkPassword);
        } finally {
            redisCache.deleteObject(EmailConstant.EMAIIL_HEADER + "1_" + email);
        }
    }

    @Override
    public ResultData validFindback(UserEmailCodeRequest userEmailCodeRequest) {
        String code = userEmailCodeRequest.getEmailCode();
        String email = userEmailCodeRequest.getEmail();
        ResultData resultData = ResultData.success();
        String emailCode = redisCache.getCacheObject(EmailConstant.EMAIIL_HEADER + 1 + "_" + email);
        boolean flag = emailCode.equals(code);
        if (flag) {
            boolean existCheck = MybatisUtils.existCheck(this, Map.of("email", email));
            if (!existCheck) {
                resultData.put("code", HttpCode.PARAMS_ERROR);
                resultData.put("msg", "该邮箱没有注册用户");
            }
        }
        return resultData;
    }

    @Override
    public String uploadUserAvatar(MultipartFile file, Long uid) {
        validFile(file);
        String fileSuffix = FileUtil.getSuffix(file.getOriginalFilename());
        String fileName = "user_" + uid + "." + fileSuffix;
        User byId = this.getById(uid);
        if (byId.getUserAvatar() != null) {
            ossClient.deleteObject(FileConstant.BUCKET_NAME, fileName);
        }
        try {
            ossClient.putObject(FileConstant.BUCKET_NAME, fileName, file.getInputStream());
        } catch (IOException e) {
            throw new BusinessException(HttpCode.OPERATION_ERROR, "上传头像失败！");
        }

        String avatarUrl = ossClient.getObjectURL(FileConstant.BUCKET_NAME, fileName);
        this.update(new UpdateWrapper<User>().set("user_avatar", avatarUrl).eq("id", uid));
        return avatarUrl;
    }

    /**
     * 获取当前登录用户（允许未登录）
     *
     * @param request
     * @return
     */
    @Override
    public UserVO getLoginUserPermitNull(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            return null;
        }
        // 从数据库查询（追求性能的话可以注释，直接走缓存）
        long userId = currentUser.getId();
        return USER_CONVERTER.toUserVo(this.getById(userId));
    }

    @Override
    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        return USER_CONVERTER.toUserVo(user);
    }


    @Override
    public List<UserVO> getUserVO(List<User> userList) {
        if (CollectionUtils.isEmpty(userList)) {
            return new ArrayList<>();
        }
        return USER_CONVERTER.toListUserVo(userList);
    }
    //校验头像图片

    private void validFile(MultipartFile multipartFile) {
        // 文件大小
        long fileSize = multipartFile.getSize();
        // 文件后缀
        String fileSuffix = FileUtil.getSuffix(multipartFile.getOriginalFilename());

        if (fileSize > ONE_M) {
            throw new BusinessException(HttpCode.PARAMS_ERROR, "文件大小不能超过 10M");
        }
        if (!Arrays.asList("jpeg", "jpg", "svg", "png", "webp").contains(fileSuffix)) {
            throw new BusinessException(HttpCode.PARAMS_ERROR, "文件类型错误");
        }
    }

    @Override
    public Long userRegister(UserRegisterRequest userRegisterRequest) {
        // 1. 校验
        String username = userRegisterRequest.getUsername();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String nickname = userRegisterRequest.getNickname();
        String email = userRegisterRequest.getEmail();
        BusinessException.throwIf(!userPassword.equals(checkPassword), "两次密码不一致");
        //使用同步块来保证在高并发的环境下,同一时间有很多人用同一个账号注册冲突
        synchronized (username.intern()) {
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("username", username);
            Long aLong = this.baseMapper.selectCount(wrapper);
            BusinessException.throwIf(aLong > 0, "账号已存在！");

            // 3. 插入数据
            User user = User.builder().username(username).nickname(nickname).build();
            String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
            user.setUserPassword(encryptPassword);
            user.setUserRole(DEFAULT_ROLE);
            user.setEmail(email);
            String accessKey = DigestUtil.md5Hex(SALT + username + RandomUtil.randomNumbers(5));
            String secretKey = DigestUtil.md5Hex(SALT + username + RandomUtil.randomNumbers(8));
            user.setAccessKey(accessKey);
            user.setSecretKey(secretKey);

            int insert = this.baseMapper.insert(user);
            BusinessException.throwIf(insert < 0, "注册失败");
            return user.getId();
        }
    }

    @Override
    public UserVO userLogin(UserLoginRequest userLoginRequest) {

        String username = userLoginRequest.getAccount();
        String userPassword = userLoginRequest.getUserPassword();
        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        queryWrapper.nested(i -> i.eq("username", username)
                        .or().eq("email", username))
                .eq("user_password", encryptPassword);
        User user = this.baseMapper.selectOne(queryWrapper);
        // 用户不存在
        if (ObjectUtil.isEmpty(user)) {
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(HttpCode.PARAMS_ERROR, "用户不存在或密码错误");
        }
        UserVO userVO = USER_CONVERTER.toUserVo(user);
        // 3. 记录用户的登录态
        return userVO;

    }

    @Override
    public UserVO getLoginUser(HttpServletRequest request) {

        Object attribute = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) attribute;
        BusinessException.throwIf(currentUser == null || currentUser.getId() == null, "未登录");
        Long id = currentUser.getId();
        User byId = this.getById(id);
        BusinessException.throwIf(byId == null, "未登录");
        return USER_CONVERTER.toUserVo(byId);

    }

    @Override
    public boolean isAdmin(HttpServletRequest request) {
        Object attribute = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) attribute;
        return user != null && ADMIN_ROLE.equals(user.getUserRole());
    }

    @Override
    public boolean userLogout(HttpServletRequest request) {
        Object attribute = request.getSession().getAttribute(USER_LOGIN_STATE);

        BusinessException.throwIf(attribute == null, "未登录");

        request.getSession().removeAttribute(USER_LOGIN_STATE);

        return true;
    }

    @Override
    public String sendEmail(String email, Integer type) {
        //如果是注册，校验邮箱是否已存在
        if (Objects.equals(type, CommonConstant.ZERO)) {
            BusinessException.throwIf(MybatisUtils.existCheck(this, Map.of("email", email)), HttpCode.PARAMS_ERROR,
                    "邮箱已占用");
        }
        //随机的邮箱验证码
        String code = StringTools.getRandomNumber(CommonConstant.LENGTH_5);
        sendEmailCode(email, code);
        return code;
    }

    @Override
    public Boolean resetPasswordForFindBack(String email, String password, String checkPassword) {

        User user = this.getOne(new QueryWrapper<User>().eq("email", email));

        BusinessException.throwIf(user == null);

        BusinessException.throwIf(!password.equals(checkPassword), HttpCode.PARAMS_ERROR, "两次密码不一致");

        String encodePassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());
        return this.update(new UpdateWrapper<User>().set("user_password", encodePassword));
    }

    private void sendEmailCode(String toEmail, String code) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            //邮件发件人
            helper.setFrom(appConfig.getSendUserName());
            //邮件收件人 1或多个
            helper.setTo(toEmail);

            SysSettingsRequest sysSettingsDto = new SysSettingsRequest();

            //邮件主题
            helper.setSubject(sysSettingsDto.getRegisterEmailTitle());
            //邮件内容
            helper.setText(String.format(sysSettingsDto.getRegisterEmailContent(), code));
            //邮件发送时间
            helper.setSentDate(new Date());
            javaMailSender.send(message);
        } catch (Exception e) {
            log.error("邮件发送失败", e);
            throw new BusinessException(HttpCode.OPERATION_ERROR, "邮件发送失败");
        }
    }
}
