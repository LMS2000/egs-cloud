package com.lms.sqlfather.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.lms.exception.BusinessException;
import com.lms.lmscommon.constant.UserConstant;
import com.lms.lmscommon.model.dto.field.FieldInfoAddRequest;
import com.lms.lmscommon.model.dto.field.FieldInfoQueryRequest;
import com.lms.lmscommon.model.dto.field.FieldInfoUpdateRequest;
import com.lms.lmscommon.model.entity.FieldInfo;
import com.lms.lmscommon.model.vo.field.FieldInfoVO;
import com.lms.result.EnableResponseAdvice;
import com.lms.lmscommon.common.DeleteRequest;
import com.lms.sqlfather.service.FieldInfoService;
import com.lms.sqlfather.service.FieldInfoServiceFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

import static com.lms.lmscommon.model.factory.FieldFactory.FIELD_CONVERTER;

/**
 * 字段信息控制类
 *
 * @author lms2000
 * @since 2024-01-30
 */
@RestController
@RequestMapping("/field/info")
@EnableResponseAdvice
@AllArgsConstructor
@Api(value = "字段管理")
public class FieldInfoController {


    private final FieldInfoService fieldInfoService;

    private final FieldInfoServiceFacade fieldInfoServiceFacade;




    /**
     * 创建
     *
     * @param fieldInfoAddRequest
     * @return
     */
    @PostMapping("/add")
    @SaCheckLogin
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "创建字段信息")
    public Long addFieldInfo(@RequestBody @Valid FieldInfoAddRequest fieldInfoAddRequest) {
        BusinessException.throwIf(fieldInfoAddRequest == null);
        return fieldInfoService.addField(fieldInfoAddRequest);
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
    @ApiOperation(value = "删除")
    public Boolean deleteFieldInfo(@RequestBody @Valid DeleteRequest deleteRequest) {
        Long loginId = Long.parseLong((String) StpUtil.getLoginId());
        return fieldInfoServiceFacade.deleteFieldInfo(deleteRequest,loginId);
    }

    /**
     * 更新（仅管理员）
     *
     * @param fieldInfoUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "修改")
    public Boolean updateFieldInfo(@RequestBody  FieldInfoUpdateRequest fieldInfoUpdateRequest) {
        BusinessException.throwIf(fieldInfoUpdateRequest == null || fieldInfoUpdateRequest.getId() <= 0);
        FieldInfo fieldInfo = new FieldInfo();
        BeanUtils.copyProperties(fieldInfoUpdateRequest, fieldInfo);
        // 参数校验
        fieldInfoService.validAndHandleFieldInfo(fieldInfo, false);
        long id = fieldInfoUpdateRequest.getId();
        // 判断是否存在
        FieldInfo oldFieldInfo = fieldInfoService.getById(id);
        BusinessException.throwIf(oldFieldInfo == null);
        return fieldInfoService.updateById(fieldInfo);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "根据id获取")
    public FieldInfoVO getFieldInfoById(@Positive(message = "id不合法") Long id) {
        return FIELD_CONVERTER.toFieldVO(fieldInfoService.getById(id));
    }

    /**
     * 获取列表（仅管理员可使用）
     *
     * @param fieldInfoQueryRequest
     * @return
     */
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    @GetMapping("/list")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "获取列表")
    public List<FieldInfoVO> listFieldInfo(FieldInfoQueryRequest fieldInfoQueryRequest) {
        return fieldInfoService.listFieldInfo(fieldInfoQueryRequest);
    }

    /**
     * 分页获取列表
     *
     * @param fieldInfoQueryRequest
     * @return
     */
    @GetMapping("/list/page")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "分页获取列表")
    public Page<FieldInfoVO> listFieldInfoByPage(FieldInfoQueryRequest fieldInfoQueryRequest) {
        return fieldInfoService.pageFieldInfo(fieldInfoQueryRequest);
    }

    /**
     * 获取当前用户可选的全部资源列表（只返回 id 和名称）
     *
     * @param fieldInfoQueryRequest
     * @return
     */
    @GetMapping("/my/list")
    @SaCheckLogin
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "获取当前用户可选的全部field列表（只返回 id 和名称）")
    public List<FieldInfoVO> listMyFieldInfo(FieldInfoQueryRequest fieldInfoQueryRequest) {
        Long loginId = Long.parseLong((String) StpUtil.getLoginId());
        return fieldInfoService.listMyFieldInfo(fieldInfoQueryRequest, loginId);
    }

    /**
     * 分页获取当前用户可选的资源列表
     *
     * @param fieldInfoQueryRequest
     * @return
     */
    @GetMapping("/my/list/page")
    @SaCheckLogin
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "分页获取当前用户可选的field列表）")
    public Page<FieldInfoVO> listMyFieldInfoByPage(FieldInfoQueryRequest fieldInfoQueryRequest) {
        Long loginId = Long.parseLong((String) StpUtil.getLoginId());
        return fieldInfoService.pageMyFieldInfo(fieldInfoQueryRequest, loginId);

    }

    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param fieldInfoQueryRequest
     * @return
     */
    @GetMapping("/my/add/list/page")
    @SaCheckLogin
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "分页获取当前用户创建的field列表）")
    public Page<FieldInfoVO> listMyAddFieldInfoByPage(FieldInfoQueryRequest fieldInfoQueryRequest) {
        BusinessException.throwIf(fieldInfoQueryRequest == null);
        Long loginId = Long.parseLong((String) StpUtil.getLoginId());
        return fieldInfoService.pageMyAddFieldInfo(fieldInfoQueryRequest, loginId);
    }

    /**
     * 生成创建字段的 SQL
     *
     * @param id
     * @return
     */
    @PostMapping("/generate/sql")
    @ApiOperationSupport(order = 8)
    @ApiOperation(value = "生成创建字段的 SQL）")
    public String generateCreateSql(@RequestBody @Positive(message = "id不合法") Long id) {
        return fieldInfoService.generateCreateSql(id);
    }
}
