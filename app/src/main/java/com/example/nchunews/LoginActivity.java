package com.example.nchunews;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.nchunews.databinding.ActivityLoginBinding;
import com.example.nchunews.mytools.Constants;
import com.example.nchunews.vo.User;
import com.example.nchunews.vo.User2;
import com.google.gson.Gson;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = getLayoutInflater();
        binding = ActivityLoginBinding.inflate(inflater);
        setContentView(binding.getRoot());
        binding.userRegister.setOnClickListener(this);
        binding.tvLogin.setOnClickListener(this);
    }

    /**
     * 根据返回的结果来进行不同操作
     * @param requestCode 请求码
     * @param resultCode 返回码
     * @param data 数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //注册成功 并且要求登陆
        if(requestCode == Constants.REQUEST_REGISTER && resultCode == Constants.REGISTER_REQUEST_LOGIN){
            String jsonData = data.getStringExtra("registerUser");
            User2 user = new Gson().fromJson(jsonData,User2.class);
            binding.account.setText(user.getUsername());
            binding.password.setText("请输入密码");
        }
        //登陆成功
        else if(requestCode == Constants.REQUEST_LOGIN && resultCode == Constants.LOGIN_SUCCESS){
            Intent intent = new Intent(this,NewsActivity.class);
            startActivity(intent);
        }else {
            //不存在 跳转到登陆
            Intent intent = new Intent(this,RegisterActivity.class);
            startActivity(intent);
            Toast.makeText(getApplicationContext(),"不存在该用户，请您注册",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //点击注册
            case R.id.userRegister:
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                //startActivityForResult(intent, Constants.REQUEST_REGISTER);
                startActivity(intent);
                break;
            //点击登陆
            case R.id.tv_login:
                Intent intentLogin = new Intent(LoginActivity.this, DoLoginActivity.class);
                String userid = binding.account.getText().toString();
                String password = binding.password.getText().toString();
                User user = new User(userid,password);
                intentLogin.putExtra(Constants.CODE_LOGIN,new Gson().toJson(user));
                startActivityForResult(intentLogin, Constants.REQUEST_LOGIN);
                break;
            default:
        }

    }
}