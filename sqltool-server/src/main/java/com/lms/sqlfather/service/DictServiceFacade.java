package com.lms.sqlfather.service;

import com.lms.lmscommon.common.DeleteRequest;

/**
 * 字典service的外观类
 * @author lms2000
 * @since 2024-02-02
 */
public interface DictServiceFacade {


    /**
     * 删除字典
     * @param deleteRequest
     * @return
     */
    Boolean deleteDict(DeleteRequest deleteRequest);

}
