package com.manyi.mall.collect;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.util.DialogBuilder;
import com.huoqiu.framework.util.GeneratedClassUtils;
import com.manyi.mall.BestarApplication;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.BaseResponse;
import com.manyi.mall.utils.JsonData;
import com.manyi.mall.cachebean.collect.CollectListBean;
import com.manyi.mall.service.RequestServerFromHttp;
import com.manyi.mall.wap.BusinessWapFragment;
import com.manyi.mall.wap.DetailProductFragment;
import com.manyi.mall.widget.refreshview.LFListView;
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
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by bestar on 2015/1/26.
 */
@EFragment(R.layout.fragment_my_collect)
public class CollectFragment extends SuperFragment  implements LFListView.IXListViewListener{
    @ViewById(R.id.myCollectListView)
    LFListView mListView;

    @ViewById(R.id.editBtn)
    ImageButton mEditBtn;

    @ViewById(R.id.bottomLayout)
    LinearLayout mBottomLayout;

    List<CollectListBean> mList = new ArrayList<>();
    private Handler mHandler;
    boolean isEditing = false;
    CollectListAdapter adapter =null;
//    boolean isAllSelected = false;//是否全部选中
    ProgressDialog mProgressDialog;
    RequestServerFromHttp request = new RequestServerFromHttp();
    @Override
    public void onAttach(Activity activity) {
        setBackOp(null);
        super.onAttach(activity);
    }

    @AfterViews
    void init(){
        mHandler = new Handler();
        mListView.setPullLoadEnable(true);
        mListView.setXListViewListener(this);
        mListView.setSelectionAfterHeaderView();
        adapter = new CollectListAdapter();
        mListView.setAdapter(adapter);
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getActivity()));
        initOption();
        showProgressDialog("数据加载中，请稍候...");
        if (mList!=null){
            mList.clear();
        }
        getData("1");

    }

    @Click(R.id.deleteBtn)
    void clickDelete(){
        final String collectIds = getCollectIDs();
        if (collectIds!=null && collectIds.length() > 0) {
            DialogBuilder.showSimpleDialog("确定删除所选？","确定","取消",getActivity(),new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteCollect(collectIds);
                }
            });
        }else{
            DialogBuilder.showSimpleDialog("请先选择要删除的收藏！",getActivity());
        }
    }

    @Background
    void deleteCollect(String collectIds){
            String msg = request.deleteCollection(collectIds);
            BaseResponse response = new JsonData().JsonBase(msg);
            if (response!=null && response.getCode()!=null && response.getCode().equals("0")){
                showDeleteSuccess();
            }
    }
    @UiThread
    void showDeleteSuccess(){
        Toast.makeText(getActivity(),"删除成功",Toast.LENGTH_LONG).show();
//        adapter.setList(mList);
//        adapter.notifyDataSetChanged();
        if (mList!=null){
            mList.clear();
        }
        getData("1");
    }

    private String getCollectIDs(){
        String msg = "";
        for (CollectListBean bean:mList){
            if (bean.isSelected()){
                msg+=bean.getID()+",";
            }
        }
        if (msg.length()>0 && msg.endsWith(",")){
            msg = msg.substring(0,msg.length()-1);
        }
        return msg;
    }

    @CheckedChange(R.id.checkAll)
    void onCheckedChange(CompoundButton buttonView, boolean isChecked){
//        isAllSelected = isChecked;
        for (int i=0;i<mList.size();i++){
            mList.get(i).setSelected(isChecked);
        }
//        adapter.setList(mList);
        adapter.notifyDataSetChanged();
    }

    @Background
    void getData(String start){
        String msg = request.getCollect(start,"50");
        List<CollectListBean> list = new JsonData().jsonCollectList(msg);
        notifyListView(list);
    }

    @UiThread
    void notifyListView(List<CollectListBean> list){
        if (list!=null && list.size()>0){
            if (mList == null || mList.size() == 0){
                mList = list;
            }else {
                mList.addAll(list);
            }
        }
        if (mList==null || mList.size()<10){
            mListView.setNoMoreData("");
        }
        if (mList == null || mList.size()==0){
            mEditBtn.setVisibility(View.GONE);
            mBottomLayout.setVisibility(View.GONE);
            isEditing = false;
            mEditBtn.setImageResource(R.drawable.selector_edit_info_btn);
        }else{
            mEditBtn.setVisibility(View.VISIBLE);
        }
//        adapter.setList(mList);
        adapter.notifyDataSetChanged();
        dismissProgressDialog();
    }
    @ItemClick(R.id.myCollectListView)
    void OnItemClick(int position){
        BusinessWapFragment fragment = GeneratedClassUtils.getInstance(BusinessWapFragment.class);
        fragment.tag = BusinessWapFragment.class.getName();
        Bundle bundle = new Bundle();
        bundle.putString("ProviderID", mList.get(position-1).getID());
        bundle.putString("CustomerID", BestarApplication.getInstance().getUserId());
        fragment.setArguments(bundle);
        fragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
                R.anim.anim_fragment_close_out);
        fragment.setContainerId(R.id.main_container);
        fragment.setManager(getFragmentManager());

        fragment.show(SuperFragment.SHOW_ADD_HIDE);
    }

    @Click(R.id.editBtn)
    void clickEdit(){
        if (mBottomLayout.getVisibility() == View.GONE){
            mBottomLayout.setVisibility(View.VISIBLE);
            isEditing = true;
            mEditBtn.setImageResource(R.drawable.selector_comple);
        }else{
            mBottomLayout.setVisibility(View.GONE);
            isEditing = false;
            mEditBtn.setImageResource(R.drawable.selector_edit_info_btn);
        }
//        adapter.setList(mList);
        adapter.notifyDataSetChanged();
    }

    private void onLoad() {
        mListView.stopRefresh();
        mListView.stopLoadMore();
        mListView.setRefreshTime("刚刚");
    }
    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mList!=null){
                    mList.clear();
                }
                getData("1");
                onLoad();
            }
        }, 500);
    }

    @Override
    public void onLoadMore() {
        mListView.setPullLoadEnable(true);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getData((mList==null || mList.size()==0)?"1":(mList.size()+1)+"");
                onLoad();
            }
        }, 500);
    }

    class CollectListAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mList!=null?mList.size():0;
        }
//        public void setList(List<CollectListBean> list){
//            mList = new ArrayList<>(list);
//        }
        @Override
        public Object getItem(int i) {
            return mList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int position, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if (view == null){
                view = LayoutInflater.from(getActivity()).inflate(R.layout.item_collect_list,null);
                holder = new ViewHolder();
                holder.img  = (ImageView)view.findViewById(R.id.imgCollect);
                holder.companyTv = (TextView) view.findViewById(R.id.companyNameTv);
                holder.cityTv = (TextView) view.findViewById(R.id.cityNameTv);
                holder.introduceTv = (TextView) view.findViewById(R.id.introduceTv);
                holder.clickCountTv = (TextView) view.findViewById(R.id.clickCountTv);
                holder.visitCountTv = (TextView) view.findViewById(R.id.visitCountTv);
                holder.praiseTv = (TextView) view.findViewById(R.id.praiseCountTv);
                holder.checkBox = (CheckBox) view.findViewById(R.id.checkBox);
                view.setTag(holder);
            }else{
                holder = (ViewHolder) view.getTag();
            }
            CollectListBean bean = mList.get(position);
            holder.companyTv.setText(bean.getProviderName());
            holder.cityTv.setText(bean.getCityName());
            holder.introduceTv.setText(bean.getFilmIntroduction());
            holder.clickCountTv.setText(String.valueOf(bean.getClickNum()));
            holder.visitCountTv.setText(String.valueOf(bean.getConsultNum()));
            holder.praiseTv.setText(String.valueOf(bean.getPraiseNum()));
            holder.checkBox.setTag(position);
            if (isEditing){
                holder.checkBox.setVisibility(View.VISIBLE);
                holder.checkBox.setChecked(mList.get((Integer) holder.checkBox.getTag()).isSelected());
            }else{
                holder.checkBox.setVisibility(View.GONE);
            }
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        int index = (int) buttonView.getTag();
                        mList.get(index).setSelected(isChecked);
                }
            });
            String imgUrl = bean.getLogo();
            if (imgUrl!=null){
                ImageLoader.getInstance().displayImage(imgUrl, holder.img, options, animateFirstListener);
            }else{
                holder.img.setBackgroundResource(R.drawable.take_photos_list_no__thumbnail);
            }

            return view;
        }

        class ViewHolder{
            ImageView img;
            CheckBox checkBox;
            TextView companyTv,cityTv,introduceTv,clickCountTv,visitCountTv,praiseTv;
        }
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
}
