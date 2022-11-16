package com.example.nchunews.dao;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.nchunews.LoginActivity;
import com.example.nchunews.mytools.Constants;
import com.example.nchunews.vo.User;
import com.example.nchunews.vo.User2;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import lombok.Getter;
import lombok.Setter;

public class UserDAO extends SaveListener<String> implements IUserDAO {
    boolean querySuccess = true;
    @Setter
    @Getter
    class MyFindListener extends FindListener<User>{
        boolean success = true;
        @Override
        public void done(List<User> list, BmobException e) {
            if (e == null && list.size()>0){
            }else{
                this.success = false;
                Log.d("BmobQuery","查询失败,不存在此数据");
            }
        }
    }
    /**
     * 向User表插入数据
     * @param user
     */
    @Override
    public boolean register(User user){
        user.save(this);
        return true;
    }

    /**
     * 使用Bmob提供的用户系统完成注册
     * @param user2
     * @param
     */
    public void register2(User2 user2, Activity activity){
        user2.signUp(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                   Toast.makeText(activity, "注册成功", Toast.LENGTH_LONG).show();
                    AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
                    dialog.setTitle("是否现在去登陆？")
                            .setCancelable(false);
                    dialog.setPositiveButton("确定", (dialogInterface, i) -> {
                        Intent intent = new Intent(activity, LoginActivity.class);
                        intent.putExtra("registerUser",new Gson().toJson(user2));
                        activity.setResult(Constants.REGISTER_REQUEST_LOGIN,intent);
                        activity.finish();
                    });
                    //点击取消不做任何操作
                    dialog.setNegativeButton("稍后", (dialogInterface, i) -> {
                    });
                    dialog.show();
                } else {
                    System.out.println("注册失败！");
                        Toast.makeText(activity, "注册失败" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean login(User user) {
        MyFindListener listener = new MyFindListener();
        //条件查询
        System.out.println(user.toString());
        //测试用语句
        BmobQuery<User> queryAccount=new BmobQuery<>();//查询账号

        queryAccount.addWhereEqualTo("userid",user.getUserid());
        BmobQuery<User> queryPassword=new BmobQuery<>();//查询密码
        queryPassword.addWhereEqualTo("password",user.getPassword());
        //联合查询
        List<BmobQuery<User>> queries=new ArrayList<>();
        queries.add(queryAccount);
        queries.add(queryPassword);
        //主查询
        BmobQuery<User> mainQuery=new BmobQuery<>();
        mainQuery.and(queries);
        mainQuery.findObjects(listener);
        if(listener.isSuccess()) return true;
        else return false;
//        querySuccess = listener.isSuccess();
//        return querySuccess;
    }

    public void login2(User2 user2,final View view){

        user2.login(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if(e== null){//登陆成功 获取当前的用户
                    Toast.makeText(view.getContext(), "登陆成功",Toast.LENGTH_SHORT).show();
                    System.out.println(user2.toString());
                    User2 nowUser = BmobUser.getCurrentUser(User2.class);
                }else {
                    System.out.println("登陆失败");
                }
            }
        });
    }


    @Override
    public void done(String s, BmobException e) {
        if (e == null){
            Log.d("Register success","注册成功");
        }else {
            Log.d("Register error","注册失败");
        }
    }
}
