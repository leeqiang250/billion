package com.billion.service.aptos.kiko;

import com.aptos.request.v1.model.Transaction;
import com.billion.model.entity.Token;
import com.billion.model.service.ICacheService;

/**
 * @author liqiang
 */
public interface TokenService extends ICacheService<Token> {


    Transaction initialize(Token token);


//    Transaction transferApt(String from, String to, String amount);


}