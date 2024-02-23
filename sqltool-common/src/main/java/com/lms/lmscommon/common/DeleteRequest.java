package com.lms.lmscommon.common;

import lombok.Data;

import javax.validation.constraints.Positive;
import java.io.Serializable;

/**
 * 删除请求
 *
 */
@Data
public class DeleteRequest implements Serializable {
    /**
     * id
     */
    @Positive(message = "id不合法")
    private Long id;

    private static final long serialVersionUID = 1L;
}