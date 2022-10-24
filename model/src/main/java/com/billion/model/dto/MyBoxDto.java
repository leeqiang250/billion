package com.billion.model.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author jason
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyBoxDto implements Serializable {
    /**
     * id
     */
    Long id;

    /**
     * boxGroupId
     */
    Long boxGroupId;

    /**
     * 链类型
     */
    String chain;

    /**
     * coinId
     */
    String coinId;

    /**
     * name_
     */
    String name;

    /**
     * symbol
     */
    String symbol;

    /**
     * decimals
     */
    Integer decimals;

    /**
     * 显示精度
     */
    Integer displayDecimals;

    /**
     * uri
     */
    String uri;

    String orderId;

    String saleType;

    String price;

    String bidder;

    String bidPrice;

    String ts;

}
