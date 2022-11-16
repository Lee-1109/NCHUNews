package com.example.nchunews;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.nchunews.mytools.Constants;
import com.example.nchunews.mytools.MyThreadPool;

import cn.bmob.v3.Bmob;

public class BaseActivity extends AppCompatActivity {
    protected MyThreadPool threadPool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        threadPool = new MyThreadPool();
        //注册引入Bmob初始化
        Bmob.initialize(this, Constants.APP_KEY);

    }
}