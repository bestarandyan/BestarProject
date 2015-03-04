package com.manyi.mall.user;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.DialogBuilder;
import com.huoqiu.framework.util.GeneratedClassUtils;
import com.huoqiu.framework.util.ManyiUtils;
import com.manyi.mall.MainActivity;
import com.manyi.mall.R;
import com.manyi.mall.Util.JsonData;
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
	String phone;
	private String mCode;
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


    @Click(R.id.getCodeBtn)
    void getCode(){
//        mGetCodeBtn.setClickable(false);
//        mGetCodeBtn.setEnabled(false);
        if (mPhoneEt.getText().toString().length() == 0){
            showDialog("请输入手机号码！");
            return;
        }
        getRegisterCode();
    }

    @Click(R.id.checkCodeBtn)
    void submitCode(){
        String code = mCodeEt.getText().toString();
        if (mPhoneEt.getText().toString().length() == 0){
            showDialog("请输入手机号码！");
            return;
        }
        if (code!=null && code.trim().length() == 0){
            showDialog("请输入验证码！");
        }else if (code .equals(mCode)){
            gotoNextStep();
        }else{
            showDialog("请输入正确的验证码！");
        }
    }
    @UiThread
    void gotoNextStep(){
        Intent loginIntent = new Intent(getActivity(), GeneratedClassUtils.get(MainActivity.class));
        getActivity().overridePendingTransition(R.anim.anim_fragment_in, R.anim.anim_fragment_out);
        startActivity(loginIntent);
        getActivity().finish();
    }
    @Background
    void getRegisterCode(){
        RequestServerFromHttp request = new RequestServerFromHttp();
        String msg = request.getRegisterCode(mPhoneEt.getText().toString());
        CodeResponse response = new JsonData().JsonCode(msg);
        mCode = response.getYZCode();
        showDialog("验证码将发送到手机上！");
    }

	@Override
	public void onDestroy() {
		super.onDestroy();
		ManyiUtils.closeKeyBoard(getActivity(), mPhoneEt);
	}

	@UiThread
	public void showDialog(String e) {
		DialogBuilder.showSimpleDialog(e, getBackOpActivity());
	}

	public void nextSuccess() {
		if (CheckDoubleClick.isFastDoubleClick()) {
			return;
		}
		if (TextUtils.isEmpty(mPhoneEt.getText().toString()) || TextUtils.isEmpty(mPhoneEt.getText().toString().trim())) {
			DialogBuilder.showSimpleDialog("请输入电话号码", getBackOpActivity());
			return;
		}
		if (TextUtils.isEmpty(mCodeEt.getText().toString()) || TextUtils.isEmpty(mCodeEt.getText().toString().trim())) {
			DialogBuilder.showSimpleDialog("请输入验证码", getBackOpActivity());
			return;
		}
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
