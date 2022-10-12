package com.billion.service.aptos.kiko;

import com.billion.model.dto.Context;
import com.billion.model.entity.BoxGroup;
import com.billion.model.response.Response;
import com.billion.model.service.ICacheService;

import java.util.Collection;
import java.util.List;

/**
 * @author jason
 */
public interface BoxGroupService  extends ICacheService<BoxGroup> {

    /**
     * getList
     * @param context
     * @param type
     * @return
     */
//    Response getList(Context context, String type);

//    BoxGroup getById();

    void initBox(Context context, String groupId);

    void addPay(String groupId);
}
