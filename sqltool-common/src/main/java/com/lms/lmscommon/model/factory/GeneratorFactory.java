package com.lms.lmscommon.model.factory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lms.lmscommon.model.entity.Dict;
import com.lms.lmscommon.model.entity.Generator;
import com.lms.lmscommon.model.vo.dict.DictVO;
import com.lms.lmscommon.model.vo.generator.GeneratorVO;
import com.lms.lmscommon.utils.MapStructUtil;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class GeneratorFactory {
    public static final GeneratorFactory.GeneratorConverter GENERATOR_CONVERTER = Mappers.getMapper(GeneratorFactory.GeneratorConverter.class);

    @Mapper
    public interface GeneratorConverter {
        @Mapping(target = "tags",  expression = "java(com.lms.lmscommon.utils.MapStructUtil.convertToList(generator.getTags()))")
        @Mapping(target = "fileConfig",expression = "java(com.lms.lmscommon.utils.MapStructUtil.covertToFileConfig(generator.getFileConfig()))")
        @Mapping(target = "modelConfig",expression = "java(com.lms.lmscommon.utils.MapStructUtil.convertToModelConfig(generator.getModelConfig()))")
        @Named("toGeneratorVO")
        GeneratorVO toGeneratorVO(Generator generator);

        @IterableMapping(qualifiedByName = "toGeneratorVO")
        List<GeneratorVO> toListGeneratorVO(List<Generator> generatorList);

    }



}
