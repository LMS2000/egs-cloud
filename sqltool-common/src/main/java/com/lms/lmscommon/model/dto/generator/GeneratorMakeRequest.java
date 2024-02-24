package com.lms.lmscommon.model.dto.generator;

import com.lms.lmscommon.meta.Meta;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lms2000
 */
@Data
@ApiModel(value = "GeneratorMakeRequest", description = "生成器制作请求")
public class GeneratorMakeRequest implements Serializable {
    /**
     * 元信息
     */
    @ApiModelProperty(value = "元信息")
    private Meta meta;

    /**
     * 模板文件压缩包路径
     */
    @ApiModelProperty(value = "模板文件压缩包路径")
    private String zipFilePath;
}
