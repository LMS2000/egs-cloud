package com.lms.lmscommon.model.dto.dict;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * 字典创建请求
 * @author lms2000
 * @since 2024-02-01
 */
@Data
@ApiModel(value = "DictAddRequest对象", description = "添加字典")
public class DictAddRequest implements Serializable {

    /**
     * 名称
     */
    @NotBlank(message = "字典名称")
    @NotNull(message = "字典名称")
    @ApiModelProperty(value = "字典名称")
    private String name;

    /**
     * 内容
     */
    @NotBlank(message = "字典内容说明")
    @NotNull(message = "字典内容说明")
    @ApiModelProperty(value = "字典内容说明")
    private String content;
}