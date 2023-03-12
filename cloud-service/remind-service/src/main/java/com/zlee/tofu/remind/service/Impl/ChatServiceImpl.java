package com.zlee.tofu.remind.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlee.tofu.feign.Result.ResponseData;
import com.zlee.tofu.feign.Result.Result;
import com.zlee.tofu.feign.clients.UserClient;
import com.zlee.tofu.feign.entity.TofuPrivateChat;
import com.zlee.tofu.feign.entity.TofuUser;
import com.zlee.tofu.remind.mapper.ChatMapper;
import com.zlee.tofu.remind.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author z-Lee
 * @date 2023-03-10-13:22
 */
@Service
public class ChatServiceImpl extends ServiceImpl<ChatMapper, TofuPrivateChat> implements ChatService {

    private final ChatMapper chatMapper;
    private final UserClient userClient;

    public ChatServiceImpl(ChatMapper chatMapper, UserClient userClient) {
        this.chatMapper = chatMapper;
        this.userClient = userClient;
    }

    public Result<Object> getAllChat(Integer userId) {

        List<TofuPrivateChat> chatList = chatMapper.selectList(new LambdaQueryWrapper<TofuPrivateChat>()
                .eq(TofuPrivateChat::getUser1Id, userId)
                .or()
                .eq(TofuPrivateChat::getUser2Id, userId)
                .orderByDesc(TofuPrivateChat::getLastMessageTime));

        ArrayList<Map<String, Object>> resultList = new ArrayList<>();
        for (TofuPrivateChat chat : chatList) {
            Map<String, Object> map = new HashMap<>(16);
            Integer userId2;
            if(Objects.equals(userId, chat.getUser1Id())){
                userId2 = chat.getUser2Id();
            }else {
                userId2 = chat.getUser1Id();
            }
            TofuUser user2 = userClient.getUserBaseInfo(userId2);
            map.put("chat", chat);
            map.put("chatWith", user2);
            resultList.add(map);
        }
        return ResponseData.success(resultList);
    }

    public Result<Object> addChat(Integer userId1, Integer userId2) {
        TofuPrivateChat chat = new TofuPrivateChat();
        chat.setUser1Id(userId1);
        chat.setUser2Id(userId2);
        chatMapper.insert(chat);
        return ResponseData.success();
    }

    public Result<Object> delChat(Integer userId1, Integer userId2) {
        chatMapper.delete(new LambdaQueryWrapper<TofuPrivateChat>()
                .eq(TofuPrivateChat::getUser1Id, userId1)
                .eq(TofuPrivateChat::getUser2Id, userId2));
        return ResponseData.success();
    }
}
