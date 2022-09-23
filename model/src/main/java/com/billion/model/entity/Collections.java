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
@TableName("collections")
public class Collections implements IModel {

    @TableId(value = "id", type = IdType.AUTO)
    Long id;

    /**
     * 所有者
     */
    @TableField("owner")
    String owner;

    /**
     * collection_data_handle
     */
    @TableField("collection_data_handle")
    String collectionDataHandle;

    /**
     * token_data_handle
     */
    @TableField("token_data_handle")
    String tokenDataHandle;

}
