package com.billion.service.aptos.kiko;

import com.baomidou.mybatisplus.extension.service.IService;
import com.billion.model.dto.Context;
import com.billion.model.dto.NftClassDto;
import com.billion.model.entity.NftClass;
import com.billion.model.service.ICacheService;

import java.util.Collection;
import java.util.List;

/**
 * @author jason
 */
public interface NftClassService extends ICacheService<NftClass> {

    List getClassByGroupId(Context context, String groupId);

    /**
     * 根据nftInfoId查询
     * @param context
     * @param infoId
     * @return
     */
    List<NftClassDto> getClassByInfoId(Context context, String infoId);


}
