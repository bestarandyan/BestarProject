package com.manyi.mall.home;

import android.animation.LayoutTransition;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
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
import android.widget.ImageView;
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
import com.manyi.mall.BestarApplication;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.AdvertBean;
import com.manyi.mall.cachebean.MainDataBean2;
import com.manyi.mall.search.SearchProductListFragment;
import com.manyi.mall.utils.JsonData;
import com.manyi.mall.cachebean.MainDataBean;
import com.manyi.mall.common.Constants;
import com.manyi.mall.service.RequestServerFromHttp;
import com.manyi.mall.user.HtmlLoadFragment;
import com.manyi.mall.wap.DetailProductFragment;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
    private int uid;
    int cityId = 0;
    Long layout1Id = 0L;
    Long layout2Id = 0L;
    Long layout3Id = 0L;
    Long layout4Id = 0L;
    Long layout5Id = 0L;
    Long layout6Id = 0L;
    Long layout7Id = 0L;
    Long layout8Id = 0L;
    Long layout9Id = 0L;
    Long layout10Id = 0L;
    String layout1Name = "";
    String layout2Name = "";
    String layout3Name = "";
    String layout4Name = "";
    String layout5Name = "";
    String layout6Name = "";
    String layout7Name = "";
    String layout8Name = "";
    String layout9Name = "";
    String layout10Name = "";
    ViewpageAdapter mAdapter = null;
    private ScheduledExecutorService scheduledExecutor;
    ArrayList<View> pageViews = new ArrayList<View>();

    List<MainDataBean2> mDataList;



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
    public void initOption(){

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.take_photos_list_no__thumbnail)
                .showImageForEmptyUri(R.drawable.take_photos_list_no__thumbnail)
                .showImageOnFail(R.drawable.take_photos_list_no__thumbnail)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(20))
                .build();
    }
    @Override
    public void onDestroy() {
        ImageLoader.getInstance().clearMemoryCache();
        ImageLoader.getInstance().clearDiskCache();
        super.onDestroy();
    }



    @Override
    public void onPause() {
        if (mViewPage != null && mIndicator != null && scheduledExecutor != null) {
            scheduledExecutor.shutdown();
            scheduledExecutor = null;
        }
        super.onPause();
    }

    @AfterViews
    public void init() {
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getActivity()));
        initOption();
        cityId = getActivity().getSharedPreferences(Constants.LOGIN_TIMES, Context.MODE_PRIVATE).getInt("cityId", 0);
        mAdapter = new ViewpageAdapter();
        mViewPage.setAdapter(mAdapter);
        showProgressDialog("数据加载中，请稍候...");
        getAdvertData();
        getDataRequest();
    }
    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                if (loadedImage!=null){
                    imageView.setImageBitmap(loadedImage);
                }
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }

        @Override
        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            ImageView imageView = (ImageView) view;
            if (imageView!=null){
                imageView.setImageResource(R.drawable.take_photos_list_no__thumbnail);
            }
            super.onLoadingFailed(imageUri, view, failReason);
        }

        @Override
        public void onLoadingCancelled(String imageUri, View view) {
            ImageView imageView = (ImageView) view;
            if (imageView!=null){
                imageView.setImageResource(R.drawable.take_photos_list_no__thumbnail);
            }
            super.onLoadingCancelled(imageUri, view);
        }
    }
    DisplayImageOptions options;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    @Background
    void getDataRequest(){
        String appkey = getActivity().getSharedPreferences("appkey", Activity.MODE_PRIVATE).getString("appkey","");
        RequestServerFromHttp requestServerFromHttp = new RequestServerFromHttp();
        String msg = requestServerFromHttp.getMainData(appkey);
        mDataList = new JsonData().jsonMainData(msg, null);
        if (mDataList!=null && mDataList.size()>0){
               for (int i=0;i<mDataList.size();i++){
                   if (mDataList.get(i).ClassName.equals("装修设计")){
                       layout9Id = mDataList.get(i).ID;
                       layout9Name = mDataList.get(i).ClassName;
                   }else if (mDataList.get(i).ClassName.equals("幼儿园日用品")){
                       layout6Id = mDataList.get(i).ID;
                       layout6Name = mDataList.get(i).ClassName;
                   }else if (mDataList.get(i).ClassName.equals("胎教早教课程")){
                       layout8Id = mDataList.get(i).ID;
                       layout8Name = mDataList.get(i).ClassName;
                   }else if (mDataList.get(i).ClassName.equals("进修培训讲座")){
                       layout7Id = mDataList.get(i).ID;
                       layout7Name = mDataList.get(i).ClassName;
                   }else if (mDataList.get(i).ClassName.equals("加盟联锁服务")){
                       layout10Id = mDataList.get(i).ID;
                       layout10Name = mDataList.get(i).ClassName;
                   }else if (mDataList.get(i).ClassName.equals("教材书籍")){
                       layout5Id = mDataList.get(i).ID;
                       layout5Name = mDataList.get(i).ClassName;
                   }else if (mDataList.get(i).ClassName.equals("国际品牌")){
                       layout3Id = mDataList.get(i).ID;
                       layout3Name = mDataList.get(i).ClassName;
                   }else if (mDataList.get(i).ClassName.equals("管理软件")){
                       layout4Id = mDataList.get(i).ID;
                       layout4Name = mDataList.get(i).ClassName;
                   }else if (mDataList.get(i).ClassName.equals("室内教玩具")){
                       layout2Id = mDataList.get(i).ID;
                       layout2Name = mDataList.get(i).ClassName;
                   }else if (mDataList.get(i).ClassName.equals("配套设备")){
                       layout1Id = mDataList.get(i).ID;
                       layout1Name = mDataList.get(i).ClassName;
                   }
               }
        }
        dismissDialog();
    }
    @UiThread
    void dismissDialog(){
        dismissProgressDialog();
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
            RequestServerFromHttp request = new RequestServerFromHttp();
            String msg = request.getAdvert();
            if (msg!=null && msg.length()>0){
                List<AdvertBean> advertList = new JsonData().jsonAdvert(msg);
                if (advertList!=null && advertList.size()>0){
                    notifyAdvert(advertList);
                }
            }
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
    public void notifyAdvert(List<AdvertBean> advertList) {
            pageViews.clear();
            for (int i = 0; i < advertList.size(); i++) {
                View mView = LayoutInflater.from(getActivity()).inflate(R.layout.item_release_viewpage, null);
                ImageView img = (ImageView) mView.findViewById(R.id.viewpage_item_img);
                mView.setTag(i);
                ImageLoader.getInstance().displayImage(advertList.get(i).IMGURL, img, options, animateFirstListener);
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

    @Click(R.id.searchEt)
    void search() {
        gotoProductListFragment(true,0l,"");
    }

    @Click(R.id.Layout1)
    void clickLayout1() {

    }
    @Click(R.id.Layout2)
    void clickLayout2() {
        gotoProductListFragment(false,layout1Id,layout1Name);
    }
    @Click(R.id.Layout3)
    void clickLayout3() {
        gotoProductListFragment(false,layout2Id,layout2Name);
    }
    @Click(R.id.Layout4)
    void clickLayout4() {
        gotoProductListFragment(false,layout3Id,layout3Name);
    }
    @Click(R.id.Layout5)
    void clickLayout5(){
        gotoProductListFragment(false,layout4Id,layout4Name);
    }
    @Click(R.id.Layout6)
    void clickLayout6(){
        gotoProductListFragment(false,layout5Id,layout5Name);
    }
    @Click(R.id.Layout7)
    void clickLayout7(){
        gotoProductListFragment(false,layout6Id,layout6Name);
    }
    @Click(R.id.Layout8)
    void clickLayout8(){
        gotoProductListFragment(false,layout7Id,layout7Name);
    }
    @Click(R.id.Layout9)
    void clickLayout9(){
        gotoProductListFragment(false,layout8Id,layout8Name);
    }
    @Click(R.id.Layout10)
    void clickLayout10(){
        gotoProductListFragment(false,layout9Id,layout9Name);
    }

    @Click(R.id.Layout11)
    void clickLayout11(){
        gotoProductListFragment(false,layout10Id,layout10Name);
    }
    private void gotoProductListFragment(boolean isHaveHistory,Long classid,String title){
        if (CheckDoubleClick.isFastDoubleClick()){
            return;
        }
        SearchProductListFragment productListFragment = GeneratedClassUtils.getInstance(SearchProductListFragment.class);
        productListFragment.tag = SearchProductListFragment.class.getName();
//        productListFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
//                R.anim.anim_fragment_close_out);
        Bundle bundle = new Bundle();
        bundle.putBoolean("isHaveHistory",isHaveHistory);
        bundle.putLong("classId",classid);
        bundle.putString("title",title);
        productListFragment.setArguments(bundle);
        productListFragment.setContainerId(R.id.main_container);
        productListFragment.setManager(getFragmentManager());

        productListFragment.show(SuperFragment.SHOW_ADD_HIDE);
    }


}
