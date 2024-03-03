package com.lms.sqlfather.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.lms.lmscommon.model.entity.PostThumb;
import com.lms.lmscommon.model.vo.user.UserVO;

/**
 * 帖子点赞服务
 *
 * @author lms2000
 */
public interface PostThumbService extends IService<PostThumb> {

    /**
     * 点赞
     *
     * @param generatorId
     * @param loginUser
     * @return
     */
    Integer doPostThumb(Long generatorId, UserVO loginUser);

    /**
     * 帖子点赞（内部服务）
     *
     * @param userId
     * @param generatorId
     * @return
     */
    Integer doPostThumbInner(Long userId, Long generatorId);
}
