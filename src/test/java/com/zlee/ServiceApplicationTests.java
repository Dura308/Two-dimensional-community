package com.zlee;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zlee.entity.ContentComment;
import com.zlee.entity.TofuUser;
import com.zlee.mapper.ContentCommentMapper;
import com.zlee.mapper.TofuUserMapper;
import com.zlee.utils.CommentUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ServiceApplicationTests {

    @Autowired
    private TofuUserMapper userMapper;

    @Autowired
    private ContentCommentMapper contentCommentMapper;

    @Test
    void insertUser() {
        TofuUser tofuUser = new TofuUser();
        tofuUser.setNickName("test");
        tofuUser.setPhone("1111111");
        int insert = userMapper.insert(tofuUser);
        System.out.println(insert);
        System.out.println(tofuUser.getUserId());
    }

    @Test
    void getComment() {
        List<ContentComment> list = contentCommentMapper.selectList(new LambdaQueryWrapper<ContentComment>().eq(ContentComment::getContentId, 1));
        System.out.println(list);
        List<ContentComment> commentList = CommentUtil.processContentComments(list, false);
    }

}
