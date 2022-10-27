package com.billion.model.entity;

import com.alibaba.fastjson2.annotation.JSONField;
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
@TableName("nft_attribute_meta")
public class NftAttributeMeta implements IModel {

    /**
     * id
     */
    @JSONField(name = "id")
    @TableId(value = "id", type = IdType.AUTO)
    Long id;

    /**
     * nft_attribute_type_id
     */
    @JSONField(name = "nft_attribute_type_id")
    @TableField("nft_attribute_type_id")
    Long nftAttributeTypeId;

    /**
     * 属性名
     */
    @JSONField(name = "attribute")
    @TableField("attribute")
    String attribute;

    /**
     * 属性值
     */
    @JSONField(name = "value")
    @TableField("value")
    String value;

    /**
     * uri
     */
    @JSONField(name = "uri")
    @TableField("uri")
    String uri;

}
