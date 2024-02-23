package com.lms.lmscommon.model.dto.sql;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * ai生成sql请求
 * @author lms2000
 * @since 2024-02-01
 */
@Data
@ApiModel(value = "AiCreateSqlRequest对象", description = "ai生成sql请求")
public class AiCreateSqlRequest implements Serializable {

    /**
     * 需求消息
     */
    @ApiModelProperty(value = "sql需求消息")
    private String message;
}
