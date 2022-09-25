package com.billion.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author liqiang
 */
public class TransactionStatus implements Serializable {

    /**
     * 交易状态
     */
    @TableField("transaction_status")
    String transactionStatus;

    /**
     * 铸造状态
     */
    @Getter
    @Setter
    @TableField(exist = false)
    public com.billion.model.enums.TransactionStatus transactionStatus_;

    private String getTransactionStatus() {
        return this.transactionStatus_.getCode();
    }

    private void setTransactionStatus(String transactionStatus) {
        this.transactionStatus_ = com.billion.model.enums.TransactionStatus.of(transactionStatus);
        this.transactionStatus = this.getTransactionStatus();
    }

    public void setTransactionStatus_(com.billion.model.enums.TransactionStatus transactionStatus) {
        this.setTransactionStatus(transactionStatus.getCode());
    }

}