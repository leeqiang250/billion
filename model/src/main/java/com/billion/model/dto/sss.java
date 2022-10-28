package com.billion.model.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.billion.model.entity.TransactionStatus;
import com.billion.model.model.IModel;

import java.io.Serializable;

public class sss extends TransactionStatus implements Serializable {

    @JSONField(name = "id")
    Long id;

    /**
     * 链类型
     */
    @JSONField(name = "chain")
    String chain;

    /**
     * 显示名称
     */
    @JSONField(name = "box_display_name")
    String boxDisplayName;

    /**
     * nft_group
     */
    @JSONField(name = "nft_group")
    Long nftGroup;

    /**
     * 询价币种
     */
    @JSONField(name = "ask_token")
    Long askToken;

    /**
     * 询价数量
     */
    @JSONField(name = "amount")
    String amount;

    /**
     * 计价币种
     */
    @JSONField(name = "bid_token")
    Long bidToken;

    /**
     * 计价价格
     */
    @JSONField(name = "price")
    String price;

    /**
     * 描述
     */
    @JSONField(name = "box_description")
    String boxDescription;

    /**
     * 规则
     */
    @JSONField(name = "rule")
    String rule;

    /**
     * 封面
     */
    @JSONField(name = "box_uri")
    String boxUri;

    /**
     * 起售时间
     */
    @JSONField(name = "ts")
    String ts;

    /**
     * 排序
     */
    @JSONField(name = "sort")
    Integer sort;

    /**
     * 是否激活(0:不可用,1:可用)
     */
    @JSONField(name = "box_is_enabled")
    Boolean boxIsEnabled;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 子系列
     */
    @JSONField(name = "split")
    Long split;

    /**
     * 所有者
     */
    @JSONField(name = "owner")
    String owner;

    @JSONField(name = "body")
    String bodyQiyong;

    /**
     * 系列名称
     */
    @JSONField(name = "nft_display_name")
    String nftDisplayName;

    /**
     * 系列描述
     */
    @JSONField(name = "nft_description")
    String nftDescription;

    /**
     * 当前供应量
     */
    @JSONField(name = "current_supply")
    String currentSupply;

    /**
     * 总计供应量
     */
    @JSONField(name = "total_supply")
    String totalSupply;

    /**
     * 图片
     */
    @JSONField(name = "nft_uri")
    String nftUri;

    /**
     * 是否激活(0:不可用,1:可用)
     */
    @JSONField(name = "nft_is_enabled")
    Boolean nftIsEnabled;

    /**
     * 是否初始化nft_op
     */
    @JSONField(name = "is_initialize_nft_op")
    Boolean isInitializeNftOp;

    /**
     * 排序
     */
    @JSONField(name = "nft_sort")
    Long nftSort;
}
