package com.lms.lmscommon.model.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.checkerframework.checker.units.qual.Length;

import javax.validation.constraints.*;
import java.io.Serializable;


/**
 * 用户注册请求
 * @author lms2000
 * @since 2024-02-01
 */
@Data
@ApiModel(value = "UserRegisterRequest对象", description = "用户注册")
public class UserRegisterRequest implements Serializable {


    /**
     * 用户名
     */
    @NotNull(message = "用户名不能为空")
    @NotBlank(message = "用户名不能为空")
    @Max(value = 16,message = "用户名过长")
    @ApiModelProperty(value = "用户昵称")
    private String nickname;
    /**
     * 账号
     */
    @NotNull(message = "账号不能为空")
    @NotBlank(message = "账号不能为空")
    @Min(value = 4,message = "账号过短")
    @ApiModelProperty(value = "账号")
    private String username;
    /**
     * 密码
     */
    @NotNull(message = "密码不能为空")
    @NotBlank(message = "密码不能为空")
    @Min(value = 8,message = "密码过短")
    @ApiModelProperty(value = "密码")
    private String userPassword;

    /**
     * 确认密码
     */
    @NotNull(message = "确认密码不能为空")
    @NotBlank(message = "确认密码不能为空")
    @Min(value = 8,message = "密码过短")
    @ApiModelProperty(value = "确认密码")
    private String checkPassword;

    /**
     * 邮箱
     */
    @NotNull(message = "邮箱不能为空")
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @ApiModelProperty(value = "邮箱")
    private String email;

    /**
     * 邮箱验证码
     */
    @NotNull(message = "邮箱验证码不能为空")
    @NotBlank(message = "邮箱验证码不能为空")
    @ApiModelProperty(value = "邮箱验证码")
    private String emailCode;
}
