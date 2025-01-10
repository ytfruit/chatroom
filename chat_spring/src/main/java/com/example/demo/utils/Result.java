package com.example.demo.utils;

import java.util.Map;

public class Result<T> {
    private String code;
    private String msg;
    private T data;
    private String token;


    // 返回数据
    public static <T> Result<T> build(T body, String code, String message) {
        Result<T> result = new Result<>();
        result.setData(body);
        result.setCode(code);
        result.setMsg(message);
        return result;
    }
    // 通过枚举构造Result对象
    public static <T> Result build(T body , ResultCodeEnum resultCodeEnum) {
        return build(body , resultCodeEnum.getCode() , resultCodeEnum.getMessage()) ;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Result() {
    }

    public Result(T data) {
        this.data = data;
    }

    public static Result success(Integer defaultRoomId) {
        Result result = new Result<>();
        result.setCode("200");
        result.setMsg("成功");
        return result;
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>(data);
        result.setCode("200");
        result.setMsg("成功");
        return result;
    }

    public static <T> Result<T> success(T data,String msg,String token) {
        Result<T> result = new Result<>(data);
        result.setCode("200");
        result.setMsg(msg);
        result.setToken(token);
        return result;
    }
    public static <T> Result<T> success(T data,String msg) {
        Result<T> result = new Result<>(data);
        result.setData(data);
        result.setCode("200");
        result.setMsg(msg);
        return result;
    }

    public static Result error(String code, String msg) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
}
