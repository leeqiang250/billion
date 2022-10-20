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
     * 衣服：nft_attribute_clothes_{nft_attribute_type_id}
     * 性别：nft_attribute_sex_{nft_attribute_type_id}
     */
    String type;

    /**
     * nft_attribute_meta::attribute
     * 衣服/蓝色：nft_attribute_clothes_blue_{nft_attribute_meta_id}
     * 衣服/红色：nft_attribute_clothes_red_{nft_attribute_meta_id}
     * 性别/雄性：nft_attribute_sex_male_{nft_attribute_meta_id}
     * 性别/雌性：nft_attribute_sex_female_{nft_attribute_meta_id}
     */
    String key;

    /**
     * nft_attribute_meta::value
     * 不需要国际化
     */
    String value;

}