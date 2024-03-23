package com.lms.lmscommon.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lms.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 帖子点赞
 *
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "post_thumb")
@Data
public class PostThumb extends BaseEntity {



    /**
     * 帖子 id
     */
    private Long postId;

    /**
     * 创建用户 id
     */
    private Long userId;



}