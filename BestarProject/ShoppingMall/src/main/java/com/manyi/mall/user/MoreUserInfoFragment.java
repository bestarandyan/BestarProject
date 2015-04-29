package com.manyi.mall.user;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.util.DialogBuilder;
import com.huoqiu.framework.util.GeneratedClassUtils;
import com.huoqiu.framework.util.ManyiUtils;
import com.manyi.mall.BestarApplication;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.user.UserInfoResponse;
import com.manyi.mall.utils.JsonData;
import com.manyi.mall.cachebean.BaseResponse;
import com.manyi.mall.service.RequestServerFromHttp;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
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
    EditText mRealNameEt;

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

    @ViewById(R.id.yeymcLayout)
    RelativeLayout mYeymcLayout;

    @ViewById(R.id.yeydhLayout)
    RelativeLayout mYeydhLayout;

    @ViewById(R.id.addressTitleTv)
    TextView mAddressTitleTv;

    @ViewById(R.id.bjLayout)
    RelativeLayout mBjLayout;

    @ViewById(R.id.studentsLayout)
    RelativeLayout mStudentsLayout;

    @ViewById(R.id.yeydhLine)
    View mYeydhLine;

    @ViewById(R.id.yeymcLine)
    View mYeymcLine;

    @ViewById(R.id.bjLine)
    View mBjLine;

    @ViewById(R.id.studentsLine)
    View mStudentsLine;

    @ViewById(R.id.editBtn)
    ImageButton mEditBtn;

    @FragmentArg
    UserInfoResponse mUserInfoResponse;
    private String selectProvince = "";
    private String selectCity = "";
    private String selectCounty = "";

    private String type = "1";//1渠道商2园长
    private int sex = 0;//男0女1
    private boolean isEditing = false;


    void setViewValue(){
        if (mUserInfoResponse==null){
            return;
        }
        mRealNameEt.setText(mUserInfoResponse.RealName);
        mGenderTv.setText(mUserInfoResponse.Sex?"女":"男");
        sex = mUserInfoResponse.Sex?1:0;
        mPhoneNumberEt.setText(mUserInfoResponse.Phone);
        mSchoolNameEt.setText(mUserInfoResponse.SchoolName);
        mSchoolPhone.setText(mUserInfoResponse.CompanyPhone);
        mQQEt.setText(mUserInfoResponse.QQ);
        mClassCountEt.setText(mUserInfoResponse.ClassNum);
        mStudentCountEt.setText(mUserInfoResponse.StudentNum);
        mAddress.setText(mUserInfoResponse.ProvinceName+"  "+mUserInfoResponse.CityName+" "+mUserInfoResponse.Address);
    }

    @Click(R.id.editBtn)
    void clickEditUserInfo(){
        if(isEditing){
            if (checkUserInfo(type.equals("2")?"幼儿园地址！":"所在地区！")){
                editUserInfo();
            }
        }else{
            isEditing = true;
            mEditBtn.setBackgroundColor(Color.TRANSPARENT);
            mEditBtn.setImageResource(R.drawable.selector_comple);
            setEnable(true);
        }
    }

    private void setEnable(boolean enable){
        mPhoneNumberEt.setEnabled(enable);
        mSchoolNameEt.setEnabled(enable);
        mSchoolPhone.setEnabled(enable);
        mQQEt.setEnabled(enable);
        mClassCountEt.setEnabled(enable);
        mStudentCountEt.setEnabled(enable);
        mAddress.setEnabled(enable);
        mGenderTv.setEnabled(enable);
        mRealNameEt.setEnabled(enable);
    }

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
        type = BestarApplication.getInstance().getType();
        changeInfoFromType();
        setViewValue();
    }

    @Override
    public boolean canFragmentGoback(int from) {
            return super.canFragmentGoback(from);
    }

    @Click(R.id.youeryuanaddress)
    void getAddress(){
        SelectAddressFragment fragment = GeneratedClassUtils.getInstance(SelectAddressFragment.class);
        fragment.tag = SelectAddressFragment.class.getName();
        fragment.setSelectListener(new SelectListener() {
            @Override
            public void onSelected(Object o) {
                Bundle bundle = (Bundle) o;
                mUserInfoResponse.ProvinceID = bundle.getString("provinceId");
                mUserInfoResponse.CityID = bundle.getString("cityId");
                mUserInfoResponse.CountyID = bundle.getString("countyId");
                mUserInfoResponse.Address = bundle.getString("address");
                String addressStr = "";
                if (mUserInfoResponse.Address.length()>=25){
                    addressStr = mUserInfoResponse.Address.substring(0,22)+"...";
                }else{
                    addressStr = mUserInfoResponse.Address;
                }
                mAddress.setText(addressStr);
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

    private void changeInfoFromType(){
        if (type.equals("1")){
            mYeydhLayout.setVisibility(View.GONE);
            mYeymcLayout.setVisibility(View.GONE);
            mBjLayout.setVisibility(View.GONE);
            mStudentsLayout.setVisibility(View.GONE);
            mBjLine.setVisibility(View.GONE);
            mStudentsLine.setVisibility(View.GONE);
            mYeydhLine.setVisibility(View.GONE);
            mYeymcLine.setVisibility(View.GONE);
            mAddressTitleTv.setText("所在地区");
        }else if(type.equals("2")){
            mYeydhLayout.setVisibility(View.VISIBLE);
            mYeymcLayout.setVisibility(View.VISIBLE);
            mBjLayout.setVisibility(View.VISIBLE);
            mStudentsLayout.setVisibility(View.VISIBLE);
            mBjLine.setVisibility(View.VISIBLE);
            mStudentsLine.setVisibility(View.VISIBLE);
            mYeydhLine.setVisibility(View.VISIBLE);
            mYeymcLine.setVisibility(View.VISIBLE);
            mAddressTitleTv.setText("幼儿园地址");
        }

    }

    private boolean checkUserInfo(String address){
        if (mRealNameEt.getText().toString().length() == 0){
            onSMSError("请输入真实姓名！");
            return false;
        }else if (mPhoneNumberEt.getText().toString().length() == 0){
            onSMSError("请输入联系电话！");
            return false;
        }else if (type.equals("2")&& mSchoolNameEt.getText().toString().length() == 0){
            onSMSError("请输入幼儿园名称！");
            return false;
        }else if (type.equals("2")&& mSchoolPhone.getText().toString().length() == 0){
            onSMSError("请输入幼儿园电话！");
            return false;
        }else if (mAddress.getText().toString().length() == 0){
            onSMSError("请选择"+address);
            return false;
        }else{
            return true;
        }
    }

    @Background
    void editUserInfo(){
        RequestServerFromHttp request = new RequestServerFromHttp();
        String realName = mRealNameEt.getText().toString().trim();
        String phone = mPhoneNumberEt.getText().toString().trim();
        String Address = mAddress.getText().toString().trim();
        String QQ = mQQEt.getText().toString().trim();
        String SchoolName = mSchoolNameEt.getText().toString().trim();
        String ClassNum = mClassCountEt.getText().toString().trim();
        String StudentNum = mStudentCountEt.getText().toString().trim();
        String schoolPhone = mSchoolPhone.getText().toString().trim();
        String msg = "";
        msg = request.updateInfo(realName,sex+"",phone,mUserInfoResponse.ProvinceID,mUserInfoResponse.CityID,mUserInfoResponse.CountyID,Address,QQ,SchoolName,ClassNum,StudentNum,schoolPhone);
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
        Toast.makeText(getActivity(),"修改成功",Toast.LENGTH_LONG).show();
        BestarApplication.getInstance().setRealName(mRealNameEt.getText().toString());
        setEnable(false);
        isEditing = false;
        mEditBtn.setImageResource(R.drawable.selector_edit_info_btn);
        remove();
    }

    @UiThread
    void registerFailed(){
        Toast.makeText(getActivity(),"修改失败",Toast.LENGTH_LONG).show();
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
                    mShengFenTv.setText("代理商");
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
