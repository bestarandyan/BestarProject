package com.manyi.mall.user;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.util.DialogBuilder;
import com.manyi.mall.R;
import com.manyi.mall.Util.JsonData;
import com.manyi.mall.service.RequestServerFromHttp;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.w3c.dom.Text;

import java.util.List;
import java.util.Map;

/**
 * Created by bestar on 2015/3/5.
 */
@EFragment(R.layout.fragment_select_address)
public class SelectAddressFragment extends SuperFragment {
    List<Map<String,String>> provinceList;
    List<Map<String,String>> cityList;
    List<Map<String,String>> countyList;

    private int provincePosition = 0;
    private int cityPosition = 0;
    private int countyPosition = 0;

    private String selectProvince = "";
    private String selectCity = "";
    private String selectCounty = "";
    @ViewById(R.id.ProvinceList)
    ListView mProvinceList;

    @ViewById(R.id.CityList)
    ListView mCityList;

    @ViewById(R.id.CountyList)
    ListView mCountyList;

    @ViewById(R.id.selectAddress)
    TextView mSelectAddress;

    @ViewById(R.id.ListLayout)
    LinearLayout mListLayout;

    @ViewById(R.id.inputAddressEt)
    EditText mInputAddressEt;

    @AfterViews
    void init(){
        getProvince();
    }


    @Background
    void getProvince(){
        RequestServerFromHttp request = new RequestServerFromHttp();
        String msg = request.getProvince();
        JsonData jsonData = new JsonData();
        provinceList = jsonData.jsonProvinceMsg(msg, null);
        notifyProvince();
    }
    @Background
    void getCity(){
        RequestServerFromHttp request = new RequestServerFromHttp();
        String provinceId = provinceList.get(provincePosition).get("ID");
        String msg = request.getCity(provinceId);
        JsonData jsonData = new JsonData();
        cityList = jsonData.jsonCityMsg(msg, null);
        notifyCity();
    }
    @Background
    void getCounty(){
        RequestServerFromHttp request = new RequestServerFromHttp();
        String cityId = cityList.get(cityPosition).get("ID");
        String msg = request.getCounty(cityId);
        JsonData jsonData = new JsonData();
        countyList = jsonData.jsonCountyMsg(msg, null);
        if (countyList!= null && countyList.size()>0){
            notifyCounty();
        }else{
            hideListLayout();
        }

    }

    @UiThread
    void notifyProvince(){
        SimpleAdapter adapter = new SimpleAdapter(getActivity(),provinceList,R.layout.item_content,new String[]{"ProvinceName"},new int[]{R.id.contentTv});
        mProvinceList.setAdapter(adapter);
    }

    @UiThread
    void notifyCity(){
        SimpleAdapter adapter = new SimpleAdapter(getActivity(),cityList,R.layout.item_content,new String[]{"CityName"},new int[]{R.id.contentTv});
        mCityList.setAdapter(adapter);
        mCityList.setVisibility(View.VISIBLE);
    }

    @UiThread
    void notifyCounty(){
        SimpleAdapter adapter = new SimpleAdapter(getActivity(),countyList,R.layout.item_content,new String[]{"CountyName"},new int[]{R.id.contentTv});
        mCountyList.setAdapter(adapter);
        mCountyList.setVisibility(View.VISIBLE);
    }

    @ItemClick(R.id.ProvinceList)
    void provinceListItemClick(int position){
        provincePosition = position;
        selectProvince = provinceList.get(provincePosition).get("ProvinceName").toString();
        address = selectProvince;
        mSelectAddress.setText(address);
        getCity();
        mCountyList.setVisibility(View.GONE);
        mSelectAddress.setVisibility(View.VISIBLE);
        mSelectAddress.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.shangla, 0);
    }

    @ItemClick(R.id.CityList)
    void cityListItemClick(int position){
        cityPosition = position;
        selectCity = cityList.get(cityPosition).get("CityName").toString();
        address = selectProvince+" - "+selectCity;
        mSelectAddress.setText(address);
        getCounty();
        mSelectAddress.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.shangla, 0);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @ItemClick(R.id.CountyList)
    void countyListItemClick(int position){
        countyPosition = position;
        selectCounty = countyList.get(countyPosition).get("CountyName").toString();
        address = selectProvince+" - "+selectCity+" - "+selectCounty;
        mSelectAddress.setText(address);
        hideListLayout();
    }
    String address;
    @UiThread
    void hideListLayout(){
        countyPosition = 0;
        mListLayout.setVisibility(View.GONE);
        mCityList.setVisibility(View.GONE);
        mCountyList.setVisibility(View.GONE);
        mSelectAddress.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.xiala, 0);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Click(R.id.selectAddress)
    void selectAddess(){
        if (mListLayout.getVisibility() == View.VISIBLE){
            mListLayout.setVisibility(View.GONE);
            mSelectAddress.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.xiala, 0);
        }else{
            mListLayout.setVisibility(View.VISIBLE);
            mSelectAddress.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.shangla, 0);
        }

    }

    @Click(R.id.addressBack)
    void back(){
        remove();
    }

    @Click(R.id.completeBtn)
    void complete(){
        if (mInputAddressEt.getText().toString().trim().length() == 0){
            DialogBuilder.showSimpleDialog("请输入详细地址",getActivity());
        }else{
            Bundle bundle =new Bundle();
            String provinceId = provinceList.get(provincePosition).get("ID");
            String cityId = cityList.get(cityPosition).get("ID");
            String countyId = (countyList!=null && countyList.size()>0)?countyList.get(countyPosition).get("ID"):"";
            bundle.putString("provinceId",provinceId);
            bundle.putString("cityId",cityId);
            bundle.putString("countyId",countyId);
            bundle.putString("address",address+"  "+mInputAddressEt.getText().toString());
            notifySelected(bundle);
        }
    }
}
