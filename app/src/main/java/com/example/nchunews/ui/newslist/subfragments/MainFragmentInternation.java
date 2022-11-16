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
import com.example.nchunews.jsoup.InternationNewsHelper;
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
@Setter
@NoArgsConstructor
public class MainFragmentInternation extends Fragment {

    private View view;

    private NewsAdapter adapter;

    private LinearLayoutManager layoutManager;

    private RecyclerView newsListRecycle;

    private TextView refreshHintText;

    private NewsDAO newsDAO;

    private Message message;

    private SwipeRefreshLayout refreshLayout;

    private InternationNewsHelper helper;

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
            dataList = newsDAO.queryAll(Constants.CATEGORY_CODE_INTERNATION);
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
            dataList = newsDAO.queryAll(Constants.CATEGORY_CODE_INTERNATION);
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
    MainFragmentInternation.MyHandler handler = new MainFragmentInternation.MyHandler(Looper.myLooper(),this);

    /**
     * 自定义Handler 类用来跨线程更新UI等
     * 在handler里面操作数据以及进行网络请求
     */
    static class MyHandler extends Handler {
        WeakReference<MainFragmentInternation> airFragment;
        //构造函数，传来的是外部类的this
        public MyHandler(@NonNull Looper looper, MainFragmentInternation fragmentAir){
            super(looper);//调用父类的显式指明的构造函数
            airFragment = new WeakReference<>(fragmentAir);
        }
        /*处理消息消息队列中的数据*/
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MainFragmentInternation fragmentAir= airFragment.get();
            if(fragmentAir == null)return ;
            switch (msg.what) {
                case Constants.MESSAGE_WHAT_NEWS_LIST:
                case Constants.MESSAGE_WHAT_INTERNET_REFRESH_SUCCESS:
                    fragmentAir.adapter.setData(fragmentAir.dataList);
                    fragmentAir.adapter.notifyDataSetChanged();
                    break;
                case Constants.WHAT_HINT_REFRESH_SUCCESS:
                    fragmentAir.refreshHintText.setText("最近更新时间:"+(String) msg.obj);
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
        helper = new InternationNewsHelper();
        dataList = newsDAO.queryAll(Constants.CATEGORY_CODE_INTERNATION);
        adapter = new NewsAdapter(dataList);
        threadPool = new MyThreadPool();
        //数据库中没数据 再请求网络
        if (dataList.isEmpty()) threadPool.execute(requireInternet);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_main_internation, container, false);
        newsListRecycle = view.findViewById(R.id.list_recycleView_news_international);
        //设置新闻列表RecycleView
        layoutManager = new LinearLayoutManager(getActivity());
        newsListRecycle.setLayoutManager(layoutManager);
        newsListRecycle.setAdapter(adapter);
        refreshLayout = view.findViewById(R.id.swipeRefreshLayout_fragment_internation);
        refreshHintText = view.findViewById(R.id.textView_fragment_internation_refresh);
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
//    private View view;
//    private Message message;
//    private RecyclerView newsListRecycle;
//    private NewsAdapter adapter;
//    private List<OneNews> dataList = new ArrayList<>();
//    private NewsDAO newsDAO;
//    private MyHandler handler = new MyHandler(Looper.myLooper(),this);
//    //自定义信息传递类
//    static class MyHandler extends Handler {
//        WeakReference<MainFragmentInternation> myActivity;
//        //构造函数，传来的是外部类的this
//        public MyHandler(@NonNull Looper looper, MainFragmentInternation fragmentAir){
//            super(looper);//调用父类的显式指明的构造函数
//            myActivity = new WeakReference<>(fragmentAir);
//        }
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            MainFragmentInternation fragmentHot= myActivity.get();
//            if(fragmentHot == null)
//                return ;//fragment都没了还处理个XXX
//            switch (msg.what) {
//                case Constants.MESSAGE_WHAT_NEWS_LIST:
//                    //在这里通过activity引用外部类
//                    fragmentHot.adapter.setData(fragmentHot.dataList);
//                    fragmentHot.adapter.notifyDataSetChanged();
//                    break;
//                default:
//                    break;
//            }
//        }
//    }
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        newsDAO = new NewsDAO(getActivity());
//        dataList = newsDAO.queryAll(Constants.CATEGORY_CODE_INTERNATION);
//        if(dataList.isEmpty()){
//            new Thread(() -> {
//                InternationNewsHelper helper = new InternationNewsHelper();
//                dataList = helper.getNewsList();
//                //将数据存入数据库
//                newsDAO = new NewsDAO(getActivity());
//                newsDAO.insert(dataList);
//                //通知视图更新
//                message = new Message();
//                message.what = Constants.MESSAGE_WHAT_NEWS_LIST;
//                message.obj = dataList;
//                handler.sendMessage(message);
//            }).start();
//        }
//
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        view = inflater.inflate(R.layout.fragment_main_internation, container, false);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
//        //从数据库中查询数据并显示
//        adapter = new NewsAdapter(dataList);
//        //配置主页中的RecycleView
//        newsListRecycle = view.findViewById(R.id.list_recycleView_news_international);
//        newsListRecycle.setLayoutManager(layoutManager);
//        newsListRecycle.setAdapter(adapter);
//        return view;
//    }
}