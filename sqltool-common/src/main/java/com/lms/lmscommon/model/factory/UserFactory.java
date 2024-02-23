package com.lms.lmscommon.model.factory;

import com.lms.lmscommon.model.entity.User;
import com.lms.lmscommon.model.vo.user.UserVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

public class UserFactory {
    public static final UserConverter USER_CONVERTER = Mappers.getMapper(UserConverter.class);

    @Mapper
    public interface UserConverter {
        @Mappings({

        })
        UserVO toUserVo(User user);

        List<UserVO> toListUserVo(List<User> fileList);
    }
}
