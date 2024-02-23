package com.lms.lmscommon.model.dto.tableinfo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Positive;
import java.io.Serializable;

/**
 * 更新请求
 *
 */
@Data
@ApiModel(value = "TableInfoUpdateRequest对象", description = "更新表信息")
public class TableInfoUpdateRequest implements Serializable {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    @Positive(message = "id不合法")
    private long id;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;

    /**
     * 内容
     */
    @ApiModelProperty(value = "内容，支持模糊查询")
    private String content;

    /**
     * 状态（0-待审核, 1-通过, 2-拒绝）
     */
    @ApiModelProperty(value = "状态（0-待审核, 1-通过, 2-拒绝）")
    private Integer reviewStatus;

    /**
     * 审核信息
     */
    @ApiModelProperty(value = "审核信息")
    private String reviewMessage;
}