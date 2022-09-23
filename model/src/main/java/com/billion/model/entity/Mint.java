package com.billion.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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
    public com.billion.model.enums.Mint mint_;

    private String getMint() {
        return this.mint_.getCode();
    }

    private void setMint(String mint) {
        this.mint_ = com.billion.model.enums.Mint.of(mint);
        this.mint = this.getMint();
    }

    public void setMint_(com.billion.model.enums.Mint mint_) {
        this.setMint(mint_.getCode());
    }

}