package com.example.nchunews.ui.me;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.nchunews.NewsActivity;
import com.example.nchunews.dao.NewsDAO;
import com.example.nchunews.vo.OneNews;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MeViewModel extends ViewModel {
    private NewsDAO newsDAO;
    //基本配置信息
    private MutableLiveData<List<String>> configString;
    //需要传递给前台的数据
    private List<String> data;
    public MeViewModel() {
        configString = new MutableLiveData<>();
        data = new ArrayList<>();
        data.add("修改密码");
        data.add("点击我修改密码");
        data.add("我的收藏");
        data.add("点击查看收藏新闻");
        data.add("我的评论");
        data.add("点击查看我的评论");
        data.add("LeeYoung1109");
        data.add("11360");
        configString.setValue(data);
    }
}