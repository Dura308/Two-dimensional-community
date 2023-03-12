package com.zlee.tofu.feign.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * tofu_event_remind
 * @author 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tofu_event_remind")
public class TofuEventRemind implements Serializable {
    /**
     * 消息 ID
     */
    @TableId(value = "EVENT_REMIND_ID", type = IdType.AUTO)
    private Integer eventRemindId;

    /**
     * 动作类型，如点赞、at(@)、回复等
     */
    private String action;

    /**
     *事件源 ID，如评论 ID、文章 ID 等
     */
    private Integer sourceId;

    /**
     * 事件源类型："Comment"、"Post"等
     */
    private String sourceType;

    /**
     * 事件源的内容，比如回复的内容，回复的评论等等
     */
    private String sourceContent;

    /**
     * 事件所发生的地点链接 url
     */
    private String url;

    /**
     * 是否已读
     * 0 FALSE
     * 1 TRUE
     */
    private Boolean state;

    /**
     * 操作者的 ID，即谁关注了你，at 了你
     */
    private Integer senderId;

    /**
     * 接受通知的用户的 ID
     */
    private Integer recipientId;

    /**
     * 提醒的时间
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm",timezone="GMT+8")
    private Date remindTime;

    @Serial
    private static final long serialVersionUID = 1L;
}