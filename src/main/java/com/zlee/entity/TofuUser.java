package com.zlee.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * tofu_user
 * @author 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tofu_user")
public class TofuUser implements Serializable {
    /**
     * 用户ID
     */
    @TableId(value = "USER_ID", type = IdType.AUTO)
    private Integer userId;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 密码
     */
    private String userPass;

    /**
     * 密码盐
     */
    private String passSalt;

    /**
     * 用户性别
     */
    private String userGender;

    /**
     * 用户地区
     */
    private String userArea;

    /**
     * 个性签名
     */
    private String userIntro;

    /**
     * 头像图片
     */
    private String avatar;

    /**
     * 邮件地址
     */
    private String email;

    /** 关注数量 */
    private Integer attentionNum;

    /** 粉丝数量 */
    private Integer fansNum;

    /** 所有获赞数量 */
    private Integer allLikesNum;

    /**
     * 用户状态
     */
    private String userStatus;

    /**
     * 最后登录时间
     */
    private Date lastLoginTime;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 更新时间
     */
    private Date updatedTime;

    private static final long serialVersionUID = 1L;
}