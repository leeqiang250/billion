package com.billion.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author jason
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NftClassDto implements Serializable {
    /**
     * id
     */
    Long id;

    /**
     * nft_group_id
     */
    Long nftGroupId;

    /**
     * nft_meta_id
     */
    Long nftMetaId;

    /**
     * 属性名
     */
    String className;

    /**
     * 类型(0:无属性;1:有属性)
     */
    Boolean isAttribute;

    /**
     * 分数
     */
    String score;

    /**
     * 属性列表
     */
    List<NftAttribute> attributes;

}
