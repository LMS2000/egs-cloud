package com.lms.lmscommon.model.factory;

import com.lms.lmscommon.model.entity.InterfaceInfo;
import com.lms.lmscommon.model.entity.User;
import com.lms.lmscommon.model.vo.interfaceInfo.InterfaceInfoVO;
import com.lms.lmscommon.model.vo.user.UserVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

public class InterfaceInfoFactory {
    public static final InterfaceInfoFactory.InterfaceInfoConverter INTERFACE_CONVERTER = Mappers.getMapper(InterfaceInfoFactory.InterfaceInfoConverter.class);

    @Mapper
    public interface InterfaceInfoConverter {
        @Mappings({

        })
        InterfaceInfoVO toInterfaceVO(InterfaceInfo interfaceInfo);

        List<InterfaceInfoVO> toListInterfaceVO(List<InterfaceInfo> interfaceInfoList);
    }
}
