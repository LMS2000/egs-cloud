package com.lms.sqlfather.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lms.lmscommon.common.DeleteRequest;
import com.lms.lmscommon.model.dto.dict.DictAddRequest;
import com.lms.lmscommon.model.dto.dict.DictQueryRequest;
import com.lms.lmscommon.model.dto.dict.DictUpdateRequest;
import com.lms.lmscommon.model.entity.Dict;
import com.lms.lmscommon.model.vo.dict.DictVO;
import com.lms.lmscommon.model.vo.generator.GeneratorVO;
import com.lms.sqlfather.core.model.vo.GenerateVO;

import java.util.List;


public interface DictService extends IService<Dict> {


    /**
     * 生成建表sql
     * @param id
     * @return
     */
    GenerateVO generateCreateSql(Long id);
    /**
     * 获取当前用户创建的dict
     * @param dictQueryRequest
     * @return
     */
    Page<DictVO> pageMyAddDict(DictQueryRequest dictQueryRequest,Long uid);


    /**
     * 获取当前用户的分页dict
     * @param dictQueryRequest
     * @param uid
     * @return
     */
    Page<DictVO> pageMyDict(DictQueryRequest dictQueryRequest,Long uid);
    /**
     * 获取当前用户的dict
     * @param dictQueryRequest
     * @return
     */
    List<DictVO> listMyDict(DictQueryRequest dictQueryRequest,Long uid);
    /**
     * 分页查询字典
     * @param dictQueryRequest
     * @return
     */
    Page<DictVO> pageDict(DictQueryRequest dictQueryRequest);
    /**
     * 获取dict列表
     * @param dictQueryRequest
     * @return
     */
    List<DictVO> listDict(DictQueryRequest dictQueryRequest);
    /**
     * 校验并处理
     *
     * @param dict
     * @param add 是否为创建校验
     */
    void validAndHandleDict(Dict dict, boolean add);


    /**
     * 创建字典
     * @param dictAddRequest
     * @return
     */
    Long addDict(DictAddRequest dictAddRequest);

    /**
     * 修改字典
     * @param dictUpdateRequest
     * @return
     */
    Boolean updateDict(DictUpdateRequest dictUpdateRequest);

}
