package com.lms.template;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.lms.maker.meta.Meta;
import com.lms.maker.meta.enums.FileGenerateTypeEnum;
import com.lms.maker.meta.enums.FileTypeEnum;
import com.lms.template.enums.FileFilterRangeEnum;
import com.lms.template.enums.FileFilterRuleEnum;
import com.lms.template.model.*;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class TemplateMaker {
    public static void main(String[] args) {


        Meta meta = new Meta();
        //输入项目信息
        String name = "acm-template-generator";
        String description = "ACM 示例模板生成器";
        meta.setName(name);
        meta.setDescription(description);

        //文件信息
        String projectPath = System.getProperty("user.dir");
        String originProjectPath = new File(projectPath).getParent() + File.separator + "lmszi-generator-demo-projects/springboot-init";
        String fileInputPath1 = "src/main/java/com/yupi/springbootinit/common";
        String fileInputPath2 = "src/main/resources/application.yml";

//        Meta.ModelConfig.ModelInfo modelInfo=new Meta.ModelConfig.ModelInfo();
//        modelInfo.setFieldName("outputText");
//        modelInfo.setType("String");
//        modelInfo.setDefaultValue("sum=");
//
//        String searchStr="Sum: ";

        Meta.ModelConfig.ModelInfo modelInfo = new Meta.ModelConfig.ModelInfo();
        modelInfo.setFieldName("className");
        modelInfo.setType("String");
        String searchStr = "BaseResponse";

        //文件过滤
        TemplateMakerFileConfig templateMakerFileConfig = new TemplateMakerFileConfig();
        TemplateMakerFileConfig.FileInfoConfig fileInfoConfig1 = new TemplateMakerFileConfig.FileInfoConfig();
        fileInfoConfig1.setPath(fileInputPath1);
        List<FileFilterConfig> filterConfigList = new ArrayList<>();
        FileFilterConfig fileFilterConfig = FileFilterConfig.builder().range(FileFilterRangeEnum.FILE_NAME.getValue())
                .rule(FileFilterRuleEnum.CONTAINS.getValue())
                .value("Base")
                .build();
        filterConfigList.add(fileFilterConfig);

        TemplateMakerFileConfig.FileInfoConfig fileInfoConfig2 = new TemplateMakerFileConfig.FileInfoConfig();
        fileInfoConfig2.setPath(fileInputPath2);
        templateMakerFileConfig.setFiles(Arrays.asList(fileInfoConfig1, fileInfoConfig2));

        // 设置分组的配置
        TemplateMakerFileConfig.FileGroupConfig fileGroupConfig = new TemplateMakerFileConfig.FileGroupConfig();
        fileGroupConfig.setGroupKey("test");
        fileGroupConfig.setGroupName("测试分组");
        fileGroupConfig.setCondition("outputTest");
        templateMakerFileConfig.setFileGroupConfig(fileGroupConfig);


        TemplateMakerModelConfig templateMakerModelConfig = new TemplateMakerModelConfig();

        // 模型分组

        TemplateMakerModelConfig.ModelGroupConfig modelGroupConfig = new TemplateMakerModelConfig.ModelGroupConfig();

        modelGroupConfig.setGroupKey("mysql");
        modelGroupConfig.setGroupName("数据库配置");
        templateMakerModelConfig.setModelGroupConfig(modelGroupConfig);

        // 模型配置

        TemplateMakerModelConfig.ModelInfoConfig modelInfoConfig1 = new TemplateMakerModelConfig.ModelInfoConfig();
        modelInfoConfig1.setFieldName("url");
        modelInfoConfig1.setType("String");
        modelInfoConfig1.setDefaultValue("jdbc:mysql://localhost:3306/my_db");
        modelInfoConfig1.setReplaceText("jdbc:mysql://localhost:3306/my_db");

        TemplateMakerModelConfig.ModelInfoConfig modelInfoConfig2 = new TemplateMakerModelConfig.ModelInfoConfig();
        modelInfoConfig2.setFieldName("username");
        modelInfoConfig2.setType("String");
        modelInfoConfig2.setDefaultValue("root");
        modelInfoConfig2.setReplaceText("root");

        TemplateMakerModelConfig.ModelInfoConfig modelInfoConfig3 = new TemplateMakerModelConfig.ModelInfoConfig();
        modelInfoConfig3.setFieldName("className");
        modelInfoConfig3.setType("String");
        modelInfoConfig3.setDefaultValue("root");
        modelInfoConfig3.setReplaceText("BaseResponse");

        List<TemplateMakerModelConfig.ModelInfoConfig> modelInfoConfigList = Arrays.asList(modelInfoConfig1, modelInfoConfig2, modelInfoConfig3);
        templateMakerModelConfig.setModels(modelInfoConfigList);
//        long id = makeTemplate(meta, originProjectPath, templateMakerFileConfig, templateMakerModelConfig, 1749616683377049600L);
//        System.out.println(id);


//        long id = makeTemplate(meta, originProjectPath, fileInputPath, modelInfo, searchStr, 1744546627474243584L);
//        System.out.println(id);


//        String metaPath="E:\\lmszi\\lmszi_generator\\lmszi-generator-maker\\.temp\\1744546627474243584\\acm-template\\meta.json";
//        Meta oldMeta=JSONUtil.toBean(FileUtil.readUtf8String(metaPath),Meta.class);
//        System.out.println(oldMeta);
//        // 复制目录
//
//        long id = IdUtil.getSnowflakeNextId();
//        String tempDirPath=projectPath+File.separator+".temp";
//        String templatePath=tempDirPath+File.separator+id;
//        if(!FileUtil.exist(templatePath)){
//            FileUtil.mkdir(tempDirPath);
//            FileUtil.copy(originProjectPath,templatePath,true);
//        }
//
//
//        String sourceRootPath=templatePath+File.separator+FileUtil.getLastPathEle(Paths.get(originProjectPath)).toString();
//
//        String fileOutputPath=fileInputPath+".ftl";
//
//
//
//
//        //使用字符串去替换生成模板文件
//        String fileInputAbsolutePath=sourceRootPath+File.separator+fileInputPath;
//        String fileOutputAbsolutePath=sourceRootPath+File.separator+fileOutputPath;
//        String fileContent=null;
//         if(FileUtil.exist(fileOutputAbsolutePath)){
//             fileContent=FileUtil.readUtf8String(fileOutputAbsolutePath);
//         }else{
//             fileContent=FileUtil.readUtf8String(fileInputAbsolutePath);
//         }
//        String replacement=String.format("${%s}",modelInfo.getFieldName());
//        String newFileContent= StrUtil.replace(fileContent,"Sum: ",replacement);
//
//        //输出模板
//
//        FileUtil.writeUtf8String(newFileContent,fileOutputAbsolutePath);
//
//
//        //生成配置文件
//
//        Meta.FileConfig.FileInfo fileInfo=new Meta.FileConfig.FileInfo();
//        fileInfo.setInputPath(fileInputPath);
//        fileInfo.setOutputPath(fileOutputPath);
//        fileInfo.setType(FileTypeEnum.FILE.getValue());
//        fileInfo.setGenerateType(FileGenerateTypeEnum.DYNAMIC.getValue());
//        String metaOutputPath=sourceRootPath+File.separator+"meta.json";
//        // 如果说已经有meta文件，说明不是第一次制作，则在meta基础上进行修改
//        if(FileUtil.exist(metaOutputPath)){
//            Meta oldMeta=JSONUtil.toBean(FileUtil.readUtf8String(metaOutputPath),Meta.class);
//
//            //追加参数列表
//            List<Meta.FileConfig.FileInfo> fileInfoList=oldMeta.getFileConfig().getFiles();
//            fileInfoList.add(fileInfo);
//            List<Meta.ModelConfig.ModelInfo> modelInfoList=oldMeta.getModelConfig().getModels();
//            modelInfoList.add(modelInfo);
//            //配置去重
//            oldMeta.getFileConfig().setFiles(distinctFiles(fileInfoList));
//            oldMeta.getModelConfig().setModels(distinctModels(modelInfoList));
//
//            //更新元信息文件
//            FileUtil.writeUtf8String(JSONUtil.toJsonPrettyStr(oldMeta),metaOutputPath);
//        }else{
//            Meta meta=new Meta();
//            meta.setName(name);
//            meta.setDescription(description);
//            Meta.FileConfig fileConfig=new Meta.FileConfig();
//            meta.setFileConfig(fileConfig);
//
//            fileConfig.setSourceRootPath(sourceRootPath);
//            List<Meta.FileConfig.FileInfo> fileInfoList=new ArrayList<>();
//            fileConfig.setFiles(fileInfoList);
//            fileInfoList.add(fileInfo);
//
//
//            Meta.ModelConfig modelConfig=new Meta.ModelConfig();
//            meta.setModelConfig(modelConfig);
//            List<Meta.ModelConfig.ModelInfo> modelInfoList=new ArrayList<>();
//            modelConfig.setModels(modelInfoList);
//            modelInfoList.add(modelInfo);
//            //输出元信息
//            FileUtil.writeUtf8String(JSONUtil.toJsonPrettyStr(meta),metaOutputPath);
//        }
    }

    /**
     * 制作模板
     * @param templateMakerConfig
     * @return
     */
    public static long makeTemplate(TemplateMakerConfig templateMakerConfig) {
        TemplateMakerModelConfig modelConfig = templateMakerConfig.getModelConfig();
        TemplateMakerFileConfig fileConfig = templateMakerConfig.getFileConfig();
        Long id = templateMakerConfig.getId();
        String originProjectPath = templateMakerConfig.getOriginProjectPath();
        Meta meta = templateMakerConfig.getMeta();
        TemplateMakerOutputConfig templateMakerOutputConfig = templateMakerConfig.getTemplateMakerOutputConfig();

        return makeTemplate(meta, originProjectPath, fileConfig, modelConfig,templateMakerOutputConfig, id);
    }

    private static long makeTemplate(Meta newMeta, String originProjectPath,
                                     TemplateMakerFileConfig templateMakerFileConfig,
                                     TemplateMakerModelConfig templateMakerModelConfig,
                                     TemplateMakerOutputConfig templateMakerOutputConfig,
                                     Long id) {

        if (id == null) {
            id = IdUtil.getSnowflakeNextId();
        }

        //文件信息
        String projectPath = System.getProperty("user.dir");
        // 复制目录
        String tempDirPath = projectPath + File.separator + ".temp";
        String templatePath = tempDirPath + File.separator + id;
        if (!FileUtil.exist(templatePath)) {
            FileUtil.mkdir(tempDirPath);
            FileUtil.copy(originProjectPath, templatePath, true);
        }

        //FileUtil.getLastPathEle 返回路径中最后一个路径
//        lastPathElement 的值将是 "file.txt"，因为这是路径 /path/to/some/file.txt 中的最后一个路径元素。
        String sourceRootPath = FileUtil.loopFiles(new File(templatePath),1,null)
                        .stream()
                        .filter(File::isDirectory)
                        .findFirst()
                        .orElseThrow(RuntimeException::new).getAbsolutePath();

       sourceRootPath= sourceRootPath.replaceAll("\\\\", "/");




        // 二、生成文件模板
        // 遍历输入文件
        List<Meta.FileConfig.FileInfo> newFileInfoList = makeFileTemplates(templateMakerFileConfig, templateMakerModelConfig, sourceRootPath);

        List<Meta.ModelConfig.ModelInfo> newModelInfoList = getModelInfoList(templateMakerModelConfig);



        // 生成模板文件  调整meta.json文件跟项目路径平级
        String metaOutputPath = templatePath + File.separator + "meta.json";
        // 如果说已经有meta文件，说明不是第一次制作，则在meta基础上进行修改
        if (FileUtil.exist(metaOutputPath)) {
            Meta oldMeta = JSONUtil.toBean(FileUtil.readUtf8String(metaOutputPath), Meta.class);

            // CopyOptions.create().ignoreNullValue() 源对象的字段不为null时拷贝
            BeanUtil.copyProperties(newMeta, oldMeta, CopyOptions.create().ignoreNullValue());
            newMeta = oldMeta;
            //追加参数列表
            List<Meta.FileConfig.FileInfo> fileInfoList = newMeta.getFileConfig().getFiles();
            fileInfoList.addAll(newFileInfoList);
            List<Meta.ModelConfig.ModelInfo> modelInfoList = newMeta.getModelConfig().getModels();
            modelInfoList.addAll(newModelInfoList);
            //配置去重
            newMeta.getFileConfig().setFiles(distinctFiles(fileInfoList));
            newMeta.getModelConfig().setModels(distinctModels(modelInfoList));


        } else {

            Meta.FileConfig fileConfig = new Meta.FileConfig();
            newMeta.setFileConfig(fileConfig);

            fileConfig.setSourceRootPath(sourceRootPath);
            List<Meta.FileConfig.FileInfo> fileInfoList = new ArrayList<>();
            fileConfig.setFiles(fileInfoList);
            fileInfoList.addAll(newFileInfoList);


            Meta.ModelConfig modelConfig = new Meta.ModelConfig();
            newMeta.setModelConfig(modelConfig);
            List<Meta.ModelConfig.ModelInfo> modelInfoList = new ArrayList<>();
            modelConfig.setModels(modelInfoList);
            modelInfoList.addAll(newModelInfoList);

        }

        // 额外的输出配置
        if(templateMakerOutputConfig!=null&& templateMakerOutputConfig.isRemoveGroupFilesFromRoot()){
            // 文件外层和分组去重
            List<Meta.FileConfig.FileInfo> fileInfoList = newMeta.getFileConfig().getFiles();
            newMeta.getFileConfig().setFiles(TemplateMakerUtils.removeGroupFilesFromRoot(fileInfoList));
        }
        //更新元信息文件
        FileUtil.writeUtf8String(JSONUtil.toJsonPrettyStr(newMeta), metaOutputPath);
        return id;
    }


    /**
     *
     * @param templateMakerModelConfig
     * @return
     */

    private static List<Meta.ModelConfig.ModelInfo> getModelInfoList(TemplateMakerModelConfig templateMakerModelConfig){

        // 如果是模型组
        List<Meta.ModelConfig.ModelInfo> newModelInfoList = new ArrayList<>();
        if(templateMakerModelConfig==null){
            return newModelInfoList;
        }
        //处理模型信息
        List<TemplateMakerModelConfig.ModelInfoConfig> models = templateMakerModelConfig.getModels();

        if(CollUtil.isEmpty(models)){
            return newModelInfoList;
        }

        //转换为配置接受的ModelInfo对
        List<Meta.ModelConfig.ModelInfo> inputModelList = models.stream().map(modelInfoConfig -> {
            Meta.ModelConfig.ModelInfo modelInfo = new Meta.ModelConfig.ModelInfo();
            BeanUtil.copyProperties(modelInfoConfig, modelInfo);
            return modelInfo;
        }).collect(Collectors.toList());

        TemplateMakerModelConfig.ModelGroupConfig modelGroupConfig = templateMakerModelConfig.getModelGroupConfig();

        if (modelGroupConfig != null) {

            Meta.ModelConfig.ModelInfo groupModelInfo = new Meta.ModelConfig.ModelInfo();
            BeanUtil.copyProperties(modelGroupConfig,groupModelInfo);


            //讲模型放在一个分组中
            groupModelInfo.setModels(inputModelList);
            newModelInfoList.add(groupModelInfo);
        } else {
            //不分组，添加所有的模型信息到列表
            newModelInfoList.addAll(inputModelList);
        }
        return newModelInfoList;
    }
    /**
     *
     * @param templateMakerFileConfig
     * @param templateMakerModelConfig
     * @param sourceRootPath
     * @return
     */
    private static List<Meta.FileConfig.FileInfo> makeFileTemplates(TemplateMakerFileConfig templateMakerFileConfig,
                                                                    TemplateMakerModelConfig templateMakerModelConfig,
                                                                    String sourceRootPath){

        List<Meta.FileConfig.FileInfo> newFileInfoList =new ArrayList<>();
        if(templateMakerFileConfig==null){
            return newFileInfoList;
        }
        List<TemplateMakerFileConfig.FileInfoConfig> fileInfoConfigList = templateMakerFileConfig.getFiles();
        if(CollUtil.isEmpty(fileInfoConfigList)){
            return newFileInfoList;
        }
        for (TemplateMakerFileConfig.FileInfoConfig fileInfoConfig : fileInfoConfigList) {

            String inputFilePath = fileInfoConfig.getPath();
            //使用字符串去替换生成模板文件
//        String fileOutputAbsolutePath=sourceRootPath+File.separator+fileOutputPath;

            if (!inputFilePath.startsWith(sourceRootPath)) {
                inputFilePath = sourceRootPath + File.separator + inputFilePath;
            }
            List<File> fileList = FileFilter.doFilter(inputFilePath, fileInfoConfig.getFilterConfigList());

            fileList = fileList.stream()
                    // 不处理已经生成的FTL文件
                    .filter(file -> !file.getAbsolutePath().endsWith(".ftl"))
                    .collect(Collectors.toList());
            for (File file : fileList) {
                Meta.FileConfig.FileInfo fileInfo = makeFileTemplate(templateMakerModelConfig, sourceRootPath, file,fileInfoConfig);
                newFileInfoList.add(fileInfo);
            }
        }
        TemplateMakerFileConfig.FileGroupConfig fileGroupConfig = templateMakerFileConfig.getFileGroupConfig();
        if (fileGroupConfig != null) {
            String groupName = fileGroupConfig.getGroupName();
            String groupKey = fileGroupConfig.getGroupKey();
            String condition = fileGroupConfig.getCondition();

            //新增分组配置
            Meta.FileConfig.FileInfo groupFileInfo = new Meta.FileConfig.FileInfo();
            groupFileInfo.setGroupKey(groupKey);
            groupFileInfo.setCondition(condition);
            groupFileInfo.setGroupName(groupName);
            groupFileInfo.setType(FileTypeEnum.GROUP.getValue());
            groupFileInfo.setFiles(newFileInfoList);
            newFileInfoList = new ArrayList<>();
            newFileInfoList.add(groupFileInfo);
        }
        return newFileInfoList;

   }

    /**
     * 制作文件模板
     *
     * @param templateMakerModelConfig
     * @param sourceRootPath
     * @param inputFile
     * @return
     */
    private static Meta.FileConfig.FileInfo makeFileTemplate(TemplateMakerModelConfig templateMakerModelConfig,
                                                             String sourceRootPath,
                                                             File inputFile,
                                                             TemplateMakerFileConfig.FileInfoConfig fileInfoConfig) {


        // 要挖坑的文件绝对路径
        String fileInputAbsolutePath = inputFile.getAbsolutePath().replaceAll("\\\\", "/");
        String fileOutputAbsolutePath = fileInputAbsolutePath + ".ftl";

        // 文件输入输出相对路径(用于生成配置)

        String fileInputPath = fileInputAbsolutePath.replace(sourceRootPath + "/", "");
        String fileOutputPath = fileInputPath + ".ftl";
        String fileContent = null;
        boolean hasTemplateFile = FileUtil.exist(fileOutputAbsolutePath);
        if (hasTemplateFile) {
            fileContent = FileUtil.readUtf8String(fileOutputAbsolutePath);
        } else {
            fileContent = FileUtil.readUtf8String(fileInputAbsolutePath);
        }


        // 支持多个模型: 对同一个文件的内容，遍历模型进行多轮替换
        TemplateMakerModelConfig.ModelGroupConfig modelGroupConfig = templateMakerModelConfig.getModelGroupConfig();
        String newFileContent = fileContent;
        String replacement;

        for (TemplateMakerModelConfig.ModelInfoConfig modelInfoConfig : templateMakerModelConfig.getModels()) {

            //不是分组
            if (modelGroupConfig == null) {
                replacement = String.format("${%s}", modelInfoConfig.getFieldName());
            } else {
                // 是分组
                String groupKey = modelGroupConfig.getGroupKey();

                // 注意挖坑要多一个层级
                replacement = String.format("${%s.%s}", groupKey, modelInfoConfig.getFieldName());
            }
            //多次替换
            newFileContent = StrUtil.replace(newFileContent, modelInfoConfig.getReplaceText(), replacement);
        }


//
//        String newFileContent= StrUtil.replace(fileContent,searchStr,replacement);
//
//        //输出模板
//        FileUtil.writeUtf8String(newFileContent,fileOutputAbsolutePath);


        //文件配置信息
        Meta.FileConfig.FileInfo fileInfo = new Meta.FileConfig.FileInfo();
        // 文件输入和输出路径相反
        fileInfo.setInputPath(fileOutputPath);
        fileInfo.setOutputPath(fileInputPath);
        fileInfo.setType(FileTypeEnum.FILE.getValue());
        fileInfo.setCondition(fileInfoConfig.getCondition());
        fileInfo.setGenerateType(FileGenerateTypeEnum.DYNAMIC.getValue());
        // 如果没有挖坑直接静态生成
        boolean contentEquals = newFileContent.equals(fileContent);
        if (!hasTemplateFile) {
            if (contentEquals) {
                fileInfo.setInputPath(fileInputPath);
                fileInfo.setGenerateType(FileGenerateTypeEnum.STATIC.getValue());
            } else {

                FileUtil.writeUtf8String(newFileContent, fileOutputAbsolutePath);
            }
        } else if (!contentEquals) {
            FileUtil.writeUtf8String(newFileContent, fileOutputAbsolutePath);
        }


        return fileInfo;
    }

    private static List<Meta.FileConfig.FileInfo> distinctFiles(List<Meta.FileConfig.FileInfo> fileInfoList) {

        //以组为单位划分
        Map<String, List<Meta.FileConfig.FileInfo>> groupKeyFileInfoMap = fileInfoList.stream()
                .filter(fileInfo -> StrUtil.isNotBlank(fileInfo.getGroupKey()))
                .collect(Collectors.groupingBy(Meta.FileConfig.FileInfo::getGroupKey));

        //同一组的文件配置合并
        //保存每个组对应的合并后的对象
        Map<String, Meta.FileConfig.FileInfo> groupKeyMergedFileInfoMap = new HashMap<>();

        for (Map.Entry<String, List<Meta.FileConfig.FileInfo>> stringFileInfoEntry : groupKeyFileInfoMap.entrySet()) {

            List<Meta.FileConfig.FileInfo> tempFileInfo = stringFileInfoEntry.getValue();
            List<Meta.FileConfig.FileInfo> newFileInfoList = new ArrayList<>(
                    tempFileInfo.stream()
                            .flatMap(fileInfo -> fileInfo.getFiles().stream())
                            .collect(Collectors.toMap(Meta.FileConfig.FileInfo::getInputPath, o -> o, (e, r) -> r))
                            .values()
            );
            //使用新的配置
            Meta.FileConfig.FileInfo newFileInfo = CollUtil.getLast(tempFileInfo);
            newFileInfo.setFiles(newFileInfoList);
            String groupKey = stringFileInfoEntry.getKey();

            groupKeyMergedFileInfoMap.put(groupKey, newFileInfo);
        }

        //将文件分组添加到结果列表
        List<Meta.FileConfig.FileInfo> resultList = new ArrayList<>(groupKeyMergedFileInfoMap.values());

        //将未分组的文件添加到结果列表
        List<Meta.FileConfig.FileInfo> noGroupFileInfoList = fileInfoList.stream()
                .filter(fileInfo -> StrUtil.isBlank(fileInfo.getGroupKey()))
                .collect(Collectors.toList());
        resultList.addAll(new ArrayList<>(noGroupFileInfoList.stream()
                .collect(Collectors.toMap(Meta.FileConfig.FileInfo::getInputPath, o -> o, (e, r) -> r)).values())
        );
        return resultList;

    }


    private static List<Meta.ModelConfig.ModelInfo> distinctModels(List<Meta.ModelConfig.ModelInfo> modelInfoList) {


        // 策略: 同分组内的模型merge, 不同分组保留

        // 1. 有分组的， 以组为单位
        Map<String, List<Meta.ModelConfig.ModelInfo>> groupKeyModelInfoListMap = modelInfoList.stream().filter(modelInfo -> StrUtil.isNotBlank(modelInfo.getGroupKey()))
                .collect(Collectors.groupingBy(Meta.ModelConfig.ModelInfo::getGroupKey));


        // 2. 同组内的模型配置合并

        // 保存每个组对应的合并后的对象map
        Map<String, Meta.ModelConfig.ModelInfo> groupKeyMergeModelInfoMap = new HashMap<>();


        for (Map.Entry<String, List<Meta.ModelConfig.ModelInfo>> stringModelInfoEntry : groupKeyModelInfoListMap.entrySet()) {

            List<Meta.ModelConfig.ModelInfo> tempModelInfo = stringModelInfoEntry.getValue();
            List<Meta.ModelConfig.ModelInfo> newModelInfoList = new ArrayList<>(
                    tempModelInfo.stream()
                            .flatMap(modelInfo -> modelInfo.getModels().stream())
                            .collect(Collectors.toMap(Meta.ModelConfig.ModelInfo::getFieldName, o -> o, (e, r) -> r))
                            .values()
            );
            //使用新的配置
            Meta.ModelConfig.ModelInfo newFileInfo = CollUtil.getLast(tempModelInfo);
            newFileInfo.setModels(newModelInfoList);
            String groupKey = stringModelInfoEntry.getKey();

            groupKeyMergeModelInfoMap.put(groupKey, newFileInfo);
        }

        //将文件分组添加到结果列表
        List<Meta.ModelConfig.ModelInfo> resultList = new ArrayList<>(groupKeyMergeModelInfoMap.values());

        //将未分组的文件添加到结果列表
        List<Meta.ModelConfig.ModelInfo> noGroupFileInfoList = modelInfoList.stream()
                .filter(modelInfo -> StrUtil.isBlank(modelInfo.getGroupKey()))
                .collect(Collectors.toList());
        resultList.addAll(new ArrayList<>(noGroupFileInfoList.stream()
                .collect(Collectors.toMap(Meta.ModelConfig.ModelInfo::getFieldName, o -> o, (e, r) -> r)).values())
        );
        return resultList;
    }


}
