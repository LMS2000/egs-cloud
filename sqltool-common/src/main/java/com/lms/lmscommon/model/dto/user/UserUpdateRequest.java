package com.lms.lmscommon.model.dto.user;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;


/**
 * 用户更新请求
 * @author lms2000
 * @since 2024-02-01
 */
@Data
@ApiModel(value = "UserUpdateRequest对象", description = "用户修改")
public class UserUpdateRequest implements Serializable {
    /**
     * id
     */
    @NotNull(message = "id不能为空")
    @Positive(message = "id不合法")
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 用户昵称
     */
    @ApiModelProperty(value = "用户昵称")
    private String nickname;


    /**
     * 用户头像
     */
    @ApiModelProperty(value = "用户头像")
    private String userAvatar;

    /**
     * 性别
     */
    @ApiModelProperty(value = "用户性别")
    private Integer gender;

    /**
     * 用户角色: user, admin
     */
    @ApiModelProperty(value = "用户角色")
    private String userRole;

    @ApiModelProperty(value = "用户简介")
    private String profile;
}