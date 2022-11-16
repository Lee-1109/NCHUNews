package com.example.nchunews.mytools;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class MyTime {
    public static String getUpdateTime(){
        SimpleDateFormat sdfTwo =new SimpleDateFormat("MM月dd日HH时mm分", Locale.getDefault());
        return sdfTwo.format(System.currentTimeMillis());
    }

}
