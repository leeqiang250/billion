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
@TableName("nft_compose")
public class NftCompose extends TransactionStatus implements IModel {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    Long id;

    /**
     * version
     */
    @TableField("version")
    Long version;

    /**
     * order_id
     */
    @TableField("order_id")
    String orderId;

    /**
     * is_execute
     */
    @TableField("is_execute")
    Boolean isExecute;

    /**
     * owner
     */
    @TableField("owner")
    String owner;

    /**
     * name
     */
    @TableField("name_")
    String name;

    /**
     * description
     */
    @TableField("description")
    String description;

    /**
     * property_keys
     */
    @TableField("property_keys")
    String propertyKeys;

    /**
     * property_values
     */
    @TableField("property_values")
    String propertyValues;

    /**
     * property_types
     */
    @TableField("property_types")
    String propertyTypes;

    /**
     * collection
     */
    @TableField("collection")
    String collection;

}