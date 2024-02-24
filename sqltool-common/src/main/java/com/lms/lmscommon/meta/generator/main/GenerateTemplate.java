package com.lms.lmscommon.meta.generator.main;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import com.lms.lmscommon.meta.Meta;
import com.lms.lmscommon.meta.MetaManager;

import java.io.File;
import java.io.IOException;


public  abstract class GenerateTemplate {

    public void doGenerate() throws IOException, InterruptedException {
        Meta metaObject = MetaManager.getMetaObject();
        System.out.println(metaObject);

        String projectPath = System.getProperty("user.dir");
        String outputPath=projectPath+ File.separator+"generated"+File.separator+metaObject.getName();
        if(!FileUtil.exist(outputPath)){
            FileUtil.mkdir(outputPath);
        }

        //复制原始文件
        String copySourcePath = copySource(metaObject, outputPath);

        //代码生成
        generateCode(metaObject,outputPath);

        //构建jar
        String jarPath = builderJar(metaObject, outputPath);

        //构建脚本
        String shellPath = buildScript(outputPath, jarPath);

        //构建精简版程序
        buildDist(outputPath,copySourcePath,jarPath,shellPath);

    }

    /**
     * 复制原始文件
     * @param meta
     * @param outputPath
     * @return
     */
    protected String copySource(Meta meta,String outputPath){
        String sourceRootPath = meta.getFileConfig().getSourceRootPath();
        String sourceCopyDestPath = outputPath + File.separator + ".source";
        FileUtil.copy(sourceRootPath, sourceCopyDestPath, false);
        return sourceCopyDestPath;
    }

    /**
     * 代码生成
     * @param metaObject
     * @param outputPath
     */
    protected void generateCode(Meta metaObject,String outputPath) throws TemplateException, IOException {
        String inputPath = new ClassPathResource("").getAbsolutePath();
        String basePackage = metaObject.getBasePackage();
        String outputBasePackagePath = StrUtil.join("/", StrUtil.split(basePackage, "."));
        String outputBaseJavaPackagePath=outputPath+File.separator+"src/main/java"+File.separator+outputBasePackagePath;
        String inputFilePath;
        String outputFilePath;
        inputFilePath=inputPath+"templates/java/model/DataModel.java.ftl";
        outputFilePath=outputBaseJavaPackagePath+"/model/DataModel.java";
        DynamicFileGenerator.doGenerate(inputFilePath,outputFilePath,metaObject);


        inputFilePath=inputPath+"templates/java/cli/command/ConfigCommand.java.ftl";
        outputFilePath=outputBaseJavaPackagePath+"/cli/command/ConfigCommand.java";
        DynamicFileGenerator.doGenerate(inputFilePath,outputFilePath,metaObject);

        inputFilePath=inputPath+"templates/java/cli/command/GenerateCommand.java.ftl";
        outputFilePath=outputBaseJavaPackagePath+"/cli/command/GenerateCommand.java";
        DynamicFileGenerator.doGenerate(inputFilePath,outputFilePath,metaObject);

        inputFilePath=inputPath+"templates/java/cli/command/ListCommand.java.ftl";
        outputFilePath=outputBaseJavaPackagePath+"/cli/command/ListCommand.java";
        DynamicFileGenerator.doGenerate(inputFilePath,outputFilePath,metaObject);

        inputFilePath=inputPath+"templates/java/cli/CommandExecutor.java.ftl";
        outputFilePath=outputBaseJavaPackagePath+"/cli/CommandExecutor.java";
        DynamicFileGenerator.doGenerate(inputFilePath,outputFilePath,metaObject);

        inputFilePath= inputPath+"templates/java/Main.java.ftl";
        outputFilePath=outputBaseJavaPackagePath+"/Main.java";
        DynamicFileGenerator.doGenerate(inputFilePath,outputFilePath,metaObject);

        inputFilePath=inputPath+"templates/java/generator/DynamicGenerator.java.ftl";
        outputFilePath=outputBaseJavaPackagePath+"/generator/DynamicGenerator.java";
        DynamicFileGenerator.doGenerate(inputFilePath,outputFilePath,metaObject);

        inputFilePath=inputPath+"templates/java/generator/MainGenerator.java.ftl";
        outputFilePath=outputBaseJavaPackagePath+"/generator/MainGenerator.java";
        DynamicFileGenerator.doGenerate(inputFilePath,outputFilePath,metaObject);

        inputFilePath=inputPath+"templates/java/generator/StaticGenerator.java.ftl";
        outputFilePath=outputBaseJavaPackagePath+"/generator/StaticGenerator.java";
        DynamicFileGenerator.doGenerate(inputFilePath,outputFilePath,metaObject);

        inputFilePath=inputPath+"templates/pom.xml.ftl";
        outputFilePath=outputPath+"/pom.xml";
        DynamicFileGenerator.doGenerate(inputFilePath,outputFilePath,metaObject);

    }

    /**
     * 构建jar包
     * @param meta
     * @param outputPath
     * @return
     */
    protected String builderJar(Meta meta, String outputPath) throws IOException, InterruptedException {
        JarGenerator.doGenerate(outputPath);
        String jarName = String.format("%s-%s-jar-with-dependencies.jar", meta.getName(), meta.getVersion());
        String jarPath = "target/" + jarName;
        return jarPath;
    }

    /**
     * 生成精简版程序
     * @param outputPath
     * @param sourceCopyDestPath
     * @param jarPath
     * @param shellOutputFilePath
     */
    protected String buildDist(String outputPath,String sourceCopyDestPath,String jarPath,String shellOutputFilePath){
        String distOutputPath = outputPath + "-dist";
        // 拷贝 jar 包
        String targetAbsolutePath = distOutputPath + File.separator + "target";
        FileUtil.mkdir(targetAbsolutePath);
        String jarAbsolutePath = outputPath + File.separator + jarPath;
        FileUtil.copy(jarAbsolutePath, targetAbsolutePath, true);
        // 拷贝脚本文件
        FileUtil.copy(shellOutputFilePath, distOutputPath, true);
        // 拷贝源模板文件
        FileUtil.copy(sourceCopyDestPath, distOutputPath, true);
        return distOutputPath;
    }

    /**
     * 封装脚本
     * @param outputPath
     * @param jarPath
     * @return
     */
    protected String buildScript(String outputPath,String jarPath) throws IOException {
        String shellOutputFilePath = outputPath + File.separator + "generator";
        ScriptGenerator.doGenerate(shellOutputFilePath, jarPath);
        return shellOutputFilePath;
    }

    /**
     * 压缩文件
     * @param outputPath
     * @return
     */
    protected String buildZip(String outputPath){
        String zipPath=outputPath+".zip";
        ZipUtil.zip(outputPath,zipPath);
        return zipPath;

    }



}
