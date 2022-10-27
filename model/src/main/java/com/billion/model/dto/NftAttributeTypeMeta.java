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
    Long nftGroupId;

    @JSONField(name = "class_name")
    String className;

    @JSONField(name = "sort")
    Integer sort;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @JSONField(name = "id")
    Long id;

    @JSONField(name = "nft_attribute_type_id")
    Long nftAttributeTypeId;

    @JSONField(name = "attribute")
    String attribute;

    @JSONField(name = "value")
    String value;

    @JSONField(name = "uri")
    String uri;

}