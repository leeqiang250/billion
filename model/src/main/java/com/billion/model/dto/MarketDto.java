package com.billion.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author jason
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketDto implements Serializable {
    Long pages;
    Long total;
    Long currentPage;
    List<MarketInfo> marketList;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MarketInfo<T> {
        /**
         * id
         */
        Long id;

        /**
         * 链类型
         */
        String chain;

        /**
         * 订单ID
         */
        String orderId;

        /**
         * 订单类型
         */
        String type;

        /**
         * price
         */
        String price;

        /**
         * maker
         */
        String maker;

        /**
         * 询价币种
         */
        T askToken;

        /**
         * 询价币种数量
         */
        String askAmount;
        /**
         * bidder
         */
        String bidder;

        /**
         * 报价币种
         */
        String bidToken;

        /**
         * 报价币种数量
         */
        String bidAmount;


        /**
         * 生成订单时间
         */
        String ts;

        /**
         * 订单截止时间
         */
        String deadTs;

        /**
         * 订单类型:0:box;1:nft
         */
        Integer orderType;
    }


}
