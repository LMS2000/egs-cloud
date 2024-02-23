package com.lms.lmscommon.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lms.lmscommon.common.DeleteFlagEntity;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 词库
 * @TableName dict
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value ="dict")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dict extends DeleteFlagEntity {


    /**
     * 词库名称
     */
    private String name;

    /**
     * 词库内容
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