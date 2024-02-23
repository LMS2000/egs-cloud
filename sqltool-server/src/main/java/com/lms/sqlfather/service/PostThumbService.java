package com.lms.sqlfather.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.lms.lmscommon.model.entity.PostThumb;
import com.lms.lmscommon.model.vo.user.UserVO;

/**
 * 帖子点赞服务
 *
 */
public interface PostThumbService extends IService<PostThumb> {

    /**
     * 点赞
     *
     * @param postId
     * @param loginUser
     * @return
     */
    int doPostThumb(long postId, UserVO loginUser);

    /**
     * 帖子点赞（内部服务）
     *
     * @param userId
     * @param postId
     * @return
     */
    int doPostThumbInner(long userId, long postId);
}
