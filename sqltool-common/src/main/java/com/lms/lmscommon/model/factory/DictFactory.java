package com.lms.lmscommon.model.factory;

import com.lms.lmscommon.model.entity.Dict;
import com.lms.lmscommon.model.vo.dict.DictVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 字段转换类
 */
public class DictFactory {
    public static final DictFactory.DictConverter DICT_CONVERTER = Mappers.getMapper(DictFactory.DictConverter.class);

    @Mapper
    public interface DictConverter {
        @Mappings({

        })
        DictVO toDictVO(Dict dict);

        List<DictVO> toListDictVO(List<Dict> interfaceInfoList);
    }

}
