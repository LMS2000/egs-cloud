package com.lms.sqlfather.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.lms.exception.BusinessException;
import com.lms.lmscommon.constant.UserConstant;
import com.lms.lmscommon.model.dto.tableinfo.TableInfoAddRequest;
import com.lms.lmscommon.model.dto.tableinfo.TableInfoQueryRequest;
import com.lms.lmscommon.model.dto.tableinfo.TableInfoUpdateRequest;
import com.lms.lmscommon.model.vo.tableinfo.TableInfoVO;
import com.lms.result.EnableResponseAdvice;
import com.lms.sqlfather.annotation.AuthCheck;
import com.lms.lmscommon.common.DeleteRequest;
import com.lms.sqlfather.service.TableInfoService;
import com.lms.sqlfather.service.TableInfoServiceFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.util.List;

import static com.lms.lmscommon.model.factory.TableInfoFactory.TABLE_INFO_CONVERTER;

/**
 * 表信息控制类
 *
 * @author lms2000
 * @since 2024-01-30
 */
@RestController
@RequestMapping("/table/info")
@EnableResponseAdvice
@AllArgsConstructor
@Api(value = "表信息管理")
public class TableInfoController {


    private final TableInfoService tableInfoService;


    private final TableInfoServiceFacade tableInfoServiceFacade;


    /**
     * 创建表信息
     *
     * @param tableInfoAddRequest
     * @return
     */
    @PostMapping("/add")
    @SaCheckLogin
    @ApiOperationSupport(order =1)
    @ApiOperation(value = "创建表信息")
    public Long add(@Validated @RequestBody  TableInfoAddRequest tableInfoAddRequest) {
        return tableInfoService.addTableInfo(tableInfoAddRequest);
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
    public Boolean delete(@Validated @RequestBody DeleteRequest deleteRequest) {
        Long loginId = Long.parseLong((String) StpUtil.getLoginId());
        return tableInfoServiceFacade.deleteFieldInfo(deleteRequest, loginId);
    }

    /**
     * 仅管理员操作
     *
     * @param tableInfoUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    @ApiOperationSupport(order =3)
    @ApiOperation(value = "修改")
    public Boolean update(@Validated @RequestBody  TableInfoUpdateRequest tableInfoUpdateRequest) {
        return tableInfoService.updateTableInfo(tableInfoUpdateRequest);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    @ApiOperationSupport(order =4)
    @ApiOperation(value = "根据id获取")
    public TableInfoVO getTableInfoById(@Positive(message = "id不合法") Long id) {
        return TABLE_INFO_CONVERTER.toTableInfoVo( tableInfoService.getById(id));
    }

    /**
     * 获取列表（仅管理员可使用）
     *
     * @param tableInfoQueryRequest
     * @return
     */
    @AuthCheck(mustRole = "admin")
    @GetMapping("/list")
    @ApiOperationSupport(order =5)
    @ApiOperation(value = "获取列表（仅管理员可使用）")
    public List<TableInfoVO> listTableInfo(TableInfoQueryRequest tableInfoQueryRequest) {
        return tableInfoService.listTableInfo(tableInfoQueryRequest);
    }

    /**
     * 分页获取列表
     *
     * @param tableInfoQueryRequest
     * @return
     */
    @GetMapping("/list/page")
    @ApiOperationSupport(order =6)
    @ApiOperation(value = "分页获取列表")
    public Page<TableInfoVO> listTableInfoByPage(TableInfoQueryRequest tableInfoQueryRequest) {
        return tableInfoService.pageTableInfo(tableInfoQueryRequest);
    }

    /**
     * 获取当前用户可选的全部资源列表（只返回 id 和名称）
     *
     * @param tableInfoQueryRequest
     * @return
     */
    @GetMapping("/my/list")
    @SaCheckLogin
    @ApiOperationSupport(order =7)
    @ApiOperation(value = "获取当前用户可选的全部资源列表（只返回 id 和名称）")
    public List<TableInfoVO> listMyTableInfo(TableInfoQueryRequest tableInfoQueryRequest) {
        Long loginId = Long.parseLong((String) StpUtil.getLoginId());
        return tableInfoService.listMyTableInfo(tableInfoQueryRequest, loginId);
    }

    /***
     * 分页获取当前用户可选的资源列表
     * @param tableInfoQueryRequest
     * @return
     */
    @GetMapping("/my/list/page")
    @SaCheckLogin
    @ApiOperationSupport(order =8)
    @ApiOperation(value = "分页获取当前用户可选的资源列表")
    public Page<TableInfoVO> listMyTableInfoByPage(TableInfoQueryRequest tableInfoQueryRequest) {
        Long loginId = Long.parseLong((String) StpUtil.getLoginId());
        return tableInfoService.pageMyTableInfo(tableInfoQueryRequest, loginId);
    }

    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param tableInfoQueryRequest
     * @return
     */
    @GetMapping("/my/add/list/page")
    @SaCheckLogin
    @ApiOperationSupport(order =9)
    @ApiOperation(value = "分页获取当前用户创建的资源列表")
    public Page<TableInfoVO> listMyAddTableInfoByPage(TableInfoQueryRequest tableInfoQueryRequest) {
        BusinessException.throwIf(tableInfoQueryRequest == null);
        Long loginId = Long.parseLong((String) StpUtil.getLoginId());
        return tableInfoService.pageMyAddTableInfo(tableInfoQueryRequest, loginId);
    }

    // endregion

    /**
     * 生成创建表的 SQL
     *
     * @param id
     * @return
     */
    @PostMapping("/generate/sql")
    @ApiOperationSupport(order =10)
    @ApiOperation(value = "生成创建表的 SQL")
    public String generateCreateSql(@RequestBody @Positive(message = "id不合法") Long id) {
        return tableInfoService.generateCreateSql(id);
    }
}
