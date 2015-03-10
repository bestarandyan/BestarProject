package com.manyi.mall.user;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.util.DialogBuilder;
import com.huoqiu.framework.util.GeneratedClassUtils;
import com.huoqiu.framework.util.ManyiUtils;
import com.manyi.mall.R;
import com.manyi.mall.Util.JsonData;
import com.manyi.mall.cachebean.BaseResponse;
import com.manyi.mall.service.RequestServerFromHttp;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;
import java.util.Map;

@EFragment(R.layout.fragment_more_user_info)
public class MoreUserInfoFragment extends SuperFragment<Object> {
    @ViewById(R.id.genderTv)
    TextView mGenderTv;

    @ViewById(R.id.youeryuanaddress)
    TextView mAddress;

    @ViewById(R.id.shenfenValue)
    TextView mShengFenTv;


    @ViewById(R.id.goonBtn)
    ImageButton mGoonBtn;

    @ViewById(R.id.real_name_et)
    TextView mRealNameEt;

    @ViewById(R.id.phone_number_et)
    TextView mPhoneNumberEt;

    @ViewById(R.id.youeryuanname)
    TextView mSchoolNameEt;

    @ViewById(R.id.youeryuanphone)
    TextView mSchoolPhone;

    @ViewById(R.id.qqEt)
    TextView mQQEt;

    @ViewById(R.id.classCountEt)
    TextView mClassCountEt;

    @ViewById(R.id.studentCountEt)
    TextView mStudentCountEt;

    @FragmentArg
    String userName;

    @FragmentArg
    String password;

    List<Map<String,String>> provinceList;
    List<Map<String,String>> cityList;
    List<Map<String,String>> countyList;

    private int provincePosition = 0;
    private int cityPosition = 0;
    private int countyPosition = 0;

    private String selectProvince = "";
    private String selectCity = "";
    private String selectCounty = "";

    private String type = "1";//1渠道商2园长
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
    @UiThread
    public void onSMSError(String e) {
        if (getActivity() == null || getActivity().isFinishing()) {
            return;
        }
        DialogBuilder.showSimpleDialog(e, getActivity());
    }
    @Click(R.id.register_back)
    void back(){
        remove();
    }
    @AfterViews
    void init(){
    }

    @Override
    public boolean canFragmentGoback(int from) {
            return super.canFragmentGoback(from);
    }
    String ProvinceID,CityID,CountyID ;
    String address;

    @Click(R.id.youeryuanaddress)
    void getAddress(){
        SelectAddressFragment fragment = GeneratedClassUtils.getInstance(SelectAddressFragment.class);
        fragment.tag = SelectAddressFragment.class.getName();
        fragment.setSelectListener(new SelectListener() {
            @Override
            public void onSelected(Object o) {
                Bundle bundle = (Bundle) o;
                ProvinceID = bundle.getString("provinceId");
                CityID = bundle.getString("cityId");
                CountyID = bundle.getString("countyId");
                address = bundle.getString("address");
                mAddress.setText(address);
            }

            @Override
            public void onCanceled() {

            }
        });
        fragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
                R.anim.anim_fragment_close_out);
        fragment.setManager(getFragmentManager());
        fragment.show(SHOW_ADD_HIDE);
        ManyiUtils.closeKeyBoard(getActivity(), mStudentCountEt);
    }


    @Click(R.id.goonBtn)
    void goon(){
        if (checkUserInfo()){
            register();
        }
    }

    private boolean checkUserInfo(){
        if (mRealNameEt.getText().toString().length() == 0){
            onSMSError("请输入真实姓名！");
            return false;
        }else if (mPhoneNumberEt.getText().toString().length() == 0){
            onSMSError("请输入联系电话！");
            return false;
        }else if (mSchoolNameEt.getText().toString().length() == 0){
            onSMSError("请输入幼儿园名称！");
            return false;
        }else if (mSchoolPhone.getText().toString().length() == 0){
            onSMSError("请输入幼儿园电话！");
            return false;
        }else if (mAddress.getText().toString().length() == 0){
            onSMSError("请选择幼儿园地址！");
            return false;
        }else{
            return true;
        }
    }

    @Background
    void register(){
        RequestServerFromHttp request = new RequestServerFromHttp();
        String realName = mRealNameEt.getText().toString().trim();
        String phone = mPhoneNumberEt.getText().toString().trim();
        String ProvinceID = provinceList.get(provincePosition).get("ID");
        String CityID = cityList.get(cityPosition).get("ID");
        String CountyID = countyList.get(countyPosition).get("ID");
        String Address = mAddress.getText().toString().trim();
        String QQ = mQQEt.getText().toString().trim();
        String SchoolName = mSchoolNameEt.getText().toString().trim();
        String ClassNum = mClassCountEt.getText().toString().trim();
        String StudentNum = mStudentCountEt.getText().toString().trim();
        String msg = "";//request.register(type,userName,password,realName,sex+"",phone,ProvinceID, CityID, CountyID, Address, QQ, SchoolName, ClassNum, StudentNum);
        if (type.equals("2")){
//            msg = request.registerYZ(type,userName,password,realName,sex+"",phone,ProvinceID, CityID, CountyID,  address , QQ, SchoolName, ClassNum, StudentNum,CompanyPhone);
        }else{
            msg = request.registerAgent(type,userName,password,realName,sex+"",phone,ProvinceID, CityID , QQ);
        }
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
        gotoNextStep();
    }
    @UiThread
    void gotoNextStep(){
        RegisterPhoneCheckFragment fragment = GeneratedClassUtils.getInstance(RegisterPhoneCheckFragment.class);
        fragment.tag = RegisterPhoneCheckFragment.class.getName();
        fragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
                R.anim.anim_fragment_close_out);
        fragment.setManager(getFragmentManager());
        fragment.show(SHOW_ADD_HIDE);
        ManyiUtils.closeKeyBoard(getActivity(), mStudentCountEt);
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
                    type = "1";
                }else if (menuItem.getItemId() == R.id.menu_shengFen_type2){
                    mShengFenTv.setText("园长");
                    type = "2";
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
