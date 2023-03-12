package com.zlee.tofu.feign.clients;

import com.zlee.tofu.feign.entity.TofuContent;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author z-Lee
 * @date 2023-03-01-17:32
 */
@FeignClient("contentService")
public interface ContentClient {

    /**
     * 获取具体内容信息
     * @param type 内容类型[PICTURE， VIDEO]
     * @param contentId 内容id
     * @return 结果
     * */
    @GetMapping("/content/getTypeList")
    List<?> getTypeList(@RequestParam("type") String type,
                        @RequestParam("contentId") Integer contentId);


    @GetMapping("/content/getContentByUserId")
    List<TofuContent> getContentByUserId(@RequestParam("type") String type,
                                         @RequestParam("userId") Integer userId);

    @PutMapping("/content/updateContentByContentId")
    void updateContentByContentId(@RequestParam("sql") String sql,
                                         @RequestParam("contentId") Integer contentId);

    @GetMapping("/content/getContentUserId")
    Integer getContentUserId(@RequestParam("contentId") Integer contentId);
}
