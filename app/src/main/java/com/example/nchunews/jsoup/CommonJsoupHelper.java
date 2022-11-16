package com.example.nchunews.jsoup;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.nchunews.vo.OneNews;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CommonJsoupHelper {
    /**
     * 通过输入新闻地址获取新闻内容
     * @param URL 新闻地址
     * @return 新闻地址对应的新闻对象
     */
    public OneNews getNewsContentByURL(String URL){
        OneNews news = new OneNews();
        System.out.println(URL);
        try {
            //获取文章内容文档对象
            Document document = Jsoup.connect(URL).get();
            //选取标题
            Elements title = document.select("h1[class=post_title]");
            //选取来源
            Elements source = document.select("div[class=post_info]>a");
            //选取内容
            Elements content = document.select("div[class=post_body]>p");
            //===========测试用=======================
            StringBuilder builder = new StringBuilder();
            for (Element element :content){
                builder.append("    "+element.text()+"\n");
            }
            //包装好该对象 并返回
            news.setHtmlURL(URL);
            news.setTitle(title.first().text());
            news.setSource(source.first().text());
            news.setContent(builder.toString());
            news.setIsLocal(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return news;
    }

    /**
     * 返回文章里所有图片URL
     * @param htmlURL 文章链接
     * @return
     */
    public List<String> getImageUrlByArticle(String htmlURL){
        List<String> data = new ArrayList<>();
        try {
            Document document = Jsoup.connect(htmlURL).get();
            Elements elements = document.select("div[class=post_body] img");
            if(!elements.isEmpty()){
                for (Element element : elements){
                    data.add(element.attr("src"));
                    System.out.println(element.attr("src"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  data;
    }

    /**
     * 使用文章的链接 抓取并下载网络图片
     * @param htmlURL 文章html URL
     * @return 所有图片列表
     */
    public List<Bitmap> getImageFileByContentHtml(String htmlURL){
        List<String> imageURList = new ArrayList<>();
        try {
            Document document = Jsoup.connect(htmlURL).get();
            Elements elements = document.select("div[class=post_body] img");
            if(!elements.isEmpty()){
                for (Element element : elements){
                    imageURList.add(element.attr("src"));
                    System.out.println(element.attr("src"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Bitmap> images = new ArrayList<>();
        for (String imageURL :imageURList){
            images.add(getBitmap(imageURL));
            System.out.println(getBitmap(imageURL).getByteCount());
        }
        return images;
    }
    /**
     * 下载图片
     * @param path 图片URL
     * @return 返回图片
     */
    private static Bitmap getBitmap(String path){
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200) {
                InputStream inputStream = conn.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
