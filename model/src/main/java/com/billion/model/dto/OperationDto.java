package com.billion.model.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.billion.model.entity.Operation;
import com.billion.model.entity.Token;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OperationDto {
    /**
     * id
     */
    Long id;

    /**
     * 链类型
     */
    String chain;

    /**
     * 操作用户地址
     */
    String maker;

    /**
     * 出价用户地址
     */
    String bidder;

    /**
     * 订单id
     */
    String orderId;


    /**
     * 交易类型 一口价or拍卖
     */
    String traType;

    /**
     * 操作类型
     */
    String type;

    /**
     * 操作对象id
     */
    String tokenId;

    /**
     * 操作对象数量
     */
    String tokenAmount;

    /**
     * 支付代币
     */
    Token bidToken;

    /**
     * 支付代币数量
     */
    String bidTokenAmount;

    /**
     * 交易价格
     */
    String price;

    /**
     *
     */
    String state;

    /**
     * 发生时间
     */
    String ts;

    String uri;

    public static OperationDto of(Operation operation){
        OperationDto operationDto = OperationDto.builder()
                .id(operation.getId())
                .chain(operation.getChain())
                .maker(operation.getMaker())
                .bidder(operation.getBidder())
                .orderId(operation.getOrderId())
                .traType(operation.getTraType())
                .type(operation.getType())
                .tokenId(operation.getTokenId())
                .tokenAmount(operation.getTokenAmount())
                .bidTokenAmount(operation.getBidTokenAmount())
                .price(operation.getPrice())
                .state(operation.getState())
                .ts(operation.getTs())
                .build();
        return operationDto;
    }
}
