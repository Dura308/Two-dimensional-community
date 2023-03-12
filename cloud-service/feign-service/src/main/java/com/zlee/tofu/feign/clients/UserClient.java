package com.zlee.tofu.feign.clients;

import com.zlee.tofu.feign.entity.TofuUser;
import com.zlee.tofu.feign.entity.TofuUserCollect;
import com.zlee.tofu.feign.entity.TofuUserLike;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author z-Lee
 * @date 2023-03-01-15:07
 */
@FeignClient("userService")
public interface UserClient {

    @GetMapping("/user/getUserLikeList")
    List<TofuUserLike> getUserLikeList(@RequestParam("userId") Integer userId);

    @GetMapping("/user/getUserCollectList")
    List<TofuUserCollect> getUserCollectList(@RequestParam("userId") Integer userId);

    @GetMapping("/user/getUserIsLike")
    boolean getUserIsLike(@RequestParam("userId") Integer userId,
                          @RequestParam("contentId") Integer contentId);

    @GetMapping("/user/getUserIsCollect")
    boolean getUserIsCollect(@RequestParam("userId") Integer userId,
                             @RequestParam("contentId") Integer contentId);

    @PostMapping("/user/insertUserLike")
    void insertUserLike(@RequestBody TofuUserLike userLike);

    @PostMapping("/user/insertUserCollect")
    void insertUserCollect(@RequestBody TofuUserCollect userCollect);

    @DeleteMapping("/user/delUserLike")
    void delUserLike(@RequestParam("userId") Integer userId,
                     @RequestParam("contentId") Integer contentId);

    @DeleteMapping("/user/delUserCollect")
    void delUserCollect(@RequestParam("userId") Integer userId,
                        @RequestParam("contentId") Integer contentId);

    @PutMapping("/user/updateUserAllLikeNum")
    void updateUserBySql(@RequestParam("sql") String sql,
                         @RequestParam("userId") Integer userId);

    @GetMapping("/user/getUserInfoByUserId")
    TofuUser getUserInfoByUserId(@RequestParam("userId") Integer userId);

    @GetMapping("/user/getUserIsAttention")
    boolean getUserIsAttention(@RequestParam("userId") Integer userId,
                               @RequestParam("cardUserId") Integer cardUserId);

    @GetMapping("/user/getUserBaseInfo")
    TofuUser getUserBaseInfo(@RequestParam("userId") Integer userId);
}
