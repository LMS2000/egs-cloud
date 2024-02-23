package com.lms.lmscommon.model.dto.field;

import com.lms.lmscommon.common.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


/**
 * 字段查询请求
 * @author lms2000
 * @since 2024-02-01
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "FieldInfoQueryRequest对象", description = "查询字段")
public class FieldInfoQueryRequest extends PageRequest implements Serializable {

    /**
     * 同时搜索名称或字段名称
     */
    @ApiModelProperty(value = "同时搜索名称或字段名称")
    private String searchName;

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
     * 内容，支持模糊查询
     */
    @ApiModelProperty(value = "字段内容")
    private String content;

    /**
     * 状态（0-待审核, 1-通过, 2-拒绝）
     */
    @ApiModelProperty(value = "状态（0-待审核, 1-通过, 2-拒绝）")
    private Integer reviewStatus;

    /**
     * 创建用户 id
     */
    @ApiModelProperty(value = "创建用户 id")
    private Long userId;

}