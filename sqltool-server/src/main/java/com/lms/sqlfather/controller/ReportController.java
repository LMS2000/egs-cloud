package com.lms.sqlfather.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.lms.lmscommon.constant.UserConstant;
import com.lms.lmscommon.model.dto.report.ReportAddRequest;
import com.lms.lmscommon.model.dto.report.ReportQueryRequest;
import com.lms.lmscommon.model.dto.report.ReportUpdateRequest;
import com.lms.lmscommon.model.vo.report.ReportVO;
import com.lms.result.EnableResponseAdvice;
import com.lms.sqlfather.annotation.AuthCheck;
import com.lms.lmscommon.common.DeleteRequest;
import com.lms.lmscommon.common.BusinessException;
import com.lms.sqlfather.service.ReportService;
import com.lms.sqlfather.service.ReportServiceFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.util.List;

import static com.lms.lmscommon.model.factory.ReportFactory.REPORT_CONVERTER;

/**
 * 举报控制类
 *
 * @author lms2000
 * @since 2024-01-30
 */
@RestController
@RequestMapping("/report")
@EnableResponseAdvice
@AllArgsConstructor
@Api(value = "举报管理")
public class ReportController {


    private final ReportService reportService;

    private final ReportServiceFacade reportServiceFacade;

    // region 增删改查

    /**
     * 创建
     *
     * @param reportAddRequest
     * @return
     */
    @PostMapping("/add")
    @ApiOperationSupport(order =1)
    @ApiOperation(value = "创建")
    public Long addReport(@RequestBody ReportAddRequest reportAddRequest) {
        return reportServiceFacade.addReport(reportAddRequest);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @return
     */
    @PostMapping("/delete")
    @SaCheckLogin
    @ApiOperationSupport(order =2)
    @ApiOperation(value = "删除")
    public Boolean deleteReport(@RequestBody DeleteRequest deleteRequest) {
        BusinessException.throwIf(deleteRequest == null || deleteRequest.getId() <= 0);
        Long loginId = Long.parseLong((String) StpUtil.getLoginId());
        return reportServiceFacade.deleteReport(deleteRequest, loginId);
    }

    /**
     * 更新（仅管理员）
     *
     * @param reportUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    @ApiOperationSupport(order =3)
    @ApiOperation(value = "更新（仅管理员）")
    public Boolean updateReport(@RequestBody ReportUpdateRequest reportUpdateRequest) {
        return reportService.updateReport(reportUpdateRequest);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    @ApiOperationSupport(order =3)
    @ApiOperation(value = "根据 id 获取")
    public ReportVO getReportById(@Positive(message = "id不合法") Long id) {
        return REPORT_CONVERTER.toReportVo(reportService.getById(id));
    }

    /**
     * 获取列表（仅管理员可使用）
     *
     * @param reportQueryRequest
     * @return
     */
    @AuthCheck(mustRole = "admin")
    @GetMapping("/list")
    @ApiOperationSupport(order =4)
    @ApiOperation(value = "获取列表（仅管理员可使用）")
    public List<ReportVO> listReport(ReportQueryRequest reportQueryRequest) {
      return reportService.listReport(reportQueryRequest);
    }

    /**
     * 分页获取列表
     *
     * @param reportQueryRequest
     * @return
     */
    @GetMapping("/list/page")
    @ApiOperationSupport(order =5)
    @ApiOperation(value = "分页获取列表")
    public Page<ReportVO> listReportByPage(ReportQueryRequest reportQueryRequest) {
        BusinessException.throwIf(reportQueryRequest == null);
        return reportService.pageReport(reportQueryRequest);

    }


}
