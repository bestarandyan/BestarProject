package com.manyi.mall.search;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.util.GeneratedClassUtils;
import com.huoqiu.widget.pinnedlistview.PinnedHeaderListView;
import com.huoqiu.widget.pinnedlistview.SectionedBaseAdapter;
import com.manyi.mall.BestarApplication;
import com.manyi.mall.R;
import com.manyi.mall.utils.JsonData;
import com.manyi.mall.cachebean.mine.FootprintListResponse;
import com.manyi.mall.cachebean.search.OrderInfoBean;
import com.manyi.mall.cachebean.search.TypeProductBean;
import com.manyi.mall.interfaces.SelectItemClickListener;
import com.manyi.mall.interfaces.SelectViewCloseListener;
import com.manyi.mall.service.RequestServerFromHttp;
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
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by bestar on 2015/1/29.
 */
@EFragment(R.layout.fragment_search_product)
public class SearchProductListFragment extends SuperFragment  implements NLPullRefreshView.RefreshListener {
    @ViewById(R.id.filtrateView)
    FiltrateView mFiltrateView;

    @ViewById(R.id.selectLayout1)
    TextView mSelectLayout1;

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
    List<Map<String,Object>> mLists =null;
    List<TypeProductBean> mTypeLists =null;
    OrderInfoBean mOrderInfo = null;
    ProductSectionListAdapter mAdapter = null;
    ArrayAdapter arrayAdapter =null;
    String[] typeArray = null;
    String[] noopsycheArray = null;
    private int typeSelected = 0;//已选择的类型
    private int orderSelected = 0;//已选择的排序下标
    RequestServerFromHttp request = new RequestServerFromHttp();
    @AfterViews
    void init(){
        mRefreshableView.setRefreshListener(this);
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getActivity()));
        initOption();
    }

    @AfterTextChange(R.id.searchEt)
    void AfterTextChange(){
        if (mSearchEt.getText().toString().trim().length() > 0){
            getData();
            getDataType();
            getOrderInfo();
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
        notifyCheckedList(false);
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
        notifyCheckedList(false);
    }


    @UiThread
    void notifyCheckedList(boolean isRefresh) {
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

    }
    @Override
    public void onRefresh(NLPullRefreshView view) {
        notifyCheckedList(true);
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
                holder.voucherImg = (ImageView) convertView.findViewById(R.id.voucherImg);
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
            ImageView voucherImg;
        }

        public class SectionHolder {
            TextView companyNameTv,cityNameTv;
        }

        @Override
        public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
            SectionHolder holder = null;
            if (convertView == null) {
                holder = new SectionHolder();
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_footprint_list_sections, null);
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
    void OnItemClick(int section,int position){
        DetailProductFragment fragment = GeneratedClassUtils.getInstance(DetailProductFragment.class);
        fragment.tag = DetailProductFragment.class.getName();
        Bundle bundle = new Bundle();
        bundle.putString("ProviderID", ((List<Map<String,String>>)mLists.get(section).get("productList")).get(position).get("ID"));
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

}
