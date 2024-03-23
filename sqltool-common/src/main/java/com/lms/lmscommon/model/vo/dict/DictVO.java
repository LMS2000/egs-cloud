package com.lms.lmscommon.model.vo.dict;

import com.lms.common.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lms2000
 * @since 2024-02-01
 */
@Data
@ApiModel(value = "DictVO对象", description = "字典视图")
public class DictVO extends BaseVO {

    /**
     * 词库名称
     */
    @ApiModelProperty(value = "字典名称")
    private String name;

    /**
     * 词库内容
     */
    @ApiModelProperty(value = "内容")
    private String content;

    /**
     * 状态（0-待审核, 1-通过, 2-拒绝）
     */
    @ApiModelProperty(value = "状态")
    private Integer reviewStatus;

    /**
     * 审核信息
     */
    @ApiModelProperty(value = "审核信息")
    private String reviewMessage;

    /**
     * 创建用户 id
     */
    @ApiModelProperty(value = "创建用户id")
    private Long userId;
}
