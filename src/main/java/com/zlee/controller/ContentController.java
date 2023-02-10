package com.zlee.controller;

import com.zlee.Result.ResponseData;
import com.zlee.Result.Result;
import com.zlee.service.impl.TofuContentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
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
                                     @RequestParam("userId") String userId,
                                     @RequestParam("nickName") String nickName,
                                     @RequestParam("contentType") String contentType,
                                     @RequestParam("text") String text) {

        HashMap<String, Object> pictureContentMap = new HashMap<>(16);
        pictureContentMap.put("userId", userId);
        pictureContentMap.put("nickName", nickName);
        pictureContentMap.put("contentType", contentType);
        pictureContentMap.put("text", text);
        return contentService.newContent(files, pictureContentMap);
    }

    @GetMapping("/getContent")
    public Result<Object> getContent() {

        List<HashMap<String, Object>> list = contentService.getContent();

        return ResponseData.success(list);
    }
}
