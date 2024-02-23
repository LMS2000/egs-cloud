package com.lms.lmscommon.model.factory;

import com.lms.lmscommon.model.entity.FieldInfo;
import com.lms.lmscommon.model.entity.Post;
import com.lms.lmscommon.model.vo.field.FieldInfoVO;
import com.lms.lmscommon.model.vo.post.PostVO;
import com.lms.lmscommon.utils.MapStructUtil;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

public class PostFactory {

    public static final PostFactory.PostConverter POST_CONVERTER = Mappers.getMapper(PostFactory.PostConverter.class);

    @Mapper
    public interface PostConverter {
        @Named("toPostVO")
        @Mapping(target = "tagList", expression = "java(com.lms.lmscommon.utils.MapStructUtil.convertToList(post.getTags()))")
        PostVO toPostVO(Post post);
        @IterableMapping(qualifiedByName = "toPostVO")
        List<PostVO> toListPostVO(List<Post> postList);
    }
}
