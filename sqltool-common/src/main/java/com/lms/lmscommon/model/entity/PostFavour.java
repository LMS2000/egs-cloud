package com.lms.lmscommon.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lms.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 帖子收藏
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 **/
@EqualsAndHashCode(callSuper = true)
@TableName(value = "post_favour")
@Data
public class PostFavour extends BaseEntity {



    /**
     * 帖子 id
     */
    private Long postId;

    /**
     * 创建用户 id
     */
    private Long userId;

}