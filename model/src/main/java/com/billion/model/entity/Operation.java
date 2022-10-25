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
 * @author jason
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("operation")
public class Operation extends TransactionStatus implements IModel {

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
    @TableField("maker")
    String maker;

    /**
     * 出价用户地址
     */
    @TableField("bidder")
    String bidder;

    /**
     * 订单id
     */
    @TableField("order_id")
    String orderId;


    /**
     * 交易类型 一口价or拍卖
     */
    @TableField("tra_type")
    String traType;

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
     * 支付代币
     */
    @TableField("bid_token")
    String bidToken;

    /**
     * 支付代币数量
     */
    @TableField("bid_token_amount")
    String bidTokenAmount;

    /**
     * 交易价格
     */
    @TableField("price")
    String price;

    /**
     *
     */
    @TableField("state")
    String state;

    /**
     * 发生时间
     */
    @TableField("ts")
    String ts;

}