package com.manyi.mall.user;

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

import org.androidannotations.annotations.AfterViews;
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
	EditText mForgetUsername;
	@ViewById(R.id.login_code)
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
		if (phone != null) {
			mForgetUsername.setText(phone);
		}
		addAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				if (isFirstEnter) {
					ManyiUtils.showKeyBoard(getActivity(), mForgetUsername);
					isFirstEnter = false;
				}
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		ManyiUtils.closeKeyBoard(getActivity(), mForgetCode);
	}
	@UiThread
	public void onSendSMSSuccess() {
		logincode.setEnabled(false);
		DialogBuilder.showSimpleDialog("验证码已发送!", getBackOpActivity(), new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				timer.start();
				int left = logincode.getPaddingLeft();
				int right = logincode.getPaddingRight();
//				logincode.setBackgroundResource(R.drawable.btn_orange_dis);
				logincode.setPadding(left, 0, right, 0);
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
	@Click(R.id.get_code_next)
	public void nextSuccess() {
		if (CheckDoubleClick.isFastDoubleClick()) {
			return;
		}
		if (TextUtils.isEmpty(mForgetUsername.getText().toString()) || TextUtils.isEmpty(mForgetUsername.getText().toString().trim())) {
			DialogBuilder.showSimpleDialog("请输入电话号码", getBackOpActivity());
			return;
		}
		if (TextUtils.isEmpty(mForgetCode.getText().toString()) || TextUtils.isEmpty(mForgetCode.getText().toString().trim())) {
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
		ManyiUtils.closeKeyBoard(getActivity(), mForgetCode);
		remove();
	}
	
	@Click(R.id.login_code)
	void logincode() {
		if (CheckDoubleClick.isFastDoubleClick()) {
			return;
		}
	}

	CountDownTimer timer = new CountDownTimer(60000, 1000) {

		@Override
		public void onTick(long arg0) {
			logincode.setText("获取短信(" + arg0 / 1000 + ")...");
		}

		@Override
		public void onFinish() {
			logincode.setText(R.string.register_get_captcha);
			logincode.setEnabled(true);
			int left = logincode.getPaddingLeft();
			int right = logincode.getPaddingRight();
			logincode.setBackgroundResource(R.drawable.start_selector);
			logincode.setPadding(left, 0, right, 0);
		}
	};
}
