package com.manyi.mall.mine;

import com.huoqiu.framework.app.SuperFragment;
import com.manyi.mall.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

/**
 * Created by bestar on 2015/1/29.
 */
@EFragment(R.layout.fragment_feedback)
public class FeedbackFragment extends SuperFragment {

    @Click(R.id.feedbackBackBtn)
    void back(){
        remove();
    }

}
