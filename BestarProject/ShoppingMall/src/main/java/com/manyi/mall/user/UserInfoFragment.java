package com.manyi.mall.user;

import android.os.Bundle;
import android.widget.TextView;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.GeneratedClassUtils;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.user.UserInfoResponse;
import com.manyi.mall.service.RequestServerFromHttp;
import com.manyi.mall.user.ForgetPasswordFragment;
import com.manyi.mall.user.UpdatePswFragment;
import com.manyi.mall.utils.JsonData;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.w3c.dom.Text;

/**
 * Created by bestar on 2015/3/4.
 */
@EFragment(R.layout.fragment_user_info)
public class UserInfoFragment extends SuperFragment {
    UserInfoResponse mUserInfoResponse =null;

    @ViewById(R.id.real_name_et)
    TextView mRealNameTv;

    @ViewById(R.id.pswTv)
    TextView mPswTv;

    @Click(R.id.userInfoBack)
    void back() {
        remove();
    }

    @Click(R.id.headViewLayout)
    void gotoUpdateHead() {
        remove();
    }

    @AfterViews
    void init(){
        getUserInfo();
    }

    @Background
    void getUserInfo(){
        RequestServerFromHttp request = new RequestServerFromHttp();
        String msg =  request.getUserInfo();
        mUserInfoResponse = new JsonData().jsonUserInfo(msg);
        if (mUserInfoResponse!=null){
            setViewValue();
        }
    }

    @UiThread
    void setViewValue(){
        mRealNameTv.setText(mUserInfoResponse.UserName);
        mPswTv.setText(mUserInfoResponse.Password);
    }
    @Click(R.id.pswLayout)
    void gotoUpdatePsw() {
        if (CheckDoubleClick.isFastDoubleClick()) {
            return;
        }
        if (CheckDoubleClick.isFastDoubleClick())
            return;
        UpdatePswFragment fragment = GeneratedClassUtils.getInstance(UpdatePswFragment.class);
        fragment.tag = UpdatePswFragment.class.getName();

        fragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
                R.anim.anim_fragment_close_out);
        fragment.setManager(getFragmentManager());
        fragment.show(SHOW_ADD_HIDE);
    }

    @Click(R.id.moreInfoLayout)
    void gotoUpdateMoInfo() {
        if (CheckDoubleClick.isFastDoubleClick())
            return;
        MoreUserInfoFragment fragment = GeneratedClassUtils.getInstance(MoreUserInfoFragment.class);
        fragment.tag = MoreUserInfoFragment.class.getName();
        Bundle bundle = new Bundle();
        bundle.putSerializable("mUserInfoResponse",mUserInfoResponse);
        fragment.setArguments(bundle);
        fragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
                R.anim.anim_fragment_close_out);
        fragment.setManager(getFragmentManager());
        fragment.show(SHOW_ADD_HIDE);
    }
}