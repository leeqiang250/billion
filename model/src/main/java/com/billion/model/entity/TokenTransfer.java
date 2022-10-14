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

import java.time.LocalDateTime;

/**
 * @author liqiang
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("token_transfer")
public class TokenTransfer extends TransactionStatus implements IModel {

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
     * module_address
     */
    @TableField("module_address")
    String moduleAddress;

    /**
     * module_name
     */
    @TableField("module_name")
    String moduleName;

    /**
     * resource_name
     */
    @TableField("resource_name")
    String resourceName;

    /**
     * amount
     */
    @TableField("amount_")
    String amount;

    /**
     * 备注
     */
    @TableField("description")
    String description;

}
