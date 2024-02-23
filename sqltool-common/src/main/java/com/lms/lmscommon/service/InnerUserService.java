package com.lms.lmscommon.service;



import com.lms.lmscommon.model.vo.user.UserVO;

/**
 * 用户服务
 *
 * @author LMS2000
 */
public interface InnerUserService {

    /**
     * 数据库中查是否已分配给用户秘钥（accessKey）
     * @param accessKey
     * @return
     */
    UserVO getInvokeUser(String accessKey);
}
