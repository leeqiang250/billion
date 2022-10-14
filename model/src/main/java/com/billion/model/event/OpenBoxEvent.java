package com.billion.model.event;

import com.alibaba.fastjson2.annotation.JSONField;
import com.aptos.request.v1.model.TokenId;
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
public class OpenBoxEvent implements Serializable {

    public static String EVENT_NAME = "::open_box::OpenBoxEvent";

    @JSONField(name = "token_id")
    TokenId tokenId;

    String owner;

    String ts;

}