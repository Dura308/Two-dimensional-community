package com.zlee.tofu.remind.config;


import com.alibaba.fastjson2.JSON;
import com.zlee.tofu.feign.websocketResult.WsMsg;
import jakarta.websocket.Encoder;
import jakarta.websocket.EndpointConfig;

/**
 * @author Administrator
 */
public class WebSocketCustomEncoding implements Encoder.Text<WsMsg<Object>> {
    @Override
    public String encode(WsMsg<Object> msg) {
        assert msg!=null;
        return JSON.toJSONString(msg);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
