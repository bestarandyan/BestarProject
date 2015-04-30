package com.manyi.mall.agency;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.DialogBuilder;
import com.huoqiu.framework.util.GeneratedClassUtils;
import com.huoqiu.framework.util.ManyiUtils;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.CityBean;
import com.manyi.mall.cachebean.GetProvinceResponse;
import com.manyi.mall.cachebean.agency.AgencyListResponse;
import com.manyi.mall.cachebean.agency.AgentCityResponse;
import com.manyi.mall.cachebean.user.UserInfoResponse;
import com.manyi.mall.service.RequestServerFromHttp;
import com.manyi.mall.user.RegisterFragment;
import com.manyi.mall.utils.JsonData;
import com.manyi.mall.wap.AgreementFragment;
import com.manyi.mall.wap.PayFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ItemClick;
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

    List<AgentCityResponse> mList;
    List<GetProvinceResponse> mProvinceList;
    CityAdapter mAdapter;
    UserInfoResponse mUserInfoResponse =null;
    String mUrl = "";
    @FragmentArg
    String providerId;
    ProgressDialog mDialog;

    GetProvinceResponse mCurrentProvince;

    @AfterViews
    void init(){
        showProgressDialog("加载数据中，请稍候...");
        getUserInfo();
    }

    @Background
    void getUserInfo(){
        RequestServerFromHttp request = new RequestServerFromHttp();
        String msg =  request.getUserInfo();
        mUserInfoResponse = new JsonData().jsonUserInfo(msg);
        if (mUserInfoResponse!=null){
            getCity(mUserInfoResponse.ProvinceID);
            setProvinceView();
        }
    }

    @UiThread
    void setProvinceView(){
        mProvinceNameTv.setText(mUserInfoResponse.ProvinceName);
    }

    @ItemClick(R.id.cityListView)
    void OnItemClick(final int position){
        if (mList.get(position).AgentNum > mList.get(position).PresentNum) {//可以代理
            DialogBuilder.showSimpleDialog("￥" + mList.get(position).AgentPrice + "\n恭喜您获得该城市的代理资格！", "下一步", "取消", getActivity(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    gotoAgreement(mList.get(position));
                }
            });
        } else {
            DialogBuilder.showSimpleDialog("抱歉,这个城市的代理名额已满", "重新选择", getActivity(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
        }
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
                    showProgressDialog("加载数据中，请稍候...");
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
    void getCity(String provinceId){
        RequestServerFromHttp requestServerFromHttp = new RequestServerFromHttp();
        String msg = requestServerFromHttp.getAgentCityByProviderIDAndProvinceID(providerId, provinceId);
        mList = new JsonData().jsonAgentCityList(msg);
        if (mList!=null && mList.size()>0){
            notifyListView();
        }
        dismissProgressDialog();
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null){
                holder = new ViewHolder();
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_city_list,null);
                holder.cityNameTv = (TextView) convertView.findViewById(R.id.cityNameTv);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            holder.cityNameTv.setText(mList.get(position).CityName);
            return convertView;
        }

        class ViewHolder{
            TextView cityNameTv;
        }
    }

    private void gotoAgreement(final AgentCityResponse response){
        if (CheckDoubleClick.isFastDoubleClick())
            return;
        AgreementFragment fragment = GeneratedClassUtils.getInstance(AgreementFragment.class);
        Bundle bundle = new Bundle();
        bundle.putString("ProviderId", providerId);
        fragment.setArguments(bundle);
        fragment.tag = AgreementFragment.class.getName();
        fragment.setSelectListener(new SelectListener() {
            @Override
            public void onSelected(Object o) {
              gotoPay(response);
            }

            @Override
            public void onCanceled() {

            }
        });
        fragment.setCustomAnimations(R.anim.alpha_in, R.anim.anim_fragment_out, R.anim.alpha_out,
                R.anim.alpha_out);
        fragment.setManager(getFragmentManager());
        fragment.show(SHOW_ADD);
    }

    private void gotoPay(AgentCityResponse response){
        if (CheckDoubleClick.isFastDoubleClick())
            return;
        PayFragment fragment = GeneratedClassUtils.getInstance(PayFragment.class);
        Bundle bundle = new Bundle();
        bundle.putString("AgentPrice", response.AgentPrice+"");
        bundle.putString("CityID", response.CityID+"");
        bundle.putString("AgentPeroid", response.AgentPeriod+"");
        bundle.putString("ProviderID", providerId);
        fragment.setArguments(bundle);
        fragment.tag = PayFragment.class.getName();
        fragment.setSelectListener(new SelectListener() {
            @Override
            public void onSelected(Object o) {
                mCurrentProvince = (GetProvinceResponse) o;
                if (mCurrentProvince!=null){
                    mProvinceNameTv.setText(mCurrentProvince.getProvinceName());
                    showProgressDialog("加载数据中，请稍候...");
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

}
