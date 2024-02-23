package com.lms.sqlfather.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lms.lmscommon.model.dto.dict.DictQueryRequest;
import com.lms.lmscommon.model.dto.field.FieldInfoAddRequest;
import com.lms.lmscommon.model.dto.field.FieldInfoQueryRequest;
import com.lms.lmscommon.model.entity.FieldInfo;
import com.lms.lmscommon.model.vo.dict.DictVO;
import com.lms.lmscommon.model.vo.field.FieldInfoVO;
import com.lms.sqlfather.core.model.vo.GenerateVO;

import java.util.List;


public interface FieldInfoService extends IService<FieldInfo> {

    /**
     * 校验并处理
     *
     * @param fieldInfo
     * @param add 是否为创建校验
     */
    void validAndHandleFieldInfo(FieldInfo fieldInfo, boolean add);


    /**
     * 新建字段
     * @param fieldInfoAddRequest
     * @return
     */
    Long addField(FieldInfoAddRequest fieldInfoAddRequest);



    /**
     * 获取当前用户创建的dict
     * @param fieldInfoQueryRequest
     * @return
     */
    Page<FieldInfoVO> pageMyAddFieldInfo(FieldInfoQueryRequest fieldInfoQueryRequest, Long uid);


    /**
     * 获取当前用户的分页dict
     * @param fieldInfoQueryRequest
     * @param uid
     * @return
     */
    Page<FieldInfoVO> pageMyFieldInfo(FieldInfoQueryRequest fieldInfoQueryRequest,Long uid);
    /**
     * 获取当前用户的dict
     * @param fieldInfoQueryRequest
     * @return
     */
    List<FieldInfoVO> listMyFieldInfo(FieldInfoQueryRequest fieldInfoQueryRequest, Long uid);
    /**
     * 分页查询字典
     * @param fieldInfoQueryRequest
     * @return
     */
    Page<FieldInfoVO> pageFieldInfo(FieldInfoQueryRequest fieldInfoQueryRequest);
    /**
     * 获取dict列表
     * @param fieldInfoQueryRequest
     * @return
     */
    List<FieldInfoVO> listFieldInfo(FieldInfoQueryRequest fieldInfoQueryRequest);


    /**
     * 生成建表sql
     * @param id
     * @return
     */
    String generateCreateSql(Long id);
}
