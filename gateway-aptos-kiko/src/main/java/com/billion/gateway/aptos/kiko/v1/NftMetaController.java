package com.billion.gateway.aptos.kiko.v1;

import com.billion.model.controller.IController;
import com.billion.model.dto.Context;
import com.billion.model.entity.NftMeta;
import com.billion.model.enums.Authenticate;
import com.billion.model.enums.AuthenticateType;
import com.billion.model.response.Response;
import com.billion.model.service.ICacheService;
import com.billion.service.aptos.kiko.NftAttributeValueService;
import com.billion.service.aptos.kiko.NftMetaService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.util.List;

import static com.billion.model.constant.v1.RequestPathNftV1.NFT_META;

/**
 * @author liqiang
 */
@RestController
@SuppressWarnings({"rawtypes"})
@RequestMapping(NFT_META)
public class NftMetaController implements IController<NftMeta> {

    @Resource
    NftMetaService nftMetaService;

    @Resource
    NftAttributeValueService nftAttributeValueService;

    @Override
    public ICacheService<NftMeta> service() {
        return this.nftMetaService;
    }

    @Override
    @Authenticate(AuthenticateType.FORBID)
    public Response cacheList(Context context) {
        return IController.super.cacheList(context);
    }

    @Override
    @Authenticate(AuthenticateType.FORBID)
    public Response cacheMap(Context context) {
        return IController.super.cacheMap(context);
    }

//    @GetMapping("getNftMetaInfoById/{nftMetaId}")
//    public Response getNftMetaInfoById(@RequestHeader Context context, @PathVariable String nftMetaId) {
//
//    }

    @RequestMapping("/group/{type}/{groupId}")
    public Response getByGroup(@RequestHeader Context context, @PathVariable String type, @PathVariable String groupId) {
        return Response.success(nftMetaService.getListByGroup(context, type, groupId));
    }

    @GetMapping("/myNfts/{account}/{saleState}")
    public Response getMyNfts(@RequestHeader Context context, @PathVariable String account, @PathVariable String saleState) {
        return Response.success(nftMetaService.getMyNfts(context, account, saleState));
    }

    @GetMapping("/getAttributeValue/{nftMetaId}")
    public Response getAttributeValue(@RequestHeader Context context, @PathVariable String nftMetaId) {
        return Response.success(nftAttributeValueService.getNftAttributeValueByMetaId(context, nftMetaId));
    }

    @GetMapping("/getNftMetaInfo/{nftMetaId}")
    public Response getNftMetaInfo(@RequestHeader Context context, @PathVariable String nftMetaId) {
        return Response.success(nftMetaService.getNftMetaInfoById(context, nftMetaId));
    }

    @GetMapping("/getNftMetaInfoByToken/{nftTokenId}")
    public Response getNftMetaInfoByToken(@RequestHeader Context context, @PathVariable String nftTokenId) {
        return Response.success(nftMetaService.getNftMetaInfoByToken(context, nftTokenId));
    }

    @GetMapping("/test")
    public void testMint() {
        nftMetaService.rank(String.valueOf(100111265));
    }

}