package com.lms.lmscommon.model.dto.dict;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Positive;
import java.io.Serializable;


/**
 * 字典更新请求
 * @author lms2000
 * @since 2024-02-01
 */
@Data
@ApiModel(value = "DictUpdateRequest对象", description = "更新字典")
public class DictUpdateRequest implements Serializable {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    @Positive(message = "id不合法")
    private long id;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;

    /**
     * 内容
     */
    @ApiModelProperty(value = "内容")
    private String content;

    /**
     * 状态（0-待审核, 1-通过, 2-拒绝）
     */
    @ApiModelProperty(value = "审核状态")
    private Integer reviewStatus;

    /**
     * 审核信息
     */
    @ApiModelProperty(value = "名称")
    private String reviewMessage;
}