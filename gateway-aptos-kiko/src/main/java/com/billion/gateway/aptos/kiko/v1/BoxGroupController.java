package com.billion.gateway.aptos.kiko.v1;

import com.billion.model.controller.IController;
import com.billion.model.dto.Context;
import com.billion.model.entity.BoxGroup;
import com.billion.model.entity.NftGroup;
import com.billion.model.response.Response;
import com.billion.model.service.ICacheService;
import com.billion.service.aptos.kiko.BoxGroupService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.io.Serializable;
import java.util.Objects;

import static com.billion.model.constant.v1.RequestPathNftV1.BOX_GROUP;

/**
 * @author jason
 */
@RestController
@RequestMapping(BOX_GROUP)
public class BoxGroupController implements IController<BoxGroup> {
    @Resource
    BoxGroupService boxGroupService;

    @Override
    public ICacheService<BoxGroup> service() {
        return this.boxGroupService;
    }


//
//    @GetMapping("/getList")
//    public Response getList(@RequestHeader Context context, @PathVariable String type) {
//
//    }

}
