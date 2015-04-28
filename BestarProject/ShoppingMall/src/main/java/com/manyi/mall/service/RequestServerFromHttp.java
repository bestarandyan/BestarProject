package com.manyi.mall.service;

import com.manyi.mall.BestarApplication;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class RequestServerFromHttp {
	public static final String SERVER_ADDRESS = "http://shopapp.iiyey.com/";//外网服务器总接口地址
	public static final String USER_SERVICE = SERVER_ADDRESS+"NoLogin.aspx";
	public static final String FOOT_SERVICE = SERVER_ADDRESS+"product.aspx";
	public static final String AGENTINFO_SERVICE = SERVER_ADDRESS+"agentinfo.aspx";
	public static final String COLLECT_SERVICE = SERVER_ADDRESS+"provider.aspx";
	public static final String GET_GETPSW_SERVICE = SERVER_ADDRESS+"customer.aspx";
    public static final String SHOPCLASS_SERVICE = SERVER_ADDRESS+"shopclass.aspx";
    public static final String PRODUCT_SERVICE = SERVER_ADDRESS+"product.aspx";
    public static final String COLLECTTION_SERVICE = SERVER_ADDRESS+"collection.aspx";
    public static final String SEARCHRECORD_SERVICE = SERVER_ADDRESS+"searchrecord.aspx";
    public static final String ADVERT_SERVICE = SERVER_ADDRESS+"shoprollpics.aspx";
    public static final String CONSULT_SERVICE = SERVER_ADDRESS+"consultrecord.aspx";
    public static final String FEEDBACK_SERVICE = SERVER_ADDRESS+"customerreview.aspx";
    public static final String AGENCYPAYLIST_SERVICE = SERVER_ADDRESS+"payrecord.aspx";
    public static final String AGENTSETTING_SERVICE = SERVER_ADDRESS+"agentsetting.aspx";
    public static final String TRACDE_SERVICE = SERVER_ADDRESS+"trace.aspx";
	public static final String USER_APPKEY = "123456";//
	public static final String IMGURL = "http://servercomponents.iiyey.com/";



    public String getAgencyPayList(String PageIndex,String PageSize){
        String msgString = "";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("method", "GetPayRecordListByCustomerID"));
        params.add(new BasicNameValuePair("appKey", BestarApplication.getInstance().getAppkey()));
        params.add(new BasicNameValuePair("PageIndex", PageIndex));
        params.add(new BasicNameValuePair("PageSize", PageSize));
        params.add(new BasicNameValuePair("CustomerID", BestarApplication.getInstance().getUserId()));
        msgString = getData(AGENCYPAYLIST_SERVICE, params);
        return msgString;
    }

    public String getProvince(){
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("method", "GetProvinceList"));
 		params.add(new BasicNameValuePair("appKey", USER_APPKEY));
 		msgString = getData(USER_SERVICE, params);
 		return msgString;
 	}

 	public String getProvinceByProviderID(String ProviderID){
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("method", "GetAgentProvinceListByProviderID"));
 		params.add(new BasicNameValuePair("appKey", BestarApplication.getInstance().getAppkey()));
 		params.add(new BasicNameValuePair("ProviderID", ProviderID));
 		msgString = getData(AGENTINFO_SERVICE, params);
 		return msgString;
 	}

 	public String getAgentProvinceByProviderID(String ProviderID){
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("method", "GetAgentProvinceListByProviderID"));
 		params.add(new BasicNameValuePair("appKey", BestarApplication.getInstance().getAppkey()));
 		params.add(new BasicNameValuePair("ProviderID", ProviderID));
 		msgString = getData(AGENTSETTING_SERVICE, params);
 		return msgString;
 	}

 	public String getAdvert(){
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("method", "GetShopRollPics"));
 		params.add(new BasicNameValuePair("appKey", BestarApplication.getInstance().getAppkey()));
 		msgString = getData(ADVERT_SERVICE, params);
 		return msgString;
 	}

    public String addConsult(String ProviderID){
        String msgString = "";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("method", "AddConsultRecord"));
        params.add(new BasicNameValuePair("ProviderID", ProviderID));
        params.add(new BasicNameValuePair("CustomerID", BestarApplication.getInstance().getUserId()));
        params.add(new BasicNameValuePair("appKey", BestarApplication.getInstance().getAppkey()));
        msgString = getData(CONSULT_SERVICE, params);
        return msgString;
    }

 	public String addFeedBack(String ReviewContent){
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("method", "AddCustomerReview"));
 		params.add(new BasicNameValuePair("appKey", BestarApplication.getInstance().getAppkey()));
 		params.add(new BasicNameValuePair("CustomerID", BestarApplication.getInstance().getUserId()));
 		params.add(new BasicNameValuePair("ReviewContent", ReviewContent));
 		msgString = getData(FEEDBACK_SERVICE, params);
 		return msgString;
 	}

    public String getCity(String ProvinceID){
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("method", "GetCityList"));
 		params.add(new BasicNameValuePair("appKey", USER_APPKEY));
 		params.add(new BasicNameValuePair("ProvinceID", ProvinceID));
 		msgString = getData(USER_SERVICE, params);
 		return msgString;
 	}

    public String getConsultByCityId(String ProviderID,String CityID,String PageIndex,String PageSize){
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("method", "GetAgentListByProviderIDandCityID"));
 		params.add(new BasicNameValuePair("appKey", BestarApplication.getInstance().getAppkey()));
 		params.add(new BasicNameValuePair("ProviderID", ProviderID));
 		params.add(new BasicNameValuePair("PageIndex", PageIndex));
 		params.add(new BasicNameValuePair("PageSize", PageSize));
 		params.add(new BasicNameValuePair("CityID", CityID));
 		msgString = getData(AGENTINFO_SERVICE, params);
 		return msgString;
 	}

    public String  getLocalConsultList(String ProviderID,String PageIndex,String PageSize){
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("method", "GetAgentListByProviderIDandCustomerLocal"));
 		params.add(new BasicNameValuePair("appKey", BestarApplication.getInstance().getAppkey()));
 		params.add(new BasicNameValuePair("ProviderID", ProviderID));
 		params.add(new BasicNameValuePair("PageIndex", PageIndex));
 		params.add(new BasicNameValuePair("PageSize", PageSize));
 		params.add(new BasicNameValuePair("CustomerID", BestarApplication.getInstance().getUserId()));
 		msgString = getData(AGENTINFO_SERVICE, params);
 		return msgString;
 	}

    public String getCityByProviderIDAndProvinceID(String ProviderID,String ProvinceID){
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("method", "GetAgentCityListByProviderIDandProvinceID"));
 		params.add(new BasicNameValuePair("appKey", BestarApplication.getInstance().getAppkey()));
 		params.add(new BasicNameValuePair("ProviderID", ProviderID));
 		params.add(new BasicNameValuePair("ProvinceID", ProvinceID));
 		msgString = getData(AGENTINFO_SERVICE, params);
 		return msgString;
 	}

    public String getAgentCityByProviderIDAndProvinceID(String ProviderID,String ProvinceID){
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("method", "GetAgentSettingListByProviderIDandProvinceID"));
 		params.add(new BasicNameValuePair("appKey", BestarApplication.getInstance().getAppkey()));
 		params.add(new BasicNameValuePair("ProviderID", ProviderID));
 		params.add(new BasicNameValuePair("ProvinceID", ProvinceID));
 		msgString = getData(AGENTSETTING_SERVICE, params);
 		return msgString;
 	}

    public String getHotSearch(String PageIndex,String PageSize){//获取最热门搜索
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("method", "GetTopSearchList"));
 		params.add(new BasicNameValuePair("appKey", BestarApplication.getInstance().getAppkey()));
 		params.add(new BasicNameValuePair("PageIndex", PageIndex));
 		params.add(new BasicNameValuePair("PageSize", PageSize));
 		msgString = getData(SEARCHRECORD_SERVICE, params);
 		return msgString;
 	}

    public String getUserSearched(String PageIndex,String PageSize){//获取用户搜索记录列表
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("method", "GetCustomerSearchList"));
 		params.add(new BasicNameValuePair("appKey", BestarApplication.getInstance().getAppkey()));
 		params.add(new BasicNameValuePair("PageIndex", PageIndex));
 		params.add(new BasicNameValuePair("PageSize", PageSize));
 		params.add(new BasicNameValuePair("CustomerID", BestarApplication.getInstance().getUserId()));
 		msgString = getData(SEARCHRECORD_SERVICE, params);
 		return msgString;
 	}

    public String getUserInfo(){//获取用户搜索记录列表
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("method", "GetCustomerInfo"));
 		params.add(new BasicNameValuePair("appKey", BestarApplication.getInstance().getAppkey()));
 		params.add(new BasicNameValuePair("CustomerID", BestarApplication.getInstance().getUserId()));
 		msgString = getData(GET_GETPSW_SERVICE, params);
 		return msgString;
 	}

    public String addCollect(String ProviderID){
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("method", "AddCollection"));
 		params.add(new BasicNameValuePair("appKey", BestarApplication.getInstance().getAppkey()));
 		params.add(new BasicNameValuePair("ProviderID", ProviderID));
 		params.add(new BasicNameValuePair("CustomerID", BestarApplication.getInstance().getUserId()));
 		msgString = getData(COLLECTTION_SERVICE, params);
 		return msgString;
 	}

    public String clearUserSearch(){//清空用户搜索记录列表
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("method", "ClearCustomerSearchRecordByCustomerID"));
 		params.add(new BasicNameValuePair("appKey", BestarApplication.getInstance().getAppkey()));
 		params.add(new BasicNameValuePair("CustomerID", BestarApplication.getInstance().getUserId()));
 		msgString = getData(SEARCHRECORD_SERVICE, params);
 		return msgString;
 	}

    public String getCollect(String pageIndex,String pageSize){
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("method", "GetCollectionList"));
 		params.add(new BasicNameValuePair("appKey", BestarApplication.getInstance().getAppkey()));
 		params.add(new BasicNameValuePair("pageIndex", pageIndex));
 		params.add(new BasicNameValuePair("pageSize", pageSize));
 		params.add(new BasicNameValuePair("CustomerID", BestarApplication.getInstance().getUserId()));
 		msgString = getData(COLLECT_SERVICE, params);
 		return msgString;
 	}

    public String getCounty(String cityId){
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("method", "GetCountyList"));
 		params.add(new BasicNameValuePair("appKey", USER_APPKEY));
 		params.add(new BasicNameValuePair("CityID", cityId));
 		msgString = getData(USER_SERVICE, params);
 		return msgString;
 	}
    public String getFootList(String pageIndex,String pageSize){
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("method", "GetProductListForTrace"));
 		params.add(new BasicNameValuePair("appKey", BestarApplication.getInstance().getAppkey()));
 		params.add(new BasicNameValuePair("pageIndex", pageIndex));
 		params.add(new BasicNameValuePair("pageSize", pageSize));
 		params.add(new BasicNameValuePair("CustomerID", BestarApplication.getInstance().getUserId()));
 		msgString = getData(FOOT_SERVICE, params);
 		return msgString;
 	}
    public String getAgentList(String pageIndex,String pageSize){
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("method", "GetAgentList"));
 		params.add(new BasicNameValuePair("appKey", BestarApplication.getInstance().getAppkey()));
 		params.add(new BasicNameValuePair("pageIndex", pageIndex));
 		params.add(new BasicNameValuePair("pageSize", pageSize));
 		params.add(new BasicNameValuePair("CustomerID", BestarApplication.getInstance().getUserId()));
 		msgString = getData(AGENTINFO_SERVICE, params);
 		return msgString;
 	}

    public String searchProducts(String pageIndex,String pageSize,String LikeStr){
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("method", "GetProductListBySearch"));
 		params.add(new BasicNameValuePair("appKey", BestarApplication.getInstance().getAppkey()));
 		params.add(new BasicNameValuePair("pageIndex", pageIndex));
 		params.add(new BasicNameValuePair("pageSize", pageSize));
 		params.add(new BasicNameValuePair("LikeStr", LikeStr));
 		params.add(new BasicNameValuePair("CustomerID", BestarApplication.getInstance().getUserId() ));
 		msgString = getData(PRODUCT_SERVICE, params);
 		return msgString;
 	}

    public String searchProductTypes(String LikeStr){
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("method", "GetShopClassListBySearch"));
 		params.add(new BasicNameValuePair("appKey", BestarApplication.getInstance().getAppkey()));
 		params.add(new BasicNameValuePair("LikeStr", LikeStr));
 		msgString = getData(PRODUCT_SERVICE, params);
 		return msgString;
 	}

    public String searchProductByTypeAndInput(String LikeStr,String ClassID,String orderField,String order,String PageIndex,String PageSize){
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("method", "GetProductListBySearch"));
 		params.add(new BasicNameValuePair("appKey", BestarApplication.getInstance().getAppkey()));
 		params.add(new BasicNameValuePair("ClassID", ClassID));
 		params.add(new BasicNameValuePair("PageIndex", PageIndex));
 		params.add(new BasicNameValuePair("PageSize", PageSize));
 		params.add(new BasicNameValuePair("OrderByField", orderField));
 		params.add(new BasicNameValuePair("OrderWay", order));
 		params.add(new BasicNameValuePair("LikeStr", LikeStr));
 		params.add(new BasicNameValuePair("CustomerID", BestarApplication.getInstance().getUserId()));
 		msgString = getData(PRODUCT_SERVICE, params);
 		return msgString;
 	}

public String registerYZ(String type,String UserName,String Password,String RealName,String Sex,String Phone,String ProvinceID,String CityID,String CountyID,String Address,String QQ,String SchoolName,String ClassNum,String StudentNum,String CompanyPhone){
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("method", "GardenerRegister"));
 		params.add(new BasicNameValuePair("appKey", USER_APPKEY));
 		params.add(new BasicNameValuePair("UserName", UserName));
 		params.add(new BasicNameValuePair("Password", Password));
 		params.add(new BasicNameValuePair("Type", type));
 		params.add(new BasicNameValuePair("RealName", RealName));
 		params.add(new BasicNameValuePair("Sex", Sex));
 		params.add(new BasicNameValuePair("Phone", Phone));
 		params.add(new BasicNameValuePair("ProvinceID", ProvinceID));
 		params.add(new BasicNameValuePair("CityID", CityID));
 		params.add(new BasicNameValuePair("CountyID", CountyID));
 		params.add(new BasicNameValuePair("Address", Address));
 		params.add(new BasicNameValuePair("QQ", QQ));
 		params.add(new BasicNameValuePair("SchoolName", SchoolName));
 		params.add(new BasicNameValuePair("ClassNum", ClassNum));
 		params.add(new BasicNameValuePair("StudentNum", StudentNum));
 		params.add(new BasicNameValuePair("CompanyPhone", CompanyPhone));
 		msgString = getData(USER_SERVICE, params);
 		return msgString;
 	}

    public String updateInfo(String RealName,String Sex,String Phone,String ProvinceID,String CityID,String CountyID,String Address,String QQ,String SchoolName,String ClassNum,String StudentNum,String CompanyPhone){
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("method", "ModifyInfo"));
 		params.add(new BasicNameValuePair("appKey", BestarApplication.getInstance().getAppkey()));
 		params.add(new BasicNameValuePair("RealName", RealName));
 		params.add(new BasicNameValuePair("Sex", Sex));
 		params.add(new BasicNameValuePair("Phone", Phone));
 		params.add(new BasicNameValuePair("ProvinceID", ProvinceID));
 		params.add(new BasicNameValuePair("CityID", CityID));
 		params.add(new BasicNameValuePair("CountyID", CountyID));
 		params.add(new BasicNameValuePair("Address", Address));
 		params.add(new BasicNameValuePair("QQ", QQ));
 		params.add(new BasicNameValuePair("SchoolName", SchoolName));
 		params.add(new BasicNameValuePair("ClassNum", ClassNum));
 		params.add(new BasicNameValuePair("StudentNum", StudentNum));
 		params.add(new BasicNameValuePair("CompanyPhone", CompanyPhone));
 		params.add(new BasicNameValuePair("CustomerID", BestarApplication.getInstance().getUserId()));
 		msgString = getData(GET_GETPSW_SERVICE, params);
 		return msgString;
 	}

public String registerAgent(String type,String UserName,String Password,String RealName,String Sex,String Phone,String ProvinceID,String CityID,String QQ){
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("method", "AgentRegister"));
 		params.add(new BasicNameValuePair("appKey", USER_APPKEY));
 		params.add(new BasicNameValuePair("UserName", UserName));
 		params.add(new BasicNameValuePair("Password", Password));
 		params.add(new BasicNameValuePair("Type", type));
 		params.add(new BasicNameValuePair("RealName", RealName));
 		params.add(new BasicNameValuePair("Sex", Sex));
 		params.add(new BasicNameValuePair("Phone", Phone));
 		params.add(new BasicNameValuePair("ProvinceID", ProvinceID));
 		params.add(new BasicNameValuePair("CityID", CityID));
 		params.add(new BasicNameValuePair("QQ", QQ));
 		msgString = getData(USER_SERVICE, params);
 		return msgString;
 	}

public String getRegisterCode(String MobilePhone){
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("method", "yanzhen"));
 		params.add(new BasicNameValuePair("appKey", USER_APPKEY));
 		params.add(new BasicNameValuePair("MobilePhone", MobilePhone));
 		msgString = getData(USER_SERVICE, params);
 		return msgString;
 	}

public String login(String userName,String psw){
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("method", "Login"));
 		params.add(new BasicNameValuePair("appKey", USER_APPKEY));
 		params.add(new BasicNameValuePair("UserName", userName));
 		params.add(new BasicNameValuePair("Password", psw));
 		msgString = getData(USER_SERVICE, params);
 		return msgString;
 	}

    public String getForgetPswCode(String mobile){
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("method", "BackPWDyanzhen"));
 		params.add(new BasicNameValuePair("appKey", USER_APPKEY));
 		params.add(new BasicNameValuePair("MobilePhone", mobile));
 		msgString = getData(USER_SERVICE, params);
 		return msgString;
 	}

    public String updatePsw(String NewPassword,String OldPassword){
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("method", "ModifyPassword"));
 		params.add(new BasicNameValuePair("appKey", BestarApplication.getInstance().getAppkey()));
 		params.add(new BasicNameValuePair("CustomerID", BestarApplication.getInstance().getUserId()));
 		params.add(new BasicNameValuePair("NewPassword", NewPassword));
 		params.add(new BasicNameValuePair("OldPassword", OldPassword));
 		msgString = getData(GET_GETPSW_SERVICE, params);
 		return msgString;
 	}

    public String deleteCollection(String ProviderIDs){
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("method", "DeleteCollection"));
 		params.add(new BasicNameValuePair("appKey", BestarApplication.getInstance().getAppkey()));
 		params.add(new BasicNameValuePair("ProviderIDs", ProviderIDs));
 		params.add(new BasicNameValuePair("CustomerID", BestarApplication.getInstance().getUserId()));
 		msgString = getData(COLLECTTION_SERVICE, params);
 		return msgString;
 	}

    public String deleteFootPrint(String ProductIDs){
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("method", "DeleteTrace"));
 		params.add(new BasicNameValuePair("appKey", BestarApplication.getInstance().getAppkey()));
 		params.add(new BasicNameValuePair("ProductIDs", ProductIDs));
 		params.add(new BasicNameValuePair("CustomerID", BestarApplication.getInstance().getUserId()));
 		msgString = getData(TRACDE_SERVICE, params);
 		return msgString;
 	}

public String getForgetPsw(String mobile){
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("method", "BackPWD"));
 		params.add(new BasicNameValuePair("appKey", USER_APPKEY));
 		params.add(new BasicNameValuePair("MobilePhone", mobile));
 		msgString = getData(USER_SERVICE, params);
 		return msgString;
 	}


    public String getTypeFirst(String appKey){
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("method", "GetFirstClass"));
 		params.add(new BasicNameValuePair("appKey", appKey));
 		msgString = getData(SHOPCLASS_SERVICE, params);
 		return msgString;
 	}

 public String getTypeSecond(String classId){
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("method", "GetSecondClass"));
 		params.add(new BasicNameValuePair("appKey", BestarApplication.getInstance().getAppkey()));
 		params.add(new BasicNameValuePair("ClassID", classId));
 		msgString = getData(SHOPCLASS_SERVICE, params);
 		return msgString;
 	}

    public String getProductList(String id,String productNum){
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("method", "GetSecondClassAndProductList"));
 		params.add(new BasicNameValuePair("appKey", BestarApplication.getInstance().getAppkey()));
 		params.add(new BasicNameValuePair("ID", id));
 		params.add(new BasicNameValuePair("ProductNum", productNum));
 		msgString = getData(SHOPCLASS_SERVICE, params);
 		return msgString;
 	}

    public String getProducts(String appKey,String classId,String PageIndex,String PageSize){
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("method", "GetProductList"));
 		params.add(new BasicNameValuePair("appKey", appKey));
 		params.add(new BasicNameValuePair("ClassID", classId));
 		params.add(new BasicNameValuePair("PageIndex", PageIndex));
 		params.add(new BasicNameValuePair("PageSize", PageSize));
 		params.add(new BasicNameValuePair("OrderByField", "ID"));
 		params.add(new BasicNameValuePair("OrderWay", "desc"));
 		msgString = getData(PRODUCT_SERVICE, params);
 		return msgString;
 	}
    public String getProductsByFirstId(String classId,String PageIndex,String PageSize,String field,String way){
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("method", "GetProductListByFirstClassID"));
 		params.add(new BasicNameValuePair("appKey", BestarApplication.getInstance().getAppkey()));
 		params.add(new BasicNameValuePair("FirstClassID", classId));
 		params.add(new BasicNameValuePair("PageIndex", PageIndex));
 		params.add(new BasicNameValuePair("PageSize", PageSize));
 		params.add(new BasicNameValuePair("OrderByField", field));
 		params.add(new BasicNameValuePair("OrderWay", way));
 		msgString = getData(PRODUCT_SERVICE, params);
 		return msgString;
 	}

    public String getOrderInfo(){
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("method", "GetOrderInfo"));
 		params.add(new BasicNameValuePair("appKey", BestarApplication.getInstance().getAppkey()));
 		msgString = getData(PRODUCT_SERVICE, params);
 		return msgString;
 	}

    public String getUrl(){
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("method", "GetWebURL"));
 		params.add(new BasicNameValuePair("appKey", BestarApplication.getInstance().getAppkey()));
 		msgString = getData(GET_GETPSW_SERVICE, params);
 		return msgString;
 	}
////http://shopapp.iiyey.com/shopclass.aspx?appKey=abcd&&method=GetAllClassAndProductList
//    public String getMainData(String appKey){
// 		String msgString = "";
// 		List<NameValuePair> params = new ArrayList<NameValuePair>();
// 		params.add(new BasicNameValuePair("method", "GetAllClassAndProductList"));
// 		params.add(new BasicNameValuePair("appKey", appKey));
// 		msgString = getData(SHOPCLASS_SERVICE, params);
// 		return msgString;
// 	}
    public String getMainData(String appKey){
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("method", "GetAllClassAndProductList2"));
 		params.add(new BasicNameValuePair("appKey", appKey));
 		msgString = getData(SHOPCLASS_SERVICE, params);
 		return msgString;
 	}


	
	/**
     * 访问服务器的总函数
     * @author 刘星星
     * @param urlString 接口地址
     * @param list 参数集合
     * @return 服务器返回值
     */
    public static  String getData(String urlString,List<NameValuePair> list){
    	String msgString = "";
    	HttpPost httpPost = new HttpPost(urlString);
    	try {
			HttpEntity httpEntity = new UrlEncodedFormEntity(list,HTTP.UTF_8);
			httpPost.setEntity(httpEntity);
			HttpClient httpClient = new DefaultHttpClient();
			//设置超时时间
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			
			msgString = EntityUtils.toString(httpResponse.getEntity());
			if(msgString.length()>0 && msgString.substring(0, 1).equals("<")){
				msgString = "404";
			}
    	} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			msgString = "404";
			e.printStackTrace();
		}
    	return msgString;
    }
    

}
