package com.example.nchunews.dao;

import com.example.nchunews.vo.OneNews;

import java.util.ArrayList;
import java.util.List;

public interface INewsDAO {
    void insert(List<OneNews> data);
    void insertOne(OneNews data);
    void update(String oldHtml, OneNews news);
    ArrayList<OneNews> queryAll(int category);
    OneNews queryByHtmlUrl(String htmlURL);

    /**
     * 执行收藏文章
     * @param htmlurl
     * @param userid
     */
    void collectArticle(OneNews news,String userid);
    /**
     * 查看评论过的文章
     * @param userId 用户ID
     * @return 文章列表
     */
    List<OneNews> commentedArticle(String userId);

    /**
     * 查看收藏过的文章
     * @param userId 用户ID
     * @return 新闻列表
     */
    List<OneNews> collectedArticle(String userId);
}
