package com.manyi.mall.search;

import android.animation.LayoutTransition;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.LayoutAnimationController;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.exception.RestException;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.DeviceUtil;
import com.huoqiu.framework.util.DialogBuilder;
import com.huoqiu.framework.util.GeneratedClassUtils;
import com.huoqiu.framework.util.ManyiUtils;
import com.huoqiu.widget.FangyouReleasedViewPage;
import com.huoqiu.widget.pinnedlistview.PinnedHeaderListView;
import com.huoqiu.widget.pinnedlistview.SectionedBaseAdapter;
import com.huoqiu.widget.viewpageindicator.CirclePageIndicator;
import com.manyi.mall.BestarApplication;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.BaseResponse;
import com.manyi.mall.cachebean.mine.FootprintListResponse;
import com.manyi.mall.cachebean.search.HotSearchBean;
import com.manyi.mall.cachebean.search.OrderInfoBean;
import com.manyi.mall.cachebean.search.SearchHistoryBean;
import com.manyi.mall.cachebean.search.TypeProductBean;
import com.manyi.mall.common.Constants;
import com.manyi.mall.common.util.CommonUtil;
import com.manyi.mall.interfaces.SelectItemClickListener;
import com.manyi.mall.interfaces.SelectViewCloseListener;
import com.manyi.mall.service.RequestServerFromHttp;
import com.manyi.mall.utils.HardwareInfo;
import com.manyi.mall.utils.JsonData;
import com.manyi.mall.utils.TextViewUtil;
import com.manyi.mall.wap.BusinessWapFragment;
import com.manyi.mall.wap.DetailProductFragment;
import com.manyi.mall.widget.filtrate.FiltrateView;
import com.manyi.mall.widget.refreshview.NLPullRefreshView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FocusChange;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by bestar on 2015/1/29.
 */
@EFragment(R.layout.fragment_search_product)
public class SearchProductListFragment extends SuperFragment  implements NLPullRefreshView.RefreshListener {
    @ViewById(R.id.filtrateView)
    FiltrateView mFiltrateView;

    @ViewById(R.id.hot_radio_layout)
    CirclePageIndicator mIndicator;
    @ViewById(R.id.hotSearchViewPager)
    FangyouReleasedViewPage mViewPage;

    @ViewById(R.id.selectLayout1)
    TextView mSelectLayout1;

    @ViewById(R.id.hotSearchLayout)
    LinearLayout mHotSearchLayout;

    @ViewById(R.id.selectLayout2)
    TextView mSelectLayout2;

    @ViewById(R.id.refreshable_view)
    NLPullRefreshView mRefreshableView;

    @ViewById(R.id.searchListView)
    PinnedHeaderListView mCheckedListView;
    @ViewById(R.id.searchEt)
    EditText mSearchEt;
    @ViewById(R.id.selectLayoutLine)
    View mLine;

    @ViewById(R.id.clearBtn)
    ImageButton mClearBtn;

    @ViewById(R.id.historyListView)
    ListView mHistoryListView;

    @FragmentArg
    boolean isHaveHistory;

    ViewpageAdapter mViewPageAdapter = null;
    int RadioID = 0x001100;

    private ScheduledExecutorService scheduledExecutor;
    ArrayList<View> mPageViews = new ArrayList<View>();

    HistoryAdapter mHistoryAdapter;
    List<Map<String,Object>> mLists =null;
    List<TypeProductBean> mTypeLists =null;
    List<HotSearchBean> mHotSearchList =null;
    OrderInfoBean mOrderInfo = null;
    ProductSectionListAdapter mAdapter = null;
    ArrayAdapter arrayAdapter =null;
    String[] typeArray = null;
    String[] noopsycheArray = null;
    private int typeSelected = 0;//已选择的类型
    private int orderSelected = 0;//已选择的排序下标
    RequestServerFromHttp request = new RequestServerFromHttp();
    TextView mFootView;
    ProgressDialog mProgressDialog;
    @FragmentArg
    Long classId;

    private void showDialog(String msg){
        if (mProgressDialog!=null){
            mProgressDialog = null;
        }
        mProgressDialog = new ProgressDialog(getActivity(),ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage(msg);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    private void dismissDialog(){
        if (mProgressDialog!=null){
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }
    @AfterViews
    void init(){
        mRefreshableView.setRefreshListener(this);
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getActivity()));
        initOption();
        if (isHaveHistory){
            getUserSearchRecord();
//        ManyiUtils.showKeyBoard(getActivity(),mSearchEt);
            initHotView();
        }else{
            showDialog("加载数据中，请稍候...");
            mFiltrateView.setVisibility(View.VISIBLE);
            getProductListFromID();
            getSecondListFromFirstId();
            getOrderInfo();
        }
    }

    private void initHotView(){
        mViewPageAdapter = new ViewpageAdapter();
        mViewPage.setAdapter(mViewPageAdapter);
        getAdvertData();
    }
    @Background
    void getProductListFromID(){
        String msg = request.getProductsByFirstId(classId + "", "0", "20", "ID", "desc");
        mLists =  new JsonData().jsonFootprint(msg);
        notifySearchListView(false);
    }

    @Background
    void getSecondListFromFirstId(){
        String msg = request.getTypeSecond(classId+"");
        mTypeLists =  new JsonData().jsonTypeProductList(msg);
        typeArray = new String[mTypeLists.size()];
        for (int i=0;i<mTypeLists.size();i++){
            typeArray[i] = mTypeLists.get(i).ClassName;
        }
    }
    @Background
    public void getAdvertData() {
        try {
            String msg = request.getHotSearch("1","20");
            mHotSearchList = new JsonData().jsonHotSearch(msg);
            notifyAdvert();
        } catch (RestException e) {

        }
    }
    @AfterTextChange(R.id.searchEt)
    void AfterTextChange(){
        if(mFiltrateView.isOpen()){
            mFiltrateView.closeSelectView();
        }
        if (mSearchEt.getText().toString().trim().length() > 0){
            mClearBtn.setVisibility(View.VISIBLE);
            getData();
            getDataType();
            getOrderInfo();
        }else{
            mClearBtn.setVisibility(View.GONE);
            if (mLists!=null){
                mLists.clear();
            }
            notifySearchListView(false);
            getUserSearchRecord();
        }

    }

    @FocusChange(R.id.selectLayout1)
    void onFocusChange1(View view, boolean b){
        if (view!=mSelectLayout1 && view!=mSelectLayout2){
            mFiltrateView.closeSelectView();
        }
    }
    @FocusChange(R.id.selectLayout2)
    void onFocusChange2(View view, boolean b){
        if (view!=mSelectLayout1 && view!=mSelectLayout2){
            mFiltrateView.closeSelectView();
        }
    }

    @Click(R.id.search_back)
    void back(){
        remove();
    }

    @Background
    void getData(){
        String msg = request.searchProducts("1", "20", mSearchEt.getText().toString());
        mLists =  new JsonData().jsonFootprint(msg);
        notifySearchListView(false);

    }

    @Background
    void getOrderInfo(){
        String msg = request.getOrderInfo();
        mOrderInfo =  new JsonData().jsonOrderInfo(msg);
        noopsycheArray = new String[mOrderInfo.OrderByField.size()*2];
        for (int i=0;i<mOrderInfo.OrderByField.size();i++){
                noopsycheArray[i*2] = "按"+mOrderInfo.OrderByField.get(i).Name+"降序";
                noopsycheArray[i*2+1] = "按"+mOrderInfo.OrderByField.get(i).Name+"升序";
        }
    }

    @Background
    void getDataType(){
        String msg = request.searchProductTypes(mSearchEt.getText().toString());
        mTypeLists =  new JsonData().jsonTypeProductList(msg);
        typeArray = new String[mTypeLists.size()];
        for (int i=0;i<mTypeLists.size();i++){
            typeArray[i] = mTypeLists.get(i).ClassName;
        }
    }
    List<SearchHistoryBean> mUserHistoryList;
    @Background
    void getUserSearchRecord(){
        String msg = request.getUserSearched("1","20");
        Log.d("bestar",msg);
        mUserHistoryList = new JsonData().jsonSearchHistory(msg);
        notifyHistory();
    }


    @Background
    void getDataByInputAndType(){
        String searchKey = mSearchEt.getText().toString().trim();
        String ClassID = mTypeLists.get(typeSelected).ID ;
        String orderField = mOrderInfo.OrderByField.get(orderSelected/2).Value;
        String order = (orderSelected%2==0)?"desc":"asc";
        String PageIndex ="1";
        String PageSize ="20";
        String msg = request.searchProductByTypeAndInput(searchKey,ClassID,orderField!=null?orderField:"ID",order,PageIndex,PageSize);
        mLists =  new JsonData().jsonFootprint(msg);
        notifySearchListView(false);
    }

    @UiThread
    void notifyHistory(){
        mHistoryAdapter = new HistoryAdapter();
        mHistoryListView.setAdapter(mHistoryAdapter);
        mHistoryListView.setFooterDividersEnabled(true);
        if (mHistoryListView.getFooterViewsCount() == 0 && mUserHistoryList!=null && mUserHistoryList.size()>0){
            mFootView = getFootView();
            mHistoryListView.addFooterView(mFootView);
        }else if (mFootView!=null && (mUserHistoryList == null || mUserHistoryList.size() == 0)){
            mHistoryListView.removeFooterView(mFootView);
        }
    }

    private TextView getFootView(){
        TextView textView = new TextView(getActivity());
        textView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT));
        textView.setText("清除历史记录");
        textView.setTextColor(Color.parseColor("#88333333"));
        textView.setBackgroundColor(Color.WHITE);
        textView.setTextSize(18);
        textView.setPadding(20, 20, 20, 20);
        textView.setGravity(Gravity.CENTER);
        textView.setClickable(true);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogBuilder.showSimpleDialog("确定清空历史搜索记录？","确定","取消",getActivity(),new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearUserSearchHistory();
                    }
                });
            }
        });
        return textView;
    }

    @Background
    void clearUserSearchHistory(){
        String msg = request.clearUserSearch();
        BaseResponse response = new JsonData().JsonBase(msg);
        mUserHistoryList.clear();
        notifyHistory();
    }

    @UiThread
    void notifySearchListView(boolean isRefresh) {
        if (isRefresh) {
            mRefreshableView.finishRefresh();
        }
        TextView emptyView = new TextView(getActivity());
        emptyView.setText("您还没有查看任何产品哦！先去看看吧！");
        emptyView.setTextColor(Color.parseColor("#333333"));
        emptyView.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        emptyView.setLayoutParams(layoutParams);
        mCheckedListView.setEmptyView(emptyView);
        mAdapter = new ProductSectionListAdapter();
        mCheckedListView.setAdapter(mAdapter);
        dismissDialog();
//        if (mLists!=null && mLists.size()>0){
//            mFiltrateView.setVisibility(View.VISIBLE);
//        }else{
//            mFiltrateView.setVisibility(View.GONE);
//        }
    }
    @Override
    public void onRefresh(NLPullRefreshView view) {
        notifySearchListView(true);
    }

    @Click(R.id.clearBtn)
    void clearInput(){
        mSearchEt.setText("");
        mClearBtn.setVisibility(View.GONE);
    }

    @ItemClick(R.id.historyListView)
    void OnHistoryItemClick(int position){
        mSearchEt.setText(mUserHistoryList.get(position).SearchWord);
        TextViewUtil.setTextSelection(mSearchEt,mSearchEt.getText().toString().length());
    }
    public class ProductSectionListAdapter extends SectionedBaseAdapter {

        FootprintListResponse responses;


        // 审核成功：文字颜色12c1c4 失败：8a000000
        @Override
        public Object getItem(int section, int position) {
            return ((List<Map<String,String>>)mLists.get(section).get("productList")).get(position);
        }

        @Override
        public long getItemId(int section, int position) {
            return position;
        }

        @Override
        public int getSectionCount() {
            return mLists.size();
        }

        @Override
        public int getCountForSection(int section) {
            return ((List<Map<String,String>>)mLists.get(section).get("productList")).size();
        }

        @Override
        public View getItemView(final int section, final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_footprint_list_product, null);
                holder.productNameTv = (TextView) convertView.findViewById(R.id.companyNameTv);
                holder.moneyTv = (TextView) convertView.findViewById(R.id.moneyTv);
                holder.clickCountTv = (TextView) convertView.findViewById(R.id.clickCountTv);
                holder.visitCountTv = (TextView) convertView.findViewById(R.id.visitCountTv);
                holder.priaseCountTv = (TextView) convertView.findViewById(R.id.praiseCountTv);
                holder.imageView = (ImageView) convertView.findViewById(R.id.imgCollect);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            // Map<String,String> map = ((ArrayList<Map<String,String>>)mList.get(section).get("itemList")).get(position);
            final Map<String,String> response = ((List<Map<String,String>>)mLists.get(section).get("productList")).get(position);
            holder.productNameTv.setText(response.get("ProductName"));
            holder.moneyTv.setText(response.get("Price"));
            holder.clickCountTv.setText(response.get("ClickNum"));
            holder.visitCountTv.setText(response.get("ConsultNum"));
            holder.priaseCountTv.setText(response.get("PraiseNum"));
            String imgUrl = response.get("PicUrl");
            if (imgUrl!=null){
                ImageLoader.getInstance().displayImage(imgUrl, holder.imageView, options, animateFirstListener);
            }else{
                holder.imageView.setBackgroundResource(R.drawable.take_photos_list_no__thumbnail);
            }
            convertView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    OnItemClick(section,position);
                }
            });
            return convertView;
        }

        public class ViewHolder {
            TextView productNameTv, moneyTv, clickCountTv,visitCountTv,priaseCountTv;
            ImageView imageView;
        }

        public class SectionHolder {
            TextView companyNameTv,cityNameTv;
        }

        @Override
        public View getSectionHeaderView(final int section, View convertView, ViewGroup parent) {
            SectionHolder holder = null;
            if (convertView == null) {
                holder = new SectionHolder();
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_search_product_sections, null);
                holder.companyNameTv = (TextView) convertView.findViewById(R.id.companyNameTv);
                holder.cityNameTv = (TextView) convertView.findViewById(R.id.cityNameTv);
                convertView.setTag(holder);
            } else {
                holder = (SectionHolder) convertView.getTag();
            }
            String companyName = mLists.get(section).get("ProviderName").toString();
            String cityName = mLists.get(section).get("ProviderCityName").toString();
            holder.companyNameTv.setText(companyName);
            holder.cityNameTv.setText(cityName);
            holder.companyNameTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnSectionItemClick(section);
                }
            });
            return convertView;
        }
    }

    @Click(R.id.selectLayout1)
    void selectLayout1(){
        if (typeArray==null || typeArray.length == 0){
            return ;
        }
        initSelect1Data();
        if (mFiltrateView.isAdded()){
            mFiltrateView.closeSelectView();
            mSelectLayout1.setTextColor(Color.parseColor("#80000000"));
            mSelectLayout1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.more,0,R.drawable.icon_down,0);
        }else{
            mSelectLayout1.setTextColor(getResources().getColor(R.color.app_theme_color));
            mSelectLayout1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.more_h,0,R.drawable.icon_up,0);
            mFiltrateView.addSelectLayout(getActivity(),arrayAdapter,mLine,new SelectItemClickListener() {
                @Override
                public void ItemClick(int position) {
                    mFiltrateView.closeSelectView();
                    typeSelected = position;
                    getDataByInputAndType();
                }
            });
            mFiltrateView.setOnSelectCloseListener(new SelectViewCloseListener() {
                @Override
                public void OnClose() {
                    mSelectLayout1.setTextColor(Color.parseColor("#80000000"));
                    mSelectLayout1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.more,0,R.drawable.icon_down,0);
                }
            });
        }

    }

    @Override
    public void onPause() {
        if (mViewPage != null && mIndicator != null && scheduledExecutor != null) {
            scheduledExecutor.shutdown();
            scheduledExecutor = null;
        }
        super.onPause();
    }
@Click(R.id.selectLayout2)
    void selectLayout2(){
    if (noopsycheArray==null || noopsycheArray.length == 0){
        return ;
    }
        initSelect2Data();
        if (mFiltrateView.isAdded()){
            mFiltrateView.closeSelectView();
            mSelectLayout2.setTextColor(Color.parseColor("#80000000"));
            mSelectLayout2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.screening,0,R.drawable.icon_down,0);
        }else{
            mSelectLayout2.setTextColor(getResources().getColor(R.color.app_theme_color));
            mSelectLayout2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.screening_h,0,R.drawable.icon_up,0);
            mFiltrateView.addSelectLayout(getActivity(),arrayAdapter,mLine,new SelectItemClickListener() {
                @Override
                public void ItemClick(int position) {
                    orderSelected = position;
                    mFiltrateView.closeSelectView();
                    getDataByInputAndType();
                }
            });
            mFiltrateView.setOnSelectCloseListener(new SelectViewCloseListener() {
                @Override
                public void OnClose() {
                    mSelectLayout2.setTextColor(Color.parseColor("#80000000"));
                    mSelectLayout2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.screening,0,R.drawable.icon_down,0);
                }
            });
        }

    }

    class HistoryAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mUserHistoryList.size();
        }

        @Override
        public Object getItem(int position) {
            return mUserHistoryList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_search_history,null);
            TextView contentView = (TextView) convertView.findViewById(R.id.contentTv);
            contentView.setText(mUserHistoryList.get(position).SearchWord);
            return convertView;
        }
    }
    void OnItemClick(int section,int position){
        DetailProductFragment fragment = GeneratedClassUtils.getInstance(DetailProductFragment.class);
        fragment.tag = DetailProductFragment.class.getName();
        Bundle bundle = new Bundle();
        bundle.putString("ProductID", ((List<Map<String,String>>)mLists.get(section).get("productList")).get(position).get("ID"));
        bundle.putString("ProviderID", (String) mLists.get(section).get("ProviderID"));
        bundle.putString("CustomerID", BestarApplication.getInstance().getUserId());
        fragment.setArguments(bundle);
        fragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
                R.anim.anim_fragment_close_out);
        fragment.setContainerId(R.id.main_container);
        fragment.setManager(getFragmentManager());

        fragment.show(SuperFragment.SHOW_ADD_HIDE);
    }

    void OnSectionItemClick(int section){
        BusinessWapFragment fragment = GeneratedClassUtils.getInstance(BusinessWapFragment.class);
        fragment.tag = BusinessWapFragment.class.getName();
        Bundle bundle = new Bundle();
        bundle.putString("ProviderID", (String) mLists.get(section).get("ProviderID"));
        bundle.putString("CustomerID", BestarApplication.getInstance().getUserId());
        fragment.setArguments(bundle);
        fragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
                R.anim.anim_fragment_close_out);
        fragment.setContainerId(R.id.main_container);
        fragment.setManager(getFragmentManager());

        fragment.show(SuperFragment.SHOW_ADD_HIDE);
    }
    private void initSelect1Data(){
        arrayAdapter = new ArrayAdapter(getActivity(),R.layout.item_select_list,R.id.selectContent,typeArray);
    }

    private void initSelect2Data(){
        arrayAdapter = new ArrayAdapter(getActivity(),R.layout.item_select_list,R.id.selectContent,noopsycheArray);
    }

    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                if (imageView!=null){
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


    @UiThread
    public void notifyAdvert() {
        mPageViews.clear();
        int countHot = mHotSearchList.size();
        if (countHot>0){
            HotSearchBean hotSearchBean = new HotSearchBean();
            hotSearchBean.SearchWord = "热门搜索";
            mHotSearchList.add(0,hotSearchBean);
            countHot = mHotSearchList.size();
        }
        int countPage = (countHot/8+1);
        for (int i = 0; i < countPage; i++) {
            View mView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_hot_search, null);
            GridLayout gridView = (GridLayout) mView.findViewById(R.id.gridLayout);
            if (i==countPage-1){
                for (int index=i*8;index<mHotSearchList.size();index++){
                    gridView.addView(getHotSearchView(index));
                }
            }else{
                for (int index=i*8;index<i*8+8;index++){
                    gridView.addView(getHotSearchView(index));
                }
            }

//            HotSearchAdapter hotSearchAdapter = new HotSearchAdapter(getActivity(),mHotSearchList);
//            gridView.setAdapter(hotSearchAdapter);
            mPageViews.add(mView);
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT,CommonUtil.dip2px(getActivity(),115));
        mHotSearchLayout.setLayoutParams(params);
        mViewPageAdapter = new ViewpageAdapter();
        mViewPage.setAdapter(mViewPageAdapter);
        if (mPageViews.size() > 1) {
            mIndicator.setViewPager(mViewPage);
        }
    }
    private View getHotSearchView(final int position){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_hot_search,null);
        TextView contentTv = (TextView) view.findViewById(R.id.contentTv);
        String searchWord = mHotSearchList.get(position).SearchWord;
        contentTv.setText(searchWord);
        if (searchWord.equals("热门搜索")){
            contentTv.setTextColor(getResources().getColor(R.color.app_theme_color));
        }
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(HardwareInfo.getScreenWidth(getActivity())/4, RelativeLayout.LayoutParams.WRAP_CONTENT);
        contentTv.setLayoutParams(params);
        contentTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position!=0){
                    mSearchEt.setText(mHotSearchList.get(position).SearchWord);
                    TextViewUtil.setTextSelection(mSearchEt,mSearchEt.getText().toString().length());
                }

            }
        });
        return view;
    }
    /**
     * view page Adapater
     *
     * @author jason
     */
    class ViewpageAdapter extends PagerAdapter implements View.OnClickListener {

        @Override
        public int getCount() {
            return mPageViews.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView(mPageViews.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = mPageViews.get(position);
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
}
