package com.manyi.mall.cachebean.agency;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huoqiu.framework.app.SuperFragment;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.CityBean;
import com.manyi.mall.cachebean.GetProvinceResponse;
import com.manyi.mall.service.RequestServerFromHttp;
import com.manyi.mall.utils.HardwareInfo;
import com.manyi.mall.utils.JsonData;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by bestar on 2015/3/11.
 */
@EFragment(R.layout.fragment_filtrate)
public class FiltrateFragment extends SuperFragment {

    @ViewById(R.id.selectListView1)
    ListView mListView1;
    @ViewById(R.id.selectListView2)
    ListView mListView2;

    @ViewById(R.id.selectLayout)
    LinearLayout mSelectLayout;

    @ViewById(R.id.alphaLayout)
    LinearLayout mAlphaLayout;

    @ViewById(R.id.moreAreaTv)
    TextView mMoreTextView;

    @ViewById(R.id.selectedTv)
    TextView mSelectedTv;

    List<GetProvinceResponse> mProvinceList;
    List<CityBean> mCityList;

    ListView1Adapter mAdapter1;
    ListView2Adapter mAdapter2;
    private String currentProvince = "";

    @FragmentArg
    String ProviderID;

    RequestServerFromHttp mRequest;

    @AfterViews
    void initSelect1Data() {
        mMoreTextView.setText("取消");
        mRequest = new RequestServerFromHttp();
        initAnimation();
        getProvinceData();
    }

    @Background
    void getProvinceData(){
       String  msg = mRequest.getProvinceByProviderID(ProviderID);
       mProvinceList = new JsonData().jsonProvince(msg);
       notifyProvinceListView();
    }
    @Background
    void getCityData(String provinceId){
       String  msg = mRequest.getCityByProviderIDAndProvinceID(ProviderID, provinceId);
       mCityList = new JsonData().jsonCityList(msg);
       notifyCityListView();
    }

    @UiThread
    void notifyProvinceListView(){
        mAdapter1 = new ListView1Adapter();
        mListView1.setAdapter(mAdapter1);
    }

    @UiThread
    void notifyCityListView(){
        mAdapter2 = new ListView2Adapter();
        mListView2.setAdapter(mAdapter2);
        if (mCityList!=null && mCityList.size()>0){
            mListView2.setVisibility(View.VISIBLE);
        }else{
            mListView2.setVisibility(View.GONE);
        }
    }

    @ItemClick(R.id.selectListView1)
    void OnItemClick1(int position){
        for (GetProvinceResponse response:mProvinceList){
            if (response.equals(mProvinceList.get(position))){
                mProvinceList.get(position).isSelected = true;
            }else{
                mProvinceList.get(position).isSelected = false;
            }
        }
        String msg = mProvinceList.get(position).getProvinceName();
        if (msg!=null && msg.length()>0){
            mSelectedTv.setText(msg);
            currentProvince = msg;
        }else{
            mSelectedTv.setText("");
            currentProvince = "";

        }
        mAdapter1.notifyDataSetChanged();
        getCityData(mProvinceList.get(position).getID());
    }

    @ItemClick(R.id.selectListView2)
    void OnItemClick2(int position){
        for (CityBean response:mCityList){
            if (response.equals(mCityList.get(position))){
                mCityList.get(position).isSelected = true;
            }else{
                mCityList.get(position).isSelected = false;
            }
        }
        mAdapter2.notifyDataSetChanged();
        String msg = mCityList.get(position).CityName;
        if (msg!=null && msg.length()>0){
            mSelectedTv.setText(currentProvince+"-"+msg);
        }else{
            mSelectedTv.setText(currentProvince);
        }
       notifySelected(mCityList.get(position));
    }
    @Click(R.id.localTextView)
    void clickLocalBtn(){
        CityBean cityBean = new CityBean();
        cityBean.CityName = "本地";
        cityBean.ID = -1L;
        notifySelected(cityBean);
    }





    /**
     * 区域列表
     */
    class ListView1Adapter extends BaseAdapter{

        @Override
        public int getCount() {
          return mProvinceList.size();
        }

        @Override
        public Object getItem(int position) {
            return mProvinceList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_filtrate_list,null);
            TextView nameTv = (TextView) convertView.findViewById(R.id.selectContent);
            nameTv.setText(mProvinceList.get(position).getProvinceName());
            convertView.setBackgroundColor(Color.TRANSPARENT);
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, HardwareInfo.dpToPx(getActivity(),48));
//            nameTv.setLayoutParams(params);
            if (mProvinceList.get(position).isSelected){
                convertView.setBackgroundColor(getResources().getColor(R.color.filtrate_listview2_bg));
            }else{
                convertView.setBackgroundColor(Color.parseColor("#ffffff"));
            }
            return convertView;
        }
    }

    /**
     * 第三列控件区域
     * 板块列表，售价列表，面积列表，户型列表
     */
    class ListView2Adapter extends BaseAdapter{

        @Override
        public int getCount() {
        return mCityList.size();
        }

        @Override
        public Object getItem(int position) {
            return mCityList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_filtrate_list,null);
            TextView nameTv = (TextView) convertView.findViewById(R.id.selectContent);
            nameTv.setText(mCityList.get(position).CityName);
            if (mCityList.get(position).isSelected){
                nameTv.setTextColor(getResources().getColor(R.color.app_theme_color));
            }else{
                nameTv.setTextColor(Color.parseColor("#333333"));
            }
            convertView.setBackgroundColor(Color.TRANSPARENT);
            return convertView;
        }
    }


    /**
     * 退出时动画
     * @param position
     */
    private void hideListView(final int position){
        TranslateAnimation animation = new TranslateAnimation(0.0f,0.0f,0.0f,-mSelectLayout.getHeight());
        animation.setDuration(300);
        mSelectLayout.startAnimation(animation);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f,0.0f);
        alphaAnimation.setDuration(300);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (position == -1){
                    remove();
                    return;
                }
                if (getSelectListener()!=null){
                    notifySelected(position);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mAlphaLayout.startAnimation(alphaAnimation);
    }

    /**
     * 进入时动画
     */
    private void initAnimation(){
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, (int) (HardwareInfo.getScreenHeight(getActivity())*0.6));
        params.addRule(RelativeLayout.BELOW,R.id.filtrateTopLayout);
        mSelectLayout.setLayoutParams(params);

        mSelectLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSelectLayout.setVisibility(View.VISIBLE);
                TranslateAnimation animation = new TranslateAnimation(0.0f,0.0f,-mSelectLayout.getHeight(),0.0f);
                animation.setDuration(300);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mAlphaLayout.setVisibility(View.VISIBLE);
                        AlphaAnimation alphaAnimation = new AlphaAnimation(0.1f,1.0f);
                        alphaAnimation.setDuration(300);
                        mAlphaLayout.startAnimation(alphaAnimation);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                mSelectLayout.startAnimation(animation);
            }
        },100);
    }

    /**
     * 点击顶部区域 隐藏当前筛选页面
     */
    @Click(R.id.topLayout)
    void topClick(){
        hideListView(-1);
    }


    /**
     * 点击透明区域隐藏页面
     */
    @Click(R.id.alphaLayout)
    void alphaLayoutClick(){
        hideListView(-1);
    }
}
