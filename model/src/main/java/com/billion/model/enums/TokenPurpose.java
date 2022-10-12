package com.billion.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author jason
 */

@Getter
@AllArgsConstructor
public enum TokenPurpose {

    ORDER("order", "交易市场挂单");

    String purpose;
    String des;

}
