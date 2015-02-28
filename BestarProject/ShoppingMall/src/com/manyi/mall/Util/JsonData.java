/**
 * 
 */
package com.manyi.mall.Util;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.manyi.mall.cachebean.GetCityResponse;
import com.manyi.mall.cachebean.GetCountyResponse;
import com.manyi.mall.cachebean.GetProvinceResponse;

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
//    private void jsonFamily(String msg,SQLiteDatabase database){
//        Type listType = new TypeToken<LinkedList<FamilyBean>>(){}.getType();
//        Gson gson = new Gson();
//        LinkedList<FamilyBean> list;
//        FamilyBean bean;
//        list = gson.fromJson(msg, listType);
//        if(list!=null && list.size()>0){
//            for(Iterator<FamilyBean> iterator = list.iterator();iterator.hasNext();){
//                bean = iterator.next();
//                ContentValues contentValues = new ContentValues();
//                contentValues.put("ID", bean.getID());
//                contentValues.put("SchoolPersonnelID", bean.getSchoolPersonnelID());
//                contentValues.put("ContactTitle", bean.getContactTitle());
//                contentValues.put("ContactName", bean.getContactName());
//                contentValues.put("ContactTel", bean.getContactTel());
//                contentValues.put("IDCard", bean.getIDCard());
//                contentValues.put("PortraitPath", bean.getPortraitPath());
//                int a = database.update(FamilyBean.tbName, contentValues, "ID=?", new String[]{bean.getID()+""});
//                if(a == 0){
//                    database.insert(FamilyBean.tbName, null, contentValues);
//                }
//            }
//        }
//    }

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
//	/**
//	 * 入园
//	 * @param msg
//	 */
//	public InSchoolBean jsonInSchool(String msg) {
//        InSchoolBean bean =null;
//        try {
//            JSONObject jsonObject = new JSONObject(msg);
//            bean = new InSchoolBean();
//            bean.setResult(jsonObject.getString("result"));
//            bean.setInfo(jsonObject.getString("info"));
//            bean.setEntertime(jsonObject.getString("entertime"));
//            bean.setLeavetime(jsonObject.getString("leavetime"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return bean;
//    }
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
