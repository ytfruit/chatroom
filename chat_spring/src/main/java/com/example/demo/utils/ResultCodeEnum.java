package com.example.demo.utils;

import lombok.Getter;

@Getter
public enum ResultCodeEnum {
    SUCCESS("200" , "操作成功") ,
    SYSTEM_ERROR("9999" , "您的网络有问题请稍后重试"),
    ;

    private String code ;      // 业务状态码
    private String message ;    // 响应消息

    private ResultCodeEnum(String code , String message) {
        this.code = code ;
        this.message = message ;
    }
}
