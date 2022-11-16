package com.example.nchunews.vo;

import cn.bmob.v3.BmobObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class User extends BmobObject {
    private String userid;
    private String username;
    private String password;
    public User(String userid,String password){
        this.userid = userid;
        this.password = password;
    }
}
