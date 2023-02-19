package com.zlee.controller;

import com.zlee.Result.ResponseData;
import com.zlee.Result.Result;
import com.zlee.service.impl.TofuContentServiceImpl;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;


/**
 * @author z-Lee
 * @date 2023-02-09-16:31
 */
@RestController
@RequestMapping("/content")
public class ContentController {

    private final TofuContentServiceImpl contentService;

    public ContentController(TofuContentServiceImpl contentService) {
        this.contentService = contentService;
    }

    @PostMapping("/newPicture")
    public Result<Object> newPicture(@RequestParam("pictureFile") List<MultipartFile> files,
                                     @RequestParam("userId") Integer userId,
                                     @RequestParam("nickName") String nickName,
                                     @RequestParam("contentType") String contentType,
                                     @RequestParam("text") String text) {

        HashMap<String, Object> pictureContentMap = new HashMap<>(16);
        pictureContentMap.put("userId", userId);
        pictureContentMap.put("nickName", nickName);
        pictureContentMap.put("contentType", contentType);
        pictureContentMap.put("text", text);
        return contentService.newPicture(files, pictureContentMap);
    }

    @PostMapping("/newVideo")
    public Result<Object> newVideo(@RequestParam("videoFile") MultipartFile file,
                                     @RequestParam("userId") Integer userId,
                                     @RequestParam("nickName") String nickName,
                                     @RequestParam("contentType") String contentType,
                                     @RequestParam("text") String text) {

        HashMap<String, Object> pictureContentMap = new HashMap<>(16);
        pictureContentMap.put("userId", userId);
        pictureContentMap.put("nickName", nickName);
        pictureContentMap.put("contentType", contentType);
        pictureContentMap.put("text", text);
        return contentService.newVideo(file, pictureContentMap);
    }

    @GetMapping("/getContent")
    public Result<Object> getContent(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                     @RequestParam(value = "userId", defaultValue = "") Integer userId) {

        return ResponseData.success(contentService.getContent(pageNum, userId));
    }

    @PutMapping("/like")
    public Result<Object> like(@RequestParam("type") String type,
                               @RequestParam("userId") Integer userId,
                               @RequestParam("contentId") Integer contentId){

        return contentService.like(type, userId, contentId);
    }

    @PutMapping("/collect")
    public Result<Object> collect(@RequestParam("type") String type,
                               @RequestParam("userId") Integer userId,
                               @RequestParam("contentId") Integer contentId){

        return contentService.collect(type, userId, contentId);
    }

}
