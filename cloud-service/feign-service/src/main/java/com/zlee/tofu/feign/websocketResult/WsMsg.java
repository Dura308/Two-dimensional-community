package com.zlee.tofu.feign.websocketResult;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author z-Lee
 * @date 2023-03-11-16:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WsMsg<T> {

    private String type;
    private T msg;
}
