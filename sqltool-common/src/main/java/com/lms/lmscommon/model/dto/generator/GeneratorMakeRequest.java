package com.lms.lmscommon.model.dto.generator;

import com.lms.maker.meta.Meta;
import com.lms.maker.template.model.TemplateMakerFileConfig;
import com.lms.maker.template.model.TemplateMakerModelConfig;
import com.lms.maker.template.model.TemplateMakerOutputConfig;
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

    /**
     * 文件配置
     */
    @ApiModelProperty(value = "文件配置")
    private TemplateMakerFileConfig fileConfig;
    /**
     * 模型配置
     */
    @ApiModelProperty(value = "模型配置")
    private TemplateMakerModelConfig modelConfig;

    /**
     * 是否去重
     */
    @ApiModelProperty(value = "是否去重")
    private TemplateMakerOutputConfig outputConfig;

}
