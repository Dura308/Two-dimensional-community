package com.zlee.tofu.feign.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * content_picture
 * @author 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("CONTENT_PICTURE")
public class ContentPicture implements Serializable {
    /**
     * 内容ID
     */
    private String contentId;

    /**
     * 图片地址
     */
    private String url;

    @Serial
    private static final long serialVersionUID = 1L;
}