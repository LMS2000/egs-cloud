package com.lms.sqlfather.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lms.lmscommon.model.dto.dict.DictAddRequest;
import com.lms.lmscommon.model.dto.dict.DictQueryRequest;
import com.lms.lmscommon.model.dto.dict.DictUpdateRequest;
import com.lms.lmscommon.model.dto.tableinfo.TableInfoAddRequest;
import com.lms.lmscommon.model.dto.tableinfo.TableInfoQueryRequest;
import com.lms.lmscommon.model.dto.tableinfo.TableInfoUpdateRequest;
import com.lms.lmscommon.model.entity.TableInfo;
import com.lms.lmscommon.model.vo.dict.DictVO;
import com.lms.lmscommon.model.vo.tableinfo.TableInfoVO;
import com.lms.sqlfather.core.model.vo.GenerateVO;

import java.util.List;


public interface TableInfoService extends IService<TableInfo> {




    /**
     * 校验并处理
     *
     * @param tableInfo
     * @param add 是否为创建校验
     */
    void validAndHandleTableInfo(TableInfo tableInfo, boolean add);


    /**
     * 创建表信息
     * @param tableInfoAddRequest
     * @return
     */
    Long addTableInfo(TableInfoAddRequest tableInfoAddRequest);
    /**
     * 修改字典
     * @param tableInfoUpdateRequest
     * @return
     */
    Boolean updateTableInfo(TableInfoUpdateRequest tableInfoUpdateRequest);

    /**
     * 生成建表sql
     * @param id
     * @return
     */
    String generateCreateSql(Long id);
    /**
     * 获取当前用户创建的TableInfo
     * @param tableInfoQueryRequest
     * @return
     */
    Page<TableInfoVO> pageMyAddTableInfo(TableInfoQueryRequest tableInfoQueryRequest, Long uid);


    /**
     * 获取当前用户的分页TableInfo
     * @param tableInfoQueryRequest
     * @param uid
     * @return
     */
    Page<TableInfoVO> pageMyTableInfo(TableInfoQueryRequest tableInfoQueryRequest,Long uid);
    /**
     * 获取当前用户的TableInfo
     * @param tableInfoQueryRequest
     * @return
     */
    List<TableInfoVO> listMyTableInfo(TableInfoQueryRequest tableInfoQueryRequest, Long uid);
    /**
     * 分页查询TableInfo
     * @param tableInfoQueryRequest
     * @return
     */
    Page<TableInfoVO> pageTableInfo(TableInfoQueryRequest tableInfoQueryRequest);
    /**
     * 获取TableInfo列表
     * @param tableInfoQueryRequest
     * @return
     */
    List<TableInfoVO> listTableInfo(TableInfoQueryRequest tableInfoQueryRequest);


}
