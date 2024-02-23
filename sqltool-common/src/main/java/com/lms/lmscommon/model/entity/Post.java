package com.lms.lmscommon.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.lms.lmscommon.common.DeleteFlagEntity;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 帖子
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "post")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post extends DeleteFlagEntity {



    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表 json
     */
    private String tags;

    /**
     * 点赞数
     */
    private Integer thumbNum;

    /**
     * 收藏数
     */
    private Integer favourNum;

    /**
     * 创建用户 id
     */
    private Long userId;

}