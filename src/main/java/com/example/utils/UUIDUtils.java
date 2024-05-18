package com.example.utils;

import java.util.UUID;

public class UUIDUtils {

    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    // 如果需要去除UUID中的短横线（"-"），可以使用以下方法：
    public static String generateUUIDWithoutHyphens() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
