package com.lms.lmscommon.model.dto.generator;

import com.lms.lmscommon.common.PageRequest;
import com.lms.maker.meta.Meta;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 生成器查询请求
 * @author lms2000
 * @since 2024-02-01
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "GeneratorQueryRequest对象", description = "生成器查询请求")
public class GeneratorQueryRequest  extends PageRequest implements Serializable {
    /**
     * id
     */
    @ApiModelProperty(value = "名称")
    private Long id;

    /**
     * id
     */
    @ApiModelProperty(value = "名称")
    private Long notId;

    /**
     * 搜索词
     */
    @ApiModelProperty(value = "名称")
    private String searchText;

    /**
     * 标签列表
     */
    @ApiModelProperty(value = "名称")
    private List<String> tags;

    /**
     * 至少有一个标签
     */
    @ApiModelProperty(value = "名称")
    private List<String> orTags;

    /**
     * 创建用户 id
     */
    @ApiModelProperty(value = "名称")
    private Long userId;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String description;

    /**
     * 基础包
     */
    @ApiModelProperty(value = "基础包")
    private String basePackage;

    /**
     * 版本
     */
    @ApiModelProperty(value = "版本")
    private String version;

    /**
     * 作者
     */
    @ApiModelProperty(value = "作者")
    private String author;


    /**
     * 图片
     */
    @ApiModelProperty(value = "图片")
    private String picture;

    /**
     * 文件配置（json字符串）
     */
    @ApiModelProperty(value = "文件配置")
    private String fileConfig;

    /**
     * 模型配置（json字符串）
     */
    @ApiModelProperty(value = "模型配置")
    private String modelConfig;
    /**
     * 状态（0-待审核, 1-通过, 2-拒绝）
     */
    @ApiModelProperty("审核状态")
    private Integer reviewStatus;

    /**
     * 代码生成器产物路径
     */
    @ApiModelProperty(value = "代码生成器产物路径")
    private String distPath;

    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    private Integer status;
}
