package com.billion.model.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.billion.model.entity.NftAttribute;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author jason
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NFTClassDto {
    /**
     * id
     */
    Long id;

    /**
     * nft_group_id
     */
    Long nftGroupId;

    /**
     * nft_info_id
     */
    Long nftInfoId;

    /**
     * 属性名
     */
    String className;

    /**
     * 类型(0:无属性;1:有属性)
     */
    Long type;

    /**
     * 分数
     */
    String score;

    /**
     * 属性列表
     */
    List<NftAttribute> attributes;

}
