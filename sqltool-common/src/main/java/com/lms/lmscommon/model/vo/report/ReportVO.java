package com.lms.lmscommon.model.vo.report;

import com.lms.common.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lms2000
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "ReportVO对象", description = "举报信息视图")
public class ReportVO extends BaseVO {
    /**
     * 内容
     */
    @ApiModelProperty("内容")
    private String content;

    /**
     * 举报实体类型（0-词库）
     */
    @ApiModelProperty("举报实体类型（0-词库）")
    private Integer type;

    /**
     * 被举报对象 id
     */
    @ApiModelProperty("被举报对象 id")
    private Long reportedId;

    /**
     * 被举报用户 id
     */
    @ApiModelProperty("被举报用户 id")
    private Long reportedUserId;

    /**
     * 状态（0-未处理, 1-已处理）
     */
    @ApiModelProperty("状态（0-未处理, 1-已处理）")
    private Integer status;

    /**
     * 创建用户 id
     */
    @ApiModelProperty("创建用户 id")
    private Long userId;
}
