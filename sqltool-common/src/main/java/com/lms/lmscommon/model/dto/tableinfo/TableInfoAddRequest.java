package com.lms.lmscommon.model.dto.tableinfo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * 创建表信息请求
 * @author lms2000
 * @since 2024-02-01
 */
@Data
@ApiModel(value = "TableInfoAddRequest对象", description = "创建表信息")
public class TableInfoAddRequest implements Serializable {

    /**
     * 名称
     */
    @ApiModelProperty(value = "根据sql代码段生成表信息")
    @NotNull(message = "名称不能为空")
    @NotBlank(message = "名称不能为空")
    private String name;

    /**
     * 内容
     */
    @ApiModelProperty(value = "根据sql代码段生成表信息")
    @NotNull(message = "名称不能为空")
    @NotBlank(message = "名称不能为空")
    private String content;

}