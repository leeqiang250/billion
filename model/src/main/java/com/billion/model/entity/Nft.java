package com.billion.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.billion.model.model.IModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liqiang
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("nft")
public class Nft extends TransactionStatus implements IModel {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    Long id;

    /**
     * 链类型
     */
    @TableField("chain")
    String chain;

    /**
     * Version
     */
    @TableField("version")
    Long version;

    /**
     * Event
     */
    @TableField("event")
    String event;

    /**
     * Owner
     */
    @TableField("owner")
    String owner;

    /**
     * TokenId
     */
    @TableField("token_id")
    String tokenId;

    /**
     * 交易时间
     */
    @TableField("ts")
    String ts;

    /**
     * 交易hash
     */
    @TableField("transaction_hash")
    String transactionHash;

    /**
     * 是否归档
     */
    @TableField("is_enabled")
    Boolean isEnabled;

}