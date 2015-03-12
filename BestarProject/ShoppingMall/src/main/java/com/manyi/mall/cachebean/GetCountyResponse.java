package com.manyi.mall.cachebean;

/**
 * Created by bestar on 2015/2/26.
 */
public class GetCountyResponse {
    public String ID;
    public String CountyName;
    public String CityID;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCountyName() {
        return CountyName;
    }

    public void setCountyName(String countyName) {
        CountyName = countyName;
    }

    public String getCityID() {
        return CityID;
    }

    public void setCityID(String cityID) {
        CityID = cityID;
    }
}
