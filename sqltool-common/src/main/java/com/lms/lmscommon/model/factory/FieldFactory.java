package com.lms.lmscommon.model.factory;

import com.lms.lmscommon.model.entity.Dict;
import com.lms.lmscommon.model.entity.FieldInfo;
import com.lms.lmscommon.model.vo.dict.DictVO;
import com.lms.lmscommon.model.vo.field.FieldInfoVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

public class FieldFactory {
    public static final FieldFactory.FieldConverter FIELD_CONVERTER = Mappers.getMapper(FieldFactory.FieldConverter.class);

    @Mapper
    public interface FieldConverter {
        @Mappings({

        })
        FieldInfoVO toFieldVO(FieldInfo fieldInfo);

        List<FieldInfoVO> toListFieldVO(List<FieldInfo> fieldInfoList);
    }
}
