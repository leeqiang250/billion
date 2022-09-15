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
@TableName("language")
public class Language implements Serializable {

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    Long id;

    /**
     * 语言类型
     */
    @TableField("language_")
    String language;

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

}
