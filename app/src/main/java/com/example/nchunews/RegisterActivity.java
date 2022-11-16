package com.example.nchunews;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.nchunews.dao.UserDAO;
import com.example.nchunews.databinding.ActivityRegisterBinding;
import com.example.nchunews.mytools.Constants;
import com.example.nchunews.vo.User;
import com.example.nchunews.vo.User2;
import com.google.gson.Gson;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityRegisterBinding binding;
    UserDAO userDAO;//用来注册用户
    User user;//用来接收值
    AlertDialog.Builder dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = getLayoutInflater();
        binding = ActivityRegisterBinding.inflate(inflater);
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dialog= new AlertDialog.Builder(this);
        binding.doRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //点击注册按钮
            case R.id.doRegister:
                String name =  binding.rAccount.getText().toString();
                String password = binding.rPassword.getText().toString();
                User2 user2 = new User2();
                user2.setUsername(name);
                user2.setPassword(password);
                UserDAO userDAO = new UserDAO();
                userDAO.register2(user2,this);
//                this.user = new User(name,password);
//                this.userDAO = new UserDAO();
//                //云端返回数据成功
//                if(userDAO.register(user)){
//                    Toast.makeText(getApplicationContext(),"注册成功",Toast.LENGTH_SHORT).show();
//                    dialog.setTitle("是否现在去登陆？")
//                    .setCancelable(false);
//                    dialog.setPositiveButton("确定", (dialogInterface, i) -> {
//                        Intent intent = new Intent();
//                        intent.putExtra("registerUser",new Gson().toJson(user));
//                        setResult(Constants.REGISTER_REQUEST_LOGIN,intent);
//                        finish();
//                    });
//                    //点击取消不做任何操作
//                    dialog.setNegativeButton("稍后", (dialogInterface, i) -> {
//                    });
//                    dialog.show();
//                }
//                //云端返回数据失败
//                else {
//                    Toast.makeText(getApplicationContext(),"账号已存在",Toast.LENGTH_SHORT).show();
//                }
                break;
            default:
        }
    }
}