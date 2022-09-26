package com.billion.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.*;

import java.io.Serializable;

/**
 * @author liqiang
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionStatus implements Serializable {

    /**
     * 交易状态
     */
    @TableField("transaction_status")
    String transactionStatus;

    /**
     * 铸造状态
     */
    @TableField(exist = false)
    com.billion.model.enums.TransactionStatus transactionStatus_;

    public String getTransactionStatus() {
        return this.transactionStatus_.getCode();
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus_ = com.billion.model.enums.TransactionStatus.of(transactionStatus);
        this.transactionStatus = this.getTransactionStatus();
    }

    public void setTransactionStatus_(com.billion.model.enums.TransactionStatus transactionStatus) {
        this.setTransactionStatus(transactionStatus.getCode());
    }

}