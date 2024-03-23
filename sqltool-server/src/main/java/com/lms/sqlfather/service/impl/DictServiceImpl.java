package com.lms.sqlfather.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.lms.contants.HttpCode;
import com.lms.exception.BusinessException;
import com.lms.lmscommon.constant.SqlConstant;
import com.lms.lmscommon.model.dto.dict.DictAddRequest;
import com.lms.lmscommon.model.dto.dict.DictQueryRequest;
import com.lms.lmscommon.model.dto.dict.DictUpdateRequest;
import com.lms.lmscommon.model.entity.Dict;
import com.lms.lmscommon.model.enums.ReviewStatusEnum;
import com.lms.lmscommon.model.vo.dict.DictVO;
import com.lms.sqlfather.core.GeneratorFacade;
import com.lms.sqlfather.core.model.enums.MockTypeEnum;
import com.lms.sqlfather.core.model.vo.GenerateVO;
import com.lms.sqlfather.core.schema.TableSchema;
import com.lms.sqlfather.mapper.DictMapper;
import com.lms.sqlfather.service.DictService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.lms.lmscommon.model.factory.DictFactory.DICT_CONVERTER;

/**
 * 字典服务类
 * @author lms2000
 * @since 2024-02-01
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {
    private final static Gson GSON = new Gson();

    @Override
    public GenerateVO generateCreateSql(Long id) {
        Dict dict = this.getById(id);
        BusinessException.throwIf(dict == null, HttpCode.NOT_FOUND_ERROR);
        String name = dict.getName();
        TableSchema tableSchema = new TableSchema();
        TableSchema.Table table = new TableSchema.Table();

        table.setTableName("dict");
        table.setTableComment(name);
        TableSchema.Field idField = new TableSchema.Field();
        idField.setFieldName("id");
        idField.setFieldType("bigint");
        idField.setNotNull(true);
        idField.setComment("id");
        idField.setPrimaryKey(true);
        idField.setAutoIncrement(true);
        TableSchema.Field dataField = new TableSchema.Field();
        dataField.setFieldName("data");
        dataField.setFieldType("text");
        dataField.setComment("数据");
        dataField.setMockType(MockTypeEnum.DICT.getValue());
        dataField.setMockParams(String.valueOf(id));
        List<TableSchema.Field> fieldList = new ArrayList<>();
        fieldList.add(idField);
        fieldList.add(dataField);
        table.setFieldList(fieldList);
        tableSchema.setTableList(List.of(table));
        return GeneratorFacade.generateAll(tableSchema);
    }

    @Override
    public Page<DictVO> pageMyAddDict(DictQueryRequest dictQueryRequest,Long uid) {
        dictQueryRequest.setUserId(uid);
        long current = dictQueryRequest.getCurrent();
        long size = dictQueryRequest.getPageSize();
        // 限制爬虫
        BusinessException.throwIf(size > 20, HttpCode.PARAMS_ERROR);
        Page<Dict> dictPage = this.page(new Page<>(current, size),
                getQueryWrapper(dictQueryRequest));
        List<DictVO> dictVOList = DICT_CONVERTER.toListDictVO(dictPage.getRecords());
        Page<DictVO> dictVOPage=new Page<>(current,size);
        dictVOPage.setRecords(dictVOList);
        dictVOPage.setTotal(dictVOList.size());
        return dictVOPage;
    }



    @Override
    public Page<DictVO> pageMyDict(DictQueryRequest dictQueryRequest, Long uid) {
        long current = dictQueryRequest.getCurrent();
        long size = dictQueryRequest.getPageSize();
        // 限制爬虫
        BusinessException.throwIf(size > 20, HttpCode.PARAMS_ERROR);
        QueryWrapper<Dict> queryWrapper = getQueryWrapper(dictQueryRequest);
        queryWrapper.eq("user_id", uid)
                .or()
                .eq("review_status", ReviewStatusEnum.PASS.getValue());
        Page<Dict> dictPage = this.page(new Page<>(current, size), queryWrapper);
        List<DictVO> dictVOList = DICT_CONVERTER.toListDictVO(dictPage.getRecords());
        Page<DictVO> dictVOPage=new Page<>(current,size);
        dictVOPage.setRecords(dictVOList);
        dictVOPage.setTotal(dictVOList.size());
        return dictVOPage;
    }

    @Override
    public List<DictVO> listMyDict(DictQueryRequest dictQueryRequest, Long uid) {
        // 先查询所有审核通过的
        dictQueryRequest.setReviewStatus(ReviewStatusEnum.PASS.getValue());
        Dict dictQuery = new Dict();
        QueryWrapper<Dict> queryWrapper = getQueryWrapper(dictQueryRequest);
        final String[] fields = new String[]{"id", "name"};
        queryWrapper.select(fields);
        List<Dict> dictList = this.list(queryWrapper);
        // 再查所有本人的
        try {
            dictQuery.setReviewStatus(null);
            dictQuery.setUserId(uid);
            queryWrapper = new QueryWrapper<>(dictQuery);
            queryWrapper.select(fields);
            dictList.addAll(this.list(queryWrapper));
        } catch (Exception e) {
            // 未登录
        }
        // 根据 id 去重
        List<Dict> myDictList = dictList.stream().collect(Collectors.collectingAndThen(
                Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Dict::getId))), ArrayList::new));
        return DICT_CONVERTER.toListDictVO(myDictList);
    }

    @Override
    public Page<DictVO> pageDict(DictQueryRequest dictQueryRequest) {
        long current = dictQueryRequest.getCurrent();
        long size = dictQueryRequest.getPageSize();
        // 限制爬虫
        BusinessException.throwIf(size > 20, HttpCode.PARAMS_ERROR);
        Page<Dict> dictPage = this.page(new Page<>(current, size),
                getQueryWrapper(dictQueryRequest));
        List<DictVO> dictVOList = DICT_CONVERTER.toListDictVO(dictPage.getRecords());
        Page<DictVO> dictVOPage=new Page<>(current,size);
        dictVOPage.setRecords(dictVOList);
        dictVOPage.setTotal(dictVOList.size());
        return dictVOPage;
    }

    @Override
    public List<DictVO> listDict(DictQueryRequest dictQueryRequest) {
        return DICT_CONVERTER.toListDictVO(this.list(getQueryWrapper(dictQueryRequest)));
    }

    @Override
    public void validAndHandleDict(Dict dict, boolean add) {
        BusinessException.throwIf(dict == null);
        String content = dict.getContent();
        String name = dict.getName();
        Integer reviewStatus = dict.getReviewStatus();
        BusinessException.throwIf(add && StringUtils.isAnyBlank(name, content));
        BusinessException.throwIf(StringUtils.isNotBlank(name) && name.length() > 30);
        if (StringUtils.isNotBlank(content)) {

            BusinessException.throwIf(content.length() > 20000);


            try {
                String[] words = content.split("[,.]");
                for (int i = 0; i < words.length; i++) {
                    words[i] = words[i].trim();
                }
                List<String> collect = Arrays.stream(words)
                        .filter(StringUtils::isNotBlank)
                        .collect(Collectors.toList());
                dict.setContent(GSON.toJson(collect));
            } catch (Exception ex) {
                throw new BusinessException(HttpCode.PARAMS_ERROR);
            }
        }
        BusinessException.throwIf(reviewStatus != null && !ReviewStatusEnum.getValues().contains(reviewStatus));
    }

    @Override
    public Long addDict(DictAddRequest dictAddRequest) {
        BusinessException.throwIf(dictAddRequest == null, HttpCode.PARAMS_ERROR);
        Dict dict = new Dict();
        BeanUtils.copyProperties(dictAddRequest, dict);
        // 校验
        validAndHandleDict(dict, true);
        Long loginId = Long.parseLong((String) StpUtil.getLoginId());
        dict.setUserId(loginId);
        boolean result = this.save(dict);
        BusinessException.throwIf(!result, HttpCode.OPERATION_ERROR);
        return dict.getId();
    }

    @Override
    public Boolean updateDict(DictUpdateRequest dictUpdateRequest) {
        Dict dict = new Dict();
        BeanUtils.copyProperties(dictUpdateRequest, dict);
        // 参数校验
        validAndHandleDict(dict, false);
        long id = dictUpdateRequest.getId();
        // 判断是否存在
        Dict oldDict = getById(id);
        BusinessException.throwIf(oldDict == null, HttpCode.NOT_FOUND_ERROR);
        return updateById(dict);
    }


    /**
     * 获取查询包装类
     *
     * @param dictQueryRequest
     * @return
     */
    private QueryWrapper<Dict> getQueryWrapper(DictQueryRequest dictQueryRequest) {

        BusinessException.throwIf(dictQueryRequest == null, HttpCode.PARAMS_ERROR, "请求参数为空");
        Dict dictQuery = new Dict();
        BeanUtils.copyProperties(dictQueryRequest, dictQuery);
        String sortField = dictQueryRequest.getSortField();
        String sortOrder = dictQueryRequest.getSortOrder();
        String name = dictQuery.getName();
        String content = dictQuery.getContent();
        // name、content 需支持模糊搜索
        dictQuery.setName(null);
        dictQuery.setContent(null);
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>(dictQuery);
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField), sortOrder.equals(SqlConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }
}
