package com.manyi.mall.user;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.exception.ClientException;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.DialogBuilder;
import com.huoqiu.framework.util.GeneratedClassUtils;
import com.huoqiu.framework.util.ManyiUtils;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.user.RegistRequest;
import com.manyi.mall.service.UcService;

@EFragment(R.layout.fragment_register)
public class RegisterFragment extends SuperFragment<Object> {

	@ViewById(R.id.phone_number)
	EditText mPhoneNumber;

	@ViewById(R.id.register_pwd)
	EditText mRegisterPwd;

	private UcService mUserService;

	private boolean isFirstEnter = true;


	@AfterViews
	public void showKeyBoard() {
			addAnimationListener(new Animation.AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					if (isFirstEnter) {
						ManyiUtils.showKeyBoard(getActivity(), mPhoneNumber);
						isFirstEnter = false ;
					}
				}

				@Override
				public void onAnimationRepeat(Animation animation) {

				}
			});

	}

	@UiThread
	public void onSendSMSSuccess() {
		if (getActivity() == null || getActivity().isFinishing()) {
			return;
		}
		DialogBuilder.showSimpleDialog("验证码已发送!", getActivity(), new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
	}

	@UiThread
	public void onSendSMSError(String e) {
		if (getActivity() == null || getActivity().isFinishing()) {
			return;
		}
		DialogBuilder.showSimpleDialog(e, getActivity());
	}


	@Click({ R.id.register_back })
	public void registerBack() {
		if (CheckDoubleClick.isFastDoubleClick())
			return;
		remove();
	}

	private boolean phonenumValidate() {
		if (mPhoneNumber.getText().toString().length() == 0) {
			onSendSMSError("请输入手机号");
			return false;
		} else if (mPhoneNumber.getText().toString().length() < 11) {
			onSendSMSError("手机号码格式不正确!");
			return false;
		} else
			return true;
	}

	@Click(R.id.register_next)
	@Background
	public void getRegisterData() {
		if (CheckDoubleClick.isFastDoubleClick())
			return;
//		if (!phonenumValidate()) {
//			return;
//		}
//		if (mRegisterPwd.getText().toString().length() == 0) {
//			onSendSMSError("请输入密码");
//			return;
//		}

        gotoRegisterNextStep();

	}

    @UiThread
    void gotoRegisterNextStep(){
        RegisterNextFragment registerNextFragment = GeneratedClassUtils.getInstance(RegisterNextFragment.class);
        registerNextFragment.tag = RegisterNextFragment.class.getName();

        registerNextFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
                R.anim.anim_fragment_close_out);
        registerNextFragment.setManager(getFragmentManager());
        registerNextFragment.show(SHOW_ADD_HIDE);
        ManyiUtils.closeKeyBoard(getActivity(), mRegisterPwd);
    }



	@UiThread
	public void onGerRegisterDataError(Exception e) {
		DialogBuilder.showSimpleDialog(e.getMessage(), getActivity());
	}

}
