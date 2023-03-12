package com.zlee.tofu.remind.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.zlee.tofu.feign.entity.TofuPrivateMessage;
import com.zlee.tofu.feign.websocketResult.WsMsg;
import com.zlee.tofu.remind.config.WebSocketCustomEncoding;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Administrator
 */
@Component
@Slf4j
@ServerEndpoint(value = "/websocket/{userId}", encoders = WebSocketCustomEncoding.class)
public class WebSocketServer {
    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;
    private static final CopyOnWriteArraySet<WebSocketServer> webSockets = new CopyOnWriteArraySet<>();
    /**
     * 用来存在线连接数
     */
    private static final Map<Integer, Session> sessionPool = new HashMap<>(16);

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "userId") Integer userId) {

        if (sessionPool.containsKey(userId)) {
            log.info("已存在当前连接");
            return;
        }

        try {
            this.session = session;
            webSockets.add(this);
            sessionPool.put(userId, session);
            log.info("websocket消息: 有新的连接，总数为:" + webSockets.size());
        } catch (Exception e) {

        }
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        String path = JSONObject.parseObject(message).getString("path");
        if (Objects.equals(path, "internal/heart_beat")) {
            session.getAsyncRemote().sendText(path);
            log.info("当前连接数：" + webSockets.size());
        }
        log.info("websocket消息: 收到客户端消息:" + path);
    }

    @OnClose
    public void onClose(@PathParam(value = "userId") Integer userId) {
        webSockets.remove(this);
        sessionPool.remove(userId);
        log.info(userId + "用户退出连接，当前连接总数为：" + webSockets.size());
    }

    /**
     * 发送通知
     */
    public static void sendMessage(Integer recipientId, WsMsg<Object> msg) {
        Session session = sessionPool.get(recipientId);
        if (session != null && session.isOpen()) {
            try {
                session.getBasicRemote().sendObject(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    /** 判断是否在线 */
    public static boolean isOnLine(Integer userId){
        return sessionPool.containsKey(userId);
    }
}