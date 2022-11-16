package com.example.nchunews.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.youth.banner.adapter.BannerAdapter;

import java.util.List;

/**
 * 自定义图片轮播控件
 */
public class MyBannerImageAdapter extends BannerAdapter<String,MyBannerImageAdapter.BannerViewHolder> {

    /**
     * @param mData 图片链接列表
     */
    public MyBannerImageAdapter(List<String> mData) {
        super(mData);
    }

    public class BannerViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;

        public BannerViewHolder(ImageView itemView) {
            super(itemView);
            this.imageView = itemView;
        }
    }

    @Override
    public BannerViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        ImageView imageView = new ImageView(parent.getContext());
        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return new BannerViewHolder(imageView);
    }

    @Override
    public void onBindView(BannerViewHolder holder, String data, int position, int size) {

        //从网页上抓取图片
        Glide.with(holder.imageView.getContext()).load(data).into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"点击了"+position,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
