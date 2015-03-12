package com.manyi.mall.mine;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.huoqiu.framework.app.SuperFragment;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.mine.AgencyPayBean;
import com.manyi.mall.cachebean.mine.VoucherBean;
import com.manyi.mall.widget.refreshview.NLPullRefreshView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bestar on 2015/1/31.
 */
@EFragment(R.layout.fragment_agency_pay)
public class AgencyPayListFragment extends SuperFragment  implements NLPullRefreshView.RefreshListener{
    @ViewById(R.id.myAgencyPayListView)
    ListView mListView;

    @ViewById(R.id.refreshable_view)
    NLPullRefreshView mRefreshView;

    List<AgencyPayBean> mList = new ArrayList<AgencyPayBean>();

    @AfterViews
    void init(){
        mRefreshView.setRefreshListener(this);
        for (int i = 0 ;i<10;i++){
            AgencyPayBean bean = new AgencyPayBean();
            bean.setImgUrl("");
            bean.setCityName("上海");
            bean.setValid("2014-12-30至2015-1-30");
            bean.setCompanyName("凯奇集团");
            bean.setMoney("￥400");
            mList.add(bean);
        }

        notifyListView(false);
    }
    private void notifyListView(boolean isRefresh){
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
            holder.companyNameTv.setText("￥"+bean.getCompanyName());
            holder.cityNameTv.setText(bean.getCityName());
            holder.moneyTv.setText(bean.getMoney());
            holder.validTv.setText(bean.getValid());
            return view;
        }

        class ViewHolder{
            ImageView img;
            TextView companyNameTv,cityNameTv,moneyTv,validTv;
        }
    }

}
