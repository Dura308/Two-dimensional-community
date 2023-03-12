package com.zlee.tofu.feign.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * content_question
 * @author 
 */
@Data
@TableName("CONTENT_QUESTION")
public class ContentQuestion implements Serializable {
    /**
     * 内容ID
     */
    private String contentId;

    /**
     * 问题标题
     */
    private String questionTitle;

    /**
     * 问题主体
     */
    private String questionMain;

    @Serial
    private static final long serialVersionUID = 1L;
}