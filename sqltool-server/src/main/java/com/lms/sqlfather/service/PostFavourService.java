package com.lms.sqlfather.service;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lms.lmscommon.model.dto.postfavour.PostFavourQueryRequest;
import com.lms.lmscommon.model.entity.Post;
import com.lms.lmscommon.model.entity.PostFavour;
import com.lms.lmscommon.model.vo.post.PostVO;
import com.lms.lmscommon.model.vo.user.UserVO;

/**
 * 帖子收藏服务
 *
 */
public interface PostFavourService extends IService<PostFavour> {

    /**
     * 帖子收藏
     *
     * @param postId
     * @param uid
     * @return
     */
    Integer doPostFavour(Long postId, Long uid);

    /**
     * 分页获取用户收藏的帖子列表
     *
     * @param postFavourQueryRequest
     * @param favourUserId
     * @return
     */
    Page<PostVO> listFavourPostByPage(PostFavourQueryRequest postFavourQueryRequest,
                                      Long favourUserId);


    /**
     * 帖子收藏（内部服务）
     *
     * @param userId
     * @param postId
     * @return
     */
    Integer doPostFavourInner(Long userId, Long  postId);
}
