package com.billion.model.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class BoxGroupDto implements Serializable {

    Long pages;
    Long total;
    Long currentPage;
    List<BoxGroupDto.BoxGroupInfo> boxGroupList;


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoxGroupInfo {
        /**
         * 主键Id
         */
        Long id;

        /**
         * 链类型
         */
        String chain;

        /**
         * 显示名称
         */
        String displayName;

        /**
         * nft_group
         */
        Long nftGroup;

        /**
         * 询价币种
         */
        Object askToken;

        /**
         * 计价币种
         */
        Object bidToken;

        /**
         * 计价价格
         */
        String price;

        /**
         * 描述
         */
        String description;

        /**
         * 规则
         */
        String rule;

        /**
         * 起售时间
         */
        String ts;

        /**
         * 排序
         */
        Integer sort;

        String url;
    }

}