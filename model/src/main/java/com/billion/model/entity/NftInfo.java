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
@TableName("nft_info")
public class NftInfo extends TransactionStatus implements IModel {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    Long id;

    /**
     * box_group_id
     */
    @TableField("box_group_id")
    Long boxGroupId;

    /**
     * nft_group_id
     */
    @TableField("nft_group_id")
    Long nftGroupId;

    /**
     * 名称
     */
    @TableField("display_name")
    String displayName;

    /**
     * 描述
     */
    @TableField("description")
    String description;

    /**
     * 所有者
     */
    @TableField("owner")
    String owner;

    /**
     * 图片
     */
    @TableField("uri")
    String uri;

    /**
     * 排名
     */
    @TableField("rank")
    Long rank;

    /**
     * 铸造哈希
     */
    @TableField("transaction_hash")
    String transactionHash;

    /**
     * 销毁状态(0:未销毁;1:已销毁)
     */
    @TableField("is_born")
    Boolean isBorn;

    /**
     * nft_id
     */
    @TableField("nft_id")
    String nftId;

    /**
     * 分数
     */
    @TableField("score")
    String score;

    @TableField("parent_nft_id")
    Long parentNftId;

}
