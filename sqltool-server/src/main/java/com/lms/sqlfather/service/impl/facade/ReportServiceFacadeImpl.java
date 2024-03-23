package com.lms.sqlfather.service.impl.facade;

import cn.dev33.satoken.stp.StpUtil;
import com.lms.contants.HttpCode;
import com.lms.exception.BusinessException;
import com.lms.lmscommon.common.DeleteRequest;
import com.lms.lmscommon.constant.UserConstant;
import com.lms.lmscommon.model.dto.report.ReportAddRequest;
import com.lms.lmscommon.model.entity.Dict;
import com.lms.lmscommon.model.entity.Report;
import com.lms.lmscommon.model.enums.ReportStatusEnum;
import com.lms.lmscommon.model.vo.user.UserVO;
import com.lms.sqlfather.service.DictService;
import com.lms.sqlfather.service.ReportService;
import com.lms.sqlfather.service.ReportServiceFacade;
import com.lms.sqlfather.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * @author lms2000
 */
@Service
public class ReportServiceFacadeImpl implements ReportServiceFacade {



    private final ReportService reportService;
    private final DictService dictService;
    private final UserService userService;

    public ReportServiceFacadeImpl(ReportService reportService, DictService dictService,@Qualifier("userServiceImpl") UserService userService) {
        this.reportService = reportService;
        this.dictService = dictService;
        this.userService = userService;
    }

    @Override
    public Long addReport(ReportAddRequest reportAddRequest) {
        Report report = new Report();
        BeanUtils.copyProperties(reportAddRequest, report);
        reportService.validReport(report, true);
        Long loginId =Long.parseLong((String) StpUtil.getLoginId());
        Dict dict = dictService.getById(report.getReportedId());
        BusinessException.throwIf(dict == null);
        report.setReportedUserId(dict.getUserId());
        report.setUserId(loginId);
        report.setStatus(ReportStatusEnum.DEFAULT.getValue());
        boolean result = reportService.save(report);
        BusinessException.throwIf(!result, HttpCode.OPERATION_ERROR);
        return report.getId();
    }

    @Override
    public Boolean deleteReport(DeleteRequest deleteRequest,Long uid) {
        UserVO loginUser = userService.getUserVO(userService.getById(uid));
        long id = deleteRequest.getId();
        // 判断是否存在
        Report oldReport = reportService.getById(id);
        BusinessException.throwIf(oldReport == null,HttpCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        BusinessException.throwIf(!oldReport.getUserId().equals(uid) && !UserConstant.ADMIN_ROLE.equals(loginUser.getUserRole()));
        return reportService.removeById(id);
    }
}
