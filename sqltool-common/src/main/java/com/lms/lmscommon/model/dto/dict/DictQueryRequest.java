package com.lms.lmscommon.model.dto.dict;

import com.lms.lmscommon.common.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 字典查询请求
 * @author lms2000
 * @since 2024-02-01
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "DictQueryRequest对象", description = "查询字典")
public class DictQueryRequest extends PageRequest implements Serializable {

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;

    /**
     * 内容，支持模糊查询
     */
    @ApiModelProperty(value = "内容")
    private String content;

    /**
     * 状态（0-待审核, 1-通过, 2-拒绝）
     */
    @ApiModelProperty(value = "审核状态")
    private Integer reviewStatus;

    /**
     * 创建用户 id
     */
    @ApiModelProperty(value = "创建用户id")
    private Long userId;

}