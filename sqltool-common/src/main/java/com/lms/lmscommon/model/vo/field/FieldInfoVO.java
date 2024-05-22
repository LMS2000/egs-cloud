package com.lms.lmscommon.model.vo.field;

import com.lms.common.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lms2000
 * @since 2024-02-01
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "FieldInfoVO对象", description = "字段视图")
public class FieldInfoVO extends BaseVO {

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;

    /**
     * 字段名称
     */
    @ApiModelProperty("字段名称")
    private String fieldName;

    /**
     * 内容
     */
    @ApiModelProperty("内容")
    private String content;

    /**
     * 状态（0-待审核, 1-通过, 2-拒绝）
     */
    @ApiModelProperty("状态（0-待审核, 1-通过, 2-拒绝）")
    private Integer reviewStatus;

    /**
     * 审核信息
     */
    @ApiModelProperty("审核信息")
    private String reviewMessage;

    /**
     * 创建用户 id
     */
    @ApiModelProperty("创建用户 id")
    private Long userId;
    /**
     * 创建的用户名
     */
    @ApiModelProperty("作者名字")
    private String author;
}
