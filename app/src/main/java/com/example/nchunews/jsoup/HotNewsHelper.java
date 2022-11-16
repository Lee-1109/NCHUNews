package com.example.nchunews.jsoup;

import com.example.nchunews.vo.OneNews;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * 用于获取头条新闻
 */
public class HotNewsHelper {
    ArrayList<OneNews> news = new ArrayList<>();
    int CATEGORY_CODE = 1;
    public  ArrayList<OneNews> getNewsList(){
        try {
            //连接网易新闻客户端
            Document document = Jsoup.connect("https://news.163.com/").get();
            //获取所有a标签内容
            Elements elements = document.select("ul[class=top_news_ul]>li>a");
            for(Element element : elements){
                OneNews oneNews = new OneNews();
                oneNews.setHtmlURL(element.attr("href"));
                oneNews.setTitle(element.text());
                oneNews.setCategory(CATEGORY_CODE);
                news.add(oneNews);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return news;
    }
}
