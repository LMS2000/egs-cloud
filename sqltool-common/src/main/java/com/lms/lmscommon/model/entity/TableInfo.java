package com.lms.lmscommon.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.lms.common.DeleteFlagEntity;
import lombok.*;

/**
 * 表信息
 * @TableName table_info
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value ="table_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TableInfo extends DeleteFlagEntity {


    /**
     * 名称
     */
    private String name;

    /**
     * 内容
     */
    private String content;

    /**
     * 状态（0-待审核, 1-通过, 2-拒绝）
     */
    private Integer reviewStatus;

    /**
     * 审核信息
     */
    private String reviewMessage;

    /**
     * 创建用户 id
     */
    private Long userId;
}