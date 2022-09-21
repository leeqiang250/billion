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

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author jason
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("nft_info")
public class NftInfo implements IModel {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    Long id;

    /**
     * box_group_id
     */
    @TableField("box_group_id")
    Long boxGroupId;

    /**
     * nft_group_id
     */
    @TableField("nft_group_id")
    Long nftGroupId;

    /**
     * nft_id
     */
    @TableField("nft_id")
    String nftId;

    /**
     * 名称
     */
    @TableField("name")
    String name;

    /**
     * 所有者
     */
    @TableField("owner")
    String owner;

    /**
     * 图片
     */
    @TableField("uri")
    String uri;

    /**
     * 分数
     */
    @TableField("score")
    String score;

    /**
     * 排名
     */
    @TableField("rank")
    Integer rank;

    /**
     * 状态(0:未铸造,1,铸造中,2:已铸造,3:铸造失败)
     */
    @TableField("state")
    Boolean state;

    /**
     * 修改时间
     */
    @TableField("mtime")
    LocalDateTime mtime;

    /**
     * 创建时间
     */
    @TableField("ctime")
    LocalDateTime ctime;

}
