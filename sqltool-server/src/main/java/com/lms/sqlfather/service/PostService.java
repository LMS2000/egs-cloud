package com.lms.sqlfather.service;





import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lms.lmscommon.model.dto.post.PostAddRequest;
import com.lms.lmscommon.model.dto.post.PostQueryRequest;
import com.lms.lmscommon.model.dto.post.PostUpdateRequest;
import com.lms.lmscommon.model.entity.Post;
import com.lms.lmscommon.model.vo.post.PostVO;

import javax.servlet.http.HttpServletRequest;

/**
 * 帖子服务
 *
 */
public interface PostService extends IService<Post> {

    /**
     * 校验
     *
     * @param post
     * @param add
     */
    void validPost(Post post, boolean add);

    /**
     * 获取查询条件
     *
     * @param postQueryRequest
     * @return
     */
    QueryWrapper<Post> getQueryWrapper(PostQueryRequest postQueryRequest);

    /**
     * 从 ES 查询
     *
     * @param postQueryRequest
     * @return
     */
//    Page<Post> searchFromEs(PostQueryRequest postQueryRequest);

    /**
     * 获取帖子封装
     *
     * @param post
     * @param uid
     * @return
     */
    PostVO getPostVO(Post post, Long uid);

    /**
     * 分页获取帖子封装
     *
     * @param postPage
     * @return
     */
    Page<PostVO> getPostVOPage(Page<Post> postPage, Long uid);

    /**
     * 分页查询帖子
     * @param postQueryRequest
     * @return
     */
    Page<PostVO> listPostVOByPage(PostQueryRequest postQueryRequest, Long uid);

    /**
     * 创建
     * @param postAddRequest
     * @return
     */
    Long addPost(PostAddRequest postAddRequest,Long uid);

    /**
     *
     * @param postUpdateRequest
     * @return
     */
    Boolean updatePost(PostUpdateRequest postUpdateRequest);


    Page<PostVO> pagePost(PostQueryRequest postQueryRequest,Long uid);

}
