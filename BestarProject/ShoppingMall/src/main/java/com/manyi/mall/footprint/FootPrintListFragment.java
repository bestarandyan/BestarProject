package com.manyi.mall.footprint;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.manyi.mall.service.RequestServerFromHttp;
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
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@EFragment(R.layout.fragment_footprintlist)
public class FootPrintListFragment extends SuperFragment  implements NLPullRefreshView.RefreshListener {
    @ViewById(R.id.refreshable_view)
    NLPullRefreshView mRefreshableView;

    @ViewById(R.id.footprintListView)
    PinnedHeaderListView mCheckedListView;

    FootprintSectionListAdapter mAdapter = null;
    List<Map<String,Object>> mLists =null;
    @AfterViews
    void init(){
        mRefreshableView.setRefreshListener(this);
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getActivity()));
        initOption();
        getData();
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

    @Background
    void getData(){
        RequestServerFromHttp request = new RequestServerFromHttp();
        String msg = request.getFootList("1","20");
//        String msg = "[{\"ID\":10,\"ProductName\":\"cc\",\"ProviderID\":11,\"ClassID\":50,\"Specification\":\"cc\",\"Price\":150.00,\"PicUrl\":\"http://shopcomponents.iiyey.com/file/SystemFiles/201503031516307459667.jpg\",\"SwfUrl\":\"http://shopcomponents.iiyey.com/file/SystemFiles/201503031516396835859.flv\",\"AddTime\":\"2015-03-03T15:01:15.03\",\"beizhu\":null,\"Recommend\":\"1\",\"ClickNum\":0,\"PraiseNum\":0,\"ConsultNum\":0,\"ProviderName\":\"aaa\",\"ProviderCityName\":\"北京\"}\n" +
//                ",{\"ID\":9,\"ProductName\":\"产品二\",\"ProviderID\":11,\"ClassID\":50,\"Specification\":\"bb\",\"Price\":200.00,\"PicUrl\":\"http://shopcomponents.iiyey.com/file/SystemFiles/201503031515145731150.jpg\",\"SwfUrl\":\"http://shopcomponents.iiyey.com/file/SystemFiles/201503031515209325760.flv\",\"AddTime\":\"2015-03-03T15:00:15.717\",\"beizhu\":null,\"Recommend\":\"1\",\"ClickNum\":0,\"PraiseNum\":0,\"ConsultNum\":0,\"ProviderName\":\"aaa\",\"ProviderCityName\":\"北京\"}\n" +
//                ",{\"ID\":8,\"ProductName\":\"产品二\",\"ProviderID\":12,\"ClassID\":51,\"Specification\":\"bb\",\"Price\":200.00,\"PicUrl\":\"http://shopcomponents.iiyey.com/file/SystemFiles/201503031515145731150.jpg\",\"SwfUrl\":\"http://shopcomponents.iiyey.com/file/SystemFiles/201503031515209325760.flv\",\"AddTime\":\"2015-03-03T15:00:15.717\",\"beizhu\":null,\"Recommend\":\"1\",\"ClickNum\":0,\"PraiseNum\":0,\"ConsultNum\":0,\"ProviderName\":\"aaa\",\"ProviderCityName\":\"上海\"}]";
        mLists =  new JsonData().jsonFootprint(msg);
        notifyCheckedList(false);
    }


    @Override
    public void onAttach(Activity activity) {
        setBackOp(null);
        super.onAttach(activity);
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
        mAdapter = new FootprintSectionListAdapter();
        mCheckedListView.setAdapter(mAdapter);

    }
    @Override
    public void onRefresh(NLPullRefreshView view) {
          notifyCheckedList(true);
    }


    public class FootprintSectionListAdapter extends SectionedBaseAdapter {

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
            convertView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    OnItemClick(section,position);
                }
            });
            String imgUrl = response.get("PicUrl");
            if (imgUrl!=null){
                ImageLoader.getInstance().displayImage(imgUrl, holder.imageView, options, animateFirstListener);
            }else{
                holder.imageView.setBackgroundResource(R.drawable.take_photos_list_no__thumbnail);
            }
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