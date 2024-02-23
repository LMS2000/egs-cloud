package com.lms.lmscommon.model.factory;

import com.lms.lmscommon.model.entity.TableInfo;
import com.lms.lmscommon.model.entity.User;
import com.lms.lmscommon.model.vo.tableinfo.TableInfoVO;
import com.lms.lmscommon.model.vo.user.UserVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

public class TableInfoFactory {
    public static final TableInfoFactory.TableInfoConverter TABLE_INFO_CONVERTER = Mappers.getMapper(TableInfoFactory.TableInfoConverter.class);

    @Mapper
    public interface TableInfoConverter {
        @Mappings({

        })
        TableInfoVO toTableInfoVo(TableInfo tableInfo);

        List<TableInfoVO> toListTableInfoVo(List<TableInfo> tableInfoList);
    }
}
