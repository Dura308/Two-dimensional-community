package com.zlee.service.impl;

import com.zlee.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static com.zlee.utils.ConstantUtil.NEW_COMMENT_MSG;
import static com.zlee.utils.ConstantUtil.UNREAD;

/**
 * @author z-Lee
 * @date 2023-02-23-16:27
 */
@Service
@Slf4j
public class ForwardServiceImpl {

    private final RedisUtil redisUtil;

    /**
     * 当前连接数
     */
    private static final AtomicInteger count = new AtomicInteger(0);

    /**
     * 使用map对象，便于根据userId来获取对应的SseEmitter，或者放redis里面
     */
    private static Map<Integer, SseEmitter> sseEmitterMap = new ConcurrentHashMap<>();

    public ForwardServiceImpl(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    public SseEmitter connect(Integer userId) {
        if (sseEmitterMap.containsKey(userId)) {
            return sseEmitterMap.get(userId);
        }
        try {
            /*设置超时时间，0表示不过期。默认30秒*/
            SseEmitter sseEmitter = new SseEmitter(0L);
            /*注册回调*/
            sseEmitter.onCompletion(completionCallBack(userId));
            sseEmitter.onError(errorCallBack(userId));
            sseEmitter.onTimeout(timeoutCallBack(userId));
            sseEmitterMap.put(userId, sseEmitter);

            /* 数量+1 */
            count.getAndIncrement();

            return sseEmitter;
        } catch (Exception e) {
            log.info("创建新的sse连接异常，当前用户：{}", userId);
        }
        return null;
    }
    /**
     * 评论消息转发
     * @param userId 接收方userId
     * */
    public void sendMessage(Integer userId, Map<String, Object> msgMap) {
        if (sseEmitterMap.containsKey(userId)) {
            try {
                sseEmitterMap.get(userId).send(msgMap);
            } catch (IOException e) {
                log.error("用户[{}]推送异常:{}", userId, e.getMessage());
                removeUser(userId);
            }
        }else{
            int size = redisUtil.hmGet(UNREAD + userId).size();
            redisUtil.hSet(UNREAD + userId, NEW_COMMENT_MSG + (size + 1), msgMap);
        }
    }

    /**
     * 移除用户连接
     */
    public void removeUser(Integer userId) {
        sseEmitterMap.remove(userId);
        // 数量-1
        count.getAndDecrement();
        log.info("移除用户：{}", userId);
    }

    private Runnable completionCallBack(Integer userId) {
        return () -> {
            log.info("结束连接：{}", userId);
            removeUser(userId);
        };
    }

    private Runnable timeoutCallBack(Integer userId) {
        return () -> {
            log.info("连接超时：{}", userId);
            removeUser(userId);
        };
    }

    private Consumer<Throwable> errorCallBack(Integer userId) {
        return throwable -> {
            log.info("连接异常：{}", userId);
            removeUser(userId);
        };
    }
}
