package com.lms.template;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.json.JSONUtil;
import com.lms.template.model.TemplateMakerConfig;


import java.io.File;

public class GenerateCodeTest {



    public void test() {
        String configStr = ResourceUtil.readUtf8Str("templateMaker.json");
        TemplateMakerConfig templateMakerConfig = JSONUtil.toBean(configStr, TemplateMakerConfig.class);
        long id = TemplateMaker.makeTemplate(templateMakerConfig);
        System.out.println(id);

    }



    public void testForMakeSpringBootTemplate() {
        String rootPath = "examples/springboot-init";

        String configStr = ResourceUtil.readUtf8Str(rootPath + File.separator + "templateMaker.json");
        TemplateMakerConfig templateMakerConfig = JSONUtil.toBean(configStr, TemplateMakerConfig.class);
        long id = TemplateMaker.makeTemplate(templateMakerConfig);
        System.out.println(id);

        configStr = ResourceUtil.readUtf8Str(rootPath + File.separator + "templateMaker1.json");
        templateMakerConfig = JSONUtil.toBean(configStr, TemplateMakerConfig.class);
        TemplateMaker.makeTemplate(templateMakerConfig);
        System.out.println(id);


        configStr = ResourceUtil.readUtf8Str(rootPath + File.separator + "templateMaker2.json");
        templateMakerConfig = JSONUtil.toBean(configStr, TemplateMakerConfig.class);
        TemplateMaker.makeTemplate(templateMakerConfig);
        System.out.println(id);

        configStr = ResourceUtil.readUtf8Str(rootPath + File.separator + "templateMaker3.json");
        templateMakerConfig = JSONUtil.toBean(configStr, TemplateMakerConfig.class);
        TemplateMaker.makeTemplate(templateMakerConfig);
        System.out.println(id);

        configStr = ResourceUtil.readUtf8Str(rootPath + File.separator + "templateMaker4.json");
        templateMakerConfig = JSONUtil.toBean(configStr, TemplateMakerConfig.class);
        TemplateMaker.makeTemplate(templateMakerConfig);
        System.out.println(id);
        configStr = ResourceUtil.readUtf8Str(rootPath + File.separator + "templateMaker5.json");
        templateMakerConfig = JSONUtil.toBean(configStr, TemplateMakerConfig.class);
        TemplateMaker.makeTemplate(templateMakerConfig);
        System.out.println(id);
        configStr = ResourceUtil.readUtf8Str(rootPath + File.separator + "templateMaker6.json");
        templateMakerConfig = JSONUtil.toBean(configStr, TemplateMakerConfig.class);
        TemplateMaker.makeTemplate(templateMakerConfig);
        System.out.println(id);
        configStr = ResourceUtil.readUtf8Str(rootPath + File.separator + "templateMaker7.json");
        templateMakerConfig = JSONUtil.toBean(configStr, TemplateMakerConfig.class);
        TemplateMaker.makeTemplate(templateMakerConfig);
        System.out.println(id);
        configStr = ResourceUtil.readUtf8Str(rootPath + File.separator + "templateMaker8.json");
        templateMakerConfig = JSONUtil.toBean(configStr, TemplateMakerConfig.class);
        TemplateMaker.makeTemplate(templateMakerConfig);
        System.out.println(id);
    }


    public void testForNeedPost() {
        String rootPath = "examples/springboot-init";
        String configStr = ResourceUtil.readUtf8Str(rootPath + File.separator + "templateMaker.json");
        TemplateMakerConfig templateMakerConfig = JSONUtil.toBean(configStr, TemplateMakerConfig.class);
        long id = TemplateMaker.makeTemplate(templateMakerConfig);
        System.out.println(id);

         configStr = ResourceUtil.readUtf8Str(rootPath + File.separator + "templateMaker1.json");
         templateMakerConfig = JSONUtil.toBean(configStr, TemplateMakerConfig.class);
         id = TemplateMaker.makeTemplate(templateMakerConfig);
        System.out.println(id);
//        configStr = ResourceUtil.readUtf8Str(rootPath + File.separator + "templateMaker2.json");
//        templateMakerConfig = JSONUtil.toBean(configStr, TemplateMakerConfig.class);
//        id = TemplateMaker.makeTemplate(templateMakerConfig);
//        System.out.println(id);
    }


}
