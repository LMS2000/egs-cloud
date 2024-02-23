package com.lms.lmscommon.model.dto.post;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 创建请求
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Data
public class PostAddRequest implements Serializable {

    /**
     * 标题
     */
    @NotNull(message = "标题不能为空")
    @NotBlank(message = "标题不能为空")
    private String title;

    /**
     * 内容
     */
    @NotNull(message = "内容不能为空")
    @NotBlank(message = "内容不能为空")
    private String content;

    /**
     * 标签列表
     */
    @NotNull(message = "标签不能为空")
    private List<String> tags;

}