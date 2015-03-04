package com.manyi.mall.user;

import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.DialogBuilder;
import com.huoqiu.framework.util.GeneratedClassUtils;
import com.huoqiu.framework.util.ManyiUtils;
import com.manyi.mall.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_update_password)
public class UpdatePswFragment extends SuperFragment<Integer> {
	@ViewById(R.id.oldPswEt)
	EditText mOldPswEt;
	@ViewById(R.id.newPswEt)
	EditText mNewPswEt;
	@ViewById(R.id.againPswEt)
    EditText mAgainPswEt;

	@AfterViews
	void init() {

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		ManyiUtils.closeKeyBoard(getActivity(), mAgainPswEt);
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
		if (TextUtils.isEmpty(mOldPswEt.getText().toString()) || TextUtils.isEmpty(mOldPswEt.getText().toString().trim())) {
			DialogBuilder.showSimpleDialog("请输入原密码", getBackOpActivity());
			return;
		}else if (TextUtils.isEmpty(mNewPswEt.getText().toString()) || TextUtils.isEmpty(mNewPswEt.getText().toString().trim())) {
			DialogBuilder.showSimpleDialog("请输入新密码", getBackOpActivity());
			return;
		}else if (TextUtils.isEmpty(mAgainPswEt.getText().toString()) || TextUtils.isEmpty(mAgainPswEt.getText().toString().trim())) {
            DialogBuilder.showSimpleDialog("请再次输入新密码", getBackOpActivity());
            return;
        }else{
            updatePsw();
        }
	}

    @Background
    void updatePsw(){
//        String mobile = mForgetPhone.getText().toString().trim();
//        RequestServerFromHttp request = new RequestServerFromHttp();
//        String msg = request.getForgetPsw(mobile);
//        BaseResponse response = new JsonData().JsonBase(msg);
//        showDialog(response.getMessage());
//        if (response.getCode().equals("0")){
//            remove();
//        }
    }

	@Click(R.id.updatePswBack)
	void back() {
		if (CheckDoubleClick.isFastDoubleClick()) {
			return;
		}
		ManyiUtils.closeKeyBoard(getActivity(), mAgainPswEt);
		remove();
	}
	
	@Click(R.id.getPswBtn)
	void getPswClick() {
		if (CheckDoubleClick.isFastDoubleClick()) {
			return;
		}
        if (CheckDoubleClick.isFastDoubleClick())
            return;
        ForgetPasswordFragment fragment = GeneratedClassUtils.getInstance(ForgetPasswordFragment.class);
        fragment.tag = ForgetPasswordFragment.class.getName();

        fragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
                R.anim.anim_fragment_close_out);
        fragment.setManager(getFragmentManager());
        fragment.show(SHOW_ADD_HIDE);
	}

}