package com.billion.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("image")
public class Image implements Serializable {

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    Long id;

    /**
     * 实际
     */
    @TableField("uri")
    String uri;

    /**
     * 代理
     */
    @TableField("proxy")
    String proxy;

}
