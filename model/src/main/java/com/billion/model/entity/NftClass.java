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

import java.io.Serializable;

/**
 * @author jason
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("nft_class")
public class NftClass implements IModel {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    Long id;

    /**
     * nft_group_id
     */
    @TableField("nft_group_id")
    Long nftGroupId;

    /**
     * nft_meta_id
     */
    @TableField("nft_meta_id")
    Long nftMetaId;

    /**
     * 属性名
     */
    @TableField("class_name")
    String className;

    /**
     * 类型(0:无属性;1:有属性)
     */
    @TableField("is_attribute")
    Boolean isAttribute;

    /**
     * 分数
     */
    @TableField("score")
    String score;

}
