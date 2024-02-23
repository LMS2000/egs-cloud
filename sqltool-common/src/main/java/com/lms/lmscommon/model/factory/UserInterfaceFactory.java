package com.lms.lmscommon.model.factory;

import com.lms.lmscommon.model.entity.InterfaceInfo;
import com.lms.lmscommon.model.entity.UserInterfaceInfo;
import com.lms.lmscommon.model.vo.interfaceInfo.InterfaceInfoVO;
import com.lms.lmscommon.model.vo.userInterfaceInfo.UserInterfaceInfoVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

public class UserInterfaceFactory {
    public static final UserInterfaceFactory.UserInterfaceInfoConverter USER_INTERFACE_CONVERTER = Mappers.getMapper(UserInterfaceFactory.UserInterfaceInfoConverter.class);

    @Mapper
    public interface UserInterfaceInfoConverter {
        @Mappings({

        })
        UserInterfaceInfoVO toUserInterfaceVO(UserInterfaceInfoVO userInterfaceInfoVO);

        List<UserInterfaceInfoVO> toListUserInterfaceVO(List<UserInterfaceInfo> userInterfaceInfoList);
    }
}
