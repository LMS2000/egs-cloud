package com.lms.generator.file;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.StrUtil;
import com.lms.maker.meta.Meta;
import com.lms.maker.meta.MetaManager;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

public class FileGenerator {
    /**
     * 生成
     *
     * @param model 数据模型
     * @throws TemplateException
     * @throws IOException
     */
    public static void doGenerate(Object model) throws TemplateException, IOException {

        String projectPath = System.getProperty("user.dir");
        File file=new File(projectPath).getParentFile();
        //输入目录，ACM实列模板的目录
        String inputPath=new File(file,"lmszi-generator-demo-projects/acm-template").getAbsolutePath();
        String outputPath=projectPath;
        StaticGenerator.copeFilesByHuTool(inputPath,outputPath);
        // 生成动态文件
        String inputDynamicFilePath = projectPath + File.separator + "src/main/resources/templates/MainTemplate.java.ftl";
        String outputDynamicFilePath = outputPath + File.separator + "acm-template/src/com/lms/acm/MainTemplate.java";
        DynamicFileGenerator.doGenerate(inputDynamicFilePath, outputDynamicFilePath, model);
    }

    public static void main(String[] args) throws TemplateException, IOException {
        Meta metaObject = MetaManager.getMetaObject();
        System.out.println(metaObject);

        String projectPath = System.getProperty("user.dir");
        String outputPath=projectPath+File.separator+"generated"+File.separator+metaObject.getName();
        if(!FileUtil.exist(outputPath)){
            FileUtil.mkdir(outputPath);
        }

        String inputPath = new ClassPathResource("").getAbsolutePath();
        String basePackage = metaObject.getBasePackage();
        String outputBasePackagePath = StrUtil.join("/", StrUtil.split(basePackage, "."));
        String outputBaseJavaPackagePath=outputPath+File.separator+"src/main/java"+File.separator+outputBasePackagePath;
        String inputFilePath;
        String outputFilePath;
        inputFilePath=inputPath+File.separator+"templates/java/model/DataModel.java.ftl";
        outputFilePath=outputBaseJavaPackagePath+"/model/DataModel.java";
        DynamicFileGenerator.doGenerate(inputFilePath,outputFilePath,metaObject);


        inputFilePath=inputPath+File.separator+"templates/java/cli/command/ConfigCommand.java.ftl";
        outputFilePath=outputBaseJavaPackagePath+"/cli/command/ConfigCommand.java";
        DynamicFileGenerator.doGenerate(inputFilePath,outputFilePath,metaObject);

//        inputFilePath=inputPath+File.separator+"templates/java/cli/command/GenerateCommand.java.ftl";
//        outputFilePath=outputBaseJavaPackagePath+"/cli/command/GenerateCommand.java";
//        DynamicGenerator.doGenerate(inputFilePath,outputFilePath,metaObject);

        inputFilePath=inputPath+File.separator+"templates/java/cli/command/ListCommand.java.ftl";
        outputFilePath=outputBaseJavaPackagePath+"/cli/command/ListCommand.java";
        DynamicFileGenerator.doGenerate(inputFilePath,outputFilePath,metaObject);

        inputFilePath=inputPath+File.separator+"templates/java/cli/CommandExecutor.java.ftl";
        outputFilePath=outputBaseJavaPackagePath+"/cli/CommandExecutor.java";
        DynamicFileGenerator.doGenerate(inputFilePath,outputFilePath,metaObject);

        inputFilePath=inputPath+File.separator+"templates/java/Main.java.ftl";
        outputFilePath=outputBaseJavaPackagePath+"/Main.java";
        DynamicFileGenerator.doGenerate(inputFilePath,outputFilePath,metaObject);
    }
}
