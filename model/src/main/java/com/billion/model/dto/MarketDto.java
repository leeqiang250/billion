package com.billion.model.dto;

import com.billion.model.entity.Token;
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

        String name;

        String score;

        String contract;

        String url;

        /**
         * 订单类型
         */
        String saleType;

        /**
         * price
         */
        String price;

        /**
         * owner
         */
        String owner;

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
        Token bidToken;

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
         * 订单类型:box或nft
         */
        String orderType;
    }


}
