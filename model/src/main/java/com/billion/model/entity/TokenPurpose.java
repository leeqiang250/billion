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
import java.time.LocalDateTime;

/**
 * @author jason
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("token_purpose")
public class TokenPurpose implements Serializable {

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
    @TableField("purpose")
    String purpose;

}
