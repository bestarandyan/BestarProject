package com.manyi.mall.cachebean;

import java.util.List;

/**
 * Created by bestar on 2015/3/3.
 */
public class MainDataBean2 {
    public Long ID;
    public String ClassName;
    public Long ParentID;
    public List<Product> SubClassAndProducts;


    public static class Product{
        public Long ID;
        public String ClassName;
        public String ProductName;
        public String ProductPicURL;
        public Long ParentID;
        public Long ProductClassID;
        public Long ProductID;
    }
}
