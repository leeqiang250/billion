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

/**
 * @author jason
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("token_scene")
public class TokenScene implements IModel {

    @TableId(value = "id", type = IdType.AUTO)
    Long id;

    /**
     * token_id
     */
    @TableField("token_id")
    Long tokenId;

    /**
     * 用途
     */
    @TableField("scene")
    String scene;

}
