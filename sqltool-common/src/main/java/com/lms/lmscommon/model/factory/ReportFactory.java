package com.lms.lmscommon.model.factory;

import com.lms.lmscommon.model.entity.Report;
import com.lms.lmscommon.model.entity.TableInfo;
import com.lms.lmscommon.model.vo.report.ReportVO;
import com.lms.lmscommon.model.vo.tableinfo.TableInfoVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

public class ReportFactory {
    public static final ReportFactory.ReportConverter REPORT_CONVERTER = Mappers.getMapper(ReportFactory.ReportConverter.class);

    @Mapper
    public interface ReportConverter {
        @Mappings({

        })
        ReportVO toReportVo(Report report);

        List<ReportVO> toListReportVo(List<Report> reportList);
    }
}
