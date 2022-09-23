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
public class Token implements IModel {

    @TableId(value = "id", type = IdType.AUTO)
    Long id;

    @TableField("module_address")
    String moduleAddress;

    @TableField("module_name")
    String moduleName;

    @TableField("struct_name")
    String structName;

    @TableField("initialize_function")
    String initializeFunction;

    @TableField("name_")
    String name;

    @TableField("symbol")
    String symbol;

}