package com.lms.sqlfather.service;

import com.lms.lmscommon.common.DeleteRequest;
import com.lms.lmscommon.model.dto.post.PostEditRequest;

public interface PostServiceFacade {


    /**
     *
     * @param postEditRequest
     * @param uid
     * @return
     */
    Boolean editPost(PostEditRequest postEditRequest,Long uid);
    /**
     *
     * @param deleteRequest
     * @param uid
     * @return
     */
    Boolean deletePost(DeleteRequest deleteRequest,Long uid);
}
