package com.billion.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author jason
 */

@Getter
@AllArgsConstructor
public enum TokenScene {

    ORDER("order", "交易市场挂单");

    String scene;

    String des;

}