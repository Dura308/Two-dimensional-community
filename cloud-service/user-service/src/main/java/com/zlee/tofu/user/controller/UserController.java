package com.zlee.tofu.user.controller;

import com.zlee.tofu.feign.Result.ResponseData;
import com.zlee.tofu.feign.Result.Result;
import com.zlee.tofu.feign.entity.TofuUser;
import com.zlee.tofu.feign.entity.TofuUserCollect;
import com.zlee.tofu.feign.entity.TofuUserLike;
import com.zlee.tofu.user.service.Impl.TofuUserServiceImpl;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author z-Lee
 * @date 2023-02-17-22:03
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final TofuUserServiceImpl userService;

    public UserController(TofuUserServiceImpl userService) {
        this.userService = userService;
    }

    @PutMapping("/updateInfo")
    public Result<Object> updateInfo(@RequestBody TofuUser user) {
        return userService.updateInfo(user);
    }

    @PutMapping("/updateAvatar")
    public Result<Object> updateAvatar(@RequestParam("userId") Integer userId,
                                       @RequestParam("avatarFile") MultipartFile file) {
        return userService.updateAvatar(userId,file);
    }

    @GetMapping("/getContent")
    public Result<Object> getUserContent(@RequestParam("userId") Integer userId,
                                            @RequestParam("type") String type) {
        return userService.getUserContent(userId, type);
    }

    @PostMapping("/attentionOperation")
    public Result<Object> attentionOperation(@RequestParam("fansUserId") Integer fansUserId,
                                        @RequestParam("attentionUserId") Integer attentionUserId){
        return userService.attentionOperation(fansUserId, attentionUserId);
    }

    @PostMapping("/cancelAttention")
    public Result<Object> cancelAttention(@RequestParam("fansUserId") Integer fansUserId,
                                          @RequestParam("attentionUserId") Integer attentionUserId){
        return userService.cancelAttention(fansUserId, attentionUserId);
    }

    @GetMapping("/getAllAttention")
    public Result<Object> getAllAttention(@RequestParam("userId") Integer userId,
                                          @RequestParam("pageNum") Integer pageNum){
        return userService.getAllAttention(pageNum, userId);
    }

    @GetMapping("/getAllFans")
    public Result<Object> getAllFans(@RequestParam("userId") Integer userId,
                                     @RequestParam("pageNum") Integer pageNum){
        return userService.getAllFans(pageNum, userId);
    }

    @GetMapping("/getUserLikeList")
    public List<TofuUserLike> getUserLikeList(@RequestParam("userId") Integer userId){
        return userService.getUserLikeList(userId);
    }

    @GetMapping("/getUserCollectList")
    public List<TofuUserCollect> getUserCollectList(@RequestParam("userId") Integer userId){
        return userService.getUserCollectList(userId);
    }

    @GetMapping("/getUserIsLike")
    public boolean getUserIsLike(@RequestParam("userId") Integer userId,
                          @RequestParam("contentId") Integer contentId){
        return userService.getUserIsLike(userId, contentId);
    }

    @GetMapping("/getUserIsCollect")
    public boolean getUserIsCollect(@RequestParam("userId") Integer userId,
                             @RequestParam("contentId") Integer contentId){
        return userService.getUserIsCollect(userId, contentId);
    }

    @PostMapping("/insertUserLike")
    public void insertUserLike(@RequestBody TofuUserLike userLike){
        userService.insertUserLike(userLike);
    }

    @PostMapping("/insertUserCollect")
    public void insertUserCollect(@RequestBody TofuUserCollect userCollect){
        userService.insertUserCollect(userCollect);
    }

    @DeleteMapping("/delUserLike")
    public void delUserLike(@RequestParam("userId") Integer userId,
                            @RequestParam("contentId") Integer contentId){
        userService.delUserLike(userId, contentId);
    }

    @DeleteMapping("/delUserCollect")
    public void delUserCollect(@RequestParam("userId") Integer userId,
                               @RequestParam("contentId") Integer contentId){
        userService.delUserCollect(userId, contentId);
    }

    @PutMapping("/updateUserAllLikeNum")
    public void updateUserBySql(@RequestParam("sql") String sql,
                                @RequestParam("userId") Integer userId){
        userService.updateUserBySql(sql, userId);
    }

    @GetMapping("/getUserInfoByUserId")
    public TofuUser getUserInfoByUserId(@RequestParam("userId") Integer userId){
        return userService.getUserInfoByUserId(userId);
    }

    @GetMapping("/getUserIsAttention")
    public boolean getUserIsAttention(@RequestParam("userId") Integer userId,
                                      @RequestParam("cardUserId") Integer cardUserId){
        return userService.getUserIsAttention(userId, cardUserId);
    }

    @GetMapping("/getUserBaseInfo")
    public TofuUser getUserBaseInfo(@RequestParam("userId") Integer userId){
        return userService.getUserBaseInfo(userId);
    }
}
