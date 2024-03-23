package com.lms.lmscommon.constant;

import java.net.Inet4Address;

/**
 * sql模块常量
 * @author lms2000
 * @since 2024-01-31
 */
public interface SqlConstant {

    /**
     * 最大生成表数量
     */
    Integer MAX_TABLE_NUM =16;
    /**
     * 升序
     */
    String SORT_ORDER_ASC = "ascend";

    /**
     * 降序
     */
    String SORT_ORDER_DESC = " descend";
    /**
     *
     */
    String AI_BUILD_SUFFIX="仅输出建表SQL";

    String DEFAULT_GENERATED_PACEKAGE="com.lms.generated";



}
