package com.billion.service.aptos.kiko;

import com.billion.model.dto.Context;
import com.billion.model.entity.NftInfo;
import com.billion.model.service.ICacheService;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * @author liqiang
 */
public interface NftInfoService extends ICacheService<NftInfo> {

    NftInfo updateState(String id, Integer state);

}