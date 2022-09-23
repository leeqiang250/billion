package com.billion.gateway.aptos.kiko.v1;

import com.billion.model.controller.IController;
import com.billion.model.dto.Context;
import com.billion.model.entity.Token;
import com.billion.model.response.Response;
import com.billion.service.aptos.kiko.TokenService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import java.util.List;
import java.util.function.Consumer;

import static com.billion.model.constant.v1.RequestPathTokenV1.TOKEN;

/**
 * @author liqiang
 */
@RestController
@SuppressWarnings({"rawtypes"})
@RequestMapping(TOKEN)
public class TokenController implements IController<Token> {

    @Resource
    TokenService tokenService;

    @RequestMapping({"/test"})
    public Response test(Context context) {
        var list = tokenService.list();
        list.forEach(new Consumer<Token>() {
            @Override
            public void accept(Token token) {
                tokenService.initialize();
            }
        });

        //        {
//            Transaction transaction = nftGroupService.transferApt(
//                    "0xf7e09293bfc8a0c70a4bf9b6fecc4527da518dc4d8a60a84c293de6854dae0d8",
//                    "0x4cd5040c25c069143f22995f0deaae6bfb674949302b008678455174b8ea8104",
//                    "000000000");
//
//            AptosService.checkTransaction(transaction.getHash());
//        }
//    {
//        Token token = Token.builder()
//                .moduleAddress("0x4cd5040c25c069143f22995f0deaae6bfb674949302b008678455174b8ea8104")
//                .moduleName("box_v1")
//                .structName("BoxV1")
//                .initializeFunction("0x4cd5040c25c069143f22995f0deaae6bfb674949302b008678455174b8ea8104::token_v1::initialize")
//                .name("name")
//                .symbol("symbol")
//                .build();
//
//        Transaction transaction = nftGroupService.ini(token);
//        AptosService.checkTransaction(transaction.getHash());
//    }
        return Response.SUCCESS;
    }

}