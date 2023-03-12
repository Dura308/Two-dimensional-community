package com.zlee.tofu.feign.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * tofu_private_message
 * @author 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tofu_private_message")
public class TofuPrivateMessage implements Serializable {
    /**
     * 私信 ID
     */
    @TableId(value = "PRIVATE_MESSAGE_ID", type = IdType.AUTO)
    private Integer privateMessageId;

    /**
     * 	
发送者 ID
     */
    private Integer senderId;

    /**
     * 接受者 ID
     */
    private Integer recipientId;

    /**
     * 	
私信内容
     */
    private String content;

    /**
     * 是否已读
     */
    private Boolean state;

    /**
     * 发送消息的人是否把这条消息从聊天记录中删除了
     */
    private Boolean senderRemove;

    /**
     * 接受人是否把这条消息从聊天记录删除了
     */
    private Boolean recipientRemove;

    /**
     * 发送时间
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm",timezone="GMT+8")
    private Date sendTime;

    @Serial
    private static final long serialVersionUID = 1L;
}