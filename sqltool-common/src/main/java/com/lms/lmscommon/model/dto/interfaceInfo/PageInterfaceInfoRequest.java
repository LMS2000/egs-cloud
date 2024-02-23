package com.lms.lmscommon.model.dto.interfaceInfo;

import com.lms.page.CustomPage;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class PageInterfaceInfoRequest  extends CustomPage implements Serializable {

    private Integer method;

    private Integer status;

    private String name;

}
