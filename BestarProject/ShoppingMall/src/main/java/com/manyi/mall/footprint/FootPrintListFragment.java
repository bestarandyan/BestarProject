package com.manyi.mall.footprint;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.util.DialogBuilder;
import com.huoqiu.framework.util.GeneratedClassUtils;
import com.huoqiu.widget.pinnedlistview.PinnedHeaderListView;
import com.huoqiu.widget.pinnedlistview.SectionedBaseAdapter;
import com.manyi.mall.BestarApplication;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.BaseResponse;
import com.manyi.mall.cachebean.collect.CollectListBean;
import com.manyi.mall.cachebean.footprint.FootPrintBean;
import com.manyi.mall.cachebean.footprint.FootPrintProductBean;
import com.manyi.mall.utils.JsonData;
import com.manyi.mall.cachebean.mine.FootprintListResponse;
import com.manyi.mall.service.RequestServerFromHttp;
import com.manyi.mall.wap.BusinessWapFragment;
import com.manyi.mall.wap.DetailProductFragment;
import com.manyi.mall.widget.refreshview.NLPullRefreshView;
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
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@EFragment(R.layout.fragment_footprintlist)
public class FootPrintListFragment extends SuperFragment  implements NLPullRefreshView.RefreshListener {
//    @ViewById(R.id.refreshable_view)
//    NLPullRefreshView mRefreshableView;

    @ViewById(R.id.footprintListView)
    PinnedHeaderListView mCheckedListView;

    @ViewById(R.id.footEditBtn)
    ImageButton mEditBtn;

    @ViewById(R.id.allCheckedView)
    CheckBox mAllCheckBox;

    FootprintSectionListAdapter mAdapter = null;
    List<FootPrintBean> mLists =null;
    @ViewById(R.id.bottomLayout)
    LinearLayout mBottomLayout;
    boolean isEditing = false;
    ProgressDialog mProgressDialog;
    RequestServerFromHttp request = new RequestServerFromHttp();
    @AfterViews
    void init(){
//        mRefreshableView.setRefreshListener(this);
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getActivity()));
        initOption();
        showProgressDialog("数据加载中，请稍候...");
        getData();
    }
    void OnItemClick(int section,int position){
        DetailProductFragment fragment = GeneratedClassUtils.getInstance(DetailProductFragment.class);
        fragment.tag = DetailProductFragment.class.getName();
        Bundle bundle = new Bundle();
        bundle.putString("ProductID", mLists.get(section).productList.get(position).ID);
        bundle.putString("ProviderID",  mLists.get(section).ProviderID);
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
        bundle.putString("ProviderID", (mLists.get(section).ProviderID));
        bundle.putString("CustomerID", BestarApplication.getInstance().getUserId());
        fragment.setArguments(bundle);
        fragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
                R.anim.anim_fragment_close_out);
        fragment.setContainerId(R.id.main_container);
        fragment.setManager(getFragmentManager());

        fragment.show(SuperFragment.SHOW_ADD_HIDE);
    }
    @CheckedChange(R.id.allCheckedView)
    void onCheckedChange(CompoundButton buttonView, boolean isChecked){
        if (mLists==null || mLists.size() == 0){
            return;
        }
//        if (isFromClick){
//            isFromClick = false;
//            return;
//        }
        resetCheckBoxList(isChecked);

    }

    private void resetCheckBoxList(boolean isChecked){
        for (int i=0;i<mLists.size();i++){
            mLists.get(i).isAllChecked = isChecked;
            List<FootPrintProductBean> list = mLists.get(i).productList;
            for (int j=0;j<list.size();j++){
                list.get(j).isChecked = isChecked;
            }
            if (mAdapter!=null){
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Click(R.id.footEditBtn)
    void edit(){
        if (mBottomLayout.getVisibility() == View.GONE){
            mBottomLayout.setVisibility(View.VISIBLE);
            mEditBtn.setImageResource(R.drawable.selector_comple);
            isEditing = true;
        }else{
            mBottomLayout.setVisibility(View.GONE);
            isEditing = false;
            mEditBtn.setImageResource(R.drawable.selector_edit_info_btn);
        }
        notifyCheckedList(false);
    }

    @Click(R.id.deleteBtn)
    void clickDelete(){
        DialogBuilder.showSimpleDialog("确定删除所选足迹？","确定","取消",getActivity(),new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DeleteFootPrint();
            }
        });
    }

    private String getProductIDs() {
        String msg = "";
        for (FootPrintBean bean : mLists) {
            for (FootPrintProductBean productBean : bean.productList) {
                if (productBean.isChecked) {
                    msg += productBean.ID + ",";
                }
            }
        }
        if (msg.length() > 0 && msg.endsWith(",")) {
            msg = msg.substring(0, msg.length() - 1);
        }
        return msg;
    }
    @Background
    void DeleteFootPrint(){
        String productIds = getProductIDs();
        if (productIds!=null && productIds.length()>0){
            String msg = request.deleteFootPrint(productIds);
            BaseResponse baseResponse = new JsonData().JsonBase(msg);
            if (baseResponse.getCode().equals("0")){
                showDialog("删除成功");
                getData();
            }else{
                showDialog(baseResponse.getMessage());
            }
        }else{
            showDialog("请先选择要删除的足迹");
        }
    }

    @UiThread
    void showDialog(String msg){
        DialogBuilder.showSimpleDialog(msg,getActivity());
    }

    @Background
    void getData(){
        String msg = request.getFootList("1","100");
        mLists =  new JsonData().jsonFootprint(msg);
        notifyCheckedList(false);
    }


    @Override
    public void onAttach(Activity activity) {
        setBackOp(null);
        super.onAttach(activity);
    }

    @Override
    public void onPause() {
        super.onPause();
        resetCheckBoxList(false);
        isEditing = false;
    }

    @UiThread
    void notifyCheckedList(boolean isRefresh) {
//        if (isRefresh) {
//            mRefreshableView.finishRefresh();
//        }
        TextView emptyView = new TextView(getActivity());
        emptyView.setText("您还没有查看任何产品哦！先去看看吧！");
        emptyView.setTextColor(Color.parseColor("#333333"));
        emptyView.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        emptyView.setLayoutParams(layoutParams);
        mCheckedListView.setEmptyView(emptyView);
        mAdapter = new FootprintSectionListAdapter();
        mCheckedListView.setAdapter(mAdapter);
        mCheckedListView.setPinHeaders(false);
        mCheckedListView.setOnItemClickListener(new PinnedHeaderListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int section, int position, long id) {
                OnItemClick(section,position);
            }

            @Override
            public void onSectionClick(AdapterView<?> adapterView, View view, int section, long id) {
                OnSectionItemClick(section);
            }
        });
        if (mLists == null || mLists.size()==0){
            mEditBtn.setVisibility(View.GONE);
        }else{
            mEditBtn.setVisibility(View.VISIBLE);
        }
    dismissProgressDialog();
    }

    @Override
    public void onRefresh(NLPullRefreshView view) {
          notifyCheckedList(true);
    }




    public class FootprintSectionListAdapter extends SectionedBaseAdapter {
        @Override
        public Object getItem(int section, int position) {
            return mLists.get(section).productList.get(position);
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
            return mLists.get(section).productList.size();
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
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBoxItem);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            // Map<String,String> map = ((ArrayList<Map<String,String>>)mList.get(section).get("itemList")).get(position);
//            final Map<String,String> response = ((List<Map<String,String>>)mLists.get(section).get("productList")).get(position);
            final FootPrintProductBean productBean = mLists.get(section).productList.get(position);
            holder.productNameTv.setText(productBean.ProductName);
            holder.moneyTv.setText("￥"+productBean.Price);
            holder.clickCountTv.setText(productBean.ClickNum);
            holder.visitCountTv.setText(productBean.ConsultNum);
            holder.priaseCountTv.setText(productBean.PraiseNum);
            convertView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    OnItemClick(section,position);
                }
            });
            String imgUrl = productBean.PicUrl;
            if (imgUrl!=null){
                ImageLoader.getInstance().displayImage(imgUrl, holder.imageView, options, animateFirstListener);
            }else{
                holder.imageView.setBackgroundResource(R.drawable.take_photos_list_no__thumbnail);
            }
            holder.checkBox.setTag(position);
            if (isEditing){
                holder.checkBox.setVisibility(View.VISIBLE);
//                String isSelected = ((List<Map<String,String>>)mLists.get(section).get("productList")).get((Integer) holder.checkBox.getTag()).get("isChecked");
                holder.checkBox.setChecked(mLists.get(section).productList.get(position).isChecked);
            }else{
                holder.checkBox.setVisibility(View.GONE);
            }
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (!isChecked){
//                        mLists.get(section).put("isAllChecked", isChecked);
                        mLists.get(section).productList.get(position).isChecked = isChecked;
//                        mAllCheckBox.setChecked(false);
//                        isFromClick = true;
                    }else{
                        mLists.get(section).productList.get(position).isChecked = isChecked;
                    }

                    mAdapter.notifyDataSetChanged();
                }
            });
            return convertView;
        }

        public class ViewHolder {
            TextView productNameTv, moneyTv, clickCountTv,visitCountTv,priaseCountTv;
            ImageView imageView;
            CheckBox checkBox;
        }

        public class SectionHolder {
            TextView companyNameTv,cityNameTv;
            CheckBox checkBox;
        }

        @Override
        public View getSectionHeaderView(final int section, View convertView, ViewGroup parent) {
            SectionHolder holder = null;
            if (convertView == null) {
                holder = new SectionHolder();
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_footprint_list_sections, null);
                holder.companyNameTv = (TextView) convertView.findViewById(R.id.companyNameTv);
                holder.cityNameTv = (TextView) convertView.findViewById(R.id.cityNameTv);
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBoxSection);
                convertView.setTag(holder);
            } else {
                holder = (SectionHolder) convertView.getTag();
            }
            String companyName = mLists.get(section).ProviderName;
            String cityName = mLists.get(section).ProviderCityName;
            holder.companyNameTv.setText(companyName);
            holder.cityNameTv.setText(cityName);
            holder.checkBox.setTag(section);
            if (isEditing){
                holder.checkBox.setVisibility(View.VISIBLE);
                holder.checkBox.setChecked(mLists.get(section).isAllChecked);
            }else{
                holder.checkBox.setVisibility(View.GONE);
            }
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int index = (int) buttonView.getTag();
                    mLists.get(index).isAllChecked = isChecked;
                    if (!isChecked){
                        isFromClick = true;
//                        mAllCheckBox.setChecked(false);
                    }
                    for (int i=0;i<mLists.get(section).productList.size();i++){
                        mLists.get(section).productList.get(i).isChecked = isChecked;
                    }

                    mAdapter.notifyDataSetChanged();
                }
            });
            return convertView;
        }
    }
    boolean isFromClick = false;
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