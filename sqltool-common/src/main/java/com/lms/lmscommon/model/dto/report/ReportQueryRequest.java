package com.lms.lmscommon.model.dto.report;

import com.lms.lmscommon.common.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询请求
 * @author lms2000
 * @since 2024-02-01
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "ReportQueryRequest对象", description = "查询举报信息")
public class ReportQueryRequest extends PageRequest implements Serializable {

    /**
     * 内容
     */
    @ApiModelProperty(value = "内容")
    private String content;

    /**
     * 举报实体类型（0-词库）
     */
    @ApiModelProperty(value = "举报实体类型（0-词库）")
    private Integer type;

    /**
     * 被举报对象 id
     */
    @ApiModelProperty(value = "被举报对象 id")
    private Long reportedId;

    /**
     * 举报用户 id
     */
    @ApiModelProperty(value = "举报对象 id")
    private Long reportedUserId;

    /**
     * 状态（0-未处理, 1-已处理）
     */
    @ApiModelProperty(value = "举报状态")
    private Integer status;

    /**
     * 创建用户 id
     */
    @ApiModelProperty(value = "创建用户")
    private Long userId;
}