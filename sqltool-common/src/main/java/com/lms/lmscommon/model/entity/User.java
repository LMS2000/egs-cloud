package com.lms.lmscommon.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.lms.lmscommon.common.DeleteFlagEntity;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户
 *
 * @TableName user
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "user")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends DeleteFlagEntity {


    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 账号
     */
    private String username;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 标签列表(json)
     */
    private String tags;

    /**
     * 用户角色: user, admin
     */
    private String userRole;

    /**
     * 密码
     */
    private String userPassword;




    private String email;

    private String profile;


    private String accessKey;
    private String secretKey;


    private String openId;

    @TableField("is_enable")
    private Integer enableFlag;

}