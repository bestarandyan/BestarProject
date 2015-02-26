package com.manyi.mall.cachebean;

import com.huoqiu.framework.rest.Response;

/**
 * Created by bestar on 2015/2/26.
 */
public class GetProvinceResponse{
    public String ID;
    public String ProvinceName;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getProvinceName() {
        return ProvinceName;
    }

    public void setProvinceName(String provinceName) {
        ProvinceName = provinceName;
    }
}
