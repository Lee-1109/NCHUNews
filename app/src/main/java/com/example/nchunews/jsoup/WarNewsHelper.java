package com.example.nchunews.jsoup;

import com.example.nchunews.vo.OneNews;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WarNewsHelper {
    ArrayList<OneNews> data = new ArrayList<>();
    /**
     * 获取军事新闻
     * @return
     */
    public List<OneNews> getNewsList(){
        try {
            Document document = Jsoup.connect("https://news.163.com/air/").get();
            //Elements elements = document.select("div[class=data_row.news_article.clearfix]");
            Elements elements = document.select("div.hidden");
            if(elements.isEmpty()) System.out.println("WarJsoupHelper=====空的=====");
            else {
                System.out.println("WarJsoupHelper=====非空=====");
//                for (Element element :elements){
//                    OneNews news = new OneNews();
//                    Element image = element.child(0);//获取图片
//                    Element text = element.child(1);//获取文本内容
//                    Elements title = text.select("[href]");//获取标题
//                    Elements time = text.select("span[class=time]");
//                    if(!title.isEmpty()){
//                        news.setTitle(title.get(0).text());//文章标题
//                        news.setHtmlURL(title.get(0).attr("href"));//获取文章html链接
//                        news.setUpdateTime(time.get(0).text());//获取更新时间
//                        System.out.println("爬取军事新闻"+news.toString());
//                    }
//                    data.add(news);
//                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}
