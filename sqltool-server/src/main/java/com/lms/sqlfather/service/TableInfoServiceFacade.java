package com.lms.sqlfather.service;

import com.lms.lmscommon.common.DeleteRequest;

public interface TableInfoServiceFacade {


    /**
     * 删除表信息
     * @param deleteRequest
     * @param uid
     * @return
     */
    Boolean deleteFieldInfo(DeleteRequest deleteRequest, Long uid);

}
