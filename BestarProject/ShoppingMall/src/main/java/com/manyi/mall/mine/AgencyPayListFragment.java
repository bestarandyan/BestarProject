package com.manyi.mall.mine;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.huoqiu.framework.app.SuperFragment;
import com.manyi.mall.BaseFragment;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.mine.AgencyPayBean;
import com.manyi.mall.cachebean.mine.VoucherBean;
import com.manyi.mall.service.RequestServerFromHttp;
import com.manyi.mall.utils.JsonData;
import com.manyi.mall.widget.refreshview.NLPullRefreshView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bestar on 2015/1/31.
 */
@EFragment(R.layout.fragment_agency_pay)
public class AgencyPayListFragment extends BaseFragment implements NLPullRefreshView.RefreshListener{
    @ViewById(R.id.myAgencyPayListView)
    ListView mListView;

    @ViewById(R.id.refreshable_view)
    NLPullRefreshView mRefreshView;

    List<AgencyPayBean> mList = new ArrayList<AgencyPayBean>();

    @Click(R.id.backBtn)
    void back(){
        remove();
    }

    @AfterViews
    void init(){
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getActivity()));
        initOption();
        mRefreshView.setRefreshListener(this);
        getList();

    }

    @Background
    void getList(){
        RequestServerFromHttp requestServerFromHttp = new RequestServerFromHttp();
        String msg = requestServerFromHttp.getAgencyPayList("0","100");
        mList = new JsonData().jsonAgencyPayList(msg);
        if (mList!=null && mList.size()>0){
            notifyListView(false);
        }
    }
    @UiThread
    void notifyListView(boolean isRefresh){
        if (isRefresh) {
            mRefreshView.finishRefresh();
        }
        VoucherListAdapter adapter = new VoucherListAdapter();
        mListView.setAdapter(adapter);
    }
    @ItemClick(R.id.myVoucherListView)
    void OnItemClick(int position){

    }

    @Override
    public void onRefresh(NLPullRefreshView view) {
        notifyListView(true);
    }

    class VoucherListAdapter extends BaseAdapter {

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
                view = LayoutInflater.from(getActivity()).inflate(R.layout.item_agency_pay_list,null);
                holder = new ViewHolder();
                holder.img  = (ImageView)view.findViewById(R.id.imgAgencyPay);
                holder.companyNameTv = (TextView) view.findViewById(R.id.companyNameTv);
                holder.cityNameTv = (TextView) view.findViewById(R.id.cityNameTv);
                holder.moneyTv = (TextView) view.findViewById(R.id.moneyTv);
                holder.validTv = (TextView) view.findViewById(R.id.validTv);
                view.setTag(holder);
            }else{
                holder = (ViewHolder) view.getTag();
            }
            AgencyPayBean bean = mList.get(i);
            holder.companyNameTv.setText(bean.ProviderName);
            holder.cityNameTv.setText(bean.CityName);
            holder.moneyTv.setText("￥"+bean.AgentPrice);
            String startTime = bean.StartTime;
            String endTime = bean.EndTime;
            if (startTime.length()>10){
                startTime= startTime.substring(0,10);
            }

            if (endTime.length()>10){
                endTime= endTime.substring(0,10);
            }
            holder.validTv.setText(startTime+"至"+endTime);
            ImageLoader.getInstance().displayImage(bean.ProviderLogo, holder.img, options, animateFirstListener);
            return view;
        }

        class ViewHolder{
            ImageView img;
            TextView companyNameTv,cityNameTv,moneyTv,validTv;
        }
    }

}
