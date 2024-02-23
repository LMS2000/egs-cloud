package com.lms.sqlfather.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lms.contants.HttpCode;
import com.lms.lmscommon.constant.SqlConstant;
import com.lms.lmscommon.model.dto.report.ReportQueryRequest;
import com.lms.lmscommon.model.dto.report.ReportUpdateRequest;
import com.lms.lmscommon.model.entity.Report;
import com.lms.lmscommon.model.enums.ReportStatusEnum;
import com.lms.lmscommon.model.vo.report.ReportVO;
import com.lms.lmscommon.common.BusinessException;
import com.lms.sqlfather.mapper.ReportMapper;
import com.lms.sqlfather.service.ReportService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.lms.lmscommon.model.factory.ReportFactory.REPORT_CONVERTER;

/**
 * @author lms2000
 */
@Service
public class ReportServiceImpl extends ServiceImpl<ReportMapper, Report> implements ReportService {
    @Override
    public void validReport(Report report, boolean add) {

      BusinessException.throwIf(report==null);
        String content = report.getContent();
        Integer status = report.getStatus();
        Long reportedId = report.getReportedId();
        BusinessException.throwIf(StringUtils.isNotBlank(content)&&content.length()>1024);
        if(add){
            BusinessException.throwIf(reportedId==null||reportedId<=0);
        }else{
            BusinessException.throwIf(status!=null&& !ReportStatusEnum.getValues().contains(status));
        }
    }

    @Override
    public Boolean updateReport(ReportUpdateRequest reportUpdateRequest) {
        Report report = new Report();
        BeanUtils.copyProperties(reportUpdateRequest, report);
        this.validReport(report, false);
        long id = reportUpdateRequest.getId();
        // 判断是否存在
        Report oldReport = this.getById(id);
        BusinessException.throwIf(oldReport == null, HttpCode.NOT_FOUND_ERROR);
        return this.updateById(report);
    }

    @Override
    public List<ReportVO> listReport(ReportQueryRequest reportQueryRequest) {
        Report reportQuery = new Report();
        if (reportQueryRequest != null) {
            BeanUtils.copyProperties(reportQueryRequest, reportQuery);
        }
        QueryWrapper<Report> queryWrapper = new QueryWrapper<>(reportQuery);
        return REPORT_CONVERTER.toListReportVo( this.list(queryWrapper));

    }

    @Override
    public Page<ReportVO> pageReport(ReportQueryRequest reportQueryRequest) {

        Report reportQuery = new Report();
        BeanUtils.copyProperties(reportQueryRequest, reportQuery);
        long current = reportQueryRequest.getCurrent();
        long size = reportQueryRequest.getPageSize();
        String sortField = reportQueryRequest.getSortField();
        String sortOrder = reportQueryRequest.getSortOrder();
        String content = reportQuery.getContent();
        // content 需支持模糊搜索
        reportQuery.setContent(null);
        // 限制爬虫
        BusinessException.throwIf(size > 50);
        QueryWrapper<Report> queryWrapper = new QueryWrapper<>(reportQuery);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField), sortOrder.equals(SqlConstant.SORT_ORDER_ASC),
                sortField);
        Page<Report> reportPage = this.page(new Page<>(current, size), queryWrapper);
        List<ReportVO> reportVOList = REPORT_CONVERTER.toListReportVo(reportPage.getRecords());
        Page<ReportVO> reportVOPage=new Page<>(current,size);
        reportVOPage.setRecords(reportVOList);
        reportPage.setTotal(reportVOList.size());
        return reportVOPage;
    }
}
