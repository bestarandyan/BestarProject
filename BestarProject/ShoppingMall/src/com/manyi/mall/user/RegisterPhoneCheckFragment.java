package com.manyi.mall.user;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.DialogBuilder;
import com.huoqiu.framework.util.GeneratedClassUtils;
import com.huoqiu.framework.util.ManyiUtils;
import com.manyi.mall.BestarApplication;
import com.manyi.mall.MainActivity;
import com.manyi.mall.MainActivity_;
import com.manyi.mall.R;
import com.manyi.mall.Util.JsonData;
import com.manyi.mall.cachebean.BaseResponse;
import com.manyi.mall.cachebean.user.CodeResponse;
import com.manyi.mall.cachebean.user.LoginResponse;
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

@EFragment(R.layout.fragment_register_phone_check)
public class RegisterPhoneCheckFragment extends SuperFragment<Integer> {
	@ViewById(R.id.phone_number_et)
	EditText mPhoneEt;
	@ViewById(R.id.forget_code)
	EditText mCodeEt;
	@ViewById(R.id.checkCodeBtn)
	Button mCheckCodeBtn;

    @ViewById(R.id.getCodeBtn)
	Button mGetCodeBtn;

    @FragmentArg
    String userName;

    @FragmentArg
    String password;

    @FragmentArg
    String realName;

    @FragmentArg
    String type;

    @FragmentArg
    String sex;

    @FragmentArg
    String ProvinceID;

    @FragmentArg
    String CityID;

    @FragmentArg
    String CountyID;

    @FragmentArg
    String address;

    @FragmentArg
    String QQ;

    @FragmentArg
    String SchoolName;

    @FragmentArg
    String ClassNum;

    @FragmentArg
    String StudentNum;

    @FragmentArg
    String CompanyPhone;

    int mTime = 60;//60s  时间
	@FragmentArg
	String phone;
	private String mCode;
	private boolean isFirstEnter = true;

    ProgressDialog mProgress;
    private void showProgress(String msg){
        mProgress = new ProgressDialog(getActivity());
        mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgress.setMessage(msg);
        mProgress.show();
    }
    private void dismiss(){
        if(mProgress!=null){
            mProgress.dismiss();
            mProgress = null;
        }
    }

    Timer mTimer;
    TimerTask mTimerTask;
    @Background
    void register(){
        RequestServerFromHttp request = new RequestServerFromHttp();
        String msg = "";
        if (type.equals("2")){
            msg = request.registerYZ(type,userName,password,realName,sex+"",phone,ProvinceID, CityID, CountyID,  address , QQ, SchoolName, ClassNum, StudentNum,CompanyPhone);
        }else{
            msg = request.registerAgent(type,userName,password,realName,sex+"",phone,ProvinceID, CityID , QQ);
        }
        BaseResponse response = new JsonData().JsonBase(msg);
        if (response.getCode().equals("0")){
           login();
        }else{
            registerFailed();
        }
    }
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
            mGetCodeBtn.setTextColor(Color.parseColor("#ffffff"));
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
                mGetCodeBtn.setText(mTime+"s");
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

	}


    @Click(R.id.getCodeBtn)
    void getCode(){
//        mGetCodeBtn.setClickable(false);
//        mGetCodeBtn.setEnabled(false);
        if (mPhoneEt.getText().toString().length() == 0){
            showDialog("请输入手机号码！");
            return;
        }else if (mPhoneEt.getText().toString().length() != 11 || !mPhoneEt.getText().toString().startsWith("1")){
            showDialog("手机号码格式不正确!");
            return;
        }
        getRegisterCode();
    }

    @Click(R.id.checkCodeBtn)
    void submitCode(){
        ManyiUtils.closeKeyBoard(getActivity(), mPhoneEt);
        String code = mCodeEt.getText().toString();
        if (mPhoneEt.getText().toString().length() == 0){
            showDialog("请输入手机号码！");
            return;
        }else if (mPhoneEt.getText().toString().length() != 11 || !mPhoneEt.getText().toString().startsWith("1")){
            showDialog("手机号码格式不正确!");
            return;
        }
        if (code!=null && code.trim().length() == 0){
            showDialog("请输入验证码！");
        }else if (code .equals(mCode)){
            showProgress("正在验证，请稍候...");
            register();
        }else{
            showDialog("请输入正确的验证码！");
        }
    }


    @UiThread
    void registerFailed(){
        Toast.makeText(getActivity(),"注册失败",Toast.LENGTH_LONG).show();
        dismiss();
    }

    @Background
    public void login() {
        try {
            RequestServerFromHttp request = new RequestServerFromHttp();
            String msg = request.login(userName,password);
            LoginResponse response = new JsonData().JsonLoginMsg(msg);
            if (response.getCode().equals("0")){
                SharedPreferences mySharedPreferences= getActivity().getSharedPreferences("appkey", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = mySharedPreferences.edit();
                editor.putString("appkey", response.getAppKey());
                editor.commit();

                SharedPreferences userInfo= getActivity().getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor1 = userInfo.edit();
                editor1.putString("userName", userName);
                editor1.putString("password", password);
                editor1.commit();

                BestarApplication.getInstance().setAppkey(response.getAppKey());
                BestarApplication.getInstance().setUserName(response.getUserName());
                BestarApplication.getInstance().setRealName(response.getRealName());
                BestarApplication.getInstance().setPassword(password);
                BestarApplication.getInstance().setType((response.getType().equals("园长") || response.getType().equals("2"))?"2":"1");
                gotoNextStep();//userName = {java.lang.String@830038226280}"12554555554"
            }else{
                loginFailed(response.getMessage());
            }
        }catch (Exception e){
            loginFailed("登陆失败，请检查网络连接，或重试！");
        }
    }

    @UiThread
    void loginFailed(String msg){
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
        disimissRequestDialog();
        dismiss();
    }
    @UiThread
    void gotoNextStep(){
        dismiss();
        Intent loginIntent = new Intent(getActivity(),MainActivity_.class);
//        getActivity().overridePendingTransition(R.anim.anim_fragment_in, R.anim.anim_fragment_out);
        getActivity().startActivity(loginIntent);
        getActivity().finish();
    }
    @Background
    void getRegisterCode(){
        RequestServerFromHttp request = new RequestServerFromHttp();
        String msg = request.getRegisterCode(mPhoneEt.getText().toString());
        CodeResponse response = new JsonData().JsonCode(msg);
        String code = response.getCode();
        if (code!=null){
            mCode = response.getYZCode();
            if (code.equals("0") && mCode!=null &&mCode.length()>0){
                showDialog("验证码将发送到:"+mPhoneEt.getText().toString());
                startTimer();
            }else{
                showDialog(response.getMessage());
            }
        }else{
            showDialog("发送失败，请重试！");
        }


    }

	@Override
	public void onDestroy() {
		super.onDestroy();
		ManyiUtils.closeKeyBoard(getActivity(), mPhoneEt);
        cancleTimeTask();
	}

	@UiThread
	public void showDialog(String e) {
		DialogBuilder.showSimpleDialog(e, getBackOpActivity());
	}


	@UiThread
	public void nextError(String e) {
		DialogBuilder.showSimpleDialog(e, getBackOpActivity());
	}

	@Click(R.id.forgetback)
	void back() {
		if (CheckDoubleClick.isFastDoubleClick()) {
			return;
		}
		ManyiUtils.closeKeyBoard(getActivity(), mPhoneEt);
		remove();
	}

}
