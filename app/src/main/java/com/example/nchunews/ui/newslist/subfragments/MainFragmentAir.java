package com.example.nchunews.ui.newslist.subfragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nchunews.R;
import com.example.nchunews.adapter.NewsAdapter;
import com.example.nchunews.dao.NewsDAO;
import com.example.nchunews.jsoup.AirNewsHelper;
import com.example.nchunews.mytools.Constants;
import com.example.nchunews.mytools.MyThreadPool;
import com.example.nchunews.mytools.MyTime;
import com.example.nchunews.vo.OneNews;

import java.lang.ref.WeakReference;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;

@Getter
@NoArgsConstructor
@Setter
public class MainFragmentAir extends Fragment {

    private View view;

    private NewsAdapter adapter;

    private LinearLayoutManager layoutManager;

    private RecyclerView newsListRecycle;

    private TextView refreshHintText;

    private NewsDAO newsDAO;

    private Message message;

    private SwipeRefreshLayout refreshLayout;

    private AirNewsHelper helper;

    private List<OneNews> dataList;

    private MyThreadPool threadPool ;
    /**
     * 进行网络请求 通过信息传递的方式
     */
    private final Runnable requireByMessage = new Runnable() {
        @Override
        public void run() {
            dataList = helper.getNewsList();
            newsDAO.insert(dataList);
            dataList = newsDAO.queryAll(Constants.CATEGORY_CODE_AIR);
            message.what = Constants.MESSAGE_WHAT_NEWS_LIST;
            message.obj = dataList;
            handler.sendMessage(message);
        }
    };

    /**
     * 自定义线程 用于刷新时网络请求 通过直接在主线程中操作的方式
     */
    Runnable requireInternet = new Runnable() {
        @SneakyThrows
        @Override
        public void run() {
            System.out.println("开始请求网络");
            /*-=====这之间是进行新闻信息的网络请求操作=======*/
            dataList = helper.getNewsList();
            newsDAO.insert(dataList);
            dataList = newsDAO.queryAll(Constants.CATEGORY_CODE_AIR);
            /*-=====这之间是进行新闻信息的网络请求操作=======*/
            System.out.println("网络请求完成");
            /*runnable中的操作是在主线程中执行的 而不是在本线程中执行*/
            handler.post(() -> {
                //直接操作主线程中的UI
                refreshHintText.setText("刷新完毕");
                adapter.setData(dataList);
                adapter.notifyDataSetChanged();
                refreshHintText.setText("最近更新："+ MyTime.getUpdateTime());
                refreshLayout.setRefreshing(false);
            });
            Thread.sleep(1000);
            //通知视图更新成功
            Message msg = new Message();
            msg.what = Constants.WHAT_HINT_REFRESH_SUCCESS;
            msg.obj = MyTime.getUpdateTime();
            handler.sendMessage(msg);
        }
    };
    /**
     * 自动绑定当前主线程
     */
    MainFragmentAir.MyHandler handler = new MainFragmentAir.MyHandler(Looper.myLooper(),this);

    /**
     * 自定义Handler 类用来跨线程更新UI等
     * 在handler里面操作数据以及进行网络请求
     */
    static class MyHandler extends Handler {
        WeakReference<MainFragmentAir> airFragment;
        //构造函数，传来的是外部类的this
        public MyHandler(@NonNull Looper looper, MainFragmentAir fragmentAir){
            super(looper);//调用父类的显式指明的构造函数
            airFragment = new WeakReference<>(fragmentAir);
        }
        /*处理消息消息队列中的数据*/
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MainFragmentAir fragmentAir= airFragment.get();
            if(fragmentAir == null)return ;
            switch (msg.what) {
                case Constants.MESSAGE_WHAT_NEWS_LIST:
                case Constants.MESSAGE_WHAT_INTERNET_REFRESH_SUCCESS:
                    fragmentAir.adapter.setData(fragmentAir.dataList);
                    fragmentAir.adapter.notifyDataSetChanged();
                    break;
                case Constants.WHAT_HINT_REFRESH_SUCCESS:
                    fragmentAir.refreshHintText.setText("最近更新时间:"+ msg.obj);
                default:
                    break;
            }
        }
    }

    /**
     * 初次创建视图时使用
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newsDAO = new NewsDAO(getActivity());
        message = new Message();
        helper = new AirNewsHelper();
        dataList = newsDAO.queryAll(Constants.CATEGORY_CODE_AIR);
        adapter = new NewsAdapter(dataList);
        threadPool = new MyThreadPool();
        //数据库中没数据 再请求网络
        if (dataList.isEmpty()) threadPool.execute(requireInternet);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main_air, container, false);
        newsListRecycle = view.findViewById(R.id.list_recycleView_news_air);
        //设置新闻列表RecycleView
        layoutManager = new LinearLayoutManager(getActivity());
        newsListRecycle.setLayoutManager(layoutManager);
        newsListRecycle.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        refreshLayout = view.findViewById(R.id.swipeRefreshLayout_fragment_air);
        refreshHintText = view.findViewById(R.id.textView_fragment_air_refresh);
        //初始化RefreshLayout
        refreshLayout.setEnabled(true);
        refreshLayout.setColorSchemeColors(R.color.gary_100,R.color.black);
        //下拉监听事件
        refreshLayout.setOnRefreshListener(() -> {
            refreshHintText.setVisibility(View.VISIBLE);
            refreshHintText.setText("正在刷新");
            threadPool.execute(requireInternet);
        });
        return view;
    }
}