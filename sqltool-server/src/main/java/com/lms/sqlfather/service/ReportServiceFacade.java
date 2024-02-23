package com.lms.sqlfather.service;

import com.lms.lmscommon.common.DeleteFlagEntity;
import com.lms.lmscommon.common.DeleteRequest;
import com.lms.lmscommon.model.dto.report.ReportAddRequest;
import net.datafaker.Bool;

/**
 * @author lms2000
 */
public interface ReportServiceFacade {

    /**
     * 创建
     * @param reportAddRequest
     * @return
     */
    Long addReport(ReportAddRequest reportAddRequest);


    /**
     * 删除
     * @param deleteRequest
     * @return
     */
    Boolean deleteReport(DeleteRequest deleteRequest,Long uid);
}
