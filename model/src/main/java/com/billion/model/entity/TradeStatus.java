package com.billion.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author liqiang
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradeStatus implements Serializable {

    /**
     * 交易状态
     */
    @TableField("trade_status")
    String tradeStatus;

    /**
     * 交易状态
     */
    @TableField(exist = false)
    com.billion.model.enums.TradeStatus tradeStatus_;

    public String getTradeStatus() {
        return this.tradeStatus_.getCode();
    }

    public void setTradeStatus(String tradeStatus) {
        this.tradeStatus_ = com.billion.model.enums.TradeStatus.of(tradeStatus);
        this.tradeStatus = this.getTradeStatus();
    }

    public void setTradeStatus_(com.billion.model.enums.TradeStatus tradeStatus) {
        this.setTradeStatus(tradeStatus.getCode());
    }

}