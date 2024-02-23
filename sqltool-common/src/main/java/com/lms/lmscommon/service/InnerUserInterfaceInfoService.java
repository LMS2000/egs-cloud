package com.lms.lmscommon.service;

import com.lms.lmscommon.model.entity.UserInterfaceInfo;

/**
 *
 */
public interface InnerUserInterfaceInfoService {

    /**
     * 调用接口统计
     *
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeCount(long interfaceInfoId, long userId);

    UserInterfaceInfo getUserInterfaceInfo(long interfaceInfoId,long uid);

}

