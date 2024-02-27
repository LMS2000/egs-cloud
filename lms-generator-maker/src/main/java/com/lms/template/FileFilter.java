package com.lms.template;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import com.lms.template.enums.FileFilterRangeEnum;
import com.lms.template.enums.FileFilterRuleEnum;
import com.lms.template.model.FileFilterConfig;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class FileFilter {


    public static boolean doSingleFileFilter(List<FileFilterConfig> filterConfigList, File file){


        String fileName = file.getName();

        String fileContent = FileUtil.readUtf8String(file);

        // 所有过滤器校验的结果
        boolean result=true;

        if(CollUtil.isEmpty(filterConfigList)){
            return true;
        }
        for (FileFilterConfig fileFilterConfig : filterConfigList) {
            String range = fileFilterConfig.getRange();
            String rule = fileFilterConfig.getRule();
            String value = fileFilterConfig.getValue();

            FileFilterRangeEnum fileFilterRangeEnum = FileFilterRangeEnum.getEnumByValues(range);

            if(fileFilterRangeEnum==null){
                continue;
            }
            //要过滤的内容
            String content=fileName;
            switch (fileFilterRangeEnum){
                case FILE_NAME:
                    content=fileName;
                    break;
                case FILE_CONTENT:
                    content=fileContent;
                    break;
                default:
            }

            FileFilterRuleEnum fileFilterRuleEnum = FileFilterRuleEnum.getEnumByValue(rule);
            if(fileFilterRuleEnum==null){
                continue;
            }
            switch (fileFilterRuleEnum){
                case CONTAINS:
                    result= content.contains(value);
                    break;
                case STARTS_WITH:
                    result= content.startsWith(value);
                    break;
                case ENDS_WITH:
                    result= content.endsWith(value);
                    break;
                case REGEX:
                    result= content.matches(value);
                    break;
                case EQUALS:
                    result= content.equals(value);
                default:
            }
            // 有一个不满足直接返回
            if(!result){
                return false;
            }


        }
        return true;
    }

    /**
     * 对某个文件或文件夹进行过滤
     * @param filePath
     * @param filterConfigList
     * @return
     */
    public static List<File> doFilter(String filePath,List<FileFilterConfig> filterConfigList){

        List<File> fileList = FileUtil.loopFiles(filePath);
        return fileList.stream()
                .filter(file -> doSingleFileFilter(filterConfigList,file))
                .collect(Collectors.toList());
    }
}
