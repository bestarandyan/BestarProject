package com.manyi.mall.agency;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.GeneratedClassUtils;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.CityBean;
import com.manyi.mall.cachebean.agency.ConsultListResponse;
import com.manyi.mall.cachebean.agency.FiltrateFragment;
import com.manyi.mall.service.RequestServerFromHttp;
import com.manyi.mall.utils.JsonData;
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
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by bestar on 2015/4/12.
 */
@EFragment(R.layout.fragment_consult_list)
public class ConsultListFragment extends SuperFragment {
    @ViewById(R.id.titleTv)
    TextView mTitleView;

    @ViewById(R.id.agentedListView)
    ListView mListView;

    @FragmentArg
    String providerId;

    List<ConsultListResponse> mList;
    AgencyListAdapter mAdapter;

    @AfterViews
    void init(){
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getActivity()));
        initOption();
        getList();
    }

    @Background
    void getList(){
        RequestServerFromHttp requestServerFromHttp = new RequestServerFromHttp();
        String msg = requestServerFromHttp.getLocalConsultList(providerId, "0", "100");
        mList = new JsonData().jsonAgentedList(msg);
        if (mList!=null && mList.size()>0){
            notifyList();
        }
    }

    @UiThread
    void notifyList(){
        mAdapter = new AgencyListAdapter();
        mListView.setAdapter(mAdapter);
    }

    class AgencyListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int i) {
            return mList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if (view == null) {
                view = LayoutInflater.from(getActivity()).inflate(R.layout.item_agented_list, null);
                holder = new ViewHolder();
                holder.img = (ImageView) view.findViewById(R.id.imgCollect);
                holder.connectNameTv = (TextView) view.findViewById(R.id.connectNameTv);
                holder.connectPhoneTv = (TextView) view.findViewById(R.id.connectTv);
                holder.callBtn = (Button) view.findViewById(R.id.callBtn);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            final ConsultListResponse bean = mList.get(i);
            holder.connectPhoneTv.setText(bean.Phone);
            holder.connectNameTv.setText(bean.RealName);

            holder.callBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCallPhoneStartIntent(bean.Phone);
                }
            });
            ImageLoader.getInstance().displayImage(bean.PortraitPath, holder.img, options, animateFirstListener);
            return view;
        }

        class ViewHolder {
            ImageView img;
            TextView connectNameTv, connectPhoneTv;
            Button callBtn;
        }
    }
    @Background
    void getConsultListByCityId(String cityId){
        RequestServerFromHttp requestServerFromHttp = new RequestServerFromHttp();
        String msg = requestServerFromHttp.getConsultByCityId(providerId, cityId, "0", "100");
        mList = new JsonData().jsonAgentedList(msg);
        if (mList!=null && mList.size()>0){
            notifyList();
        }
    }
    @Click(R.id.moreAreaTv)
    void clickMoreArea(){
        if (CheckDoubleClick.isFastDoubleClick())
            return;
        FiltrateFragment fragment = GeneratedClassUtils.getInstance(FiltrateFragment.class);
        Bundle bundle = new Bundle();
        bundle.putString("ProviderID", providerId);
        fragment.setArguments(bundle);
        fragment.setSelectListener(new SelectListener() {
            @Override
            public void onSelected(Object o) {
                CityBean bean = (CityBean) o;
                if (bean!=null){
                    if (bean.ID == -1){
                        getList();
                    }else{
                        getConsultListByCityId(String.valueOf(bean.ID));
                        mTitleView.setText("地区经理-"+bean.CityName);
                    }
                }

            }

            @Override
            public void onCanceled() {

            }
        });
        fragment.tag = FiltrateFragment.class.getName();
        fragment.setManager(getFragmentManager());
        fragment.show(SHOW_ADD);
    }
        /**
         * 拨打电话
         *
         * @param uri
         */
        public void onCallPhoneStartIntent(String uri) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + uri));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    @Click(R.id.backbtn)
    void back(){
        remove();
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
