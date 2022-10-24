package com.billion.service.aptos.kiko;

import com.aptos.request.v1.model.Response;
import com.aptos.request.v1.model.TableTokenData;
import com.billion.model.dto.Context;
import com.billion.model.dto.NftMetaDto;
import com.billion.model.entity.NftMeta;
import com.billion.model.service.ICacheService;

import java.io.Serializable;
import java.util.List;

/**
 * @author liqiang
 */
public interface NftMetaService extends ICacheService<NftMeta> {

    /**
     * initialize
     *
     * @param boxGroupId boxGroupId
     * @param nftGroupId nftGroupId
     * @return boolean
     */
    boolean initialize(Serializable boxGroupId, Serializable nftGroupId);

    /**
     * getTableTokenData
     *
     * @param id id
     * @return Response<TableTokenData>
     */
    Response<TableTokenData> getTableTokenData(Serializable id);


    NftMetaDto getNftMetaInfoById(Context context, String NftMetaId);

    /**
     * getListByGroup
     *
     * @param context
     * @param type
     * @param groupId
     * @return
     */
    List<NftMeta> getListByGroup(Context context, String type, String groupId);


    /**
     * getMyNfts 我的NFT
     * @param context
     * @param account
     * @return
     */
    List<NftMeta> getMyNfts(Context context, String account, String saleState);

    /**
     * getListByTokenIds
     * @param tokenIds
     * @return
     */
    List<NftMeta> getListByTokenIds(List<String> tokenIds);
}