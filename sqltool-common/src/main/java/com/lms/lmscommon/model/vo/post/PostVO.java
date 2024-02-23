package com.lms.lmscommon.model.vo.post;


import com.lms.lmscommon.common.BaseVO;
import com.lms.lmscommon.model.vo.user.UserVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 帖子视图
 * @author lms2000
 * @since 2024-02-01
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "PostVO对象", description = "帖子视图")
public class PostVO extends BaseVO {
    /**
     * 标题
     */
    @ApiModelProperty("标题")
    private String title;

    /**
     * 内容
     */
    @ApiModelProperty("内容")
    private String content;

    /**
     * 点赞数
     */
    @ApiModelProperty("点赞数")
    private Integer thumbNum;

    /**
     * 收藏数
     */
    @ApiModelProperty("收藏数")
    private Integer favourNum;

    /**
     * 创建用户 id
     */
    @ApiModelProperty("创建用户 id")
    private Long userId;


    /**
     * 标签列表
     */
    @ApiModelProperty("标签列表")
    private List<String> tagList;

    /**
     * 创建人信息
     */
    @ApiModelProperty("创建人信息")
    private UserVO user;

    /**
     * 是否已点赞
     */
    @ApiModelProperty("是否已点赞")
    private Boolean hasThumb;

    /**
     * 是否已收藏
     */
    @ApiModelProperty("是否已收藏")
    private Boolean hasFavour;


}
