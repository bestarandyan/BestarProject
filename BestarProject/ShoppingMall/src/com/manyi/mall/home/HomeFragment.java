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
import com.manyi.mall.R;
import com.manyi.mall.Util.JsonData;
import com.manyi.mall.cachebean.MainDataBean;
import com.manyi.mall.common.Constants;
import com.manyi.mall.service.RequestServerFromHttp;
import com.manyi.mall.user.HtmlLoadFragment;
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
import java.util.concurrent.ScheduledExecutorService;

@SuppressLint("HandlerLeak")
@EFragment(R.layout.fragment_home)
public class HomeFragment extends SuperFragment<Object> {

    @ViewById(R.id.release_radio_group)
    CirclePageIndicator mIndicator;

    @ViewById(R.id.view_page)
    FangyouReleasedViewPage mViewPage;

    @ViewById(R.id.sjImg1)
    ImageView mSjImg1;

    @ViewById(R.id.sjImg2)
    ImageView mSjImg2;

    @ViewById(R.id.sjImg3)
    ImageView mSjImg3;

    @ViewById(R.id.sjImg4)
    ImageView mSjImg4;

    @ViewById(R.id.sjImg5)
    ImageView mSjImg5;

    @ViewById(R.id.sjImg6)
    ImageView mSjImg6;

    @ViewById(R.id.sjTitle1)
    TextView mSjTitle1;

    @ViewById(R.id.sjTitle2)
    TextView mSjTitle2;

    @ViewById(R.id.sjTitle3)
    TextView mSjTitle3;

    @ViewById(R.id.sjTitle4)
    TextView mSjTitle4;

    @ViewById(R.id.sjTitle5)
    TextView mSjTitle5;

    @ViewById(R.id.sjTitle6)
    TextView mSjTitle6;

    @ViewById(R.id.advertLayout)
    RelativeLayout advertLayout;
    private int currentItem;// 页面当前所处的位置
    private int uid;
    int cityId = 0;

    ViewpageAdapter mAdapter = null;
    int RadioID = 0x001100;

    private ScheduledExecutorService scheduledExecutor;
    ArrayList<View> pageViews = new ArrayList<View>();

    List<MainDataBean> mDataList;
    TextView[] mSjTvArray = null;
    ImageView[] mSjImgArray = null;


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
        mSjTvArray = new TextView[]{mSjTitle1,mSjTitle2,mSjTitle3,mSjTitle4,mSjTitle5,mSjTitle6};
        mSjImgArray = new ImageView[]{mSjImg1,mSjImg2,mSjImg3,mSjImg4,mSjImg5,mSjImg6};
        cityId = getActivity().getSharedPreferences(Constants.LOGIN_TIMES, Context.MODE_PRIVATE).getInt("cityId", 0);
        mAdapter = new ViewpageAdapter();
        mViewPage.setAdapter(mAdapter);
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
            imageView.setImageResource(R.drawable.take_photos_list_no__thumbnail);
            super.onLoadingFailed(imageUri, view, failReason);
        }

        @Override
        public void onLoadingCancelled(String imageUri, View view) {
            ImageView imageView = (ImageView) view;
            imageView.setImageResource(R.drawable.take_photos_list_no__thumbnail);
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
                   if (mDataList.get(i).getClassName().equals("装修设计")){

                   }else if (mDataList.get(i).getClassName().equals("幼儿园日用品")){

                   }else if (mDataList.get(i).getClassName().equals("胎教早教课程")){

                   }else if (mDataList.get(i).getClassName().equals("进修培训讲座")){

                   }else if (mDataList.get(i).getClassName().equals("加盟连锁服务")){

                   }else if (mDataList.get(i).getClassName().equals("教材书籍")){
                       setSjData(i);
                   }else if (mDataList.get(i).getClassName().equals("国际品牌")){

                   }else if (mDataList.get(i).getClassName().equals("管理软件")){

                   }else if (mDataList.get(i).getClassName().equals("室内教玩具")){

                   }else if (mDataList.get(i).getClassName().equals("配套设备")){

                   }
               }
        }
    }
    @UiThread
    void setSjData(int position){
        List<MainDataBean.ShopClasses> shopClasseses = mDataList.get(position).getShopclasses();
        for (int s=0;s<shopClasseses.size();s++){
            MainDataBean.ShopClasses shopClasses = shopClasseses.get(s);
            mSjTvArray[s].setText(shopClasses.getClassName());
            MainDataBean.Products products = getProducts(position,shopClasses.getID());
            if (products!=null){
                ImageLoader.getInstance().displayImage(products.getPicUrl(), mSjImgArray[s], options, animateFirstListener);
            }
        }
    }

    private MainDataBean.Products getProducts(int position,Long classId){
        List<MainDataBean.Products> productses = mDataList.get(position).getProducts();
        for (int i=0;i<productses.size();i++){
            MainDataBean.Products product = productses.get(i);
            if(product.getClassID() == classId){
                return product;
            }
        }
        return null;
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

    @Click(R.id.searchBtn)
    void search() {
        if (CheckDoubleClick.isFastDoubleClick()){
            return;
        }
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


}
