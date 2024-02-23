package com.lms.lmscommon.model.dto.sql;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 根据 SQL 生成请求体
 * @author lms2000
 * @since 2024-02-01
 */
@Data
@ApiModel(value = "GenerateBySqlRequest对象", description = "智能生成请求体")
public class GenerateBySqlRequest implements Serializable {

    /**
     * sql代码段
     */
    @ApiModelProperty(value = "根据sql代码段生成表信息")
    private String sql;
}
