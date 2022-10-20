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
public class NftCompose implements IModel {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    Long id;

    @TableField("order_id")
    String orderId;

    @TableField("is_execute")
    Boolean isExecute;

    @TableField("owner")
    String owner;

    @TableField("name")
    String name;

    @TableField("description")
    String description;

}
