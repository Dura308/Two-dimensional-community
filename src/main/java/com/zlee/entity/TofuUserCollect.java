package com.zlee.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author z-Lee
 * @date 2023-02-14-12:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tofu_user_collect")
public class TofuUserCollect {

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 点赞的帖子ID
     */
    private Integer contentId;

    /**
     * 创建时间
     */
    private Date createdTime;
}