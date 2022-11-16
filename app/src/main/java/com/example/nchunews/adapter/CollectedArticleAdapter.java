package com.example.nchunews.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nchunews.NewsActivity;
import com.example.nchunews.NewsInfoActivity;
import com.example.nchunews.R;
import com.example.nchunews.mytools.MyTime;
import com.example.nchunews.vo.OneNews;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * 收藏文章的适配器类
 */
public class CollectedArticleAdapter extends  RecyclerView.Adapter<CollectedArticleAdapter.ViewHolder> {
    private List<OneNews> news;

    public CollectedArticleAdapter(List<OneNews> newList){
        this.news = newList;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView author;
        TextView collectedTime;
        TextView htmlurl;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.collect_item_title);
            author = itemView.findViewById(R.id.collect_item_author);
            collectedTime = itemView.findViewById(R.id.collect_time);
            htmlurl = itemView.findViewById(R.id.collect_item_htmlurl);
        }
    }
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(
                LayoutInflater.from(
                        parent.getContext()).inflate(R.layout.recycleview_item_one_collect_article,
                        parent, false)
        );
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
            OneNews tempNews = news.get(position);
            holder.title.setText(tempNews.getTitle());
            holder.htmlurl.setText(tempNews.getHtmlURL());
            holder.author.setText(tempNews.getSource());
            holder.collectedTime.setText(tempNews.getUpdateTime());
            holder.title.setOnClickListener(view -> {
                Intent intent = new Intent(view.getContext(), NewsInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("htmlURL",holder.htmlurl.getText().toString());
                intent.putExtra("URL",bundle);
                view.getContext().startActivity(intent);
            });
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

}
