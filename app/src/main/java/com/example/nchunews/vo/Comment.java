package com.example.nchunews.vo;

import cn.bmob.v3.BmobObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends BmobObject {
    private String htmlUrl;//文章网址页面 -- 唯一标识
    private String userid;//用户账号 --唯一标识
    private String userName;//用户名
    private String comment;//用户的评论
    private String commentTime;//用户评论时间
    private int agree;//赞同数量
    private int disAgree;//不赞同数量
    public Comment(String htmlUrl,String userName, String comment){
        this.htmlUrl = htmlUrl;
        this.userName = userName;
        this.comment = comment;
    }
}
