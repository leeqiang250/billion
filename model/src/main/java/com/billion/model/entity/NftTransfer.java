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
import lombok.experimental.SuperBuilder;

/**
 * @author liqiang
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("nft_transfer")
public class NftTransfer extends TransactionStatus implements IModel {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    Long id;

    /**
     * from
     */
    @TableField("from_")
    String from;

    /**
     * to
     */
    @TableField("to_")
    String to;

    /**
     * collection
     */
    @TableField("collection")
    String collection;

    /**
     * creator
     */
    @TableField("creator_")
    String creator;

    /**
     * name
     */
    @TableField("name_")
    String name;

    /**
     * 交易哈希
     */
    @TableField("transaction_hash")
    String transactionHash;

    /**
     * 备注
     */
    @TableField("description")
    String description;

}
