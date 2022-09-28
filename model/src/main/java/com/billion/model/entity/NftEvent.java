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
@TableName("nft_event")
public class NftEvent implements IModel {

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    Long id;

    /**
     * 链类型
     */
    @TableField("chain")
    String chain;

    /**
     * hash
     */
    @TableField("hash")
    String hash;

    /**
     * key
     */
    @TableField("key_")
    String key;

    /**
     * account
     */
    @TableField("account_")
    String account;

    /**
     * type
     */
    @TableField("type_")
    String type;

    /**
     * collection
     */
    @TableField("collection")
    String collection;

    /**
     * creator
     */
    @TableField("creator")
    String creator;

    /**
     * name
     */
    @TableField("name")
    String name;

}
