package com.lms.lmscommon.model.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lms.lmscommon.common.PageRequest;
import com.lms.lmscommon.validator.RangeCheck;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;


/**
 * 用户查询请求
 * @author lms2000
 * @since 2024-02-01
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "UserQueryRequest对象", description = "用户查询")
public class UserQueryRequest extends PageRequest implements Serializable {
    /**
     * id
     */
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 用户昵称
     */
    @ApiModelProperty(value = "昵称")
    private String nickname;

    /**
     * 账号
     */
    @ApiModelProperty(value = "账号")
    private String username;

//    /**
//     * 用户头像
//     */
//    private String userAvatar;

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

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private String createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private String updateTime;

}