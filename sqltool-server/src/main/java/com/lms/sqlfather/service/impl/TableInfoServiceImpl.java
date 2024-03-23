package com.lms.sqlfather.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.lms.contants.HttpCode;
import com.lms.exception.BusinessException;
import com.lms.lmscommon.constant.SqlConstant;
import com.lms.lmscommon.model.dto.tableinfo.TableInfoAddRequest;
import com.lms.lmscommon.model.dto.tableinfo.TableInfoQueryRequest;
import com.lms.lmscommon.model.dto.tableinfo.TableInfoUpdateRequest;
import com.lms.lmscommon.model.entity.TableInfo;
import com.lms.lmscommon.model.enums.ReviewStatusEnum;
import com.lms.lmscommon.model.vo.tableinfo.TableInfoVO;
import com.lms.sqlfather.core.GeneratorFacade;
import com.lms.sqlfather.core.builder.SqlBuilder;
import com.lms.sqlfather.core.schema.TableSchema;
import com.lms.sqlfather.mapper.TableInfoMapper;

import com.lms.sqlfather.service.TableInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static com.lms.lmscommon.model.factory.TableInfoFactory.TABLE_INFO_CONVERTER;

@Service
public class TableInfoServiceImpl extends ServiceImpl<TableInfoMapper, TableInfo> implements TableInfoService {

     private static final Gson GSON  =new Gson();

    @Override
    public void validAndHandleTableInfo(TableInfo tableInfo, boolean add) {
        BusinessException.throwIf(tableInfo==null);
        String content = tableInfo.getContent();
        Integer reviewStatus = tableInfo.getReviewStatus();
        String name = tableInfo.getName();

        BusinessException.throwIf(add& StringUtils.isAnyBlank(name,content));

        BusinessException.throwIf(StringUtils.isNotBlank(name)&&name.length()>30);

        if(StringUtils.isNotBlank(content)){
            BusinessException.throwIf(content.length()>20000);
            try{
                TableSchema schema = GSON.fromJson(content, TableSchema.class);
                GeneratorFacade.validSchema(schema);
            }catch (Exception ex){
                   throw new BusinessException(HttpCode.PARAMS_ERROR,"内容错误");
            }

        }
        BusinessException.throwIf(reviewStatus!=null&& !ReviewStatusEnum.getValues().contains(reviewStatus));
    }

    @Override
    public Long addTableInfo(TableInfoAddRequest tableInfoAddRequest) {

        TableInfo tableInfo = new TableInfo();
        BeanUtils.copyProperties(tableInfoAddRequest, tableInfo);
        validAndHandleTableInfo(tableInfo, true);
        Long loginId =Long.parseLong((String) StpUtil.getLoginId());
        tableInfo.setUserId(loginId);
        boolean result = save(tableInfo);
        BusinessException.throwIf(!result, HttpCode.OPERATION_ERROR);
        return tableInfo.getId();
    }

    @Override
    public Boolean updateTableInfo(TableInfoUpdateRequest tableInfoUpdateRequest) {
        TableInfo tableInfo = new TableInfo();
        BeanUtils.copyProperties(tableInfoUpdateRequest, tableInfo);
        validAndHandleTableInfo(tableInfo, false);
        TableInfo byId = getById(tableInfo.getId());
        BusinessException.throwIf(byId == null, HttpCode.NOT_FOUND_ERROR);
        return updateById(tableInfo);
    }

    @Override
    public String generateCreateSql(Long id) {
        TableInfo tableInfo = this.getById(id);
        BusinessException.throwIf(tableInfo == null, HttpCode.NOT_FOUND_ERROR);
        TableSchema tableSchema = GSON.fromJson(tableInfo.getContent(), TableSchema.class);
        SqlBuilder sqlBuilder = new SqlBuilder();
        return sqlBuilder.buildCreateTableSql(tableSchema);
    }

    @Override
    public Page<TableInfoVO> pageMyAddTableInfo(TableInfoQueryRequest tableInfoQueryRequest, Long uid) {

        tableInfoQueryRequest.setUserId(uid);
        long current = tableInfoQueryRequest.getCurrent();
        long size = tableInfoQueryRequest.getPageSize();
        // 限制爬虫
        BusinessException.throwIf(size > 20, HttpCode.PARAMS_ERROR);
        Page<TableInfo> tableInfoPage = this.page(new Page<>(current, size),
                getQueryWrapper(tableInfoQueryRequest));
        List<TableInfoVO> tableInfoVOList = TABLE_INFO_CONVERTER.toListTableInfoVo(tableInfoPage.getRecords());
        Page<TableInfoVO> tableInfoVOPage=new Page<>(current,size);
        tableInfoVOPage.setRecords(tableInfoVOList);
        tableInfoVOPage.setTotal(tableInfoVOList.size());
        return tableInfoVOPage;
    }

    @Override
    public Page<TableInfoVO> pageMyTableInfo(TableInfoQueryRequest tableInfoQueryRequest, Long uid) {

        long current = tableInfoQueryRequest.getCurrent();
        long size = tableInfoQueryRequest.getPageSize();
        BusinessException.throwIf(size > 20);
        QueryWrapper<TableInfo> queryWrapper = getQueryWrapper(tableInfoQueryRequest);
        queryWrapper.eq("user_id",uid).or().eq("review_status", ReviewStatusEnum.PASS.getValue());
        Page<TableInfo> tableInfoPage = this.page(new Page<>(current, size), getQueryWrapper(tableInfoQueryRequest));
        List<TableInfoVO> tableInfoVOList = TABLE_INFO_CONVERTER.toListTableInfoVo(tableInfoPage.getRecords());
        Page<TableInfoVO> tableInfoVOPage=new Page<>(current,size);
        tableInfoVOPage.setRecords(tableInfoVOList);
        tableInfoVOPage.setTotal(tableInfoVOList.size());
        return tableInfoVOPage;
    }

    @Override
    public List<TableInfoVO> listMyTableInfo(TableInfoQueryRequest tableInfoQueryRequest, Long uid) {
        TableInfo tableInfoQuery = new TableInfo();
        if (tableInfoQueryRequest != null) {
            BeanUtils.copyProperties(tableInfoQueryRequest, tableInfoQuery);
        }
        QueryWrapper<TableInfo> queryWrapper = getQueryWrapper(tableInfoQueryRequest);
        String[] fields = new String[]{"id", "name"};
        queryWrapper.eq("review_status", ReviewStatusEnum.PASS.getValue());
        queryWrapper.select(fields);
        List<TableInfo> tableInfoList = this.list(queryWrapper);

        try {
            tableInfoQuery.setReviewStatus(null);
            tableInfoQuery.setUserId(uid);
            queryWrapper = new QueryWrapper<>(tableInfoQuery);
            queryWrapper.select(fields);
            tableInfoList.addAll(this.list(queryWrapper));
        } catch (Exception ex) {

        }
        //stream 去重
        List<TableInfo> resultList = tableInfoList.stream().collect(Collectors
                .collectingAndThen(Collectors.toCollection(() ->
                        new TreeSet<>(Comparator.comparing(TableInfo::getId))
                ), ArrayList::new));
        return TABLE_INFO_CONVERTER.toListTableInfoVo(resultList);
    }

    @Override
    public Page<TableInfoVO> pageTableInfo(TableInfoQueryRequest tableInfoQueryRequest) {
        long current = tableInfoQueryRequest.getCurrent();
        long size = tableInfoQueryRequest.getPageSize();
        // 限制爬虫
        BusinessException.throwIf(size > 20, HttpCode.PARAMS_ERROR);
        Page<TableInfo> tableInfoPage = this.page(new Page<>(current, size),
                getQueryWrapper(tableInfoQueryRequest));
        List<TableInfoVO> tableInfoVOList = TABLE_INFO_CONVERTER.toListTableInfoVo(tableInfoPage.getRecords());
        Page<TableInfoVO> tableInfoVOPage=new Page<>(current,size);
        tableInfoVOPage.setRecords(tableInfoVOList);
        tableInfoVOPage.setTotal(tableInfoVOList.size());
        return tableInfoVOPage;
    }

    @Override
    public List<TableInfoVO> listTableInfo(TableInfoQueryRequest tableInfoQueryRequest) {
        return TABLE_INFO_CONVERTER.toListTableInfoVo(this.list(getQueryWrapper(tableInfoQueryRequest)));
    }

    private QueryWrapper<TableInfo> getQueryWrapper(TableInfoQueryRequest tableInfoQueryRequest) {
        BusinessException.throwIf(tableInfoQueryRequest == null, HttpCode.NOT_FOUND_ERROR);
        TableInfo tableInfoQuery = new TableInfo();

        BeanUtils.copyProperties(tableInfoQueryRequest, tableInfoQuery);

        String sortField = tableInfoQueryRequest.getSortField();
        String sortOrder = tableInfoQueryRequest.getSortOrder();
        String name = tableInfoQuery.getName();
        String content = tableInfoQuery.getContent();
        tableInfoQuery.setContent(null);
        tableInfoQuery.setName(null);
        QueryWrapper<TableInfo> wrapper = new QueryWrapper<>(tableInfoQuery);
        wrapper.like(StringUtils.isNotBlank(name), "name", name);
        wrapper.like(StringUtils.isNotBlank(content), "content", content);
        wrapper.orderBy(StringUtils.isNotBlank(sortField)
                , sortOrder.equals(SqlConstant.SORT_ORDER_ASC), sortField);
        return wrapper;
    }
}
