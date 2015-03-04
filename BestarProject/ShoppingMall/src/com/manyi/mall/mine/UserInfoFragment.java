package com.manyi.mall.mine;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.GeneratedClassUtils;
import com.manyi.mall.R;
import com.manyi.mall.user.ForgetPasswordFragment;
import com.manyi.mall.user.UpdatePswFragment;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

/**
 * Created by bestar on 2015/3/4.
 */
@EFragment(R.layout.fragment_user_info)
public class UserInfoFragment extends SuperFragment {


    @Click(R.id.userInfoBack)
    void back() {
        remove();
    }

    @Click(R.id.headViewLayout)
    void gotoUpdateHead() {
        remove();
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
        remove();
    }

}
