package com.lms.lmscommon.model.dto.report;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;

/**
 * 创建请求
 * @author lms2000
 * @since 2024-02-01
 */
@Data
@ApiModel(value = "ReportAddRequest对象", description = "创建举报信息")
public class ReportAddRequest implements Serializable {

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
    @NotNull(message = "被举报对象id不能为空")
    @Positive(message = "id不合法")
    private Long reportedId;
}