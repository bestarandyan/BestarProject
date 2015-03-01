package com.manyi.mall.cachebean.user;

public class LoginResponse{
    String id;
    String Type;
    String UserName;
    String RealName;
    String AppKey;
    String Message;
    String Code;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getRealName() {
        return RealName;
    }

    public void setRealName(String realName) {
        RealName = realName;
    }

    public String getAppKey() {
        return AppKey;
    }

    public void setAppKey(String appKey) {
        AppKey = appKey;
    }
}
