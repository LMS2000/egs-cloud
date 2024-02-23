package com.lms.lmscommon.model.dto.generator;



import com.lms.lmscommon.meta.Meta;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 生成器创建请求
 * @author lms2000
 * @since 2024-02-01
 */
@Data
@ApiModel(value = "GeneratorAddRequest对象", description = "生成器创建请求")
public class GeneratorAddRequest implements Serializable {

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
     * 标签列表（json 数组）
     */
    @ApiModelProperty(value = "标签列表")
    private List<String> tags;

    /**
     * 图片
     */
    @ApiModelProperty(value = "图片")
    private String picture;

    /**
     * 文件配置（json字符串）
     */
    @ApiModelProperty(value = "文件配置")
    private Meta.FileConfig fileConfig;

    /**
     * 模型配置（json字符串）
     */
    @ApiModelProperty(value = "模型配置")
    private Meta.ModelConfig modelConfig;

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