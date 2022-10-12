package com.billion.gateway.aptos.kiko.v1;

import com.billion.model.controller.IController;
import com.billion.model.dto.Context;
import com.billion.model.entity.Token;
import com.billion.model.response.Response;
import com.billion.service.aptos.kiko.TokenService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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
        {
//            var list = tokenService.list();
//            list.forEach(token -> tokenService.initialize(token.getId()));
        }
        {
//        TransactionPayload transactionPayload = TransactionPayload.builder()
//                .type(TransactionPayload.ENTRY_FUNCTION_PAYLOAD)
//                .function("0x3::token::create_collection_script")
//                .arguments(List.of(
//                        Hex.encode("我的名称"),
//                        Hex.encode("我是描述"),
//                        Hex.encode("https://imagedelivery.net/3mRLd_IbBrrQFSP57PNsVw/76360568-5c54-4342-427d-68992ded7b00/public"),
//                        "9999999999999999999",
//                        List.of(true, true, true)
//                ))
//                .typeArguments(List.of())
//                .build();
//
//        var transaction = AptosService.getAptosClient().requestSubmitTransaction(
//                ContextService.getNftOwnerAddress(),
//                transactionPayload);
//        System.out.println(transaction.toString());
//        AptosService.checkTransaction(transaction.getHash());
        }
        {
//            var collectionData = AptosService.getAptosClient().requestTableCollectionData("0x1081a174e44878c38fc8d278e9c329039ab27594b64c6ef77d143e0b8316d5b3", Hex.encode(Hex.encode("我的名称")));
//            System.out.println(collectionData);
        }

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

    @GetMapping("/listByScene/{scene}")
    public Response getListByScene(@RequestHeader Context context, @PathVariable String scene) {
        return Response.success(tokenService.getListByScene(context, scene));
    }

}