package com.manyi.mall.release;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huoqiu.framework.analysis.ManyiAnalysis;
import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.exception.RestException;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.DeviceUtil;
import com.huoqiu.framework.util.DialogBuilder;
import com.huoqiu.framework.util.GeneratedClassUtils;
import com.huoqiu.widget.FangYouInfoBar;
import com.huoqiu.widget.FangyouReleasedViewPage;
import com.huoqiu.widget.viewpageindicator.CirclePageIndicator;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.search.AdvertRequest;
import com.manyi.mall.cachebean.search.AdvertResponse;
import com.manyi.mall.cachebean.search.AdvertResponse.AdvertisingResponse;
import com.manyi.mall.cachebean.search.UserTaskCountRequest;
import com.manyi.mall.cachebean.search.UserTaskCountResponse;
import com.manyi.mall.cachebean.user.UpdateUserPublicNumRequest;
import com.manyi.mall.common.Constants;
import com.manyi.mall.common.util.TaskCountResponseUtil;
import com.manyi.mall.mine.ContactsFragment;
import com.manyi.mall.service.AdvertisingService;
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
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SuppressLint("HandlerLeak")
@EFragment(R.layout.fragment_realse)
public class ReleaseFragment extends SuperFragment<Object> {

    @ViewById(R.id.release_radio_group)
    CirclePageIndicator mIndicator;

    @ViewById(R.id.view_page)
    FangyouReleasedViewPage mViewPage;

    @ViewById(R.id.advertLayout)
    RelativeLayout advertLayout;


    private int currentItem;// 页面当前所处的位置

    public UserTaskService mTaskService;

    private UserTaskCountResponse countResponse;

    private int uid;
    int cityId = 0;

    ViewpageAdapter mAdapter = null;
    int RadioID = 0x001100;

    private ScheduledExecutorService scheduledExecutor;

    List<AdvertisingResponse> mDataList;

    AdvertisingService mAdvertService;
    CommonService commonService;
    UcService mUserService;


    ArrayList<View> pageViews = new ArrayList<View>();

    @Click(R.id.model8Layout)
    void allTypeLayoutClick(){
        if (CheckDoubleClick.isFastDoubleClick()) {
            return;
        }
        ManyiAnalysis.onEvent(getActivity(), "gotoAdvertDetailClick");
        AllAreaFragment allAreaFragment = GeneratedClassUtils.getInstance(AllAreaFragment.class);
        allAreaFragment.tag = AllAreaFragment.class.getName();
        allAreaFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
                R.anim.anim_fragment_close_out);
        allAreaFragment.setManager(getFragmentManager());
        allAreaFragment.show(SuperFragment.SHOW_ADD_HIDE);
    }

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

    ;

    @AfterViews
    public void onUserTaskCountLoad() {
        cityId = getActivity().getSharedPreferences(Constants.LOGIN_TIMES, Context.MODE_PRIVATE).getInt("cityId", 0);
        mDataList = new ArrayList<AdvertisingResponse>();
        mAdapter = new ViewpageAdapter();
        mViewPage.setAdapter(mAdapter);

//        getAdvertData();
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

            AdvertRequest request = new AdvertRequest();
            request.setUserId(uid);
            request.setCityId(cityId);

            AdvertResponse response = mAdvertService.getAdvertList(request);
            if (response != null && response.getErrorCode() == 0) {
                mDataList = response.getList();
            }
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


        if (mDataList != null && mDataList.size() > 0) {
            pageViews.clear();
            for (int i = 0; i < mDataList.size(); i++) {
                View mView = LayoutInflater.from(getActivity()).inflate(R.layout.item_release_viewpage, null);
                TextView text = (TextView) mView.findViewById(R.id.viewpage_item_text);
                text.setText(mDataList.get(i).getTitle());
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
            scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
            // * 四个参数：①要执行的任务②执行一次任务所用的时间③两次任务之间所隔的时间④时间单位
            scheduledExecutor.scheduleAtFixedRate(new MyPageTask(), 3, 3, TimeUnit.SECONDS);

        }

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void addAnimForView(View rootView) {

        ViewGroup vg = null;
        if (DeviceUtil.getSDKVersionInt() >= 11 && rootView instanceof ViewGroup) {
            vg = (ViewGroup) rootView;
            LayoutTransition layoutTransition = new LayoutTransition();
            layoutTransition.setDuration(400);
            vg.setLayoutTransition(layoutTransition);
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

            ManyiAnalysis.onEvent(getActivity(), "gotoAdvertDetailClick");
            AdvertDetailFragment advertDetailFragment = GeneratedClassUtils.getInstance(AdvertDetailFragment.class);
            advertDetailFragment.tag = AdvertDetailFragment.class.getName();
            advertDetailFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
                    R.anim.anim_fragment_close_out);
            advertDetailFragment.setManager(getFragmentManager());
            Bundle bundle = new Bundle();
            bundle.putInt("advertId", mDataList.get(index).getId());
            advertDetailFragment.setArguments(bundle);
            advertDetailFragment.show(SuperFragment.SHOW_ADD_HIDE);

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
