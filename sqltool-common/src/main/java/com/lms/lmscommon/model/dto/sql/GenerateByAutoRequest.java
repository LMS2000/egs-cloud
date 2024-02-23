package com.lms.lmscommon.model.dto.sql;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 智能生成请求体
 * @author lms2000
 * @since 2024-02-01
 */
@Data
@ApiModel(value = "GenerateByAutoRequest对象", description = "智能生成请求体")
public class GenerateByAutoRequest implements Serializable {

    /**
     * content 内容为 文本 以,隔开
     */
    @ApiModelProperty(value = "根据content内容生成表信息")
    private String content;
}
