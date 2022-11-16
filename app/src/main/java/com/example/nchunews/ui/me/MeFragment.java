package com.example.nchunews.ui.me;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.nchunews.R;
import com.example.nchunews.adapter.CollectedArticleAdapter;
import com.example.nchunews.dao.NewsDAO;

import com.example.nchunews.databinding.FragmentMeBinding;
import com.example.nchunews.vo.OneNews;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class MeFragment extends Fragment implements View.OnClickListener {
    private MeViewModel meViewModel;
    private FragmentMeBinding binding;
    private NewsDAO newsDAO;
    private List<OneNews> collectedNewsList;
    private List<OneNews> commentedNews;
    private BottomSheetDialog collectDialog;
    private BottomSheetBehavior collectDialogBehavior;
    private BottomSheetDialog commentDialog;
    private BottomSheetBehavior commentDialogBehavior;
    private RecyclerView collectedRecycleView;
    private RecyclerView commentedRecycleView;
    private CollectedArticleAdapter collectedArticleAdapter;
    private LinearLayoutManager layoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        meViewModel = new ViewModelProvider(this).get(MeViewModel.class);
        binding = FragmentMeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        newsDAO = new NewsDAO(getActivity());
        collectedNewsList = new ArrayList<>();
        commentedNews = new ArrayList<>();
        layoutManager = new LinearLayoutManager(getActivity());

        meViewModel.getConfigString().observe(getViewLifecycleOwner(), strings -> {
            binding.userName.setText(strings.get(1));
            //设置背景磨砂效果
            Glide.with(getContext()).load(R.drawable.head)
                    .apply(RequestOptions.bitmapTransform(new BlurTransformation(1, 100)))
                    .centerCrop()
                    .into(binding.hBack);
            //设置圆形图像
            Glide.with(getContext()).load(R.drawable.head)
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.hHead);
            //设置基本的提示信息
            binding.meItemText.setText(strings.get(0));
            binding.meItemTextHint.setText(strings.get(1));
            binding.meItemText2.setText(strings.get(2));
            binding.meItemTextHint2.setText(strings.get(3));
            binding.meItemText3.setText(strings.get(4));
            binding.meItemTextHint3.setText(strings.get(5));
            binding.userName.setText(strings.get(6));
            binding.userVal.setText(strings.get(7));
        });

        //设置各个点击事件
        binding.hHead.setOnClickListener(this);
        binding.meItemTextHint.setOnClickListener(this);
        binding.meItemTextHint2.setOnClickListener(this);
        binding.meItemTextHint3.setOnClickListener(this);
        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.h_head:
                Toast.makeText(getActivity(),"点击更换头像",Toast.LENGTH_SHORT).show();
                break;
            case R.id.me_item_text_hint:
                updatePassword();
                break;
            case R.id.me_item_text_hint2:
                viewCollectedArticle();
                collectDialog.show();
                break;
            case R.id.me_item_text_hint3:
                viewCommentedArticle();
                commentDialog.show();
                break;
            case R.id.me_collect_close:
                collectDialog.dismiss();
                break;
            default:
        }
    }

    /**
     * 此处更新密码
     */
    private void updatePassword(){
        Toast.makeText(getActivity(),"你点击了更新密码",Toast.LENGTH_SHORT).show();

    }

    /**
     * 此处查看收藏文章
     */
    private void viewCollectedArticle(){
        View view = View.inflate(getActivity(), R.layout.bottom_sheet_dialog_collected_layout, null);
        collectDialog = new BottomSheetDialog(getActivity(),R.style.Theme_AppCompat);
        collectDialog.setContentView(view);
        collectedRecycleView = view.findViewById(R.id.me_collect_recycleList);//找到列表控件
        collectedNewsList = newsDAO.collectedArticle("11360");//获取用户用户收藏的文章
        collectedArticleAdapter = new CollectedArticleAdapter(collectedNewsList);//初始化适配器
        //设置布局管理器
        collectedRecycleView.setLayoutManager(layoutManager);
        collectedRecycleView.setAdapter(collectedArticleAdapter);
        //设置透明背景
        collectDialog.getWindow().setBackgroundDrawable(new ColorDrawable(255));
        collectDialogBehavior = BottomSheetBehavior.from((View) view.getParent());
        //设置高度
        collectDialogBehavior.setPeekHeight(650);
        collectDialogBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    collectDialog.dismiss();
                    collectDialogBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
            @Override
            public void onSlide(@NonNull @NotNull View bottomSheet, float slideOffset) {}
        });
    }

    /**
     * 此处查看评论的文章
     */
    private void viewCommentedArticle(){
        Toast.makeText(getActivity(),"你点击查看评论过的文章",Toast.LENGTH_SHORT).show();
        View view = View.inflate(getActivity(), R.layout.bottom_sheet_dialog_collected_layout, null);
        commentedNews = newsDAO.collectedArticle("11360");//获取用户用户收藏的文章
        commentDialog = new BottomSheetDialog(getActivity(),R.style.Theme_AppCompat);
        commentDialog.setContentView(view);
        //设置布局管理器
        commentedRecycleView = view.findViewById(R.id.me_commented_recycleList);
        commentedRecycleView.setLayoutManager(layoutManager);
        commentedRecycleView.setAdapter(collectedArticleAdapter);
        //设置透明背景
        commentDialog.getWindow().setBackgroundDrawable(new ColorDrawable(255));
        commentDialogBehavior = BottomSheetBehavior.from((View) view.getParent());
        //设置高度
        commentDialogBehavior.setPeekHeight(650);
        commentDialogBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    commentDialog.dismiss();
                    commentDialogBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
            @Override
            public void onSlide(@NonNull @NotNull View bottomSheet, float slideOffset) {}
        });
    }
    private void getCollectedArticle(){

        collectedArticleAdapter.notifyDataSetChanged();
    }

}