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
@TableName("box_group_copy")
public class BoxGroupCopy extends TransactionStatus implements IModel {

    /**
     * 主键Id
     */
    @TableId(value = "id", type = IdType.AUTO)
    Long id;

    /**
     * 链类型
     */
    @TableField("chain")
    String chain;

    /**
     * 显示名称
     */
    @TableField("display_name")
    String displayName;

    /**
     * 询价币种
     */
    @TableField("ask_token")
    Long askToken;

    /**
     * 询价数量
     */
    @TableField("amount")
    String amount;

    /**
     * 计价币种
     */
    @TableField("bid_token")
    Long bidToken;

    /**
     * 计价价格
     */
    @TableField("price")
    String price;

    /**
     * 描述
     */
    @TableField("description")
    String description;

    /**
     * 规则
     */
    @TableField("rule")
    String rule;

    /**
     * 起售时间
     */
    @TableField("ts")
    String ts;

    /**
     * 排序
     */
    @TableField("sort")
    Integer sort;

    /**
     * 是否激活(0:不可用,1:可用)
     */
    @TableField("enabled")
    Boolean enabled;

    /**
     * 交易hash
     */
    @TableField("transaction_hash")
    String transactionHash;

    @TableField("x")
    String x;

}