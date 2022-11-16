package com.example.nchunews.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.nchunews.mytools.MyTime;
import com.example.nchunews.vo.OneNews;

import java.util.ArrayList;
import java.util.List;

public class NewsDAO extends SQLiteOpenHelper implements INewsDAO {
    String DB_TAG =" newsDAO ";
    String CODE_CREATE_DATABASE_SUCCESS ="create database newsHot Successful ";
    SQLiteDatabase db;
    private String CREATE_NEWS_TABLE="create table news ( htmlurl text primary key, " +
            " title text default null , " +
            " author text default null, " +
            " content text default null," +
            " category integer default 0 ," +
            " islocal integer default 0) ";
    private String CREATE_COLLECTED_TABLE="create table collected ( htmlurl text, " +
            " userid text not null , " +
            " title text, " +
            " author text," +
            " time text ," +
            "primary key(htmlurl,userid)" +
            ") ";
    public NewsDAO(@Nullable Context context) {
        super(context, "hotNews.db", null, 1);
        Log.d(DB_TAG,CODE_CREATE_DATABASE_SUCCESS);
        this.db =getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_NEWS_TABLE);
            sqLiteDatabase.execSQL(CREATE_COLLECTED_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    /**
     * 插入新闻信息
     * @param data 新闻信息对象List
     */
    @Override
    public void insert(List<OneNews> data) {
        for(OneNews news:data){
            // a. 创建ContentValues对象
            ContentValues values = new ContentValues();
            values.put("htmlurl", news.getHtmlURL());
            values.put("title", news.getTitle());
            values.put("author",news.getSource());
            values.put("content",news.getContent());
            values.put("category",news.getCategory());
            values.put("islocal",news.getIsLocal());
            //存在就插入，不存在就更新
            db.replace("news",null,values);
        }
    }

    @Override
    public void insertOne(OneNews data) {
        ContentValues values = new ContentValues();
        values.put("htmlurl", data.getHtmlURL());
        values.put("title", data.getTitle());
        values.put("author",data.getSource());
        values.put("content",data.getContent());
        values.put("category",data.getCategory());
        values.put("islocal",data.getIsLocal());
        db.replace("news",null,values);
    }

    @Override
    public void update(String oldHtml, OneNews news) {

    }
    /**
     * 查询所有的新闻信息
     * @return
     */
    @Override
    public ArrayList<OneNews> queryAll(int category) {
        String cate = String.valueOf(category);
        String[] condition ={cate};
        Cursor cursor = db.query("news", null, "category=?",condition , null, null, null);
        ArrayList<OneNews> data = new ArrayList<>();
        while (cursor.moveToNext()) {
            OneNews news = new OneNews();
            news.setHtmlURL(cursor.getString(cursor.getColumnIndex("htmlurl")));
            news.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            news.setSource(cursor.getString(cursor.getColumnIndex("author")));
            news.setContent(cursor.getString(cursor.getColumnIndex("content")));
            data.add(news);
        }
        return data;
    }

    /**
     * 根据URL查找本地数据库中文章的内容
     * @param htmlURL
     * @return
     */
    @Override
    public OneNews queryByHtmlUrl(String htmlURL) {
        String[] args={htmlURL};
        Cursor cursor = db.query("news", null, "htmlurl=? and content is not null", args, null, null, null);
        if (cursor.getCount()==0) return null;
        else {
            cursor.moveToFirst();
            OneNews news = new OneNews();
            news.setHtmlURL(cursor.getString(cursor.getColumnIndex("htmlurl")));
            news.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            news.setSource(cursor.getString(cursor.getColumnIndex("author")));
            news.setContent(cursor.getString(cursor.getColumnIndex("content")));
            return news;
        }
    }

    /**
     * 执行 收藏文章信息
     * @param news 收藏的信息
     * @param userid 用户id
     */
    @Override
    public void collectArticle(OneNews news, String userid) {
        //保存收藏文章信息
        ContentValues values = new ContentValues();
        values.put("htmlurl",news.getHtmlURL());
        values.put("userid",userid);
        values.put("title",news.getTitle());
        values.put("author",news.getSource());
        values.put("time",MyTime.getUpdateTime());
        db.replace("collected",null,values);
    }

    /**
     * 查看评论过的文章
     * @param userId 用户ID
     * @return
     */
    @Override
    public List<OneNews> commentedArticle(String userId) {
        String[] args={userId};
        List<OneNews> newsList = new ArrayList<>();
        Cursor cursor = db.query("collected", null, "userid=?", args, null, null, null);
        if (cursor.getCount()==0) return null;
        else {
            while(cursor.moveToNext()){
                OneNews news = new OneNews();
                news.setHtmlURL(cursor.getString(cursor.getColumnIndex("htmlurl")));
                news.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                news.setSource(cursor.getString(cursor.getColumnIndex("author")));
                news.setUpdateTime(cursor.getString(cursor.getColumnIndex("time")));
                newsList.add(news);
            }
            return newsList;
        }
    }

    /**
     * 查询已经收藏过的信息
     * @param userId 用户ID
     * @return
     */
    @Override
    public List<OneNews> collectedArticle(String userId) {
        String[] args={userId};
        List<OneNews> newsList = new ArrayList<>();
        Cursor cursor = db.query("collected", null, "userid=?", args, null, null, null);
        if (cursor.getCount()==0){
            OneNews news = new OneNews();
            news.setTitle("您暂无收藏");
            news.setSource("");
            news.setUpdateTime("");
            newsList.add(news);
        } else {
            while(cursor.moveToNext()){
                OneNews news = new OneNews();
                news.setHtmlURL(cursor.getString(cursor.getColumnIndex("htmlurl")));
                news.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                news.setSource(cursor.getString(cursor.getColumnIndex("author")));
                news.setUpdateTime(cursor.getString(cursor.getColumnIndex("time")));
                newsList.add(news);
            }
        }
        return newsList;
    }
}
