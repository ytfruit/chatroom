package com.example.demo.controller.file;

import com.example.demo.utils.ResultCodeEnum;
import lombok.Data;

@Data
public class MinioException extends RuntimeException{

    private String code;
    private String message;
    private ResultCodeEnum resultCodeEnum;

    public MinioException(ResultCodeEnum resultCodeEnum) {
        this.resultCodeEnum = resultCodeEnum;
        this.code = resultCodeEnum.getCode();
        this.message = resultCodeEnum.getMessage();
    }
}
