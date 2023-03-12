package com.zlee.tofu.feign.clients;

import com.zlee.tofu.feign.entity.ContentComment;
import com.zlee.tofu.feign.entity.TofuContent;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author z-Lee
 * @date 2023-03-03-14:44
 */
@FeignClient("remindService")
public interface RemindClient {

    @PostMapping("/remind/remindComment")
    void remindComment(@RequestBody ContentComment commentInfo,
                       @RequestParam("action") String action,
                       @RequestParam("recipientId") Integer recipientId);

    @PostMapping("/remind/remindContentLike")
    void remindContentLike(@RequestBody TofuContent content,
                           @RequestParam("action") String action,
                           @RequestParam("senderId") Integer senderId);
}
