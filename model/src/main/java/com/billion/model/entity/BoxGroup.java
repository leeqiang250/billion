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
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * @author jason
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("box_group")
public class BoxGroup implements IModel {

    /**
     * 主键Id
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
     * token_id
     */
    @TableField("token_id")
    Long tokenId;

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
     * 支付币种，格式:
     */
    @TableField("support_token")
    String supportToken;

    /**
     * 图片
     */
    @TableField("uri")
    String uri;

    /**
     * 描述
     */
    @TableField("description")
    String description;

    /**
     * 规则
     */
    @TableField("rule")
    String rule;

    /**
     * 单账号购买限制
     */
    @TableField("max_amount")
    Long maxAmount;

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

    /**
     * 排序
     */
    @TableField("sort")
    Integer sort;

    /**
     * 起售时间
     */
    @TableField("sale_time")
    Timestamp saleTime;

    /**
     * 截止时间
     */
    @TableField("end_time")
    Timestamp endTime;

    /**
     * 修改时间
     */
    @TableField("mtime")
    Timestamp mtime;

    /**
     * 创建时间
     */
    @TableField("ctime")
    Timestamp ctime;

}
