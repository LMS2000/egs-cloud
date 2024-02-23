package com.lms.lmscommon.model.vo.userInterfaceInfo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lms.lmscommon.common.BaseVO;
import com.lms.lmscommon.model.vo.interfaceInfo.InterfaceInfoVO;
import com.lms.lmscommon.model.vo.user.UserVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户接口视图
 * @author lms2000
 * @since 2024-02-01
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "UserInterfaceInfoVO对象", description = "帖子视图")
public class UserInterfaceInfoVO extends BaseVO {



    /**
     * 调用用户 id
     */
    @ApiModelProperty("调用用户 id")
    private Long userId;


    /**
     * 用户
     */
    @ApiModelProperty("用户")
    private UserVO userVo;

    /**
     * 接口 id
     */
    @ApiModelProperty("接口 id")
    private Long interfaceInfoId;


    /**
     * 接口信息
     */
    @ApiModelProperty("接口信息")
    private InterfaceInfoVO interfaceInfoVo;

    /**
     * 总调用次数
     */
    @ApiModelProperty("总调用次数")
    private Integer totalNum;

    /**
     * 剩余调用次数
     */
    @ApiModelProperty("剩余调用次数")
    private Integer leftNum;

    /**
     * 0-正常，1-禁用
     */
    @ApiModelProperty(" 0-正常，1-禁用")
    private Integer status;



    /**
     * 是否删除(0-未删, 1-已删)
     */
    @ApiModelProperty("是否删除(0-未删, 1-已删)")
    private Integer deleteFlag;


}
