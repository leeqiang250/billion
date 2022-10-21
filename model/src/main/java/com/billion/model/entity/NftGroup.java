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
@TableName("nft_group")
public class NftGroup extends TransactionStatus implements IModel {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    Long id;

    /**
     * 链类型
     */
    @TableField("chain")
    String chain;

    /**
     * 可拆性(0:不可拆卸,1:可拆卸)
     */
    @TableField("split")
    Boolean split;

    /**
     * 所有者
     */
    @TableField("owner")
    String owner;

    /**
     * meta
     */
    @TableField("meta")
    String meta2;

    /**
     * body
     */
    @TableField("body")
    String body2;

    /**
     * 系列名称
     */
    @TableField("display_name")
    String displayName;

    /**
     * 系列描述
     */
    @TableField("description")
    String description;

    /**
     * 当前供应量
     */
    @TableField("current_supply")
    String currentSupply;

    /**
     * 总计供应量
     */
    @TableField("total_supply")
    String totalSupply;

    /**
     * 图片
     */
    @TableField("uri")
    String uri;

    /**
     * 是否激活(0:不可用,1:可用)
     */
    @TableField("is_enabled")
    Boolean isEnabled;

    /**
     * 排序
     */
    @TableField("sort")
    Long sort;

    //TODO renjain 需要国际化
    public String getNftContract() {
        return this.owner + "::" + this.displayName;
    }

}