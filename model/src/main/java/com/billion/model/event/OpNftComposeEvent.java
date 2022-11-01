package com.billion.model.event;

import com.alibaba.fastjson2.annotation.JSONField;
import com.aptos.request.v1.model.Event;
import com.billion.model.dto.NftCollection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author liqiang
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpNftComposeEvent implements Serializable {

    public static String EVENT_NAME = "::op_nft::NftComposeEvent";

    @JSONField(name = "order_id")
    String orderId;

    boolean execute;

    String owner;

    String name;

    String description;

    @JSONField(name = "property_keys")
    List<String> propertyKeys;

    @JSONField(name = "property_values")
    List<String> propertyValues;

    @JSONField(name = "property_types")
    List<String> propertyTypes;

    NftCollection collection;

    /**
     * isOpNftComposeEvent
     *
     * @param event event
     * @return boolean
     */
    public static boolean isOpNftComposeEvent(Event event) {
        return OpNftComposeEvent.EVENT_NAME.equals(event.getType());
    }

}