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
@TableName("nft_attribute_type")
public class NftAttributeType implements IModel {

    /**
     * id
     */
    @JSONField(name = "id")
    @TableId(value = "id", type = IdType.AUTO)
    Long id;

    /**
     * nft_group_id
     */
    @JSONField(name = "nft_group_id")
    @TableField("nft_group_id")
    Long nftGroupId;

    /**
     * 属性名
     */
    @JSONField(name = "class_name")
    @TableField("class_name")
    String className;

    /**
     * 排序
     */
    @JSONField(name = "sort")
    @TableField("sort")
    Integer sort;

}
