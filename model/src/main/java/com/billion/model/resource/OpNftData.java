package com.billion.model.resource;

import com.alibaba.fastjson2.annotation.JSONField;
import com.aptos.request.v1.model.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author liqiang
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpNftData implements Serializable {

    String id;

    Table mapping;

    @JSONField(name = "order_compose")
    Table orderCompose;

    @JSONField(name = "order_spilt")
    Table orderSpilt;

}