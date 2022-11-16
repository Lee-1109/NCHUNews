package com.example.nchunews.dao;

import com.example.nchunews.vo.Comment;
import com.example.nchunews.vo.OneNews;
import com.example.nchunews.vo.User;

import java.util.ArrayList;
import java.util.List;

public interface ICommentDAO {
    /**
     * 添加一条评论
     * @param comment 评论内容
     */
    void insertOne(Comment comment);

    /**
     *
     * @param htmlUrl 文章的html页面
     * @param user 用户信息
     * @param comment 用户更新的评论
     */
    void update(String htmlUrl, User user, Comment comment);

    /**
     * 删除一条评论
     * @param comment 评论的详细内同
     */
    void deleteOne(Comment comment);//删除一条评论

    /**
     * 删除一条文章的所有评论
     * @param HtmlUrl
     */
    void deleteByHtmlUrl(String HtmlUrl);//删除所有评论

    List<Comment> findAllCommentsNewsByUserId(String userid);
    List<Comment> findAllCommentsByArticle(String htmlUrl);


}
