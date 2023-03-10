package com.zlee.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * tofu_content
 * @author 
 */
@Data
@TableName("TOFU_CONTENT")
public class TofuContent implements Serializable {
    /**
     * 内容ID
     */
    @TableId(value = "CONTENT_ID", type = IdType.AUTO)
    private Integer contentId;

    /**
     * 发布用户ID
     */
    private String userId;

    /**
     * 发布用户昵称
     */
    private String nickName;

    /**
     * 发布内容文本
     */
    private String text;

    /**
     * 发布内容类型
     */
    private String contentType;

    /**
     * 收藏数
     */
    private Integer collectionNum;

    /**
     * 点赞数
     */
    private Integer likeNum;

    /**
     * 评论数
     */
    private Integer commentNum;

    /**
     * 分享数
     */
    private Integer shareNum;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 更新时间
     */
    private Date updatedTime;

    @Serial
    private static final long serialVersionUID = 1L;
}