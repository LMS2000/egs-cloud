package com.lms.lmscommon.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.lms.lmscommon.meta.Meta;
import org.mapstruct.Named;

import java.util.List;

public class MapStructUtil {


    /**
     * 将 json字符串的tags转换为List<String>
     *
     * @param tags
     * @return
     */
    @Named("convertToList")
    public static List<String> convertToList(String tags) {
        return convertToClass(tags, List.class);
    }

    /**
     * 转换为fileconfig
     *
     * @param fileConfig
     * @return
     */
    @Named("convertToFileConfig")
    public static Meta.FileConfig covertToFileConfig(String fileConfig) {
        return convertToClass(fileConfig, Meta.FileConfig.class);
    }


    /**
     * 转换为modelconfig
     *
     * @param modelConfig
     * @return
     */
    @Named("convertToModelConfig")
    public static Meta.ModelConfig convertToModelConfig(String modelConfig) {
        return convertToClass(modelConfig, Meta.ModelConfig.class);
    }

    public static <T> T convertToClass(String source, Class<?> clazz) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            TypeFactory typeFactory = objectMapper.getTypeFactory();
            JavaType javaType = typeFactory.constructType(clazz);
            return objectMapper.readValue(source, javaType);
        } catch (JsonProcessingException e) {
           throw new RuntimeException();
        }
    }
}
