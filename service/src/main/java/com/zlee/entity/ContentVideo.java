package com.zlee.entity;

import java.io.Serial;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * content_video
 * @author 
 */
@Data
@TableName("CONTENT_VIDEO")
public class ContentVideo implements Serializable {
    /**
     * 内容ID
     */
    private String contentId;

    /**
     * 视频地址
     */
    private String video;

    @Serial
    private static final long serialVersionUID = 1L;
}