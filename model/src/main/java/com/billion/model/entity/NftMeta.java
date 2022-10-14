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
@TableName("nft_meta")
public class NftMeta extends TransactionStatus implements IModel {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    Long id;

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
     * 销毁状态(0:未销毁;1:已销毁)
     */
    @TableField("is_born")
    Boolean isBorn;

    /**
     * token_id
     */
    @TableField("token_id")
    String tokenId;

    /**
     * 分数
     */
    @TableField("score")
    String score;

    @TableField("attribute_type")
    Integer attributeType;

    /**
     * table_handle
     */
    @TableField("table_handle")
    String tableHandle;

    /**
     * table_collection
     */
    @TableField("table_collection")
    String tableCollection;

    /**
     * table_creator
     */
    @TableField("table_creator")
    String tableCreator;

    /**
     * table_name
     */
    @TableField("table_name")
    String tableName;

}
