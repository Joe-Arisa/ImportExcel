package com.example.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class User {

    private String userId;

    @ExcelProperty("username")
    private String userName;
}
