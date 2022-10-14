package com.billion.model.event;

import com.aptos.request.v1.model.TokenDataId;
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
public class NftCreateTokenDataEvent implements Serializable {

    public static final String EVENT_NAME = "0x3::token::CreateTokenDataEvent";

    TokenDataId id;

    public TokenId getTokenId() {
        return TokenId.builder()
                .tokenDataId(id)
                .propertyVersion("0")
                .build();
    }

}