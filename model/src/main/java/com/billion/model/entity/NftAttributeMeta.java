package com.billion.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author jason
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("nft_attribute_meta")
public class NftAttributeMeta implements Serializable {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    Long id;

    /**
     * nft_class_id
     */
    @TableField("nft_attribute_type")
    Long nftAttributeType;

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

    @TableField("uri")
    String uri;

    @TableField("sort")
    String sort;

}
