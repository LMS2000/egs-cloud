package com.lms.lmscommon.model.enums;

import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lms2000
 */
public enum ModelConfigTypeEnum {

    BOOLEAN_TYPE("boolean","布尔型"),
    STRING_TYPE("String","字符串类型");


    @Getter
    private String type;

    @Getter
    private String description;

    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.type).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static ModelConfigTypeEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (ModelConfigTypeEnum anEnum : ModelConfigTypeEnum.values()) {
            if (anEnum.type.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }
    ModelConfigTypeEnum(String type, String description) {
        this.type = type;
        this.description = description;
    }
}
