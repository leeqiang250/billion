package com.billion.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.billion.model.model.IModel;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author liqiang
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("nft_group")
public class NftGroup extends Mint implements IModel {

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
    String meta;

    /**
     * body
     */
    @TableField("body")
    String body;

    /**
     * 系列名称
     */
    @TableField("display_name")
    String displayName;

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
     * 系列描述
     */
    @TableField("description")
    String description;

    /**
     * 创作者ID
     */
    @TableField("creator_id")
    Long creatorId;

    /**
     * 排序
     */
    @TableField("sort")
    Integer sort;

    /**
     * 铸造哈希
     */
    @TableField("initialize_hash")
    String initializeHash;

    /**
     * 是否激活(0:不可用,1:可用)
     */
    @TableField("enabled")
    Boolean enabled;

}