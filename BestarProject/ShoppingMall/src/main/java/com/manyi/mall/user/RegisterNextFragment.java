package com.manyi.mall.user;

import android.annotation.TargetApi;
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

import com.huoqiu.framework.util.DialogBuilder;
import com.huoqiu.framework.util.GeneratedClassUtils;
import com.huoqiu.framework.util.ManyiUtils;
import com.manyi.mall.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_register_next)
public class RegisterNextFragment extends ImageLoaderFragment {
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

    @FragmentArg
    String userName;

    @FragmentArg
    String password;
    String address;


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


    @Click(R.id.youeryuanaddress)
    void getAddress(){
        SelectAddressFragment fragment = GeneratedClassUtils.getInstance(SelectAddressFragment.class);
        fragment.tag = SelectAddressFragment.class.getName();
        Bundle bundle = new Bundle();
        bundle.putString("type",type);
        fragment.setArguments(bundle);
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
            gotoNextStep();
        }
    }

    private boolean checkUserInfo(){
        if (mRealNameEt.getText().toString().length() == 0){
            onSMSError("请输入真实姓名！");
            return false;
        }else if (mPhoneNumberEt.getText().toString().length() == 0){
            onSMSError("请输入联系电话！");
            return false;
        }else if (mSchoolNameEt.getText().toString().length() == 0 && type.equals("2")){
            onSMSError("请输入幼儿园名称！");
            return false;
        }else if (mSchoolPhone.getText().toString().length() == 0  && type.equals("2")){
            onSMSError("请输入幼儿园电话！");
            return false;
        }else if (mAddress.getText().toString().length() == 0){
            if (type.equals("2")){
                onSMSError("请选择幼儿园地址！");
            }else{
                onSMSError("请选择所在地区！");
            }
            return false;
        }else if(mQQEt.getText().toString().length() == 0 ){
            onSMSError("请输入您的QQ号码！");
            return false;
        }else if(mClassCountEt.getText().toString().length() == 0 && type.equals("2")){
            onSMSError("请输入班级数！");
            return false;
        }else if(mStudentCountEt.getText().toString().length() == 0 && type.equals("2")){
            onSMSError("请输入学生人数！");
            return false;
        }else{
            return true;
        }
    }
    String ProvinceID,CityID,CountyID ;


    @UiThread
    void gotoNextStep(){
        String realName = mRealNameEt.getText().toString().trim();
        String phone = mPhoneNumberEt.getText().toString().trim();
        String QQ = mQQEt.getText().toString().trim();
        String SchoolName = mSchoolNameEt.getText().toString().trim();
        String ClassNum = mClassCountEt.getText().toString().trim();
        String StudentNum = mStudentCountEt.getText().toString().trim();
        RegisterPhoneCheckFragment fragment = GeneratedClassUtils.getInstance(RegisterPhoneCheckFragment.class);
        fragment.tag = RegisterPhoneCheckFragment.class.getName();
        Bundle bundle = new Bundle();
        bundle.putString("userName",userName);
        bundle.putString("password",password);
        bundle.putString("realName",realName);
        bundle.putString("phone",phone);
        bundle.putString("CompanyPhone",mSchoolPhone.getText().toString().trim());
        bundle.putString("QQ",QQ);
        bundle.putString("SchoolName",type.equals("2")?SchoolName:"");
        bundle.putString("ClassNum",type.equals("2")?ClassNum:"");
        bundle.putString("StudentNum",type.equals("2")?StudentNum:"");
        bundle.putString("type",type);
        bundle.putString("sex",sex+"");
        bundle.putString("ProvinceID",ProvinceID);
        bundle.putString("CityID",CityID);
        bundle.putString("CountyID",CountyID);
        bundle.putString("address",address);
        fragment.setArguments(bundle);
        fragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
                R.anim.anim_fragment_close_out);
        fragment.setManager(getFragmentManager());
        fragment.show(SHOW_ADD_HIDE);
        ManyiUtils.closeKeyBoard(getActivity(), mStudentCountEt);
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
                    changeInfoFromType();
                }else if (menuItem.getItemId() == R.id.menu_shengFen_type2){
                    mShengFenTv.setText("园长");
                    type = "2";
                    changeInfoFromType();
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
}
