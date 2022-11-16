package com.example.nchunews.layout;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.nchunews.R;
import com.example.nchunews.databinding.MeSubItemBinding;

import lombok.Getter;
import lombok.Setter;

/**
 * 我页面的子布局
 * 一定要实现三个构造方法 不然会报错
 */
@Getter
@Setter
public class MeSubLayout extends RelativeLayout {
    private TextView itemString;
    private TextView itemHint;
    private MeSubItemBinding binding;

    public MeSubLayout(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.me_sub_item,this);
        itemString = binding.meItemText;
        itemHint = binding.meItemTextHint;
    }
    public MeSubLayout(Context context, AttributeSet attrs) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.me_sub_item,this);
        itemString = binding.meItemText;
        itemHint = binding.meItemTextHint;
    }
    public MeSubLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.me_sub_item,this);
        itemString = binding.meItemText;
        itemHint = binding.meItemTextHint;
    }

}
