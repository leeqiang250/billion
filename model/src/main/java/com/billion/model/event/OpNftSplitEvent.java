package com.billion.model.event;

import com.alibaba.fastjson2.annotation.JSONField;
import com.aptos.request.v1.model.Event;
import com.aptos.request.v1.model.TableTokenData;
import com.aptos.request.v1.model.TokenId;
import com.billion.model.dto.NftCollection;
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
public class OpNftSplitEvent implements Serializable {

    public static String EVENT_NAME = "::op_nft::NftSplitEvent";

    @JSONField(name = "order_id")
    String orderId;

    boolean execute;

    String owner;

    @JSONField(name = "token_id")
    TokenId tokenId;

    TableTokenData.DefaultProperties property;

    NftCollection collection;

    /**
     * isOpNftSplitEvent
     *
     * @param event event
     * @return boolean
     */
    public static boolean isOpNftSplitEvent(Event event) {
        return OpNftSplitEvent.EVENT_NAME.equals(event.getType());
    }

}