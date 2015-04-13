package com.manyi.mall.agency;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.GeneratedClassUtils;
import com.huoqiu.framework.util.ManyiUtils;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.CityBean;
import com.manyi.mall.cachebean.GetProvinceResponse;
import com.manyi.mall.cachebean.agency.AgencyListResponse;
import com.manyi.mall.service.RequestServerFromHttp;
import com.manyi.mall.user.RegisterFragment;
import com.manyi.mall.utils.JsonData;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by bestar on 2015/4/12.
 */
@EFragment(R.layout.fragment_add_agency)
public class AddAgencyFragment extends SuperFragment {
    @ViewById(R.id.provinceNameTv)
    TextView mProvinceNameTv;
    @ViewById(R.id.agencyTitle)
    TextView mAgencyTitle;

    @ViewById(R.id.cityListView)
    ListView mListView;

    List<CityBean> mList;
    List<GetProvinceResponse> mProvinceList;
    CityAdapter mAdapter;

    @FragmentArg
    String providerId;

    GetProvinceResponse mCurrentProvince;

    @AfterViews
    void init(){
        providerId = "11";
        getProvince();
    }

    @Click(R.id.provinceLayout)
    void clickProvinceLayout(){
        if (CheckDoubleClick.isFastDoubleClick())
            return;
        GetAgencyProvinceFragment fragment = GeneratedClassUtils.getInstance(GetAgencyProvinceFragment.class);
        Bundle bundle = new Bundle();
        bundle.putString("providerId", providerId);
        fragment.setArguments(bundle);
        fragment.tag = GetAgencyProvinceFragment.class.getName();
        fragment.setSelectListener(new SelectListener() {
            @Override
            public void onSelected(Object o) {
                mCurrentProvince = (GetProvinceResponse) o;
                if (mCurrentProvince!=null){
                    mProvinceNameTv.setText(mCurrentProvince.getProvinceName());
                    getCity(mCurrentProvince.getID());
                }
            }

            @Override
            public void onCanceled() {

            }
        });
        fragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
                R.anim.anim_fragment_close_out);
        fragment.setManager(getFragmentManager());
        fragment.show(SHOW_ADD_HIDE);
    }
    @Background
    void getProvince(){
        RequestServerFromHttp requestServerFromHttp = new RequestServerFromHttp();
        String msg= requestServerFromHttp.getAgentProvinceByProviderID(providerId);
        mProvinceList = new JsonData().jsonProvince(msg);
        if (mProvinceList!=null && mProvinceList.size()>0){
            setProvinceInfo();
        }
    }

    @UiThread
    void setProvinceInfo(){
        if (mProvinceList!=null && mProvinceList.size()>0){
            mProvinceNameTv.setText(mProvinceList.get(0).getProvinceName());
            getCity(mProvinceList.get(0).getID());
        }
    }

    @Background
    void getCity(String provinceId){
        RequestServerFromHttp requestServerFromHttp = new RequestServerFromHttp();
        String msg = requestServerFromHttp.getAgentCityByProviderIDAndProvinceID(providerId,provinceId);
        mList = new JsonData().jsonCityList(msg);
        if (mList!=null && mList.size()>0){
            notifyListView();
        }
    }

    @UiThread
    void notifyListView(){
        mAdapter = new CityAdapter();
        mListView.setAdapter(mAdapter);
    }

    @Click(R.id.addAgencyBackBtn)
    void back(){
        remove();
    }

    class CityAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null){
                holder = new ViewHolder();
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_city_list,null);
                holder.cityNameTv = (TextView) convertView.findViewById(R.id.cityNameTv);
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            holder.cityNameTv.setText(mList.get(position).CityName);
            return convertView;
        }

        class ViewHolder{
            TextView cityNameTv;
            CheckBox checkBox;
        }
    }
}
