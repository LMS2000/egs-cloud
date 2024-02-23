package com.lms.lmscommon.model.dto.report;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;


/**
 * 更新请求
 * @author lms2000
 * @since 2024-02-01
 */
@Data
@ApiModel(value = "ReportUpdateRequest对象", description = "更新举报信息")
public class ReportUpdateRequest implements Serializable {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    @Positive(message = "主键")
    @NotNull(message = "id不能为空")
    private Long id;

    /**
     * 举报实体类型（0-词库）
     */
    @ApiModelProperty(value = "举报实体类型（0-词库）")
    private Integer type;

    /**
     * 状态（0-未处理, 1-已处理）
     */

    @ApiModelProperty(value = "状态")
    private Integer status;
}