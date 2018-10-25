package com.imooc.apigateway.enums;

import lombok.Getter;

@Getter
public enum ResultEnum {
    AUTH_ERROR(1001,"令牌获取失败"),
    ;
    private Integer code;
    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
