package com.manyi.mall.user;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.DialogBuilder;
import com.huoqiu.framework.util.GeneratedClassUtils;
import com.huoqiu.framework.util.ManyiUtils;
import com.manyi.mall.BestarApplication;
import com.manyi.mall.R;
import com.manyi.mall.utils.JsonData;
import com.manyi.mall.cachebean.BaseResponse;
import com.manyi.mall.cachebean.user.CodeResponse;
import com.manyi.mall.service.RequestServerFromHttp;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@EFragment(R.layout.fragment_forget_password)
public class ForgetPasswordFragment extends SuperFragment<Integer> {
	@ViewById(R.id.forget_code)
	EditText mForgetCode;
	@ViewById(R.id.forget_username)
    EditText mForgetPhone;
	@ViewById(R.id.getCodeNextBtn)
	Button logincode;
    @ViewById(R.id.getPswCodeBtn)
    Button mGetCodeBtn;
	@FragmentArg
	String phone;
    Timer mTimer;
    TimerTask mTimerTask;
    int mTime = 60;//60s  时间
    private String mCode;
	private boolean isFirstEnter = true;
	
	public static boolean isPhoneNumberValid(String text) {
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(text);
		return m.matches();
	}
    @UiThread
    void startTimer(){
        if (mTime<=0){
            mTime = 60;
        }
        setBtnState(false);
        cancleTimeTask();
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            public void run() {
                mTime--;
                handler.sendEmptyMessage(0);
            }
        };
        mTimer.schedule(mTimerTask, 1000L, 1000L);
    }
    private void setBtnState(boolean state){
        mGetCodeBtn.setClickable(state);
        mGetCodeBtn.setEnabled(state);
        if (state){
            mGetCodeBtn.setTextColor(getResources().getColor(R.color.app_theme_color));
            mGetCodeBtn.setBackgroundResource(R.drawable.selector_mine_exit_btn_bg);
        }else{
            mGetCodeBtn.setTextColor(Color.parseColor("#999999"));
            mGetCodeBtn.setBackgroundResource(R.drawable.shape_mine_exit_btn_bg_pre);
        }
    }
    private void cancleTimeTask(){
        if (mTimer!=null){
            mTimer.cancel();
            mTimer = null;
            if (mTimerTask!=null){
                mTimerTask.cancel();
                mTimerTask = null;
            }
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0){
                mGetCodeBtn.setText(mTime+"秒后重发");
                if (mTime<=0){
                    mTime = 60;
                    cancleTimeTask();
                    mGetCodeBtn.setText("获取验证码");
                    setBtnState(true);
                }
            }
        }
    };
	/**
	 * Initialize fragment, show keyboard after animation.
	 */
	@AfterViews
	void init() {
        String userName = BestarApplication.getInstance().getUserName();
        if (userName!=null && userName.length()>0){
            mForgetPhone.setText(userName);
            mForgetPhone.setEnabled(false);
        }else{
            mForgetPhone.setEnabled(true);
        }

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		ManyiUtils.closeKeyBoard(getActivity(), mForgetCode);
        cancleTimeTask();
	}

	@UiThread
	public void showDialog(String e) {
		DialogBuilder.showSimpleDialog(e, getBackOpActivity());
	}



	@UiThread
	@Click(R.id.getCodeNextBtn)
	public void nextSuccess() {
		if (CheckDoubleClick.isFastDoubleClick()) {
			return;
		}
		if (TextUtils.isEmpty(mForgetPhone.getText().toString()) || TextUtils.isEmpty(mForgetPhone.getText().toString().trim())) {
			DialogBuilder.showSimpleDialog("请输入电话号码", getBackOpActivity());
			return;
		}else if (TextUtils.isEmpty(mForgetCode.getText().toString()) || TextUtils.isEmpty(mForgetCode.getText().toString().trim())) {
			DialogBuilder.showSimpleDialog("请输入验证码", getBackOpActivity());
			return;
		}else{
            getBackPsw();
        }
	}

    @Background
    void getBackPsw(){
        String mobile = mForgetPhone.getText().toString().trim();
        RequestServerFromHttp request = new RequestServerFromHttp();
        String msg = request.getForgetPsw(mobile);
        BaseResponse response = new JsonData().JsonBase(msg);
        showDialog(response.getMessage());
        if (response.getCode().equals("0")){
            remove();
        }
    }

	@Click(R.id.forgetback)
	void back() {
		if (CheckDoubleClick.isFastDoubleClick()) {
			return;
		}
		ManyiUtils.closeKeyBoard(getActivity(), mForgetCode);
		remove();
	}

    @Click(R.id.registerBtn)
    @UiThread
    void regisger() {
        if (CheckDoubleClick.isFastDoubleClick())
            return;
        RegisterFragment registerFragment = GeneratedClassUtils.getInstance(RegisterFragment.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("phone", mLoginUsername.getText().toString().trim());
//        registerFragment.setArguments(bundle);
        registerFragment.tag = RegisterFragment.class.getName();

        registerFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
                R.anim.anim_fragment_close_out);
        registerFragment.setManager(getFragmentManager());
        registerFragment.show(SHOW_ADD_HIDE);
//        ManyiUtils.closeKeyBoard(getActivity(), mLoginPassword);
    }
	
	@Click(R.id.getPswCodeBtn)
	void getCodeClick() {
		if (CheckDoubleClick.isFastDoubleClick()) {
			return;
		}
        if (TextUtils.isEmpty(mForgetPhone.getText().toString()) || TextUtils.isEmpty(mForgetPhone.getText().toString().trim())) {
            DialogBuilder.showSimpleDialog("请输入电话号码", getBackOpActivity());
            return;
        }else if (mForgetPhone.getText().toString().length() != 11 || !mForgetPhone.getText().toString().startsWith("1")){
            showDialog("手机号码格式不正确!");
            return;
        }else{
            getCode();
        }

	}
    CodeResponse response =null;
    @Background
    void getCode(){
        try{
            String mobile = mForgetPhone.getText().toString().trim();
            RequestServerFromHttp request = new RequestServerFromHttp();
            String msg = request.getForgetPswCode(mobile);
            if (msg!=null && !msg.equals("404") && !msg.equals("")) {
                response = new JsonData().JsonCode(msg);
                String code = response.getCode();
                if (code != null) {
                    mCode = response.getYZCode();
                    if (code.equals("0") && mCode != null && mCode.length() > 0) {
                        showDialog("验证码将发送到:" + mForgetPhone.getText().toString());
                        startTimer();
                    } else {
                        showDialog(response.getMessage());
                    }
                } else {
                    showDialog("发送失败，请重试！");
                }
            }else {
                showDialog("发送失败，请重试！");
            }
        }catch (Exception e){
            showDialog("发送失败，请重试！");
        }

    }

}
