package com.manyi.mall.mine;

import com.huoqiu.framework.app.SuperFragment;
import com.manyi.mall.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

/**
 * Created by bestar on 2015/4/8.
 */
@EFragment(R.layout.fragment_about_us)
public class AboutUsFragment extends SuperFragment {


    @Click(R.id.aboutUsBackBtn)
    void back(){
        remove();
    }
}
