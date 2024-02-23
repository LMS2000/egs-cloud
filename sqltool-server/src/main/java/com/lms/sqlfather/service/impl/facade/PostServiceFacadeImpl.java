package com.lms.sqlfather.service.impl.facade;

import com.google.gson.Gson;
import com.lms.contants.HttpCode;
import com.lms.lmscommon.common.DeleteRequest;
import com.lms.lmscommon.constant.UserConstant;
import com.lms.lmscommon.model.dto.post.PostEditRequest;
import com.lms.lmscommon.model.entity.Post;
import com.lms.lmscommon.model.entity.User;
import com.lms.lmscommon.model.vo.user.UserVO;
import com.lms.lmscommon.common.BusinessException;
import com.lms.sqlfather.service.PostService;
import com.lms.sqlfather.service.PostServiceFacade;
import com.lms.sqlfather.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lms2000
 */
@Service
public class PostServiceFacadeImpl implements PostServiceFacade {

    private final UserService userService;
    private final PostService postService;
    private final static Gson GSON=new Gson();

    @Autowired
    public PostServiceFacadeImpl(@Qualifier("userServiceImpl") UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    @Override
    public Boolean editPost(PostEditRequest postEditRequest, Long uid) {
        Post post = new Post();
        BeanUtils.copyProperties(postEditRequest, post);
        List<String> tags = postEditRequest.getTags();
        if (tags != null) {
            post.setTags(GSON.toJson(tags));
        }
        // 参数校验
        postService.validPost(post, false);
        User loginUser = userService.getById(uid);
        long id = postEditRequest.getId();
        // 判断是否存在
        Post oldPost = postService.getById(id);
        BusinessException.throwIf(oldPost==null);
        // 仅本人或管理员可编辑
        BusinessException.throwIf(!oldPost.getUserId().equals(loginUser.getId()) && !UserConstant.ADMIN_ROLE.equals(loginUser.getUserRole()));
        return postService.updateById(post);
    }

    @Override
    public Boolean deletePost(DeleteRequest deleteRequest, Long uid) {
        UserVO loginUser = userService.getUserVO(userService.getById(uid));
        long id = deleteRequest.getId();
        // 判断是否存在
        Post oldPost = postService.getById(id);
        BusinessException.throwIf(oldPost == null, HttpCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        BusinessException.throwIf(!oldPost.getUserId().equals(uid) && !UserConstant.ADMIN_ROLE.equals(loginUser.getUserRole()));
        return postService.removeById(id);
    }
}
