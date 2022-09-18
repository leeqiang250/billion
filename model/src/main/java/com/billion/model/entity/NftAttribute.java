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
@TableName("nft_attribute")
public class NftAttribute implements IModel {

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    Long id;

    /**
     * 系列ID
     */
    @TableField("group_id")
    Long groupId;

    /**
     * 属性名
     */
    @TableField("attribute")
    String attribute;

    /**
     * 属性值
     */
    @TableField("value")
    String value;

}
