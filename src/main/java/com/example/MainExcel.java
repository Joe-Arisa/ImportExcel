package com.example;

import com.alibaba.excel.EasyExcel;
import com.example.domain.User;
import com.example.listener.UserDataListener;

public class MainExcel {
    public static void main(String[] args) {

        //建议使用绝对地址
        String fileName = "/Users/arisa/Desktop/userExcel.xlsx";
        // 这里默认读取第一个sheet
        EasyExcel.read(fileName, User.class, new UserDataListener()).sheet().doRead();
    }
}
