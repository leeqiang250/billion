package com.billion.service.aptos.kiko;

import com.baomidou.mybatisplus.extension.service.IService;
import com.billion.model.dto.Context;
import com.billion.model.entity.NftGroup;
import lombok.NonNull;

import java.util.Map;

/**
 * @author liqiang
 */
public interface NftGroupService extends IService<NftGroup>, RedisService<NftGroup> {

    Map getAllById(@NonNull Context context);

    Map getAllByMetaBody(@NonNull Context context);

    Object getById(@NonNull String id, @NonNull Context context);

    Object getById(@NonNull String meta, @NonNull String body, @NonNull Context context);

}
