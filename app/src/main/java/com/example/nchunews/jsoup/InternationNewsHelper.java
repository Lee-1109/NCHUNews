package com.example.nchunews.jsoup;

import android.util.Log;

import com.example.nchunews.vo.OneNews;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InternationNewsHelper {
    ArrayList<OneNews> data = new ArrayList<>();
    int CATEGORY_CODE = 2;

    /**
     * 获取并返回国际新闻
     * @return 信息列表 带有简单的标题 与内容网址
     */
    public List<OneNews> getNewsList(){
        try {
            Document document = Jsoup.connect("https://news.163.com/world/").get();
            Elements list = document.select("div.hidden").select("div>a");
            if(list.isEmpty()){
                Log.d("NewAirJsoupHelper","暂未获得任何信息");
            }else {
                for (Element element: list){
                    OneNews news = new OneNews();
                    news.setTitle(element.text());
                    news.setHtmlURL(element.attr("href"));
                    news.setCategory(CATEGORY_CODE);
                    data.add(news);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}
