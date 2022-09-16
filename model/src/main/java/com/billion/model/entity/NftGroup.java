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
 * @author liqiang
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("nft_group")
public class NftGroup implements Serializable {

    /**
     * 主键id
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
    @TableField("type")
    Boolean type;

    /**
     * Meta
     */
    @TableField("meta")
    String meta;

    /**
     * Body
     */
    @TableField("body")
    String body;

    /**
     * 系列名称
     */
    @TableField("display_name")
    String displayName;

    /**
     * 总供应量
     */
    @TableField("total_supply")
    String totalSupply;

    /**
     * Logo
     */
    @TableField("logo")
    String logo;

    /**
     * 系列描述
     */
    @TableField("desc")
    String desc;

    /**
     * 创作者ID
     */
    @TableField("creator_id")
    Long creatorId;

    /**
     * 创作者地址
     */
    @TableField("creator_address")
    String creatorAddress;

    /**
     * 是否激活(0:不可用,1:可用)
     */
    @TableField("enabled")
    Boolean enabled;

}
