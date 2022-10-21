package com.billion.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.billion.model.model.IModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.billion.model.constant.RequestPath.EMPTY;

/**
 * @author liqiang
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("config")
public class Config implements IModel {

    @TableId(value = "id", type = IdType.AUTO)
    Long id;

    /**
     * key
     */
    @TableField("key_")
    String key;

    /**
     * value
     */
    @TableField("value_")
    String value;

    public static Config SCAN_CHAIN_CURSOR = Config.builder()
            .id(1L)
            .key("scan_chain_cursor")
            .value(EMPTY)
            .build();

}