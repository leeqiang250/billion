package com.billion.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Jason
 */
@Getter
@AllArgsConstructor
public enum OperationTraStateType {

    SELL("SELL", ""),
    SELL_CANCEL("SELL_CANCEL", ""),
    OVER_PRICE("OVER_PRICE", ""),
    HIGHEST_PRICE("HIGHEST_PRICE", ""),
    ACCEPT_PRICE("ACCEPT_PRICE", ""),
    DONE("DONE", ""),
    ;

    String type;
    String desc;
}
