package com.billion.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 铸造时：通过NFT ID查询nft_attribute_value的全部数据，返回List<NftAttribute>
 *
 * @author liqiang
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NftAttribute {

    /**
     * nft_attribute_type::class_name
     */
    String type;

    /**
     * nft_attribute_meta::attribute
     */
    String key;

    /**
     * nft_attribute_meta::value
     */
    String value;

}