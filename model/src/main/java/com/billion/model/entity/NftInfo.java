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
import java.math.BigDecimal;

/**
 * @author liqiang
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("nft_info")
public class NftInfo implements Serializable {

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    Long id;

    /**
     * 合约上nft的tokenId
     */
    @TableField("nft_id")
    Long nftId;

    /**
     * nft_group表主键id
     */
    @TableField("group_id")
    Long groupId;

    /**
     * 类型: nft:不可拆解的nft,composit_element:组成卡牌的元素,composite_card:卡牌
     */
    @TableField("type")
    String type;

    /**
     * 名称
     */
    @TableField("name")
    String name;

    /**
     * 所有者地址
     */
    @TableField("owner")
    String owner;

    /**
     * 图片链接
     */
    @TableField("image_link")
    String imageLink;

    /**
     * 图片内容
     */
    @TableField("image_data")
    String imageData;

    /**
     * 分数
     */
    @TableField("score")
    BigDecimal score;

    /**
     * 排名
     */
    @TableField("rank")
    Integer rank;

    /**
     * 已创建
     */
    @TableField("created")
    Boolean created;

    /**
     * 状态: init,success,invalid
     */
    @TableField("state")
    String state;

    /**
     * 开盲盒时间(单位:毫秒)
     */
    @TableField("open_box_time")
    Long openBoxTime;

    /**
     * 创建时间
     */
    @TableField("create_time")
    Long createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    Long updateTime;

}
