package com.manyi.mall.cachebean;

import java.util.List;

/**
 * Created by bestar on 2015/3/3.
 */
public class MainDataBean {
    public Long ID;
    public String ClassName;
    public Long ParentID;
    public List<ShopClasses> shopclasses;
    public List<Products> products;

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String className) {
        ClassName = className;
    }

    public Long getParentID() {
        return ParentID;
    }

    public void setParentID(Long parentID) {
        ParentID = parentID;
    }

    public List<ShopClasses> getShopclasses() {
        return shopclasses;
    }

    public void setShopclasses(List<ShopClasses> shopclasses) {
        this.shopclasses = shopclasses;
    }

    public List<Products> getProducts() {
        return products;
    }

    public void setProducts(List<Products> products) {
        this.products = products;
    }

    public static class ShopClasses{
        public Long ID;
        public String ClassName;
        public Long ParentID;

        public Long getID() {
            return ID;
        }

        public void setID(Long ID) {
            this.ID = ID;
        }

        public String getClassName() {
            return ClassName;
        }

        public void setClassName(String className) {
            ClassName = className;
        }

        public Long getParentID() {
            return ParentID;
        }

        public void setParentID(Long parentID) {
            ParentID = parentID;
        }
    }

    public static class Products{
        public Long ID;
        public String ProductName;
        public Long ProviderID;
        public Long ClassID;
        public String Specification;
        public float Price;
        public String PicUrl;
        public String SwfUrl;
        public String AddTime;
        public String beizhu;
        public String Recommend;
        public Long ClickNum;
        public Long PraiseNum;
        public Long ConsultNum;

        public Long getID() {
            return ID;
        }

        public void setID(Long ID) {
            this.ID = ID;
        }

        public String getProductName() {
            return ProductName;
        }

        public void setProductName(String productName) {
            ProductName = productName;
        }

        public Long getProviderID() {
            return ProviderID;
        }

        public void setProviderID(Long providerID) {
            ProviderID = providerID;
        }

        public Long getClassID() {
            return ClassID;
        }

        public void setClassID(Long classID) {
            ClassID = classID;
        }

        public String getSpecification() {
            return Specification;
        }

        public void setSpecification(String specification) {
            Specification = specification;
        }

        public float getPrice() {
            return Price;
        }

        public void setPrice(float price) {
            Price = price;
        }

        public String getPicUrl() {
            return PicUrl;
        }

        public void setPicUrl(String picUrl) {
            PicUrl = picUrl;
        }

        public String getSwfUrl() {
            return SwfUrl;
        }

        public void setSwfUrl(String swfUrl) {
            SwfUrl = swfUrl;
        }

        public String getAddTime() {
            return AddTime;
        }

        public void setAddTime(String addTime) {
            AddTime = addTime;
        }

        public String getBeizhu() {
            return beizhu;
        }

        public void setBeizhu(String beizhu) {
            this.beizhu = beizhu;
        }

        public String getRecommend() {
            return Recommend;
        }

        public void setRecommend(String recommend) {
            Recommend = recommend;
        }

        public Long getClickNum() {
            return ClickNum;
        }

        public void setClickNum(Long clickNum) {
            ClickNum = clickNum;
        }

        public Long getPraiseNum() {
            return PraiseNum;
        }

        public void setPraiseNum(Long praiseNum) {
            PraiseNum = praiseNum;
        }

        public Long getConsultNum() {
            return ConsultNum;
        }

        public void setConsultNum(Long consultNum) {
            ConsultNum = consultNum;
        }
    }
}
