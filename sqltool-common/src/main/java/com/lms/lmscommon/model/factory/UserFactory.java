package com.lms.lmscommon.model.factory;

import com.lms.lmscommon.model.entity.User;
import com.lms.lmscommon.model.vo.user.UserVO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

public class UserFactory {
    public static final UserConverter USER_CONVERTER = Mappers.getMapper(UserConverter.class);

    @Mapper
    public interface UserConverter {
        @Mapping(target = "tags",  expression = "java(com.lms.lmscommon.utils.MapStructUtil.convertToList(user.getTags()))")
        @Named("toUserVo")
        UserVO toUserVo(User user);
        @IterableMapping(qualifiedByName = "toUserVo")
        List<UserVO> toListUserVo(List<User> userList);
    }
}
