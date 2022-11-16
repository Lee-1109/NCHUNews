package com.example.nchunews.vo;

import android.graphics.Bitmap;

import java.util.List;

import lombok.Data;

//一条新闻的值对象
@Data
public class OneNews {
    private String title;//文章标题
    private String htmlURL;//文章的来源网址
    private String source;//文章来源
    private String content;//文章内容
    private String updateTime;//最近更新时间
    private int category;
    private int isLocal;
    private int isCollected;//标识是否收藏了文章
    private List<Bitmap> images;//图片资源

}
