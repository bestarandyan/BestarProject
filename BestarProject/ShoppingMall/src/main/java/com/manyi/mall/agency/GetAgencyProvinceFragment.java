package com.manyi.mall.agency;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.huoqiu.framework.app.SuperFragment;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.CityBean;
import com.manyi.mall.cachebean.GetProvinceResponse;
import com.manyi.mall.service.RequestServerFromHttp;
import com.manyi.mall.utils.JsonData;

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
@EFragment(R.layout.fragment_get_agency_province)
public class GetAgencyProvinceFragment extends SuperFragment {
    @ViewById(R.id.agencyTitle)
    TextView mAgencyTitle;

    @ViewById(R.id.provinceListView)
    ListView mListView;

    List<GetProvinceResponse> mList;
    ProvinceAdapter mAdapter;

    @FragmentArg
    String providerId;

    @AfterViews
    void init(){
        getProvince();
    }

    @ItemClick(R.id.provinceListView)
    void ItemClick(int position){
        if (getSelectListener()!=null){
            notifySelected(mList.get(position));
        }
    }

    @Background
    void getProvince(){
        RequestServerFromHttp requestServerFromHttp = new RequestServerFromHttp();
        String msg= requestServerFromHttp.getAgentProvinceByProviderID(providerId);
        mList = new JsonData().jsonProvince(msg);
        if (mList!=null && mList.size()>0){
            notifyListView();
        }
    }

    @UiThread
    void notifyListView(){
        mAdapter = new ProvinceAdapter();
        mListView.setAdapter(mAdapter);
    }

    @Click(R.id.BackBtn)
    void back(){
        remove();
    }

    class ProvinceAdapter extends BaseAdapter {

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
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_province_list,null);
                holder.cityNameTv = (TextView) convertView.findViewById(R.id.cityNameTv);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            holder.cityNameTv.setText(mList.get(position).getProvinceName());
            return convertView;
        }

        class ViewHolder{
            TextView cityNameTv;
        }
    }
}
