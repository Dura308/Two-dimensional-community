package com.zlee.tofu.comment.controller;

import com.zlee.tofu.comment.service.Impl.ContentCommentServiceImpl;
import com.zlee.tofu.feign.Result.ResponseData;
import com.zlee.tofu.feign.Result.Result;
import com.zlee.tofu.feign.entity.ContentComment;
import org.springframework.web.bind.annotation.*;

/**
 * @author z-Lee
 * @date 2023-02-03-15:09
 */

@RestController
@RequestMapping("/comment")
public class CommentController {

    private final ContentCommentServiceImpl commentService;

    public CommentController(ContentCommentServiceImpl commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/getComments")
    public Result<Object> getComment(@RequestParam("contentId") Integer contentId) {
        return ResponseData.success(commentService.getComment(contentId));
    }

    @PostMapping("/rootReply")
    public Result<Object> rootReply(@RequestBody ContentComment commentInfo) {
        String action = "comment";
        return commentService.insertComment(action, commentInfo);
    }

    @PostMapping("/subReply")
    public Result<Object> subReply(@RequestBody ContentComment commentInfo) {
        String action = "reply";
        return commentService.insertComment(action, commentInfo);
    }


}
