package com.lms.lmscommon.model.vo.user;

import com.lms.lmscommon.common.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户视图
 * @author lms2000
 * @since 2024-02-01
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "UserVO对象", description = "帖子视图")
public class UserVO extends BaseVO {


    /**
     * 用户昵称
     */
    @ApiModelProperty("用户名")
    private String username;

    /**
     * 账号
     */
    @ApiModelProperty("昵称")
    private String nickname;

    /**
     * 用户头像
     */
    @ApiModelProperty("用户头像")
    private String userAvatar;

    /**
     * 性别
     */
    @ApiModelProperty("性别")
    private Integer gender;

    /**
     * 用户角色: user, admin
     */
    @ApiModelProperty("用户角色")
    private String userRole;


    /**
     * 公钥
     */
    @ApiModelProperty("用户公钥")
    private String accessKey;
    /**
     * 私钥
     */
    @ApiModelProperty("用户密钥")
    private String secretKey;
    /**
     * 私钥
     */
    @ApiModelProperty("是否可用")
    private Integer enableFlag;


}