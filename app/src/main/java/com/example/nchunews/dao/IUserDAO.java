package com.example.nchunews.dao;

import com.example.nchunews.vo.User;

public interface IUserDAO {
    /**
     * 注册
     * @param user 用户信息
     * @return 注册成功true 失败false
     */
    boolean register(User user);
    boolean login(User user);
}
