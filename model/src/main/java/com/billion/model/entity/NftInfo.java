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
@TableName("nft_info")
public class NftInfo implements IModel {

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

}
