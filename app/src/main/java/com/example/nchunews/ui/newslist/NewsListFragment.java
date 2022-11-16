package com.example.nchunews.ui.newslist;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.nchunews.R;
import com.example.nchunews.adapter.ListFragmentPagerAdapter;
import com.example.nchunews.databinding.FragmentNewsListBinding;
import com.example.nchunews.ui.newslist.subfragments.MainFragmentHot;
import com.example.nchunews.ui.newslist.subfragments.MainFragmentInternation;
import com.example.nchunews.ui.newslist.subfragments.MainFragmentAir;

import java.util.ArrayList;
import java.util.List;

public class NewsListFragment extends Fragment implements View.OnClickListener {

    private NewsListModel newsListModel;
    private FragmentNewsListBinding binding;//本fragment的视图绑定
    List<Fragment> fragments;
    ListFragmentPagerAdapter adapter;

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.tv_item_Hot:
                setColorByPosition(0,Color.RED,Color.WHITE,Color.WHITE);
                break;
            case R.id.tv_item_war:
                setColorByPosition(1,Color.WHITE,Color.RED,Color.WHITE);
                break;
            case R.id.tv_item_air:
                setColorByPosition(2,Color.WHITE,Color.WHITE,Color.RED);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
    }
    //设置一个ViewPager的侦听事件，当左右滑动ViewPager时菜单栏被选中状态跟着改变
    public class MyPagerChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }
        @Override
        public void onPageSelected(int arg0) {
            switch (arg0) {
                case 0:
                    setColor(Color.RED,Color.WHITE,Color.WHITE);
                    break;
                case 1:
                    setColor(Color.WHITE,Color.RED,Color.WHITE);
                    break;
                case 2:
                    setColor(Color.WHITE,Color.WHITE,Color.RED);
                    break;
                default:
            }
        }
    }
    //创建视图时使用
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        newsListModel = new ViewModelProvider(this).get(NewsListModel.class);
        binding = FragmentNewsListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.tvItemHot.setOnClickListener(this);
        binding.tvItemWar.setOnClickListener(this);
        binding.tvItemAir.setOnClickListener(this);
        binding.listViewPager.addOnPageChangeListener(new MyPagerChangeListener());
        fragments = new ArrayList<>();
        fragments.add(new MainFragmentHot());
        fragments.add(new MainFragmentInternation());
        fragments.add(new MainFragmentAir());
        adapter = new ListFragmentPagerAdapter(getActivity().getSupportFragmentManager(),fragments);
        binding.listViewPager.setAdapter(adapter);
        //设置初始页面
        binding.listViewPager.setCurrentItem(0);
        binding.tvItemHot.setBackgroundColor(Color.RED);

        newsListModel.getText().observe(getViewLifecycleOwner(), s -> {
            binding.tvItemHot.setText("热点");
            binding.tvItemWar.setText("国际");
            binding.tvItemAir.setText("航空");
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * 设置颜色方法
     * @param position 当前点击的上方TextView位置
     * @param color1 第一个的颜色
     * @param color2 第二个色块的颜色
     * @param color3 第三个色块的颜色
     */
    private void setColorByPosition(int position,int color1,int color2,int color3){
        binding.listViewPager.setCurrentItem(position);
        binding.tvItemHot.setBackgroundColor(color1);
        binding.tvItemWar.setBackgroundColor(color2);
        binding.tvItemAir.setBackgroundColor(color3);
    }
    private void setColor(int color1,int color2,int color3){
        binding.tvItemHot.setBackgroundColor(color1);
        binding.tvItemWar.setBackgroundColor(color2);
        binding.tvItemAir.setBackgroundColor(color3);
    }
}