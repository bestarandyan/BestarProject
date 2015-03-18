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
import java.util.concurrent.ScheduledExecutorService;

@SuppressLint("HandlerLeak")
@EFragment(R.layout.fragment_home)
public class HomeFragment extends SuperFragment<Object> {
    @ViewById(R.id.release_radio_group)
    CirclePageIndicator mIndicator;
    @ViewById(R.id.view_page)
    FangyouReleasedViewPage mViewPage;
    @ViewById(R.id.sjImg)
    ImageView mSjImg;
    @ViewById(R.id.sjTitle)
    TextView mSjTitle;
    @ViewById(R.id.sjCp)
    TextView mSjcp;
    @ViewById(R.id.rypImg1)
    ImageView mrypImg1;
    @ViewById(R.id.rypImg2)
    ImageView mrypImg2;
    @ViewById(R.id.rypImg3)
    ImageView mrypImg3;
    @ViewById(R.id.rypImg4)
    ImageView mrypImg4;
    @ViewById(R.id.rypImg5)
    ImageView mrypImg5;
    @ViewById(R.id.rypTitle1)
    TextView mrypTitle1;
    @ViewById(R.id.rypTitle2)
    TextView mrypTitle2;
    @ViewById(R.id.rypTitle3)
    TextView mrypTitle3;
    @ViewById(R.id.rypTitle4)
    TextView mrypTitle4;
    @ViewById(R.id.rypTitle5)
    TextView mrypTitle5;
    @ViewById(R.id.rypCp1)
    TextView mrypcp1;
    @ViewById(R.id.rypCp1)
    TextView mrypcp2;
    @ViewById(R.id.rypCp1)
    TextView mrypcp3;
    @ViewById(R.id.rypCp1)
    TextView mrypcp4;
    @ViewById(R.id.rypCp1)
    TextView mrypcp5;
    @ViewById(R.id.zxTitle1)
    TextView mZxTitle1;
    @ViewById(R.id.zxTitle2)
    TextView mZxTitle2;
    @ViewById(R.id.zxTitle3)
    TextView mZxTitle3;
    @ViewById(R.id.zxTitle4)
    TextView mZxTitle4;
    @ViewById(R.id.zxcp1)
    TextView mZxCp1;
    @ViewById(R.id.zxcp2)
    TextView mZxCp2;
    @ViewById(R.id.zxcp3)
    TextView mZxCp3;
    @ViewById(R.id.zxcp4)
    TextView mZxCp4;
    @ViewById(R.id.zximg1)
    ImageView mZxImg1;
    @ViewById(R.id.zximg2)
    ImageView mZxImg2;
    @ViewById(R.id.zximg3)
    ImageView mZxImg3;
    @ViewById(R.id.zximg4)
    ImageView mZxImg4;
    @ViewById(R.id.zjcp1)
    TextView mZjCp1;
    @ViewById(R.id.zjcp2)
    TextView mZjCp2;
    @ViewById(R.id.zjImg1)
    ImageView mZjImg1;
    @ViewById(R.id.zjImg2)
    ImageView mZjImg2;
    @ViewById(R.id.zjTitle1)
    TextView mZjTitle1;
    @ViewById(R.id.zjTitle2)
    TextView mZjTitle2;
    @ViewById(R.id.jmcp)
    TextView mjmCp;
    @ViewById(R.id.jmImg)
    ImageView mJmImg;
    @ViewById(R.id.jmtitle)
    TextView mJmTitle;
    @ViewById(R.id.jxCp)
    TextView mjxCp;
    @ViewById(R.id.jxImg)
    ImageView mjxImg;
    @ViewById(R.id.jxtitle)
    TextView mjxTitle;

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
    TextView[] mSjcpArray = null;
    TextView[] mZxTvArray = null;
    ImageView[] mZxImgArray = null;
    TextView[] mZxcpArray = null;
    TextView[] mrypTvArray = null;
    ImageView[] mrypImgArray = null;
    TextView[] mrypcpArray = null;
    TextView[] mzjTvArray = null;
    ImageView[] mzjImgArray = null;
    TextView[] mzjCpArray = null;
    TextView[] mjmTvArray = null;
    ImageView[] mjmImgArray = null;
    TextView[] mjmcpArray = null;
    TextView[] mjxTvArray = null;
    ImageView[] mjxImgArray = null;
    TextView[] mjxcpArray = null;


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

    @Click(R.id.shujuLayout)
    void gotoDetailProdect(){
        DetailProductFragment fragment = GeneratedClassUtils.getInstance(DetailProductFragment.class);
        fragment.tag = DetailProductFragment.class.getName();
        Bundle bundle = new Bundle();
        bundle.putString("ProviderID", "11");
        bundle.putString("CustomerID", BestarApplication.getInstance().getUserId());
        fragment.setArguments(bundle);
        fragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
                R.anim.anim_fragment_close_out);
        fragment.setContainerId(R.id.main_container);
        fragment.setManager(getFragmentManager());

        fragment.show(SuperFragment.SHOW_ADD_HIDE);
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
        mSjTvArray = new TextView[]{mSjTitle};
        mSjImgArray = new ImageView[]{mSjImg};
        mSjcpArray = new TextView[]{mSjcp};

        mZxTvArray = new TextView[]{mZxTitle1,mZxTitle2,mZxTitle3,mZxTitle4};
        mZxImgArray = new ImageView[]{mZxImg1,mZxImg2,mZxImg3,mZxImg4};
        mZxcpArray = new TextView[]{mZxCp1,mZxCp2,mZxCp3,mZxCp4};

        mrypTvArray = new TextView[]{mrypTitle1,mrypTitle2,mrypTitle3,mrypTitle4,mrypTitle5};
        mrypImgArray = new ImageView[]{mrypImg1,mrypImg2,mrypImg3,mrypImg4,mrypImg5};
        mrypcpArray = new TextView[]{mrypcp1,mrypcp2,mrypcp3,mrypcp4,mrypcp5};
        mzjTvArray = new TextView[]{mZjTitle1,mZjTitle2};
        mzjImgArray = new ImageView[]{mZjImg1,mZjImg2};
        mzjCpArray = new TextView[]{mZjCp1,mZjCp2};
        mjmTvArray = new TextView[]{mJmTitle};
        mjmImgArray = new ImageView[]{mJmImg};
        mjmcpArray = new TextView[]{mjmCp};
        mjxTvArray = new TextView[]{mjxTitle};
        mjxImgArray = new ImageView[]{mjxImg};
        mjxcpArray = new TextView[]{mjxCp};

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
                       setViewData(i,mZxTvArray,mZxImgArray,mZxcpArray);
                   }else if (mDataList.get(i).getClassName().equals("幼儿园日用品")){
                       setViewData(i,mrypTvArray,mrypImgArray,mrypcpArray);
                   }else if (mDataList.get(i).getClassName().equals("胎教早教课程")){
                       setViewData(i,mzjTvArray,mzjImgArray,mzjCpArray);
                   }else if (mDataList.get(i).getClassName().equals("进修培训讲座")){
                       setViewData(i,mjxTvArray,mjxImgArray,mjxcpArray);
                   }else if (mDataList.get(i).getClassName().equals("加盟联锁服务")){
                       setViewData(i,mjmTvArray,mjmImgArray,mjmcpArray);
                   }else if (mDataList.get(i).getClassName().equals("教材书籍")){
                       setViewData(i,mSjTvArray,mSjImgArray,mSjcpArray);
                   }else if (mDataList.get(i).getClassName().equals("国际品牌")){

                   }else if (mDataList.get(i).getClassName().equals("管理软件")){

                   }else if (mDataList.get(i).getClassName().equals("室内教玩具")){

                   }else if (mDataList.get(i).getClassName().equals("配套设备")){

                   }
               }
        }
    }
    @UiThread
    void setViewData(int position,TextView[] titleArray,ImageView[] imgArray,TextView[] productArray){
        List<MainDataBean.ShopClasses> shopClasseses = mDataList.get(position).getShopclasses();
        for (int s=0;s<shopClasseses.size();s++){
            if (s > titleArray.length-1){
                break;
            }
            MainDataBean.ShopClasses shopClasses = shopClasseses.get(s);
            titleArray[s].setText(shopClasses.getClassName());
            MainDataBean.Products products = getProducts(position,shopClasses.getID());
            if (products!=null){
                ImageLoader.getInstance().displayImage(products.getPicUrl(), imgArray[s], options, animateFirstListener);
                productArray[s].setText(products.getProductName());
            }
        }
    }

    private MainDataBean.Products getProducts(int position,Long classId){
        List<MainDataBean.Products> productses = mDataList.get(position).getProducts();
        if (productses==null){
            return null;
        }
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
        gotoProductListFragment();
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
