package com.lms.lmscommon.model.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 校验邮箱请求
 * @author lms2000
 * @since 2024-02-01
 */
@Data
@ApiModel(value = "UserEmailCodeRequest对象", description = "校验邮箱")
public class UserEmailCodeRequest implements Serializable {


    /**
     * 邮箱名
     */
    @NotNull(message = "email不能为空")
    @NotBlank(message = "email不能为空")
    @Email(message = "email格式不正确")
    @ApiModelProperty(value = "邮箱名")
    private String email;

    /**
     * 邮箱验证码
     */
    @NotNull(message = "邮箱验证码不能为空")
    @NotBlank(message = "邮箱验证码不能为空")
    @ApiModelProperty(value = "邮箱验证码")
    private String emailCode;
}
