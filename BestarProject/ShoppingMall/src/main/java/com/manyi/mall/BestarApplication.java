package com.manyi.mall;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EApplication;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.baidu.frontia.FrontiaApplication;
import com.huoqiu.framework.app.AppConfig;
import com.manyi.mall.common.util.DBUtil;
public class BestarApplication extends FrontiaApplication {
	private static BestarApplication application = null;

	public static BestarApplication getInstance() {
        if (application == null){
            application = new BestarApplication();
        }
		return application;
	}

    @Override
    public void onCreate() {
        super.onCreate();

    }
    public String userName;
    public String password;
    public String appkey;
    public String realName;
    public String type;
    public String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }


}
