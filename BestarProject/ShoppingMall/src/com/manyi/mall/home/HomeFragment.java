package com.manyi.mall.home;

import android.animation.LayoutTransition;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.LayoutAnimationController;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.exception.RestException;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.DeviceUtil;
import com.huoqiu.framework.util.DialogBuilder;
import com.huoqiu.framework.util.GeneratedClassUtils;
import com.huoqiu.widget.FangyouReleasedViewPage;
import com.huoqiu.widget.viewpageindicator.CirclePageIndicator;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.user.UpdateUserPublicNumRequest;
import com.manyi.mall.common.Constants;
import com.manyi.mall.search.SearchFragment;
import com.manyi.mall.service.CommonService;
import com.manyi.mall.service.UcService;
import com.manyi.mall.service.UserTaskService;
import com.manyi.mall.user.HtmlLoadFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.concurrent.ScheduledExecutorService;

@SuppressLint("HandlerLeak")
@EFragment(R.layout.fragment_home)
public class HomeFragment extends SuperFragment<Object> {

    @ViewById(R.id.release_radio_group)
    CirclePageIndicator mIndicator;

    @ViewById(R.id.view_page)
    FangyouReleasedViewPage mViewPage;

    @ViewById(R.id.advertLayout)
    RelativeLayout advertLayout;


    private int currentItem;// 页面当前所处的位置

    public UserTaskService mTaskService;


    private int uid;
    int cityId = 0;

    ViewpageAdapter mAdapter = null;
    int RadioID = 0x001100;

    private ScheduledExecutorService scheduledExecutor;

    CommonService commonService;
    UcService mUserService;


    ArrayList<View> pageViews = new ArrayList<View>();


    private class MyPageTask implements Runnable {
        public void run() {
            handler.sendEmptyMessage(0);
        }

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (currentItem == pageViews.size() - 1) {
                currentItem = 0;
            } else {
                currentItem++;
            }
            if (mIndicator != null && mIndicator.getViewPager() != null) {
                mIndicator.setCurrentItem(currentItem);
            }
        }

        ;
    };


    @Override
    public void onPause() {
        if (mViewPage != null && mIndicator != null && scheduledExecutor != null) {
            scheduledExecutor.shutdown();
            scheduledExecutor = null;
        }
        super.onPause();
    }

    @AfterViews
    public void onUserTaskCountLoad() {
        cityId = getActivity().getSharedPreferences(Constants.LOGIN_TIMES, Context.MODE_PRIVATE).getInt("cityId", 0);
        mAdapter = new ViewpageAdapter();
        mViewPage.setAdapter(mAdapter);

        getAdvertData();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            if (mAdapter != null && mViewPage != null) {
                mViewPage.setAdapter(mAdapter);
            }
        }
        super.onHiddenChanged(hidden);
    }


    @Override
    public void onAttach(Activity activity) {
        setBackOp(null);
        super.onAttach(activity);
    }

    @Background
    public void getAdvertData() {
        try {
            int uid = getActivity().getSharedPreferences(Constants.LOGIN_TIMES, 0).getInt("uid", 0);

            notifyAdvert();
        } catch (RestException e) {
            // TODO Auto-generated catch block
            showDialog(e.getMessage());

        }
    }

    @UiThread
    public void showDialog(String msgString) {
        DialogBuilder.showSimpleDialog(msgString, getActivity());
    }

    @UiThread
    public void notifyAdvert() {
        //cityId


            pageViews.clear();
            for (int i = 0; i < 2; i++) {
                View mView = LayoutInflater.from(getActivity()).inflate(R.layout.item_release_viewpage, null);
                TextView text = (TextView) mView.findViewById(R.id.viewpage_item_text);
                text.setText("ViewPage"+i);
                text.setGravity(Gravity.CENTER);
                mView.setTag(i);
                pageViews.add(mView);
            }

            mAdapter = new ViewpageAdapter();
            mViewPage.setAdapter(mAdapter);
            if (pageViews.size() > 1) {
                mIndicator.setViewPager(mViewPage);
                mIndicator.setOnPageChangeListener(new OnPageChangeListener() {

                    @Override
                    public void onPageSelected(int position) {
                        currentItem = position;
                    }

                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                    }
                });
            }

            addAnimForView((View) advertLayout.getParent());
            advertLayout.setVisibility(View.VISIBLE);
            currentItem = 0;
//            scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
//            // * 四个参数：①要执行的任务②执行一次任务所用的时间③两次任务之间所隔的时间④时间单位
//            scheduledExecutor.scheduleAtFixedRate(new MyPageTask(), 3, 3, TimeUnit.SECONDS);

        }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void addAnimForView(View rootView) {

        ViewGroup vg = null;
        if (DeviceUtil.getSDKVersionInt() >= 11 && rootView instanceof ViewGroup) {
            vg = (ViewGroup) rootView;
            LayoutTransition layoutTransition = new LayoutTransition();
            layoutTransition.setDuration(600);
            vg.setLayoutTransition(layoutTransition);
            AlphaAnimation alphaAnimation =new AlphaAnimation(0.0f,1.0f);
            alphaAnimation.setDuration(1000);
            LayoutAnimationController controller = new LayoutAnimationController(alphaAnimation);
            controller.setDelay(1000);
//            vg.setLayoutAnimation(controller);

        }
    }

    /**
     * view page Adapater
     *
     * @author jason
     */
    class ViewpageAdapter extends PagerAdapter implements OnClickListener {

        @Override
        public int getCount() {
            return pageViews.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView(pageViews.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = pageViews.get(position);
            container.addView(view, 0);
            view.setOnClickListener(this);
            view.setTag(position);
            return view;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void onClick(View v) {
            if (CheckDoubleClick.isFastDoubleClick()) {
                return;
            }
            int index = Integer.parseInt(v.getTag().toString());


        }

    }


    /**
     * 异常处理
     *
     * @param e
     */
    @UiThread
    public void onTaskCountError(String e) {
        if (this == null || getActivity().isFinishing()) {
            return;
        }
        DialogBuilder.showSimpleDialog(e, getActivity());
    }


    @UiThread
    void rewardDescription() {

        HtmlLoadFragment htmlLoadFragment = GeneratedClassUtils.getInstance(HtmlLoadFragment.class);
        htmlLoadFragment.tag = HtmlLoadFragment.class.getName();
        Bundle bundle = new Bundle();
        bundle.putString("mHtmLTitle", "奖惩说明");// String mHtmLTitle, mHtmlUrl;
        String cityId_ = cityId + "";
        bundle.putString("mHtmlUrl", "/rest/common/getIncentiveAgreement.rest?cityId=" + cityId_.trim());
        htmlLoadFragment.setArguments(bundle);
        htmlLoadFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
                R.anim.anim_fragment_close_out);
        htmlLoadFragment.setContainerId(R.id.main_container);
        htmlLoadFragment.setManager(getFragmentManager());

        htmlLoadFragment.show(SuperFragment.SHOW_ADD_HIDE);
    }

    @Click(R.id.searchLayout)
    void search() {
        if (CheckDoubleClick.isFastDoubleClick()){
            return;
        }
        SearchFragment searchFragment = GeneratedClassUtils.getInstance(SearchFragment.class);
        searchFragment.tag = SearchFragment.class.getName();
        searchFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
                R.anim.anim_fragment_close_out);
        searchFragment.setContainerId(R.id.main_container);
        searchFragment.setManager(getFragmentManager());

        searchFragment.show(SuperFragment.SHOW_ADD_HIDE);
    }

    @Click(R.id.model1Layout1)
    void model1Layout1() {
        gotoProductListFragment();
    }
    @Click(R.id.model1Layout2)
    void model1Layout2() {
        gotoProductListFragment();
    }
    @Click(R.id.model1Layout3)
    void model1Layout3() {
        gotoProductListFragment();
    }
    @Click(R.id.model1Layout4)
    void model1Layout4() {
        gotoProductListFragment();
    }

    private void gotoProductListFragment(){
        if (CheckDoubleClick.isFastDoubleClick()){
            return;
        }
        ProductListFragment productListFragment = GeneratedClassUtils.getInstance(ProductListFragment.class);
        productListFragment.tag = ProductListFragment.class.getName();
        productListFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
                R.anim.anim_fragment_close_out);
        productListFragment.setContainerId(R.id.main_container);
        productListFragment.setManager(getFragmentManager());

        productListFragment.show(SuperFragment.SHOW_ADD_HIDE);
    }

    @Background
    public void onUpdateSellRecord() {
        uid = getActivity().getSharedPreferences(Constants.LOGIN_TIMES, 0).getInt("uid", 0);
        UpdateUserPublicNumRequest numRequest = new UpdateUserPublicNumRequest();
        numRequest.setUserId(uid);
        numRequest.setType(2);
        mUserService.updatePublicNum(numRequest);
    }



    @Background
    public void onUpdateRentRecord() {
        uid = getActivity().getSharedPreferences(Constants.LOGIN_TIMES, 0).getInt("uid", 0);
        UpdateUserPublicNumRequest numRequest = new UpdateUserPublicNumRequest();
        numRequest.setUserId(uid);
        numRequest.setType(1);
        mUserService.updatePublicNum(numRequest);
    }

}
