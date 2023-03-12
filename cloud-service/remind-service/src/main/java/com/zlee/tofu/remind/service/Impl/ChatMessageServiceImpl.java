package com.zlee.tofu.remind.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlee.tofu.feign.Result.ResponseData;
import com.zlee.tofu.feign.Result.Result;
import com.zlee.tofu.feign.entity.TofuPrivateChat;
import com.zlee.tofu.feign.entity.TofuPrivateMessage;
import com.zlee.tofu.feign.websocketResult.WsMsg;
import com.zlee.tofu.remind.controller.WebSocketServer;
import com.zlee.tofu.remind.mapper.ChatMessageMapper;
import com.zlee.tofu.remind.service.ChatMessageService;
import com.zlee.tofu.remind.service.ChatService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author z-Lee
 * @date 2023-03-10-16:35
 */
@Service
public class ChatMessageServiceImpl extends ServiceImpl<ChatMessageMapper, TofuPrivateMessage> implements ChatMessageService {

    private final ChatMessageMapper chatMessageMapper;
    private final ChatService chatService;

    public ChatMessageServiceImpl(ChatMessageMapper chatMessageMapper, ChatService chatService) {
        this.chatMessageMapper = chatMessageMapper;
        this.chatService = chatService;
    }

    public Result<Object> getChatMessage(Integer senderId, Integer recipientId) {

        List<TofuPrivateMessage> list = new LambdaQueryChainWrapper<>(chatMessageMapper)
                .and(wrapper -> wrapper.eq(TofuPrivateMessage::getRecipientId, recipientId).eq(TofuPrivateMessage::getSenderId, senderId))
                .or(wrapper -> wrapper.eq(TofuPrivateMessage::getRecipientId, senderId).eq(TofuPrivateMessage::getSenderId, recipientId))
                .orderByAsc(TofuPrivateMessage::getSendTime)
                .list();

        readMessage(senderId, recipientId);

        return ResponseData.success(list);
    }

    public Result<Object> readMessage(Integer senderId, Integer recipientId) {
        //更改消息已读
        update(new LambdaUpdateWrapper<TofuPrivateMessage>()
                .eq(TofuPrivateMessage::getRecipientId, recipientId).eq(TofuPrivateMessage::getSenderId, senderId)
                .setSql("STATE = 1"));
        return ResponseData.success();
    }

    public Result<Object> addChatMessage(Integer senderId, Integer recipientId, String content) {

        TofuPrivateMessage message = new TofuPrivateMessage();
        message.setSenderId(senderId);
        message.setRecipientId(recipientId);
        message.setContent(content);
        message.setSendTime(new Date());
        chatMessageMapper.insert(message);

        //设置当前聊天最后的message
        chatService.update(new LambdaUpdateWrapper<TofuPrivateChat>()
                        .and(wrapper -> wrapper.eq(TofuPrivateChat::getUser1Id, senderId).eq(TofuPrivateChat::getUser2Id, recipientId))
                        .or(wrapper -> wrapper.eq(TofuPrivateChat::getUser1Id, recipientId).eq(TofuPrivateChat::getUser2Id, senderId))
                .setSql("LAST_MESSAGE = '" + content + "'"));


        WebSocketServer.sendMessage(recipientId, new WsMsg<>("private", message));
        return ResponseData.success(message);
    }

    public Result<Object> getUnreadMessageNum(Integer senderId, Integer recipientId) {
        //获取和该联系人的未读消息数量
        Long unreadMessageNum = chatMessageMapper.selectCount(new LambdaQueryWrapper<TofuPrivateMessage>()
                .eq(TofuPrivateMessage::getSenderId, senderId)
                .eq(TofuPrivateMessage::getRecipientId, recipientId)
                .eq(TofuPrivateMessage::getState, false));
        return ResponseData.success(unreadMessageNum);
    }
}
