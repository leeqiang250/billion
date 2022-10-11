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
@TableName("market")
public class Market extends TradeStatus implements IModel {

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
     * version
     */
    @TableField("version")
    Long version;

    /**
     * 订单ID
     */
    @TableField("order_id")
    String orderId;

    /**
     * 订单类型
     */
    @TableField("type_")
    String type;

    /**
     * maker
     */
    @TableField("maker")
    String maker;

    /**
     * price
     */
    @TableField("price")
    String price;

    /**
     * 询价币种
     */
    @TableField("ask_token")
    String askToken;

    /**
     * 询价币种数量
     */
    @TableField("ask_amount")
    String askAmount;

    /**
     * 报价币种
     */
    @TableField("bid_token")
    String bidToken;

    /**
     * 报价币种数量
     */
    @TableField("bid_amount")
    String bidAmount;

    /**
     * bidder
     */
    @TableField("bidder")
    String bidder;

    /**
     * 生成订单时间
     */
    @TableField("ts")
    String ts;

    /**
     * 订单截止时间
     */
    @TableField("dead_ts")
    String deadTs;

}