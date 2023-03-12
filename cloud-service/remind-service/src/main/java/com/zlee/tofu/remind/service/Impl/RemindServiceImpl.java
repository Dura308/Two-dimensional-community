package com.zlee.tofu.remind.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlee.tofu.feign.Result.ResponseData;
import com.zlee.tofu.feign.Result.Result;
import com.zlee.tofu.feign.clients.UserClient;
import com.zlee.tofu.feign.entity.*;
import com.zlee.tofu.feign.websocketResult.WsMsg;
import com.zlee.tofu.remind.controller.WebSocketServer;
import com.zlee.tofu.remind.mapper.ChatMessageMapper;
import com.zlee.tofu.remind.mapper.RemindMapper;
import com.zlee.tofu.remind.service.RemindService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * @author z-Lee
 * @date 2023-03-03-12:35
 */
@Service
public class RemindServiceImpl extends ServiceImpl<RemindMapper, TofuEventRemind> implements RemindService {

    private final RemindMapper remindMapper;
    private final ChatMessageMapper chatMessageMapper;
    private final UserClient userClient;

    public RemindServiceImpl(RemindMapper remindMapper, UserClient userClient, ChatMessageMapper chatMessageMapper) {
        this.remindMapper = remindMapper;
        this.userClient = userClient;
        this.chatMessageMapper = chatMessageMapper;
    }

    public void sendCommentMessage(String action, Integer recipientId, ContentComment commentInfo) {

        //自己的评论不用提醒
        if (Objects.equals(commentInfo.getUserId(), recipientId)){
            return;
        }

        //添加一条评论消息提醒
        TofuEventRemind eventRemind = new TofuEventRemind();
        eventRemind.setAction(action);
        eventRemind.setSourceId(commentInfo.getContentId());
        eventRemind.setSourceType("content");
        eventRemind.setSourceContent(commentInfo.getComment());
        eventRemind.setUrl(null);
        eventRemind.setSenderId(commentInfo.getUserId());
        eventRemind.setRecipientId(recipientId);
        remindMapper.insert(eventRemind);
        WebSocketServer.sendMessage(recipientId, new WsMsg<>(action, null));
    }


    public void sendContentLikeMessage(String action, TofuContent content, Integer senderId) {

        //自己的点赞不用提醒
        if (Objects.equals(content.getUserId(), senderId)){
            return;
        }

        //判断是否已存在一条点赞通知
        TofuEventRemind selectOne = remindMapper.selectOne(new LambdaQueryWrapper<TofuEventRemind>()
                .eq(TofuEventRemind::getAction, action)
                .eq(TofuEventRemind::getSourceId, content.getContentId())
                .eq(TofuEventRemind::getSenderId, senderId));
        if (selectOne != null){
            return;
        }

        //添加一条帖子点赞提醒
        TofuEventRemind eventRemind = new TofuEventRemind();
        eventRemind.setAction(action);
        eventRemind.setSourceId(content.getContentId());
        eventRemind.setSourceType("content");
        eventRemind.setSourceContent(null);
        eventRemind.setUrl(null);
        eventRemind.setSenderId(senderId);
        eventRemind.setRecipientId(content.getUserId());
        remindMapper.insert(eventRemind);
        WebSocketServer.sendMessage(content.getUserId(), new WsMsg<>(action, null));
    }

    public Result<Object> getUnreadRemind(Integer userId) {
        HashMap<String, Long> unreadMap = new HashMap<>(16);

        //获取未读评论数量
        Long unreadCommentNum = remindMapper.selectCount(new LambdaQueryWrapper<TofuEventRemind>()
                .eq(TofuEventRemind::getRecipientId, userId)
                .eq(TofuEventRemind::getState, false)
                .eq(TofuEventRemind::getAction, "comment"));

        //获取未读回复数量
        Long unreadReplyNum = remindMapper.selectCount(new LambdaQueryWrapper<TofuEventRemind>()
                .eq(TofuEventRemind::getRecipientId, userId)
                .eq(TofuEventRemind::getState, false)
                .eq(TofuEventRemind::getAction, "reply"));

        //获取未读点赞数量
        Long unreadLikeNum = remindMapper.selectCount(new LambdaQueryWrapper<TofuEventRemind>()
                .eq(TofuEventRemind::getRecipientId, userId)
                .eq(TofuEventRemind::getState, false)
                .eq(TofuEventRemind::getAction, "like"));

        //获取未读私信数量
        Long unreadMessageNum = chatMessageMapper.selectCount(new LambdaQueryWrapper<TofuPrivateMessage>()
                .eq(TofuPrivateMessage::getRecipientId, userId)
                .eq(TofuPrivateMessage::getState, false));


        unreadMap.put("unreadCommentNum", unreadCommentNum);
        unreadMap.put("unreadReplyNum", unreadReplyNum);
        unreadMap.put("unreadLikeNum", unreadLikeNum);
        unreadMap.put("unreadMessageNum", unreadMessageNum);
        return ResponseData.success(unreadMap);

    }

    public Result<Object> getAllComment(Integer userId) {
        List<TofuEventRemind> commentList = remindMapper.selectList(new LambdaQueryWrapper<TofuEventRemind>()
                .eq(TofuEventRemind::getRecipientId, userId)
                .eq(TofuEventRemind::getAction, "comment")
                .orderByDesc(TofuEventRemind::getRemindTime));

        ArrayList<HashMap<String, Object>> resultList = new ArrayList<>();
        for (TofuEventRemind commentRemind : commentList) {
            HashMap<String, Object> senderMap = new HashMap<>(16);
            TofuUser sender = userClient.getUserInfoByUserId(commentRemind.getSenderId());
            senderMap.put("senderId", sender.getUserId());
            senderMap.put("avatar", sender.getAvatar());
            senderMap.put("nickName", sender.getNickName());
            senderMap.put("commentRemind", commentRemind);
            resultList.add(senderMap);
        }

        return ResponseData.success(resultList);
    }

    public Result<Object> getAllReply(Integer userId) {
        List<TofuEventRemind> replyList = remindMapper.selectList(new LambdaQueryWrapper<TofuEventRemind>()
                .eq(TofuEventRemind::getRecipientId, userId)
                .eq(TofuEventRemind::getAction, "reply")
                .orderByDesc(TofuEventRemind::getRemindTime));

        ArrayList<HashMap<String, Object>> resultList = new ArrayList<>();
        for (TofuEventRemind replyRemind : replyList) {
            HashMap<String, Object> senderMap = new HashMap<>(16);
            TofuUser sender = userClient.getUserInfoByUserId(replyRemind.getSenderId());
            senderMap.put("senderId", sender.getUserId());
            senderMap.put("avatar", sender.getAvatar());
            senderMap.put("nickName", sender.getNickName());
            senderMap.put("replyRemind", replyRemind);
            resultList.add(senderMap);
        }

        return ResponseData.success(resultList);
    }

    public Result<Object> getAllLike(Integer userId) {
        List<TofuEventRemind> likeList = remindMapper.selectList(new LambdaQueryWrapper<TofuEventRemind>()
                .eq(TofuEventRemind::getRecipientId, userId)
                .eq(TofuEventRemind::getAction, "like"));
        ArrayList<HashMap<String, Object>> resultList = new ArrayList<>();
        for (TofuEventRemind likeRemind : likeList) {
            HashMap<String, Object> senderMap = new HashMap<>(16);
            TofuUser sender = userClient.getUserInfoByUserId(likeRemind.getSenderId());
            senderMap.put("senderId", sender.getUserId());
            senderMap.put("avatar", sender.getAvatar());
            senderMap.put("nickName", sender.getNickName());
            senderMap.put("likeRemind", likeRemind);
            resultList.add(senderMap);
        }

        return ResponseData.success(resultList);
    }

    public Result<Object> readRemind(Integer userId, String type) {
        LambdaUpdateWrapper<TofuEventRemind> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.setSql("STATE = TRUE");
        updateWrapper.eq(TofuEventRemind::getRecipientId, userId);
        updateWrapper.eq(TofuEventRemind::getAction, type);
        update(updateWrapper);

        return ResponseData.success("消息已读");
    }
}
