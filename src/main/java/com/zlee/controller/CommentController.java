package com.zlee.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.zlee.Result.ResponseData;
import com.zlee.Result.Result;
import com.zlee.entity.ContentComment;
import com.zlee.service.impl.ContentCommentServiceImpl;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

/**
 * @author z-Lee
 * @date 2023-02-03-15:09
 */

@RestController
@RequestMapping("/home")
public class CommentController {

    private final ContentCommentServiceImpl commentService;

    public CommentController(ContentCommentServiceImpl commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/getComments")
    public Result<Object> getComment(@Param("contentId") Integer contentId) {
        return ResponseData.success(commentService.getComment(contentId));
    }

    @PostMapping("/reply")
    public Result<Object> reply(@RequestBody ContentComment comment) {
        return commentService.insertComment(comment);
    }

    @GetMapping("/getCommentMsg")
    public Result<Object> commentForward(@RequestParam Integer userId){
        return commentService.commentForward(userId);
    }

    @GetMapping("/test")
    public Result<Object> test() {
        System.out.println(1);
        throw new JWTVerificationException("测试");
    }
}
