package com.billion.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author jason
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("operation")
public class Operation implements Serializable {

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
     * 操作用户地址
     */
    @TableField("account")
    String account;

    /**
     * 操作类型
     */
    @TableField("type")
    String type;

    /**
     * 操作对象id
     */
    @TableField("token_id")
    String tokenId;

    /**
     * 操作对象数量
     */
    @TableField("token_amount")
    Long tokenAmount;

    /**
     * 交易代币
     */
    @TableField("bid_token")
    String bidToken;

    /**
     * 交易代币数量
     */
    @TableField("price")
    Long price;

    /**
     * 交易状态
     */
    @TableField("transaction_status")
    String transactionStatus;

    /**
     * 交易哈希
     */
    @TableField("transaction_hash")
    String transactionHash;

    /**
     * 发生时间
     */
    @TableField("ts")
    String ts;

}
