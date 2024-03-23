package com.lms.lmscommon.model.vo.tableinfo;

import com.lms.common.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 表信息视图
 *
 * @author lms2000
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "TableInfoVO对象", description = "表信息视图")
public class TableInfoVO extends BaseVO {


    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;

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


}
