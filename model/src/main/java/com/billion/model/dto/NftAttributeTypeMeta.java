package com.billion.model.dto;

import com.alibaba.fastjson2.annotation.JSONField;
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
public class NftAttributeTypeMeta implements Serializable {

    @JSONField(name = "nft_group_id")
    String nftGroupId;

    @JSONField(name = "class_name")
    String className;

    @JSONField(name = "class_name_zh-TC")
    String classNameZhTC;

    @JSONField(name = "class_name_en")
    String classNameEn;

    @JSONField(name = "sort")
    String sort;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @JSONField(name = "id")
    String id;

    @JSONField(name = "nft_attribute_type_id")
    String nftAttributeTypeId;

    @JSONField(name = "attribute")
    String attribute;

    @JSONField(name = "attribute_zh-TC")
    String attributeZhTC;

    @JSONField(name = "attribute_en")
    String attributeEn;

    @JSONField(name = "value")
    String value;

    @JSONField(name = "uri")
    String uri;

}