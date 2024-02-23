package com.lms.lmscommon.model.dto.email;


import com.lms.lmscommon.validator.RangeCheck;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
/**
 * 发送邮箱请求
 * @author lms2000
 * @since 2024-02-01
 */
@Data
@ApiModel(value = "SendEmailRequest对象", description = "发送邮箱")
public class SendEmailRequest implements Serializable {

    /**
     * 邮箱
     */
    @Email(message = "邮箱格式不正确")
    @NotNull(message = "邮箱不能为空")
    @NotBlank(message = "邮箱不能为空")
    @ApiModelProperty(value = "邮箱")
    private String email;

    /**
     * 邮件类型 0注册 1 找回密码
     */
    @RangeCheck(range = {0,1})
    @NotNull(message = "邮件类型不能为空")
    @ApiModelProperty(value = "邮件类型")
    private Integer type;
    /**
     * 图片校验码
     */
    @NotNull(message = "图片校验码不能为空")
    @NotBlank(message = "图片校验码不能为空")
    @ApiModelProperty(value = "图片校验码")
    private String code;
}
