package com.zlee.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author z-Lee
 * @date 2023-02-20-19:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tofu_user_attention_fans")
public class AttentionFans implements Serializable {

    /** 粉丝的id */
    private Integer fansUserId;

    /** 关注的id */
    private Integer attentionUserId;

    @Serial
    private static final long serialVersionUID = 1L;
}
