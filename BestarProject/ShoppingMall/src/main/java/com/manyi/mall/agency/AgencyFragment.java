package com.manyi.mall.agency;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.huoqiu.framework.app.SuperFragment;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.agency.AgencyListResponse;
import com.manyi.mall.cachebean.mine.AgencyBean;
import com.manyi.mall.service.RequestServerFromHttp;
import com.manyi.mall.utils.JsonData;
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
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by bestar on 2015/1/26.
 */
@EFragment(R.layout.fragment_my_agency)
public class AgencyFragment extends SuperFragment implements NLPullRefreshView.RefreshListener {
    @ViewById(R.id.myAgencyListView)
    ListView mListView;

    @ViewById(R.id.refreshable_view)
    NLPullRefreshView mRefreshView;


    List<AgencyListResponse> mList = new ArrayList<AgencyListResponse>();

    @AfterViews
    void init(){
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getActivity()));
        initOption();
        getAgencyList();
    }

    @Background
    void getAgencyList(){
        RequestServerFromHttp requestServerFromHttp = new RequestServerFromHttp();
        String msg = requestServerFromHttp.getAgentList("0","20");
        mList = new JsonData().jsonAgencyList(msg);
        if (mList!=null && mList.size()>0){
//            for (int i=0;i<13;i++){
//                AgencyListResponse.CityBean bean = new AgencyListResponse.CityBean();
//                bean.CityName = "上海";
//                bean.ID = 11L;
//                mList.get(0).citys.add(bean);
//            }
//
//            for (int s=0;s<13;s++){
//                AgencyListResponse response = new AgencyListResponse();
//                response.citys = new ArrayList<>();
//                for (int r=0;r<13;r++){
//                    AgencyListResponse.CityBean bean1 = new AgencyListResponse.CityBean();
//                    bean1.CityName = "上海";
//                    bean1.ID = 11L;
//                    response.citys.add(bean1);
//                }
//                response.cityname= "adsf";
//                response.ClickNum = 0;
//                response.ConsultNum = 10;
//                response.ContactName="aaaaaaaa";
//                response.ContactTel = "1231423523424";
//                response.PraiseNum = 18;
//                response.ProviderLogo = mList.get(0).ProviderLogo;
//                response.ProviderName="adsfadf";
//                mList.add(response);
//            }
            notifyListView();
        }
    }
    @UiThread
    void notifyListView(){
        AgencyListAdapter adapter = new AgencyListAdapter();
        mListView.setAdapter(adapter);
    }
    @ItemClick(R.id.myCollectListView)
    void OnItemClick(int position){

    }

    @Override
    public void onRefresh(NLPullRefreshView view) {

    }

    class AgencyListAdapter extends BaseAdapter{

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
            if (view == null){
                view = LayoutInflater.from(getActivity()).inflate(R.layout.item_agency_list,null);
                holder = new ViewHolder();
                holder.img  = (ImageView)view.findViewById(R.id.imgCollect);
                holder.companyTv = (TextView) view.findViewById(R.id.companyNameTv);
                holder.connectTv = (TextView) view.findViewById(R.id.connectTv);
                holder.agencyCityLayout = (LinearLayout) view.findViewById(R.id.agencyCityLayout);
                view.setTag(holder);
            }else{
                holder = (ViewHolder) view.getTag();
            }
            AgencyListResponse bean = mList.get(i);
            holder.companyTv.setText(bean.ProviderName);
            holder.connectTv.setText(bean.ContactName);
            ImageLoader.getInstance().displayImage(bean.ProviderLogo, holder.img, options, animateFirstListener);
            List<AgencyListResponse.CityBean> citys = mList.get(i).citys;
            int count = (citys.size()%5==0)?citys.size()/5:citys.size()/5+1;
            if (holder.agencyCityLayout.getChildCount()<count){
                for (int index=0;index<count;index++){
                    holder.agencyCityLayout.addView(getAgencyCityLayout(i,index*5));
                }
            }
            return view;
        }

        class ViewHolder{
            ImageView img;
            TextView companyTv,connectTv;
            LinearLayout agencyCityLayout;
        }
        public View getAgencyCityLayout(final int position,int start){
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_agency,null);
            TextView city1 = (TextView) view.findViewById(R.id.city1);
            TextView city2 = (TextView) view.findViewById(R.id.city2);
            TextView city3 = (TextView) view.findViewById(R.id.city3);
            TextView city4 = (TextView) view.findViewById(R.id.city4);
            TextView city5 = (TextView) view.findViewById(R.id.city5);
            View lineView =  view.findViewById(R.id.bottomLineView);
            ImageView arrow = (ImageView) view.findViewById(R.id.rightArrowImg);
            TextView[] tvArray = new TextView[]{city1,city2,city3,city4,city5};
            List<AgencyListResponse.CityBean> citys = mList.get(position).citys;
            if (citys.size()<=5 || start>=5){
                arrow.setVisibility(View.INVISIBLE);
            }
            if (start>=5){
                view.setVisibility(View.GONE);
            }
            if (start>=citys.size()-5){
                lineView.setVisibility(View.GONE);
            }
            for (int i=start;i<start+5;i++){
                if (i<citys.size()){
                    tvArray[i-start].setText(citys.get(i).CityName);
                }
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        LinearLayout parent = (LinearLayout) v.getParent();
                        for (int a = 1;a<parent.getChildCount();a++){
                            if (parent.getChildAt(a).getVisibility() == View.GONE){
                                parent.getChildAt(a).setVisibility(View.VISIBLE);
                                parent.getChildAt(0).findViewById(R.id.bottomLineView).setVisibility(View.VISIBLE);
                                parent.getChildAt(a).findViewById(R.id.bottomLineView).setVisibility(View.VISIBLE);
                                parent.getChildAt(parent.getChildCount()-1).findViewById(R.id.bottomLineView).setVisibility(View.GONE);
                                ((ImageView)v.findViewById(R.id.rightArrowImg)).setImageResource(R.drawable.up);
                            }else{
                                parent.getChildAt(a).setVisibility(View.GONE);
                                ((ImageView)v.findViewById(R.id.rightArrowImg)).setImageResource(R.drawable.down);
                                parent.getChildAt(0).findViewById(R.id.bottomLineView).setVisibility(View.GONE);
                                parent.getChildAt(a).findViewById(R.id.bottomLineView).setVisibility(View.GONE);
                            }
                        }
                    }
            });
            return  view;
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
