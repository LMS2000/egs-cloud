package com.lms.lmscommon.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lms.common.DeleteFlagEntity;
import lombok.*;

/**
 * 字段信息
 * @TableName field_info
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value ="field_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FieldInfo extends DeleteFlagEntity {


    /**
     * 名称
     */
    private String name;

    /**
     * 字段名称
     */
    private String fieldName;

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