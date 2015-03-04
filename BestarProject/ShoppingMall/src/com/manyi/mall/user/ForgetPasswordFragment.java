package com.manyi.mall.user;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.DialogBuilder;
import com.huoqiu.framework.util.ManyiUtils;
import com.manyi.mall.R;
import com.manyi.mall.Util.JsonData;
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
	@FragmentArg
	String phone;
	private long mCode;
	private boolean isFirstEnter = true;
	
	public static boolean isPhoneNumberValid(String text) {
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(text);
		return m.matches();
	}

	/**
	 * Initialize fragment, show keyboard after animation.
	 */
	@AfterViews
	void init() {

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		ManyiUtils.closeKeyBoard(getActivity(), mForgetCode);
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
	
	@Click(R.id.getPswCodeBtn)
	void getCodeClick() {
		if (CheckDoubleClick.isFastDoubleClick()) {
			return;
		}
        if (TextUtils.isEmpty(mForgetPhone.getText().toString()) || TextUtils.isEmpty(mForgetPhone.getText().toString().trim())) {
            DialogBuilder.showSimpleDialog("请输入电话号码", getBackOpActivity());
            return;
        }else{
            getCode();
        }

	}
    CodeResponse response =null;
    @Background
    void getCode(){
        String mobile = mForgetPhone.getText().toString().trim();
        RequestServerFromHttp request = new RequestServerFromHttp();
        String msg = request.getForgetPswCode(mobile);
        response = new JsonData().JsonCode(msg);
        showDialog("验证码以发送到您手机！");
    }

}
