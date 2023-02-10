package com.zlee.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * content_comment
 * @author 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("content_comment")
public class ContentComment implements Serializable {
    /**
     * 评论ID
     */
    @TableId(value = "COMMENT_ID", type = IdType.AUTO)
    private Integer commentId;

    /**
     * 评论人ID
     */
    private Integer userId;

    /**
     * 评论人ID
     */
    private String nickName;

    /**
     * 评论人ID
     */
    private String parentNickName;

    /**
     * 评论人头像
     */
    private String avatar;

    /**
     * 内容ID
     */
    private Integer contentId;

    /**
     * 评论内容
     */
    private String comment;

    /**
     * 父评论ID
     */
    private Integer parentId;

    /**
     * 根评论ID
     */
    private Integer rootParentId;

    /**
     * 创建时间
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm",timezone="GMT+8")
    private Date createdTime;

    /**
     * 子评论
     */
    @TableField(exist = false)
    private List<ContentComment> child;

    @Serial
    private static final long serialVersionUID = 1L;
}