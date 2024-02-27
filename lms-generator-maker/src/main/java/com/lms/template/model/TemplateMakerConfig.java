package com.lms.template.model;

import com.lms.maker.meta.Meta;
import lombok.Data;


/**
 * 模板制作配置类
 */
@Data
public class TemplateMakerConfig {

    private Long id;

    private Meta meta=new Meta();

    private String originProjectPath;
    TemplateMakerFileConfig fileConfig=new TemplateMakerFileConfig();
    TemplateMakerModelConfig modelConfig=new TemplateMakerModelConfig();

    TemplateMakerOutputConfig templateMakerOutputConfig=new TemplateMakerOutputConfig();

}
