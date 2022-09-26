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
@TableName("token")
public class Token extends TransactionStatus implements IModel {

    @TableId(value = "id", type = IdType.AUTO)
    Long id;

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
     * struct_name
     */
    @TableField("struct_name")
    String structName;

    /**
     * initialize_function
     */
    @TableField("initialize_function")
    String initializeFunction;

    /**
     * name_
     */
    @TableField("name_")
    String name;

    /**
     * symbol
     */
    @TableField("symbol")
    String symbol;

    /**
     * 初始化哈希
     */
    @TableField("transaction_hash")
    String transactionHash;

}