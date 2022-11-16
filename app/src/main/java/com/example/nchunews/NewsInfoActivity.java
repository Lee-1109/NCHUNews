package com.example.nchunews;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nchunews.adapter.CommentsAdapter;
import com.example.nchunews.adapter.MyBannerImageAdapter;
import com.example.nchunews.dao.CommentsDAO;
import com.example.nchunews.dao.NewsDAO;

import com.example.nchunews.databinding.ActivityNewsInfoBinding;
import com.example.nchunews.jsoup.CommonJsoupHelper;
import com.example.nchunews.mytools.Constants;
import com.example.nchunews.mytools.MyTime;
import com.example.nchunews.vo.Comment;
import com.example.nchunews.vo.OneNews;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.listener.OnPageChangeListener;
import com.youth.banner.transformer.ScaleInTransformer;
import com.youth.banner.util.BannerUtils;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
@SuppressWarnings({"all", "unchecked"})
public class NewsInfoActivity extends BaseActivity implements View.OnClickListener, OnPageChangeListener {
    private ActivityNewsInfoBinding binding;
    private String url;
    private OneNews news;
    private MyHandler handler = new MyHandler(Looper.myLooper(),this);
    //获取Looper并传递 消息处理机制
    private List<String> imageUrlList = new ArrayList<>();
    private NewsDAO newsDAO;
    private CommentsDAO commentsDAO;
    private TextView closeComment;
    private RecyclerView commentRecycle;
    private CommonJsoupHelper helper;
    private CommentsAdapter commentsAdapter;
    private List<Comment> commentList = new ArrayList<>();
    private BottomSheetDialog bottomSheetDialog;
    private BottomSheetBehavior mDialogBehavior;
    private MyBannerImageAdapter bannerImageAdapter;
    /**
     * 自定义Handler类
     * 避免handler内存泄露
     */
    static class MyHandler extends Handler{
        WeakReference<NewsInfoActivity> myActivity;
        //构造函数，传来的是外部类的this
        public MyHandler(@NonNull Looper looper, NewsInfoActivity activity){
            super(looper);//调用父类的显式指明的构造函数
            myActivity = new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            NewsInfoActivity activity= myActivity.get();
            if(activity == null)
                return ;//activity都没了还处理个XXX
            switch (msg.what) {
                case Constants.MESSAGE_WHAT_REQUIRE_ARTICLE_SUCCESS:
                    //在这里通过activity引用外部类
                    OneNews news = (OneNews) msg.obj;
                    activity.binding.oneNewsTitle.setText(news.getTitle());
                    activity.binding.oneNewsSource.setText(news.getSource());
                    activity.binding.oneNewsContent.setText(news.getContent());
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 用来在主线程中设置Banner图片
     */
    Runnable setBannerImages = new Runnable() {
        @Override
        public void run() {
            bannerImageAdapter.setDatas(imageUrlList);
            bannerImageAdapter.notifyDataSetChanged();
        }
    };
    /**
     * 用于在子线程中通过网络请求图片
     */
    Runnable requireImages = new Runnable() {
        @Override
        public void run() {
            List<String> tempImage = helper.getImageUrlByArticle(url);
            for(String one:tempImage){
                imageUrlList.add(one);
            }
            handler.post(setBannerImages);
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //发送评论
            case R.id.comment_send_button:
                sendComments();
                break;
            case R.id.info_show_comment_dialog:
                prepareCommentSheetDialog();
                bottomSheetDialog.show();
                break;
                //收藏
            case R.id.info_show_comment_collect:
                saveCollector();
                Toast.makeText(this,"收藏成功",Toast.LENGTH_SHORT).show();
                break;
            case R.id.info_comment_close:
                bottomSheetDialog.dismiss();//关闭弹出评论框
            default:
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater layoutInflater = getLayoutInflater();
        binding = ActivityNewsInfoBinding.inflate(layoutInflater);
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //获取文章url
        url = (String) getIntent().getBundleExtra("URL").get("htmlURL");
        newsDAO = new NewsDAO(this);
        commentsDAO = new CommentsDAO(this);
        helper = new CommonJsoupHelper();
        news = new OneNews();
        bannerImageAdapter = new MyBannerImageAdapter(imageUrlList);
        threadPool.execute(requireImages);
        //初始化数据库
        int isExist = (null == newsDAO.queryByHtmlUrl(url)?0:1);
        switch (isExist){
            case 0:
                Runnable requireArticle = () -> {
                    CommonJsoupHelper jsoup = new CommonJsoupHelper();
                    Message message = new Message();
                    message.what = Constants.MESSAGE_WHAT_REQUIRE_ARTICLE_SUCCESS;
                    news = jsoup.getNewsContentByURL(url);
                    List<String> tempImage = jsoup.getImageUrlByArticle(url);
                    for(String one:tempImage){
                        imageUrlList.add(one);
                    }
                    newsDAO.insertOne(news);
                    message.obj = news;
                    handler.sendMessage(message);
                };
                threadPool.execute(requireArticle);
                Toast.makeText(this,"内容来源：网络页面",Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(this,"内容来源本地数据库",Toast.LENGTH_SHORT).show();
                this.news = newsDAO.queryByHtmlUrl(url);
                binding.oneNewsTitle.setText(news.getTitle());
                binding.oneNewsSource.setText(news.getSource());
                binding.oneNewsContent.setText(news.getContent());
                break;
            default:
        }
        //Banner的初始化
        binding.oneNewsBanner.setAdapter(bannerImageAdapter)
                .isAutoLoop(true)
                .setCurrentItem(1,false)
                .addBannerLifecycleObserver(this)//添加生命周期观察者
                .setBannerRound(BannerUtils.dp2px(35))//圆角
                .addPageTransformer(new ScaleInTransformer())//添加切换效果
                .setIndicator(new CircleIndicator(this))//设置指示器
                .setIndicatorSelectedColor(Color.BLACK);
        binding.oneNewsBanner.start();//自动联播
        //当点击查看评论按钮时
        binding.infoShowCommentDialog.setOnClickListener(this);
        binding.infoShowCommentCollect.setOnClickListener(this);
        binding.commentSendButton.setOnClickListener(this);
    }

    /**
     * 准备评论划出框
     */
    private void prepareCommentSheetDialog(){
        //加载弹出框的基本布局
        View view = View.inflate(NewsInfoActivity.this, R.layout.bottom_sheet_dialog_comment_layout, null);
        //关闭评论按钮
        closeComment =  view.findViewById(R.id.info_comment_close);
        //评论列表
        commentRecycle = view.findViewById(R.id.info_comment_recycleList);
        closeComment.setOnClickListener(this);//关闭评论按钮的监听
        commentList = commentsDAO.findAllCommentsByArticle(url);
        commentsAdapter = new CommentsAdapter(commentList);
        commentRecycle.setHasFixedSize(true);
        commentRecycle.setLayoutManager(new LinearLayoutManager(NewsInfoActivity.this));
        commentRecycle.setItemAnimator(new DefaultItemAnimator());
        commentRecycle.setAdapter(commentsAdapter);
        //设置弹出框的样式
        bottomSheetDialog = new BottomSheetDialog(NewsInfoActivity.this, R.style.Theme_AppCompat);
        bottomSheetDialog.setContentView(view);
        //设置透明背景
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(255));
        mDialogBehavior = BottomSheetBehavior.from((View) view.getParent());
        //设置高度
        mDialogBehavior.setPeekHeight(650);
        mDialogBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetDialog.dismiss();
                    mDialogBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
            @Override
            public void onSlide(@NonNull @NotNull View bottomSheet, float slideOffset) {}
        });
    }


    private void sendComments(){
        String comm = binding.editComment.getText().toString();
        if(comm.isEmpty()){
            Toast.makeText(this,"评论不能为空",Toast.LENGTH_SHORT).show();
        }else {
            //将评论插入数据库
            Comment comment = new Comment();
            comment.setHtmlUrl(url);//设置文章URL
            comment.setComment(comm);//设置文章内容
            comment.setUserName("11360");//设置用户名
            comment.setUserid("11360");
            comment.setCommentTime(MyTime.getUpdateTime());
            commentsDAO.insertOne(comment);
            commentList = commentsDAO.findAllCommentsByArticle(url);
            binding.editComment.setText("");
            Toast.makeText(this,"评论成功",Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * 点击收藏执行该方法
     */
    private void saveCollector(){
        newsDAO.collectArticle(news,"11360");
    }
}