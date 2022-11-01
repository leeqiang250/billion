package com.billion.model.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import com.billion.model.entity.TransactionStatus;

import java.io.Serializable;

public class MintGroup extends TransactionStatus implements Serializable {

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
     * 询价币种
     */
    @JSONField(name = "box_ask_token")
    Long askToken;

    /**
     * 询价数量
     */
    @JSONField(name = "box_amount")
    String amount;

    /**
     * 计价币种
     */
    @JSONField(name = "box_bid_token")
    Long bidToken;

    /**
     * 计价价格
     */
    @JSONField(name = "box_price")
    String price;

    /**
     * 描述
     */
    @JSONField(name = "box_description")
    String boxDescription;

    /**
     * 规则
     */
    @JSONField(name = "box_rule")
    String rule;

    /**
     * 封面
     */
    @JSONField(name = "box_uri")
    String boxUri;

    /**
     * 起售时间
     */
    @JSONField(name = "box_ts")
    String ts;

    /**
     * 排序
     */
    @JSONField(name = "box_sort")
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
    @JSONField(name = "nft_split")
    Long nftSplit;

    /**
     * 所有者
     */
    @JSONField(name = "nft_owner")
    String nftOwner;

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
    @JSONField(name = "nft_current_supply")
    String nftCurrentSupply;

    /**
     * 总计供应量
     */
    @JSONField(name = "nft_total_supply")
    String nftTotalSupply;

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
    @JSONField(name = "nft_is_initialize_nft_op")
    Boolean nftIsInitializeNftOp;

    /**
     * 排序
     */
    @JSONField(name = "nft_sort")
    Long nftSort;
}