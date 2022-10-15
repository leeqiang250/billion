package com.billion.service.aptos.kiko;

import com.billion.model.dto.BoxGroupDto;
import com.billion.model.dto.Context;
import com.billion.model.entity.BoxGroup;
import com.billion.model.entity.Token;
import com.billion.model.service.ICacheService;

import java.io.Serializable;
import java.util.List;


/**
 * @author liqiang
 */
public interface BoxGroupService extends ICacheService<BoxGroup> {

    /**
     * initialize
     *
     * @param id id
     * @return boolean
     */
    boolean initialize(Serializable id);

    /**
     * initializeMarket
     *
     * @return boolean
     */
    boolean initializeMarket();

    /**
     * getMyBox 我的盲盒
     * @param context
     * @param account
     * @return
     */
    List<Token> getMyBox(Context context, String account);

    /**
     * getListByTokenIds
     * @param context
     * @param tokenIdList
     * @return
     */
    List<Token> getListByTokenIds(Context context,  List<String> tokenIdList);

    /**
     * getSaleList 公开售卖列表
     * @param context
     * @return
     */
    List<BoxGroupDto> getSaleList(Context context);
}