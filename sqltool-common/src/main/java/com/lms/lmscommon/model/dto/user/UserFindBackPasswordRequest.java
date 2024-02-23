package com.lms.lmscommon.model.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * 用户找回密码请求
 * @author lms2000
 * @since 2024-02-01
 */
@Data
@ApiModel(value = "UserFindBackPasswordRequest对象", description = "用户找回密码")
public class UserFindBackPasswordRequest implements Serializable {


    /**
     * 邮箱名
     */
    @NotNull(message = "邮箱不能为空")
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @ApiModelProperty(value = "邮箱名")
    private String email;

    /**
     * 邮箱验证码
     */
    @NotNull(message = "邮箱验证码不能为空")
    @NotBlank(message = "邮箱验证码不能为空")
    @ApiModelProperty(value = "邮箱验证码")
    private String emailCode;

    /**
     * 新密码
     */
    @NotNull(message = "新密码不能为空")
    @NotBlank(message = "新密码不能为空")
    @ApiModelProperty(value = "新密码")
    private String userPassword;

    /**
     * 确认密码
     */
    @NotNull(message = "确认密码不能为空")
    @NotBlank(message = "确认密码不能为空")
    @ApiModelProperty(value = "确认密码")
    private String checkPassword;
}
