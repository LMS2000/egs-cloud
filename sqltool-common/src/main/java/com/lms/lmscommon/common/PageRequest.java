package com.lms.lmscommon.common;


import com.lms.lmscommon.constant.CommonConstant;
import com.lms.lmscommon.constant.SqlConstant;
import lombok.Data;

/**
 * 分页请求
 *
 */
@Data
public class PageRequest {

    /**
     * 当前页号
     */
    private Long current = 1L;

    /**
     * 页面大小
     */
    private Long pageSize = 10L;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序顺序（默认升序）
     */
    private String sortOrder = SqlConstant.SORT_ORDER_ASC;
}
