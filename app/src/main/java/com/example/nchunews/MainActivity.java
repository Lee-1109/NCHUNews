package com.example.nchunews;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;

import com.example.nchunews.adapter.NewsAdapter;
import com.example.nchunews.databinding.ActivityMainBinding;
import com.example.nchunews.jsoup.HotNewsHelper;
import com.example.nchunews.mytools.Constants;
import com.example.nchunews.vo.OneNews;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    ActivityMainBinding binding;
    List<OneNews> newsList = new ArrayList<>();//从网页上爬取的新闻信息对象列表
    NewsAdapter adapter = new NewsAdapter(newsList);//新闻列表RecycleView适配器
    MyHandler handler = new MyHandler(Looper.myLooper(),this);//消息传递机制 用于不同线程之间信息通信
    //自定义信息传递类
    static class MyHandler extends Handler {
        WeakReference<MainActivity> myActivity;
        //构造函数，传来的是外部类的this
        public MyHandler(@NonNull Looper looper, MainActivity activity){
            super(looper);//调用父类的显式指明的构造函数
            myActivity = new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MainActivity activity= myActivity.get();
            if(activity == null)
                return ;//activity都没了还处理个XXX
            switch (msg.what) {
                case Constants.MESSAGE_WHAT_NEWS_LIST:
                    //在这里通过activity引用外部类
                    activity.adapter.setData(activity.newsList);
                    activity.adapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = getLayoutInflater();
        binding = ActivityMainBinding.inflate(inflater);
        setContentView(binding.getRoot());
        getActionBar().hide();
        //设置适配器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.mainList.setLayoutManager(layoutManager);
        binding.mainList.setAdapter(adapter);
        //进行网络请求
        new Thread(() -> {
            HotNewsHelper helper = new HotNewsHelper();
            newsList = helper.getNewsList();
            Message message = new Message();
            message.what = Constants.MESSAGE_WHAT_NEWS_LIST;
            message.obj = newsList;
            handler.sendMessage(message);
        }).start();
    }
}