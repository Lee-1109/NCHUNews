package com.example.nchunews;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.nchunews.dao.UserDAO;
import com.example.nchunews.mytools.Constants;
import com.example.nchunews.vo.User;
import com.google.gson.Gson;

public class DoLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserDAO userDAO = new UserDAO();
        Intent intent = getIntent();
        String jsonData = intent.getStringExtra(Constants.CODE_LOGIN);
        User user = new Gson().fromJson(jsonData,User.class);
        if(userDAO.login(user)){
            setResult(Constants.LOGIN_SUCCESS);
        }else {
            setResult(Constants.LOGIN_ERROR);
        }
        finish();
    }
}