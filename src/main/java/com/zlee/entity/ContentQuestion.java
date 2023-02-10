package com.zlee.entity;

import java.io.Serial;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

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