package com.manyi.mall.user;

import android.annotation.TargetApi;
import android.drm.DrmStore;
import android.os.Build;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.manyi.mall.R;
import com.manyi.mall.Util.JsonData;
import com.manyi.mall.cachebean.BaseResponse;
import com.manyi.mall.cachebean.GetProvinceRequest;
import com.manyi.mall.cachebean.GetProvinceResponse;
import com.manyi.mall.service.RequestServerFromHttp;
import com.manyi.mall.service.UcService;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.w3c.dom.Text;

import java.util.List;
import java.util.Map;

@EFragment(R.layout.fragment_register_next)
public class RegisterNextFragment extends ImageLoaderFragment {
    @ViewById(R.id.genderTv)
    TextView mGenderTv;

    @ViewById(R.id.youeryuanaddress)
    TextView mAddress;

    @ViewById(R.id.shenfenValue)
    TextView mShengFenTv;

    @ViewById(R.id.ProvinceList)
    ListView mProvinceList;

    @ViewById(R.id.CityList)
    ListView mCityList;

    @ViewById(R.id.CountyList)
    ListView mCountyList;

    @ViewById(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @ViewById(R.id.goonBtn)
    Button mGoonBtn;

    @ViewById(R.id.real_name_et)
    EditText mRealNameEt;

    @ViewById(R.id.phone_number_et)
    EditText mPhoneNumberEt;

    @ViewById(R.id.youeryuanname)
    EditText mSchoolNameEt;

    @ViewById(R.id.youeryuanphone)
    EditText mSchoolPhone;

    @ViewById(R.id.qqEt)
    EditText mQQEt;

    @ViewById(R.id.classCountEt)
    EditText mClassCountEt;

    @ViewById(R.id.studentCountEt)
    EditText mStudentCountEt;

    @FragmentArg
    String userName;

    @FragmentArg
    String password;

    UcService mUcService;
    List<Map<String,String>> provinceList;
    List<Map<String,String>> cityList;
    List<Map<String,String>> countyList;

    private int provincePosition = 0;
    private int cityPosition = 0;
    private int countyPosition = 0;

    private String selectProvince = "";
    private String selectCity = "";
    private String selectCounty = "";

    private int type = 1;//1渠道商2园长
    private int sex = 0;//男0女1
    @Click(R.id.genderTv)
    void selectGender(){
        showGenderPopmenu();
    }
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void showGenderPopmenu(){
        PopupMenu popup = new PopupMenu(getActivity(), mGenderTv);
        MenuInflater inflater = popup.getMenuInflater();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.menu_gender_type1){
                    mGenderTv.setText("男");
                    sex = 0;
                }else if (menuItem.getItemId() == R.id.menu_gender_type2){
                    mGenderTv.setText("女");
                    sex = 1;
                }
                return false;
            }
        });
        inflater.inflate(R.menu.actions_gender, popup.getMenu());
        popup.show();
        popup.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu popupMenu) {
            }
        });
    }

    @Click(R.id.youeryuanaddress)
    void getAddress(){
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
        mDrawerLayout.openDrawer(Gravity.RIGHT);
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
        getCity();
        mCountyList.setVisibility(View.GONE);
    }

    @ItemClick(R.id.CityList)
    void cityListItemClick(int position){
        cityPosition = position;
        selectCity = cityList.get(cityPosition).get("CityName").toString();
        getCounty();
    }

    @ItemClick(R.id.CountyList)
    void countyListItemClick(int position){
        countyPosition = position;
        selectCounty = countyList.get(countyPosition).get("CountyName").toString();
        mDrawerLayout.closeDrawer(Gravity.RIGHT);
        mAddress.setText(selectProvince+"  "+selectCity+"  "+selectCounty);
    }

    @Click(R.id.goonBtn)
    void goon(){
        register();
    }

    @Background
    void register(){
        RequestServerFromHttp request = new RequestServerFromHttp();
        String realName = mRealNameEt.getText().toString().trim();
        String phone = mPhoneNumberEt.getText().toString().trim();
        String ProvinceID = provinceList.get(provincePosition).get("ID");
        String CityID = cityList.get(provincePosition).get("ID");
        String CountyID = countyList.get(provincePosition).get("ID");
        String Address = mAddress.getText().toString().trim();
        String QQ = mQQEt.getText().toString().trim();
        String SchoolName = mSchoolNameEt.getText().toString().trim();
        String ClassNum = mClassCountEt.getText().toString().trim();
        String StudentNum = mStudentCountEt.getText().toString().trim();
        String msg = request.register(userName,password,realName,sex+"",phone,ProvinceID, CityID, CountyID, Address, QQ, SchoolName, ClassNum, StudentNum);
        System.out.print(msg);
        BaseResponse response = new JsonData().JsonBase(msg);
        if (response.getCode().equals("0")){
            registerSuccess();
        }else{
            registerFailed();
        }
    }

    @UiThread
    void registerSuccess(){
        Toast.makeText(getActivity(),"注册成功",Toast.LENGTH_LONG).show();
    }

    @UiThread
    void registerFailed(){
        Toast.makeText(getActivity(),"注册失败",Toast.LENGTH_LONG).show();
    }

    @Click(R.id.shenfenValue)
    void selectShenFen(){
        showShengFenPopmenu();
    }
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void showShengFenPopmenu(){
        PopupMenu popup = new PopupMenu(getActivity(), mShengFenTv);
        MenuInflater inflater = popup.getMenuInflater();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.menu_shengFen_type1){
                    mShengFenTv.setText("商家");
                    type = 1;
                }else if (menuItem.getItemId() == R.id.menu_shengFen_type2){
                    mShengFenTv.setText("园长");
                    type = 2;
                }
                return false;
            }
        });
        inflater.inflate(R.menu.actions_shengfen, popup.getMenu());
        popup.show();
        popup.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu popupMenu) {
            }
        });
    }
}
