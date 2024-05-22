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
import com.lms.sqlfather.service.*;
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
    private final TableInfoService tableInfoService;

    private final FieldInfoService fieldInfoService;
    private final GeneratorService generatorService;


    public ReportServiceFacadeImpl(ReportService reportService, DictService dictService, @Qualifier("userServiceImpl") UserService userService, TableInfoService tableInfoService, FieldInfoService fieldInfoService, GeneratorService generatorService) {
        this.reportService = reportService;
        this.dictService = dictService;
        this.userService = userService;
        this.tableInfoService = tableInfoService;
        this.fieldInfoService = fieldInfoService;
        this.generatorService = generatorService;
    }

    @Override
    public Long addReport(ReportAddRequest reportAddRequest) {
        Report report = new Report();
        BeanUtils.copyProperties(reportAddRequest, report);
        reportService.validReport(report, true);
        Long loginId =Long.parseLong((String) StpUtil.getLoginId());
        Long reportedUserId = getReportedUserId(report.getReportedId(), report.getType());
        report.setReportedUserId(reportedUserId);
        report.setUserId(loginId);
        report.setStatus(ReportStatusEnum.DEFAULT.getValue());
        boolean result = reportService.save(report);
        BusinessException.throwIf(!result, HttpCode.OPERATION_ERROR);
        return report.getId();
    }

    private Long getReportedUserId(Long typeId,Integer type){
        switch (type){
            case 0:
                return dictService.getById(typeId).getUserId();
            case 1:
                return tableInfoService.getById(typeId).getUserId();
            case 2:
                return fieldInfoService.getById(typeId).getUserId();
            default:
                return generatorService.getById(typeId).getUserId();
        }
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
