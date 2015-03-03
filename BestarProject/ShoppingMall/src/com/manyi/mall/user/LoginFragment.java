package com.manyi.mall.user;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.DialogBuilder;
import com.huoqiu.framework.util.GeneratedClassUtils;
import com.huoqiu.framework.util.ManyiUtils;
import com.manyi.mall.MainActivity;
import com.manyi.mall.R;
import com.manyi.mall.Util.JsonData;
import com.manyi.mall.cachebean.user.LoginResponse;
import com.manyi.mall.common.Constants;
import com.manyi.mall.service.RequestServerFromHttp;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

@SuppressLint({"WorldReadableFiles", "WorldWriteableFiles"})
@EFragment(R.layout.fragment_login)
public class LoginFragment extends SuperFragment<Integer> {
    private final String mPageName = LoginFragment.class.getSimpleName();
    private boolean isfisrt;

    @ViewById(R.id.login_password)
    EditText mLoginPassword;
    @ViewById(R.id.login_username)
    EditText mLoginUsername;

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLoginPassword != null) {
            ManyiUtils.closeKeyBoard(getActivity(), mLoginPassword);
        }
    }

    @AfterViews
    void init(){
        String username = getActivity().getSharedPreferences("userInfo", Activity.MODE_PRIVATE).getString("userName","");
        String password = getActivity().getSharedPreferences("userInfo", Activity.MODE_PRIVATE).getString("password","");
        if (username!=null && username.length() > 0 && password!=null && password.length()>0){
            mLoginUsername.setText(username);
            mLoginPassword.setText(password);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            showRequestDialog("登录", "正在登录，请稍候!");
            loginw();
        }
    }

    @Click(R.id.forget_tv)
    @UiThread
    void forgetv() {
        if (CheckDoubleClick.isFastDoubleClick())
            return;
        ForgetPasswordFragment forgetPasswordFragment = GeneratedClassUtils.getInstance(ForgetPasswordFragment.class);
        Bundle bundle = new Bundle();
        bundle.putString("phone", mLoginUsername.getText().toString().trim());
        forgetPasswordFragment.setArguments(bundle);
        forgetPasswordFragment.tag = ForgetPasswordFragment.class.getName();

        forgetPasswordFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
                R.anim.anim_fragment_close_out);
        forgetPasswordFragment.setManager(getFragmentManager());
        forgetPasswordFragment.show(SHOW_ADD_HIDE);
        ManyiUtils.closeKeyBoard(getActivity(), mLoginPassword);
    }

    @Click(R.id.userRegisterTv)
    @UiThread
    void regisger() {
        if (CheckDoubleClick.isFastDoubleClick())
            return;
        RegisterFragment registerFragment = GeneratedClassUtils.getInstance(RegisterFragment.class);
        Bundle bundle = new Bundle();
        bundle.putString("phone", mLoginUsername.getText().toString().trim());
        registerFragment.setArguments(bundle);
        registerFragment.tag = RegisterFragment.class.getName();

        registerFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
                R.anim.anim_fragment_close_out);
        registerFragment.setManager(getFragmentManager());
        registerFragment.show(SHOW_ADD_HIDE);
        ManyiUtils.closeKeyBoard(getActivity(), mLoginPassword);
    }

    @SuppressLint("CommitPrefEdits")
    @Click(R.id.login_loginbutton)
    void loginbutton() {
        if (CheckDoubleClick.isFastDoubleClick())
            return;
        if (mLoginPassword.getText().toString().trim().length() != 0 && mLoginUsername.getText().toString().trim().length() != 0) {
            showRequestDialog("登录","正在登录，请稍候!");
            loginw();
        } else if (mLoginUsername.getText().toString().trim().length() == 0) {
            DialogBuilder.showSimpleDialog("请输入手机号", getBackOpActivity());

        } else if (mLoginPassword.getText().toString().trim().length() == 0) {
            DialogBuilder.showSimpleDialog("请输入密码", getBackOpActivity());

        }
    }

    @Background
    public void loginw() {

        String password = mLoginPassword.getText().toString().trim();
        String name = mLoginUsername.getText().toString().trim();
        RequestServerFromHttp request = new RequestServerFromHttp();
        String msg = request.login(name,password);
        LoginResponse response = new JsonData().JsonLoginMsg(msg);
        if (response.getCode().equals("0")){
            SharedPreferences mySharedPreferences= getActivity().getSharedPreferences("appkey", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = mySharedPreferences.edit();
            editor.putString("appkey", response.getAppKey());
            editor.commit();

            SharedPreferences userInfo= getActivity().getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor1 = userInfo.edit();
            editor1.putString("userName", name);
            editor1.putString("password", password);
            editor1.commit();
            initMainActivity();
        }else{
            loginFailed(response.getMessage());
        }
    }

    @UiThread
    void loginFailed(String msg){
        Toast.makeText(getActivity(),msg,Toast.LENGTH_LONG).show();
        disimissRequestDialog();
    }

    @UiThread
    void initTab(int state, String alipayAccount) {
        boolean isSkip = getBackOpActivity().getSharedPreferences(Constants.LOGIN_TIMES, 0).getBoolean("isSkip", false);
        switch (state) {
            case Constants.ACCOUNT_REVIEW_SUCCESS:
                if (isSkip || (alipayAccount != null && !TextUtils.isEmpty(alipayAccount))) {
                    initMainActivity();
                }
                break;


        }
    }

    @UiThread
    void initMainActivity() {
        disimissRequestDialog();
        Intent loginIntent = new Intent(getActivity(), GeneratedClassUtils.get(MainActivity.class));
        getActivity().overridePendingTransition(R.anim.anim_fragment_in, R.anim.anim_fragment_out);
        startActivity(loginIntent);
        getActivity().finish();
    }

    @UiThread
    public void loginBack() {
        if (CheckDoubleClick.isFastDoubleClick())
            return;
        ManyiUtils.closeKeyBoard(getActivity(), mLoginPassword);
        remove();
    }

    @UiThread
    void showError(String e) {
        DialogBuilder.showSimpleDialog(e, getBackOpActivity());
    }
}
