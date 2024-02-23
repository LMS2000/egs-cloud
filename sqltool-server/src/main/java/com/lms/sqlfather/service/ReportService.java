package com.lms.sqlfather.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lms.lmscommon.model.dto.report.ReportQueryRequest;
import com.lms.lmscommon.model.dto.report.ReportUpdateRequest;
import com.lms.lmscommon.model.entity.Report;
import com.lms.lmscommon.model.vo.report.ReportVO;

import java.util.List;


public interface ReportService extends IService<Report> {

    /**
     * 校验
     *
     * @param report
     * @param add
     */
    void validReport(Report report, boolean add);

    /**
     * 修改
     * @param reportUpdateRequest
     * @return
     */
    Boolean updateReport(ReportUpdateRequest reportUpdateRequest);

    /**
     * 获取report列表
     * @param reportQueryRequest
     * @return
     */
    List<ReportVO> listReport(ReportQueryRequest reportQueryRequest);

    /**
     * 获取分页report
     * @param reportQueryRequest
     * @return
     */
    Page<ReportVO> pageReport(ReportQueryRequest reportQueryRequest);

}


