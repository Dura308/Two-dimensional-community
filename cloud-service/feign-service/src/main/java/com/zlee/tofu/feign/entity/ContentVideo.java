package com.zlee.tofu.feign.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * content_video
 * @author 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("CONTENT_VIDEO")
public class ContentVideo implements Serializable {
    /**
     * 内容ID
     */
    private String contentId;

    /**
     * 视频地址
     */
    private String url;

    @Serial
    private static final long serialVersionUID = 1L;
}