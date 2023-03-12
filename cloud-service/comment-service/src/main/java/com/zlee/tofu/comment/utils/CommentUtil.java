package com.zlee.tofu.comment.utils;



import com.zlee.tofu.feign.entity.ContentComment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author z-Lee
 * @date 2023-02-02-19:23
 */
public class CommentUtil {

    public static List<ContentComment> processContentComments(List<ContentComment> list, boolean useTree) {
        // 空间换时间：把comment_id和comment放入map，能够快速取到ContentComment
        Map<Integer, ContentComment> map = new HashMap<>(16);
        // 最终要返回的list
        List<ContentComment> result = new ArrayList<>();
        // 遍历一次，把所有comment加入到map中，方便快速查找映射
        // 如果是父评论，就直接放入result列表
        for (ContentComment comment : list) {
            if (comment.getParentId() == null) {
                result.add(comment);
            }
            map.put(comment.getCommentId(), comment);
        }

        // 再次遍历，子评论放入到父评论的child中
        for (ContentComment comment : list) {
            // 为子评论设置父评论的nickname和email，以用于前端展示
            if (comment.getParentId() != null) {
                ContentComment parentComment = map.get(comment.getParentId());
                comment.setParentNickName(parentComment.getNickName());
                //comment.setParent_email(parentComment.getEmail());
            }
            Integer id;
            // 如果是以树形结构构建子评论，需要获取的id就是parent_id
            // 如果是以其它方式（二维数组），需要获取的id就是root_parent_id
            if (useTree) {
                id = comment.getParentId();
            } else {
                id = comment.getRootParentId();
            }
            // 只要存在父评论/根父评论
            if (id != null) {
                // 直接通过map快速取得评论，并把自己加入到父评论的子集
                ContentComment p = map.get(id);
                if (p.getChild() == null) {
                    p.setChild(new ArrayList<>());
                }
                p.getChild().add(comment);
            }
        }
        return result;
    }
}
