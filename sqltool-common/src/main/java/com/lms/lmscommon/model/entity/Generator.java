package com.lms.lmscommon.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.lms.common.DeleteFlagEntity;
import lombok.*;

/**
 * 代码生成器
 *
 * @TableName generator
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "generator")
@Data
@NoArgsConstructor
public class Generator extends DeleteFlagEntity {


    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 基础包
     */
    private String basePackage;

    /**
     * 版本
     */
    private String version;

    /**
     * 作者
     */
    private String author;

    /**
     * 标签列表（json 数组）
     */
    private String tags;
    /**
     * 状态（0-待审核, 1-通过, 2-拒绝）
     */
    private Integer reviewStatus;

    /**
     * 图片
     */
    private String picture;

    /**
     * 文件配置（json字符串）
     */
    private String fileConfig;

    /**
     * 模型配置（json字符串）
     */
    private String modelConfig;

    /**
     * 代码生成器产物路径
     */
    private String distPath;

    /**
     * 点赞数量
     */
    private Integer thumbNum;

    /**
     * 收藏数量
     */
    private Integer favourNum;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 创建用户 id
     */
    private Long userId;


}
