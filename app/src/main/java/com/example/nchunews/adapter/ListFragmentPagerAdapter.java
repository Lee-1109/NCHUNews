package com.example.nchunews.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;
//用于页面切换与适配Fragment
public class ListFragmentPagerAdapter extends FragmentPagerAdapter {
    List<Fragment> fragments;

    public ListFragmentPagerAdapter(@NonNull @NotNull FragmentManager fm,List<Fragment> fragments) {
        super(fm,FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.fragments = fragments;
    }

    @NonNull
    @NotNull
    @Override
    public Fragment getItem(int position) {
        //返回指定的Fragment
        return this.fragments.get(position);
    }

    @Override
    public int getCount() {
        //对页面个数计数
        return this.fragments.size();
    }
}
