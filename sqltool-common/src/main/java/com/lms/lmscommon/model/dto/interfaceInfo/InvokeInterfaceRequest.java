package com.lms.lmscommon.model.dto.interfaceInfo;

import lombok.Data;

import javax.validation.constraints.Positive;
import java.io.Serializable;
@Data
public class InvokeInterfaceRequest implements Serializable {

    @Positive(message = "id不合法")
    private Long id;

    private String requestParams;
}
