package com.billion.model.event;

import com.alibaba.fastjson2.annotation.JSONField;
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
public class BoxMakerEvent implements Serializable {

    String id;

    String type;

    String maker;

    String price;

    String amount;

    String ts;

    @JSONField(name = "dead_ts")
    String deadTs;

}