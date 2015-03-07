package com.manyi.mall.cachebean.user;

/**
 * Created by bestar on 2015/3/7.
 */
public class UserInfoBean {
    public static UserInfoBean userInfoBean = null;
    public static UserInfoBean getInstance(){
        if (userInfoBean == null){
            return new UserInfoBean();
        }
        return null;
    }
    public UserInfoBean(){

    }
}
