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
@TableName("handle")
public class Handle implements IModel {

    @TableId(value = "id", type = IdType.AUTO)
    Long id;

    /**
     * 所有者
     */
    @TableField("owner")
    String owner;

    /**
     * collections_collection_data_handle
     */
    @TableField("collections_collection_data_handle")
    String collectionsCollectionDataHandle;

    /**
     * collections_token_data_handle
     */
    @TableField("collections_token_data_handle")
    String collectionsTokenDataHandle;

    /**
     * token_store_tokens_handle
     */
    @TableField("token_store_tokens_handle")
    String tokenStoreTokensHandle;

}