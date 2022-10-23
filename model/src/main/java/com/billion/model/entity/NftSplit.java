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
@TableName("nft_split")
public class NftSplit extends TransactionStatus implements IModel {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    Long id;

    /**
     * Version
     */
    @TableField("version")
    Long version;

    /**
     * OrderId
     */
    @TableField("order_id")
    String orderId;

    /**
     * IsExecute
     */
    @TableField("is_execute")
    Boolean isExecute;

    /**
     * owner
     */
    @TableField("owner")
    String owner;

    /**
     * MetaIds
     */
    @TableField("meta_ids")
    String metaIds;

}