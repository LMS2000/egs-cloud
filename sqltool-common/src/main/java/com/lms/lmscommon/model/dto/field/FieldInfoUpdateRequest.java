package com.lms.lmscommon.model.dto.field;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 字段更新请求
 * @author lms2000
 * @since 2024-02-01
 */
@Data
@ApiModel(value = "FieldInfoUpdateRequest对象", description = "更新字段")
public class FieldInfoUpdateRequest implements Serializable {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private long id;

    /**
     * 名称
     */
    @ApiModelProperty(value = "字段名称")
    private String name;

    /**
     * 字段名称
     */
    @ApiModelProperty(value = "字段名称")
    private String fieldName;

    /**
     * 内容
     */
    @ApiModelProperty(value = "字段内容")
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