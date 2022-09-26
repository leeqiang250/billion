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
 * @author liqiang
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("log_chain")
public class LogChain implements IModel {

    @TableId(value = "id", type = IdType.AUTO)
    Long id;

    @TableField("is_result")
    Boolean isResult;

    @TableField("path")
    String path;

    @TableField("query")
    String query;

    @TableField("body")
    String body;

    @TableField("message")
    String message;

    @TableField("error_code")
    String errorCode;

    @TableField("vm_error_code")
    String vmErrorCode;

}
