package com.lms.lmscommon.model.dto.field;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * 字段创建请求
 * @author lms2000
 * @since 2024-02-01
 */
@Data
@ApiModel(value = "FieldInfoAddRequest对象", description = "添加字段")
public class FieldInfoAddRequest implements Serializable {

    /**
     * 名称
     */
    @NotBlank(message = "字段名称")
    @NotNull(message = "字段名称")
    @ApiModelProperty(value = "字段名称")
    private String name;

    /**
     * 内容
     */
    @NotBlank(message = "内容名称")
    @NotNull(message = "内容名称")
    @ApiModelProperty(value = "内容名称")
    private String content;
}