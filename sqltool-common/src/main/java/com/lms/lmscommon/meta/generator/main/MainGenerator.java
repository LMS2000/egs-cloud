package com.lms.lmscommon.meta.generator.main;


import freemarker.template.TemplateException;

import java.io.IOException;


public class MainGenerator extends GenerateTemplate {


    public static void main(String[] args) throws TemplateException, IOException, InterruptedException {

        GenerateTemplate mainGenerator=new ZipGenerator();
        mainGenerator.doGenerate();

    }
}
