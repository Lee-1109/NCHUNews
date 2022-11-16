package com.example.nchunews.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.nchunews.mytools.Constants;
import com.example.nchunews.mytools.MyTime;
import com.example.nchunews.vo.Comment;
import com.example.nchunews.vo.OneNews;
import com.example.nchunews.vo.User;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于本地保存评论
 * 避免多次请求网络
 */
public class CommentsDAO extends SQLiteOpenHelper implements ICommentDAO {
    String DB_TAG =" CommentDAO ";
    SQLiteDatabase db;
    ContentValues values = new ContentValues();
    /**
     * 用户评论表
     */
    private String CREATE_COMMENT_TABLE="create table comments ( " +
            " htmlurl text , " +
            " userid text," +
            " username text , " +
            " comment text default null , " +
            " time text default null, " +
            " agree integer default 0, " +
            " disagree integer default 0," +
            " primary key(htmlurl,userid) " +
            " ) ";

    public CommentsDAO(@Nullable Context context) {
        super(context, "comments.db", null, 1);
        Log.d(DB_TAG, Constants.CODE_CREATE_DATABASE_SUCCESS);
        this.db =getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_COMMENT_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    @Override
    public void insertOne(Comment comment) {
        values.clear();
        values.put("htmlurl",comment.getHtmlUrl());
        values.put("userid",comment.getUserid());
        values.put("username",comment.getUserName());
        values.put("comment",comment.getComment());
        values.put("time", MyTime.getUpdateTime());
        values.put("agree",comment.getComment());
        values.put("disagree",comment.getDisAgree());
        db.replace("comments",null,values);
    }

    @Override
    public void update(String htmlUrl, User user, Comment comment) {
        values.clear();
        String[] args={comment.getHtmlUrl(),user.getUserid()};
        values.put("comment",comment.getComment());
        db.update("comments",values,"htmlurl=? and userid=?",args);
    }

    @Override
    public void deleteOne(Comment comment) {
        String[] args = {comment.getHtmlUrl(),comment.getUserid()};
        values.clear();
        db.delete("comments","htmlurl=? and userid=?",args);
    }

    @Override
    public void deleteByHtmlUrl(String HtmlUrl) {
        String[] args={HtmlUrl};
        values.clear();
        db.delete("comments","htmlurl=?",args);
    }

    @Override
    public List<Comment> findAllCommentsNewsByUserId(String userid) {
        String[] args ={userid};
        List<Comment> comments =new ArrayList();
        Cursor cursor = db.query("comments",null,"userid=?",args,null,null,null);
        while (cursor.moveToNext()){
            Comment comment = new Comment();
            comment.setUserid(cursor.getString(cursor.getColumnIndex("userid")));
            comment.setUserName(cursor.getString(cursor.getColumnIndex("username")));
            comment.setComment(cursor.getString(cursor.getColumnIndex("comment")));
            comment.setCommentTime(cursor.getString(cursor.getColumnIndex("time")));
            comment.setAgree(cursor.getInt(cursor.getColumnIndex("agree")));
            comment.setDisAgree(cursor.getInt(cursor.getColumnIndex("disagree")));
            comments.add(comment);
        }
        return comments;
    }

    @Override
    public List<Comment> findAllCommentsByArticle(String htmlUrl) {
        String[] args ={htmlUrl};
        List<Comment> comments = new ArrayList();
        Cursor cursor = db.query("comments",null,"htmlurl=?",args,null,null,null);
        while (cursor.moveToNext()){
            Comment comment = new Comment();
            comment.setUserid(cursor.getString(cursor.getColumnIndex("userid")));
            comment.setUserName(cursor.getString(cursor.getColumnIndex("username")));
            comment.setComment(cursor.getString(cursor.getColumnIndex("comment")));
            comment.setCommentTime(cursor.getString(cursor.getColumnIndex("time")));
            comment.setAgree(cursor.getInt(cursor.getColumnIndex("agree")));
            comment.setDisAgree(cursor.getInt(cursor.getColumnIndex("disagree")));
            comments.add(comment);
        }
        if (comments.isEmpty()){
            Comment comment = new Comment();
            comment.setUserName(null);
            comment.setComment("暂无评论");
            comments.add(comment);
        }
        return comments;
    }
}
