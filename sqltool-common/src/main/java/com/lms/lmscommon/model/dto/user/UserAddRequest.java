package com.lms.lmscommon.model.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 用户创建请求
 * @author lms2000
 * @since 2024-02-01
 */
@Data
@ApiModel(value = "UserAddRequest对象", description = "添加用户")
public class UserAddRequest implements Serializable {

    /**
     * 用户昵称
     */
    @NotNull(message = "昵称不能为空")
    @NotBlank(message = "昵称不能为空")
    @ApiModelProperty(value = "昵称")
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
     * 用户头像
     */
    @ApiModelProperty(value = "用户头像")
    private String userAvatar;

    /**
     * 性别
     */
    @ApiModelProperty(value = "性别")
    private Integer gender;

    /**
     * 用户角色: user, admin
     */
    @ApiModelProperty(value = "角色")
    private String userRole;

    @ApiModelProperty(value = "简介")
    private String profile;

    /**
     * 密码
     */
    @ApiModelProperty(value = "用户密码")
    private String userPassword;

    private static final long serialVersionUID = 1L;
}