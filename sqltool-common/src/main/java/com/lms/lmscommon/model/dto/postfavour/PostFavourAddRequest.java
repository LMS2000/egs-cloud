package com.lms.lmscommon.model.dto.postfavour;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.io.Serializable;

/**
 * 帖子收藏 / 取消收藏请求
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Data
public class PostFavourAddRequest implements Serializable {

    /**
     * 生成器 id
     */
    @NotNull(message = "生成器id不能为空")
    @Positive(message = "id不合法")
    private Long generatorId;

    private static final long serialVersionUID = 1L;
}