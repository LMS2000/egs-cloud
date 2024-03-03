package com.lms.lmscommon.model.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;
import java.io.Serializable;


/**
 * 用户登录请求
 * @author lms2000
 * @since 2024-02-01
 */
@Data
@Validated
@ApiModel(value = "UserLoginRequest对象", description = "用户登录")
public class UserLoginRequest implements Serializable {


    /**
     * 账号
     */
    @NotNull(message = "账号不能为空")
    @NotBlank(message = "账号不能为空")
//    @Min(value = 4)
    @Size(min = 4,message = "账号不能小于4位")
    @ApiModelProperty(value = "账号")
    private String account;

    /**
     * 密码
     */
    @NotNull(message = "密码不能为空")
    @NotBlank(message = "密码不能为空")
    @Size(min = 8,message = "密码过短")
    @ApiModelProperty(value = "用户密码")
    private String userPassword;

    /**
     * 图片验证码
     */
    @NotNull(message = "图片验证码不能为空")
    @NotBlank(message = "图片验证码不能为空")
    @ApiModelProperty(value = "图片验证码")
    private String code;
}
