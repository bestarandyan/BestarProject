/**
 * 
 */
package com.manyi.mall.utils;

import android.database.sqlite.SQLiteDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.manyi.mall.cachebean.BaseResponse;
import com.manyi.mall.cachebean.GetCityResponse;
import com.manyi.mall.cachebean.GetCountyResponse;
import com.manyi.mall.cachebean.GetProvinceResponse;
import com.manyi.mall.cachebean.MainDataBean;
import com.manyi.mall.cachebean.collect.CollectListBean;
import com.manyi.mall.cachebean.mine.FootprintListResponse;
import com.manyi.mall.cachebean.search.HotSearchBean;
import com.manyi.mall.cachebean.search.OrderInfoBean;
import com.manyi.mall.cachebean.search.SearchHistoryBean;
import com.manyi.mall.cachebean.search.TypeProductBean;
import com.manyi.mall.cachebean.user.CodeResponse;
import com.manyi.mall.cachebean.user.LoginResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author bestar
 * @createDate 2015、2、10
 * 用json
 *
 */
public class JsonData {


    public boolean isSuccessGetInfo(String msg,String tag){
        boolean isSuccess = false;
        String code = getJsonObject(msg,tag);
        if (code != null && code.equals("0")){
            isSuccess = true;
        }else{
            isSuccess = false;
        }
        return isSuccess;
    }

    public List<Map<String,String>> jsonProvinceMsg(String msg,SQLiteDatabase database){
        List<Map<String,String>> listData = new ArrayList<>();
        Type listType = new TypeToken<LinkedList<GetProvinceResponse>>(){}.getType();
        Gson gson = new Gson();
        LinkedList<GetProvinceResponse> list;
        GetProvinceResponse bean;
        list = gson.fromJson(msg, listType);
        if(list!=null && list.size()>0){
            for(Iterator<GetProvinceResponse> iterator = list.iterator();iterator.hasNext();){
                bean = iterator.next();
                Map<String,String> map = new HashMap<>();
                map.put("ID", bean.getID());
                map.put("ProvinceName", bean.getProvinceName());
                listData.add(map);
//                int a = database.update(GetProvinceResponse.tbName, contentValues, "ID=?", new String[]{bean.getID()+""});
//                if(a == 0){
//                    database.insert(GetProvinceResponse.tbName, null, contentValues);
//                }
            }
        }
        return listData;
    }
    public List<Map<String,String>> jsonCityMsg(String msg,SQLiteDatabase database){
        List<Map<String,String>> listData = new ArrayList<>();
        Type listType = new TypeToken<LinkedList<GetCityResponse>>(){}.getType();
        Gson gson = new Gson();
        LinkedList<GetCityResponse> list;
        GetCityResponse bean;
        list = gson.fromJson(msg, listType);
        if(list!=null && list.size()>0){
            for(Iterator<GetCityResponse> iterator = list.iterator();iterator.hasNext();){
                bean = iterator.next();
                Map<String,String> map = new HashMap<>();
                map.put("ID", bean.getID());
                map.put("CityName", bean.getCityName());
                listData.add(map);
//                int a = database.update(GetProvinceResponse.tbName, contentValues, "ID=?", new String[]{bean.getID()+""});
//                if(a == 0){
//                    database.insert(GetProvinceResponse.tbName, null, contentValues);
//                }
            }
        }
        return listData;
    }

    public List<Map<String,String>> jsonCountyMsg(String msg,SQLiteDatabase database){
        List<Map<String,String>> listData = new ArrayList<>();
        Type listType = new TypeToken<LinkedList<GetCountyResponse>>(){}.getType();
        Gson gson = new Gson();
        LinkedList<GetCountyResponse> list;
        GetCountyResponse bean;
        list = gson.fromJson(msg, listType);
        if(list!=null && list.size()>0){
            for(Iterator<GetCountyResponse> iterator = list.iterator();iterator.hasNext();){
                bean = iterator.next();
                Map<String,String> map = new HashMap<>();
                map.put("ID", bean.getID());
                map.put("CountyName", bean.getCountyName());
                map.put("CityID", bean.getCityID());
                listData.add(map);
//                int a = database.update(GetProvinceResponse.tbName, contentValues, "ID=?", new String[]{bean.getID()+""});
//                if(a == 0){
//                    database.insert(GetProvinceResponse.tbName, null, contentValues);
//                }
            }
        }
        return listData;
    }
    public List<MainDataBean> jsonMainData(String msg,SQLiteDatabase database){
        LinkedList<MainDataBean> list =null;
        MainDataBean bean;
        try {
            Type listType = new TypeToken<LinkedList<MainDataBean>>(){}.getType();
            Gson gson = new Gson();
            list = gson.fromJson(msg, listType);
//        if(list!=null && list.size()>0){
//            for(Iterator<MainDataBean> iterator = list.iterator();iterator.hasNext();){
//                bean = iterator.next();
//
//            }
//        }
        }catch (Exception e){
            e.printStackTrace();
        }

        return list;
    }

    public List<SearchHistoryBean> jsonSearchHistory(String msg){//用户历史搜索记录
        LinkedList<SearchHistoryBean> list =null;
        try {
            Type listType = new TypeToken<LinkedList<SearchHistoryBean>>(){}.getType();
            Gson gson = new Gson();
            list = gson.fromJson(msg, listType);
        }catch (Exception e){
            e.printStackTrace();
        }

        return list;
    }
    public List<HotSearchBean> jsonHotSearch(String msg){//最热搜索记录
        LinkedList<HotSearchBean> list =null;
        try {
            Type listType = new TypeToken<LinkedList<HotSearchBean>>(){}.getType();
            Gson gson = new Gson();
            list = gson.fromJson(msg, listType);
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }
    public OrderInfoBean jsonOrderInfo(String msg){
        OrderInfoBean orderInfoBean = new OrderInfoBean();
        String OrderByField = getStr(msg,"OrderByField");
        String OrderWay = getStr(msg,"OrderWay");
        LinkedList<OrderInfoBean.OrderInfo> list =null;
        try {
            Type listType = new TypeToken<LinkedList<OrderInfoBean.OrderInfo>>(){}.getType();
            Gson gson = new Gson();
            list = gson.fromJson(OrderByField, listType);
            orderInfoBean.OrderByField = list;
            list = gson.fromJson(OrderWay, listType);
            orderInfoBean.OrderWay = list;
        }catch (Exception e){
            e.printStackTrace();
        }

        return orderInfoBean;
    }

    public List<CollectListBean> jsonCollectList(String msg){
        LinkedList<CollectListBean> list =null;
        CollectListBean bean;
        try {
            Type listType = new TypeToken<LinkedList<CollectListBean>>(){}.getType();
            Gson gson = new Gson();
            list = gson.fromJson(msg, listType);
//        if(list!=null && list.size()>0){
//            for(Iterator<MainDataBean> iterator = list.iterator();iterator.hasNext();){
//                bean = iterator.next();
//
//            }
//        }
        }catch (Exception e){
            e.printStackTrace();
        }

        return list;
    }
    public List<TypeProductBean> jsonTypeProductList(String msg){
        LinkedList<TypeProductBean> list =null;
        TypeProductBean bean;
        try {
            Type listType = new TypeToken<LinkedList<TypeProductBean>>(){}.getType();
            Gson gson = new Gson();
            list = gson.fromJson(msg, listType);
//        if(list!=null && list.size()>0){
//            for(Iterator<TypeProductBean> iterator = list.iterator();iterator.hasNext();){
//                bean = iterator.next();
//
//            }
//        }
        }catch (Exception e){
            e.printStackTrace();
        }

        return list;
    }
    public List<Map<String,Object>> jsonFootprint(String msg){
        List<Map<String,Object>> resultList = new ArrayList<>();
        LinkedList<FootprintListResponse> list =null;
        FootprintListResponse bean;
        try {
            Type listType = new TypeToken<LinkedList<FootprintListResponse>>(){}.getType();
            Gson gson = new Gson();
            list = gson.fromJson(msg, listType);
            int isAddedPosition = 0;
            if(list!=null && list.size()>0){
                for(Iterator<FootprintListResponse> iterator = list.iterator();iterator.hasNext();){
                    bean = iterator.next();
                    String ProviderID = bean.getProviderID();
                    String ClassID = bean.getClassID();
                    String AddTime = bean.getAddTime();
                    String beizhu = bean.getBeizhu();
                    String ClickNum = bean.getBeizhu();
                    String ConsultNum = bean.getConsultNum();
                    String ID = bean.getID();
                    String PicUrl = bean.getPicUrl();
                    String PraiseNum = bean.getPraiseNum();
                    String Price = bean.getPrice();
                    String ProductName = bean.getProductName();
                    String ProviderCityName = bean.getProviderCityName();
                    String ProviderName = bean.getProviderName();
                    String Recommend = bean.getRecommend();
                    String Specification = bean.getSpecification();
                    String SwfUrl = bean.getSwfUrl();
                    isAddedPosition = 0;
                    boolean isAdded = false;
                    for (int i=0;i<resultList.size();i++){
                        Map<String,Object> map = resultList.get(i);
                        String providerId = map.get("ProviderID").toString();
                        if (providerId.equals(ProviderID)){
                            isAddedPosition = i;
                            isAdded = true;
                            break;
                        }
                    }
                    if (!isAdded){//代表没有加入过resultList
                        Map<String,Object> map = new HashMap<>();
                        map.put("ProviderID",ProviderID);
                        map.put("ClassID",ClassID);
                        map.put("ProviderCityName",ProviderCityName);
                        map.put("ProviderName",ProviderName);
                        map.put("isAllChecked",false);
                        List<Map<String,String>> productList = new ArrayList<>();
                        Map<String,String> productMap = new HashMap<>();
                        productMap.put("AddTime",AddTime);
                        productMap.put("beizhu",beizhu);
                        productMap.put("ClickNum",ClickNum);
                        productMap.put("ConsultNum",ConsultNum);
                        productMap.put("ID",ID);
                        productMap.put("PicUrl",PicUrl);
                        productMap.put("PraiseNum",PraiseNum);
                        productMap.put("Price",Price);
                        productMap.put("ProductName",ProductName);
                        productMap.put("Recommend",Recommend);
                        productMap.put("Specification",Specification);
                        productMap.put("SwfUrl",SwfUrl);
                        productMap.put("isChecked","0");
                        productList.add(productMap);
                        map.put("productList",productList);
                        resultList.add(map);
                    }else{
                        List<Map<String,String>> productList = (List<Map<String, String>>) resultList.get(isAddedPosition).get("productList");
                        Map<String,String> productMap = new HashMap<>();
                        productMap.put("AddTime",AddTime);
                        productMap.put("beizhu",beizhu);
                        productMap.put("ClickNum",ClickNum);
                        productMap.put("ConsultNum",ConsultNum);
                        productMap.put("ID",ID);
                        productMap.put("PicUrl",PicUrl);
                        productMap.put("PraiseNum",PraiseNum);
                        productMap.put("Price",Price);
                        productMap.put("ProductName",ProductName);
                        productMap.put("Recommend",Recommend);
                        productMap.put("Specification",Specification);
                        productMap.put("SwfUrl",SwfUrl);
                        productMap.put("isChecked","0");
                        productList.add(productMap);
                    }

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return resultList;
    }

    public String getJsonObject(String msg,String json) {
        String jsonStr = "";
        try {
            JSONObject jsonObject = new JSONObject(msg);
            jsonStr = jsonObject.getString(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonStr;
    }

	public BaseResponse JsonBase(String msg) {
        BaseResponse bean =null;
        try {
            JSONObject jsonObject = new JSONObject(msg);
            bean = new BaseResponse();
            bean.setCode(jsonObject.getString("Code"));
            bean.setMessage(jsonObject.getString("Message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bean;
    }
	public String getStr(String msg,String flag) {
        String str = "";
        try {
            JSONObject jsonObject = new JSONObject(msg);
            str = jsonObject.getString(flag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return str;
    }
public String JsonUrl(String msg,String tag ) {
        try {
            JSONObject jsonObject = new JSONObject(msg);
            return jsonObject.getString(tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public CodeResponse JsonCode(String msg) {
        CodeResponse bean =null;
        try {
            JSONObject jsonObject = new JSONObject(msg);
            bean = new CodeResponse();
            bean.setCode(jsonObject.getString("Code"));
            bean.setMessage(jsonObject.getString("Message"));
            bean.setYZCode(jsonObject.getString("YZCode"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bean;
    }

    public LoginResponse JsonLoginMsg(String msg){
        LoginResponse bean = new LoginResponse();
        try {
            JSONObject jsonObject = new JSONObject(msg);
            String code = jsonObject.getString("Code");
            String message = jsonObject.getString("Message");
            bean.setCode(code);
            bean.setMessage(message);
            if (code.equals("0")){
                String data = jsonObject.getString("Data");
                if (data!=null && data.length()>0){
                    JsonLoginData(data,bean);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bean;
    }

    public void JsonLoginData(String msg,LoginResponse bean) {
        try {
            JSONObject jsonObject = new JSONObject(msg);
            bean.setAppKey(jsonObject.getString("AppKey"));
            bean.setId(jsonObject.getString("id"));
            bean.setRealName(jsonObject.getString("RealName"));
            bean.setType(jsonObject.getString("Type"));
            bean.setUserName(jsonObject.getString("UserName"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
//
//	/**
//	 * 出园
//	 * @param msg
//	 */
//	public OutSchoolBean jsonOutSchool(String msg) {
//        OutSchoolBean bean = null;
//        try {
//            JSONObject jsonObject = new JSONObject(msg);
//            bean = new OutSchoolBean();
//            bean.setResult(jsonObject.getString("result"));
//            bean.setInfo(jsonObject.getString("info"));
//            bean.setEntertime(jsonObject.getString("entertime"));
//            bean.setLeavetime(jsonObject.getString("leavetime"));
//        }catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return bean;
//    }


	
	/**
	 * 判断服务器返回值是否为无数据的格式
	 * @param str
	 * @return
	 */
	public boolean isNoData(String str){
		String format = "\\d+\\,20[1,2]\\d-((0?[0-9])|1[0-2])-(([0-2][0-9])|3[0-1])(\\s([0-1][0-9]|2[0-4]):([0-5][0-9])(:([0-5][0-9]))?)?";
		Matcher matcher = Pattern.compile(format).matcher(str);
		while(matcher.find()){
			return true;
		}
		return false;
	}
}
