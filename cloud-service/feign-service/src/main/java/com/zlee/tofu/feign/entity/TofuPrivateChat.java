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
 * tofu_private_chat
 *
 * @author
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tofu_private_chat")
public class TofuPrivateChat implements Serializable {
    /**
     * 聊天室 ID
     */
    @TableId(value = "PRIVATE_CHAT_ID", type = IdType.AUTO)
    private Integer privateChatId;

    /**
     * 用户 1 的 ID
     */
    private Integer user1Id;

    /**
     * 用户 2 的 ID
     */
    private Integer user2Id;

    /**
     * 最后一条消息的内容
     */
    private String lastMessage;

    /**
     * 最后一条消息的时间
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm",timezone="GMT+8")
    private Date lastMessageTime;

    @Serial
    private static final long serialVersionUID = 1L;
}