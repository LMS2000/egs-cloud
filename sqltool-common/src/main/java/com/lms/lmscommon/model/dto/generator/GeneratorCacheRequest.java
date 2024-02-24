package com.lms.lmscommon.model.dto.generator;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;

/**
 * @author lms2000
 */
@Data
@ApiModel(value = "GeneratorCacheRequest", description = "生成器缓存请求")
public class GeneratorCacheRequest implements Serializable {

    /**
     * 生成器的 id
     */
    @NotNull(message = "id不能为空")
    @Positive(message = "id不合法")
    @ApiModelProperty(value = "生成器的 id")
    private Long id;
}
