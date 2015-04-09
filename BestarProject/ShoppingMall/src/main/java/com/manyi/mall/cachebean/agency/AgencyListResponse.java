package com.manyi.mall.cachebean.agency;

import java.util.List;

/**
 * Created by bestar on 2015/4/10.
 */
public class AgencyListResponse  {
    public String cityname;
    public int ClickNum;
    public int ConsultNum;
    public int PraiseNum;
    public String ContactName;
    public String ContactTel;
    public String ProviderLogo;
    public String ProviderName;
    public List<CityBean> citys;

    public static class CityBean{
        public String CityName;
        public Long ID;
    }
}
