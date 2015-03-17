package com.manyi.mall.cachebean.search;

import java.util.List;

/**
 * Created by bestar on 2015/3/17.
 */
public class OrderInfoBean {

    public List<OrderInfo> OrderByField;
    public List<OrderInfo> OrderWay;

    public static class OrderInfo{
        public String Name;
        public String Value;
    }
}
