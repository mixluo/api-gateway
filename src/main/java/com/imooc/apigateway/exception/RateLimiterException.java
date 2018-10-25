package com.imooc.apigateway.exception;

import com.imooc.apigateway.enums.ResultEnum;

public class RateLimiterException extends RuntimeException{
    private Integer code;
    private String message;

    public RateLimiterException(Integer code, String message) {
        super(message);
        this.code = code;
    }
    public RateLimiterException(ResultEnum resultEnum){
        super(resultEnum.getMessage());
        this.code = code;
    }
}
