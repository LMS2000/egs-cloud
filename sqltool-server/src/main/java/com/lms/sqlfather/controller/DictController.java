package com.lms.sqlfather.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.lms.contants.HttpCode;
import com.lms.lmscommon.constant.UserConstant;
import com.lms.lmscommon.model.dto.dict.DictAddRequest;
import com.lms.lmscommon.model.dto.dict.DictQueryRequest;
import com.lms.lmscommon.model.dto.dict.DictUpdateRequest;
import com.lms.lmscommon.model.vo.dict.DictVO;
import com.lms.result.EnableResponseAdvice;
import com.lms.lmscommon.common.DeleteRequest;

import com.lms.sqlfather.core.model.vo.GenerateVO;
import com.lms.lmscommon.common.BusinessException;

import com.lms.sqlfather.service.DictService;
import com.lms.sqlfather.service.DictServiceFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

import static com.lms.lmscommon.model.factory.DictFactory.DICT_CONVERTER;

/**
 * 字典控制类
 *
 * @author lms2000
 * @since 2024-01-30
 */
@RestController
@RequestMapping("/dict")
@EnableResponseAdvice
@AllArgsConstructor
@Api(value = "字典管理")
public class DictController {


    private final DictServiceFacade dictServiceFacade;


    private final DictService dictService;

    /**
     * 创建
     *
     * @param dictAddRequest
     * @return
     */
    @PostMapping("/add")
    @SaCheckLogin
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "创建dict")
    public Long addDict(@RequestBody @Valid DictAddRequest dictAddRequest) {
        return dictService.addDict(dictAddRequest);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @return
     */
    @PostMapping("/delete")
    @SaCheckLogin
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "根据id删除dict")
    public Boolean deleteDict(@RequestBody DeleteRequest deleteRequest) {
        return dictServiceFacade.deleteDict(deleteRequest);
    }

    /**
     * 更新（仅管理员）
     *
     * @param dictUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "修改dict")
    public Boolean updateDict(@RequestBody @Valid DictUpdateRequest dictUpdateRequest) {
        return dictService.updateDict(dictUpdateRequest);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "根据id获取dict")
    public DictVO getDictById(@Positive(message = "id不合法") Long id) {
        return  DICT_CONVERTER.toDictVO(dictService.getById(id));

    }

    /**
     * 获取列表（仅管理员可使用）
     *
     * @param dictQueryRequest
     * @return
     */
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    @GetMapping("/list")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "查询dict列表")
    public List<DictVO> listDict(DictQueryRequest dictQueryRequest) {
        return dictService.listDict(dictQueryRequest);
    }

    /**
     * 分页获取列表
     *
     * @param dictQueryRequest
     * @return
     */
    @GetMapping("/list/page")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "查询dict分页列表")
    public Page<DictVO> listDictByPage(DictQueryRequest dictQueryRequest) {
        return dictService.pageDict(dictQueryRequest);
    }

    /**
     * 获取当前用户可选的全部资源列表（只返回 id 和名称）
     *
     * @param dictQueryRequest
     * @return
     */
    @GetMapping("/my/list")
    @SaCheckLogin
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "查询当前用户可选的全部dict列表")
    public List<DictVO> listMyDict(DictQueryRequest dictQueryRequest) {
        BusinessException.throwIf(dictQueryRequest == null, HttpCode.PARAMS_ERROR);
        Long loginId = Long.parseLong((String) StpUtil.getLoginId());
        return dictService.listMyDict(dictQueryRequest, loginId);
    }

    /**
     * 分页获取当前用户可选的资源列表
     *
     * @param dictQueryRequest
     * @return
     */
    @GetMapping("/my/list/page")
    @SaCheckLogin
    @ApiOperationSupport(order = 8)
    @ApiOperation(value = "分页获取当前用户可选的dict列表")
    public Page<DictVO> listMyDictByPage(DictQueryRequest dictQueryRequest) {
        Long loginId = Long.parseLong((String) StpUtil.getLoginId());
        return dictService.pageMyDict(dictQueryRequest, loginId);
    }

    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param dictQueryRequest
     * @return
     */
    @GetMapping("/my/add/list/page")
    @SaCheckLogin
    @ApiOperationSupport(order = 9)
    @ApiOperation(value = "分页获取当前用户创建的dict列表")
    public Page<DictVO> listMyAddDictByPage(DictQueryRequest dictQueryRequest) {
        BusinessException.throwIf(dictQueryRequest == null, HttpCode.PARAMS_ERROR);
        Long loginId = Long.parseLong((String) StpUtil.getLoginId());
        return dictService.pageMyAddDict(dictQueryRequest, loginId);
    }
    /**
     * 生成创建表的 SQL
     *
     * @param id
     * @return
     */
    @PostMapping("/generate/sql")
    @ApiOperationSupport(order =10)
    @ApiOperation(value = "生成创建表的 SQL")
    public GenerateVO generateCreateSql(@RequestBody @Positive(message = "id不合法") Long id) {
      return dictService.generateCreateSql(id);
    }

}
