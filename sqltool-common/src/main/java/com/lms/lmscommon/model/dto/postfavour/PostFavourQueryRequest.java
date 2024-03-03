package com.lms.lmscommon.model.dto.postfavour;


import com.lms.lmscommon.common.PageRequest;
import com.lms.lmscommon.model.dto.generator.GeneratorQueryRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 帖子收藏查询请求
 *
 * @author lms2000
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PostFavourQueryRequest extends PageRequest implements Serializable {

    /**
     * 生成器查询请求
     */
    private GeneratorQueryRequest generatorQueryRequest;

    /**
     * 用户 id
     */
    private Long userId;

    private static final long serialVersionUID = 1L;
}