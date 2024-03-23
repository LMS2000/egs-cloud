package com.lms.lmscommon.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.lms.common.DeleteFlagEntity;
import lombok.*;

/**
 * 举报
 * @TableName report
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value ="report")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report extends DeleteFlagEntity {


    /**
     * 内容
     */
    private String content;

    /**
     * 举报实体类型（0-词库）
     */
    private Integer type;

    /**
     * 被举报对象 id
     */
    private Long reportedId;

    /**
     * 被举报用户 id
     */
    private Long reportedUserId;

    /**
     * 状态（0-未处理, 1-已处理）
     */
    private Integer status;

    /**
     * 创建用户 id
     */
    private Long userId;

}