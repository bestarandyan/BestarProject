package com.manyi.mall.user;

import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.huoqiu.framework.app.SuperFragment;
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
        notifyCounty();
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
        mSelectAddress.setText(selectProvince);
        getCity();
        mCountyList.setVisibility(View.GONE);
        mSelectAddress.setVisibility(View.VISIBLE);
    }

    @ItemClick(R.id.CityList)
    void cityListItemClick(int position){
        cityPosition = position;
        selectCity = cityList.get(cityPosition).get("CityName").toString();
        mSelectAddress.setText(selectProvince+" - "+selectCity);
        getCounty();
    }

    @ItemClick(R.id.CountyList)
    void countyListItemClick(int position){
        countyPosition = position;
        selectCounty = countyList.get(countyPosition).get("CountyName").toString();
        String address = selectProvince+"  "+selectCity+"  "+selectCounty;
        mSelectAddress.setText(selectProvince+" - "+selectCity+" - "+selectCounty);
        mListLayout.setVisibility(View.GONE);
        mCityList.setVisibility(View.GONE);
        mCountyList.setVisibility(View.GONE);
    }

    @Click(R.id.selectAddress)
    void selectAddess(){
        mListLayout.setVisibility(View.VISIBLE);

    }
}
