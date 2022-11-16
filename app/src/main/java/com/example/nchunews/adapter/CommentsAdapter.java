package com.example.nchunews.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nchunews.R;
import com.example.nchunews.vo.Comment;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> implements View.OnClickListener {
    List<Comment> data;
    Button up;
    Button down;
    public CommentsAdapter(List<Comment> data) {
        this.data = data;
    }

    @Override
    public void onClick(View view) {
        up = view.findViewById(R.id.comment_item_up);
        down = view.findViewById(R.id.comment_item_down);
        switch (view.getId()){
            case R.id.comment_item_up:
                if(up.isSelected()){//已经点赞 取消点赞
                    up.setBackgroundResource(R.drawable.ic_item_up_black16);
                    up.setSelected(false);
                }else {//没有点赞 设置点赞效果
                    up.setBackgroundResource(R.drawable.ic_item_up_red16);
                    if(down.isSelected())//如果有点踩 就将点踩取消
                    down.setBackgroundResource(R.drawable.ic_item_down_black16);
                    up.setSelected(true);
               }

                break;
            case R.id.comment_item_down:
                if(down.isSelected()){//已经点踩 取消点踩
                    up.setBackgroundResource(R.drawable.ic_item_down_black16);
                    up.setSelected(false);
                }else {//没有点踩 设置点踩效果
                    up.setBackgroundResource(R.drawable.ic_item_down_red16);
                    if(up.isSelected())//如果同时有点赞 就取消点赞
                        up.setBackgroundResource(R.drawable.ic_item_up_black16);
                    up.setSelected(true);
                }
                Toast.makeText(view.getContext(),"你不赞成了",Toast.LENGTH_SHORT).show();
                break;
            default:
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        TextView comment;
        ImageView up;
        ImageView down;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.comment_item_username);
            comment = itemView.findViewById(R.id.comment_item_comments);
            up = itemView.findViewById(R.id.comment_item_up);
            down = itemView.findViewById(R.id.comment_item_down);
        }
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(
                LayoutInflater.from(
                        parent.getContext()).inflate(R.layout.recycleview_item_one_comments,
                        parent,
                        false)
        );
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Comment comment = data.get(position);
        holder.userName.setText(comment.getUserName());
        holder.comment.setText(comment.getComment());
        holder.up.setOnClickListener(this);
        holder.down.setOnClickListener(this);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


}
