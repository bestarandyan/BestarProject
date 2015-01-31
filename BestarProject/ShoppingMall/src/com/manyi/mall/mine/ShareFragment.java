package com.manyi.mall.mine;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.huoqiu.framework.app.SuperFragment;
import com.manyi.mall.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Otto on 2015/1/31.
 */
@EFragment(R.layout.fragment_share)
public class ShareFragment extends SuperFragment {
    @ViewById(R.id.bottomShareLayout)
    LinearLayout mBottomShareLayout;

    @ViewById(R.id.transLayout)
    LinearLayout mTransLayout;


    @AfterViews
    void initView(){
        Animation animation = AnimationUtils.loadAnimation(getActivity(),R.anim.slide_in_from_bottom);
        mBottomShareLayout.startAnimation(animation);

        Animation scaleAnimation = AnimationUtils.loadAnimation(getActivity(),R.anim.alpha_in);
        mTransLayout.setAnimation(scaleAnimation);
    }

    @Click(R.id.transLayout)
    void dismissShare(){
        Animation animation = AnimationUtils.loadAnimation(getActivity(),R.anim.slide_out_to_bottom);
        mBottomShareLayout.startAnimation(animation);

        Animation scaleAnimation = AnimationUtils.loadAnimation(getActivity(),R.anim.alpha_out);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                remove();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mTransLayout.setAnimation(scaleAnimation);


    }

}
