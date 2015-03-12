package com.manyi.mall.cachebean;

/**
 * Created by bestar on 2015/2/28.
 */
public class BaseResponse {
    String Code = "";
    String Message = "";

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
