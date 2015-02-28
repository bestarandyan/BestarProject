package com.manyi.mall.service;

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
	public static final String USER_SERVICE = SERVER_ADDRESS+"NoLogin.aspx";//外网服务器总接口地址
	public static final String USER_APPKEY = "123456";//
	public static final String IMGURL = "http://servercomponents.iiyey.com/";//


 	public String getProvince(){
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("method", "GetProvinceList"));
 		params.add(new BasicNameValuePair("appKey", USER_APPKEY));
 		msgString = getData(USER_SERVICE, params);
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

    public String getCounty(String cityId){
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("method", "GetCountyList"));
 		params.add(new BasicNameValuePair("appKey", USER_APPKEY));
 		params.add(new BasicNameValuePair("CityID", cityId));
 		msgString = getData(USER_SERVICE, params);
 		return msgString;
 	}

public String register(String UserName,String Password,String RealName,String Sex,String Phone,String ProvinceID,String CityID,String CountyID,String Address,String QQ,String SchoolName,String ClassNum,String StudentNum){
 		String msgString = "";
 		List<NameValuePair> params = new ArrayList<NameValuePair>();
 		params.add(new BasicNameValuePair("method", "GardenerRegister"));
 		params.add(new BasicNameValuePair("appKey", USER_APPKEY));
 		params.add(new BasicNameValuePair("UserName", UserName));
 		params.add(new BasicNameValuePair("Password", Password));
 		params.add(new BasicNameValuePair("Type", "2"));
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
