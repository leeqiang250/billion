package com.billion.model.event;

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
public class NftWithdrawEvent implements Serializable {

    public static String SIMPLE_NAME = NftWithdrawEvent.class.getSimpleName();

    String amount;

    TokenId id;

}