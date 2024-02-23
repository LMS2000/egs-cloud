package com.lms.lmscommon.constant;

/**
 * 业务状态常量
 * @author lms2000
 * @since 2024-01-31
 */
public interface StatusConstant {

    /**
     * 标记删除
     */
    Integer DELETED=1;
    /**
     * 标记未删除
     */
    Integer NOT_DELETED=0;

    /**
     * 可用
     */
    Integer  ENABLE=0;
    /**
     * 不可用
     */
    Integer DESIABLE=1;
}
