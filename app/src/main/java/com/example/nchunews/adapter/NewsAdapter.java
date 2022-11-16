package com.example.nchunews.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nchunews.NewsInfoActivity;
import com.example.nchunews.R;
import com.example.nchunews.vo.OneNews;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private List<OneNews> data;


    public NewsAdapter(List<OneNews> data) {
        this.data = data;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView htmlUrl;
        TextView isLocal;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_title);
            htmlUrl = itemView.findViewById(R.id.item_source);
            isLocal = itemView.findViewById(R.id.item_isLocal);
        }
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(
                LayoutInflater.from(
                        parent.getContext()).inflate(R.layout.recycleview_item_one_news,
                        parent,
                        false)
        );
        return viewHolder;

    }
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        //滚动时传递位置给此处
        OneNews news = this.data.get(position);
        //为每个子项添加事件
        holder.htmlUrl.setText(news.getHtmlURL());
        //设置新闻内容的htmlURL不可见，仅仅用于存储数据
        holder.htmlUrl.setVisibility(View.GONE);
        holder.title.setText(news.getTitle());
        System.out.println("NewAdapter:"+news.getTitle()+"::在本地？::"+news.getIsLocal());
        holder.title.setOnClickListener(view -> {
            //获取网址
            String url = (String) holder.htmlUrl.getText();
            Intent intent = new Intent(view.getContext(), NewsInfoActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("htmlURL",url);
            intent.putExtra("URL",bundle);
            view.getContext().startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
