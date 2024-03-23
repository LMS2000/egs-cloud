package com.lms.lmscommon.model.vo.generator;

import com.lms.common.BaseVO;
import com.lms.lmscommon.model.vo.user.UserVO;
import com.lms.maker.meta.Meta;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


import java.util.List;

/**
 * @author lms2000
 * @since 2024-02-01
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "GeneratorVO对象", description = "生成器视图")
public class GeneratorVO extends BaseVO {



    /**
     * 名称
     */
    @ApiModelProperty("生成器名称")
    private String name;

    /**
     * 描述
     */
    @ApiModelProperty("说明")
    private String description;

    /**
     * 基础包
     */
    @ApiModelProperty("基础包")
    private String basePackage;

    /**
     * 版本
     */
    @ApiModelProperty("版本")
    private String version;

    /**
     * 作者
     */
    @ApiModelProperty("作者")
    private String author;
    /**
     * 状态（0-待审核, 1-通过, 2-拒绝）
     */
    @ApiModelProperty("审核状态")
    private Integer reviewStatus;

    /**
     * 标签列表（json 数组）
     */
    @ApiModelProperty("标签列表")
    private List<String> tags;

    /**
     * 图片
     */
    @ApiModelProperty("图片路径")
    private String picture;

    /**
     * 文件配置（json字符串）
     */
    @ApiModelProperty("文件配置")
    private Meta.FileConfig fileConfig;

    /**
     * 模型配置（json字符串）
     */
    @ApiModelProperty("模型配置")
    private Meta.ModelConfig modelConfig;

    /**
     * 代码生成器产物路径
     */
    @ApiModelProperty("代码生成器产物路径")
    private String distPath;

    /**
     * 状态
     */
    @ApiModelProperty("代码生成器状态")
    private Integer status;


    /**
     * 点赞数量
     */
    @ApiModelProperty("点赞数量")
    private Integer thumbNum;

    /**
     * 收藏数量
     */
    @ApiModelProperty("收藏数量")
    private Integer favourNum;


    /**
     * 当前用户是否点赞
     */
    @ApiModelProperty("当前用户是否点赞")
    private Integer stared;

    /**
     * 当前用户是否收藏
     */
    @ApiModelProperty("当前用户是否收藏")
    private Integer favoured;
    /**
     * 创建用户 id
     */
    @ApiModelProperty("创建人id")
    private Long userId;



    /**
     * 创建人信息
     */
    @ApiModelProperty("创建人信息")
    private UserVO user;



//    /**
//     * 包装类转对象
//     *
//     * @param generatorVO
//     * @return
//     */
//    public static Generator voToObj(GeneratorVO generatorVO) {
//        if (generatorVO == null) {
//            return null;
//        }
//        Generator generator = new Generator();
//        BeanUtils.copyProperties(generatorVO, generator);
//        List<String> tagList = generatorVO.getTags();
//        generator.setTags(JSONUtil.toJsonStr(tagList));
//        Meta.FileConfig fileConfig = generatorVO.getFileConfig();
//        generator.setFileConfig(JSONUtil.toJsonStr(fileConfig));
//        Meta.ModelConfig modelConfig = generatorVO.getModelConfig();
//        generator.setModelConfig(JSONUtil.toJsonStr(modelConfig));
//        return generator;
//    }
//
//    /**
//     * 对象转包装类
//     *
//     * @param generator
//     * @return
//     */
//    public static GeneratorVO objToVo(Generator generator) {
//        if (generator == null) {
//            return null;
//        }
//        GeneratorVO generatorVO = new GeneratorVO();
//        BeanUtils.copyProperties(generator, generatorVO);
//        generatorVO.setTags(JSONUtil.toList(generator.getTags(), String.class));
//        generatorVO.setFileConfig(JSONUtil.toBean(generator.getFileConfig(), Meta.FileConfig.class));
//        generatorVO.setModelConfig(JSONUtil.toBean(generator.getModelConfig(), Meta.ModelConfig.class));
//        return generatorVO;
//    }


}
