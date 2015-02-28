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
import com.manyi.mall.cachebean.user.GetRegisterCodeResponse;
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
	@ViewById(R.id.get_code_next)
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
//		addAnimationListener(new Animation.AnimationListener() {
//			@Override
//			public void onAnimationStart(Animation animation) {
//
//			}
//
//			@Override
//			public void onAnimationEnd(Animation animation) {
//				if (isFirstEnter) {
//					ManyiUtils.showKeyBoard(getActivity(), mForgetUsername);
//					isFirstEnter = false;
//				}
//			}
//
//			@Override
//			public void onAnimationRepeat(Animation animation) {
//
//			}
//		});
	}

    @Click(R.id.getCodeBtn)
    void getCode(){
        mGetCodeBtn.setClickable(false);
        mGetCodeBtn.setEnabled(false);
        getRegisterCode();
    }

    @Click(R.id.get_code_next)
    void submitCode(){
        String code = mCodeEt.getText().toString();
        if (code .equals(mCode)){
            gotoNextStep();
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
        GetRegisterCodeResponse response = new JsonData().JsonRegisterCode(msg);
        mCode = response.getYZCode();
        onSendSMSSuccess();
    }

	@Override
	public void onDestroy() {
		super.onDestroy();
		ManyiUtils.closeKeyBoard(getActivity(), mPhoneEt);
	}
	@UiThread
	public void onSendSMSSuccess() {

		DialogBuilder.showSimpleDialog("验证码已发送!", getBackOpActivity(), new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
	}

	@UiThread
	public void logincodeError(String e) {
		DialogBuilder.showSimpleDialog(e, getBackOpActivity());
	}

	@UiThread
	public void showerror() {
		DialogBuilder.showSimpleDialog("验证码不能为空", getBackOpActivity());
	}


	@UiThread
	@Click(R.id.checkCodeBtn)
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
	
	@Click(R.id.login_code)
	void logincode() {
		if (CheckDoubleClick.isFastDoubleClick()) {
			return;
		}
	}

}
