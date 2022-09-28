package com.billion.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author jason
 */
@Getter
@AllArgsConstructor
@SuppressWarnings({"all"})
public enum NftPropertyType {
    KEYS("keys", "属性key集合"),

    VALUES("values", "属性值集合"),

    TYPES("types", "属性类型集合")
    ;

    final String type;

    final String desc;
}

