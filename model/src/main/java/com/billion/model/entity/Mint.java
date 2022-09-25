package com.billion.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.billion.model.enums.TransactionStatus;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author liqiang
 */
public class Mint implements Serializable {

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
    public TransactionStatus transactionStatus_;

    private String getTransactionStatus() {
        return this.transactionStatus_.getCode();
    }

    private void setTransactionStatus(String transactionStatus) {
        this.transactionStatus_ = TransactionStatus.of(transactionStatus);
        this.transactionStatus = this.getTransactionStatus();
    }

    public void setTransactionStatus_(TransactionStatus transactionStatus) {
        this.setTransactionStatus(transactionStatus.getCode());
    }

}