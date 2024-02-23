package com.lms.lmscommon.model.dto.userInterfaceInfo;

import com.lms.page.CustomPage;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class PageUserInterfaceInfoRequest extends CustomPage implements Serializable {

    private Integer method;

    private String name;

    private Integer status;
}
