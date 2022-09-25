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
     * 铸造状态
     */
    @TableField("mint")
    String mint;

    /**
     * 铸造状态
     */
    @Getter
    @Setter
    @TableField(exist = false)
    public TransactionStatus mint_;

    private String getMint() {
        return this.mint_.getCode();
    }

    private void setMint(String mint) {
        this.mint_ = TransactionStatus.of(mint);
        this.mint = this.getMint();
    }

    public void setMint_(TransactionStatus mint_) {
        this.setMint(mint_.getCode());
    }

}