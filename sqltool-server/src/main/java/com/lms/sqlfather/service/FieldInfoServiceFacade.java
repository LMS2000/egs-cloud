package com.lms.sqlfather.service;

import com.lms.lmscommon.common.DeleteRequest;

public interface FieldInfoServiceFacade {

    Boolean deleteFieldInfo(DeleteRequest deleteRequest,Long uid);
}
