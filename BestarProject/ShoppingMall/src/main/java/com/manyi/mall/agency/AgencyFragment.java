package com.manyi.mall.agency;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.huoqiu.framework.app.SuperFragment;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.mine.AgencyBean;
import com.manyi.mall.cachebean.mine.CollectBean;
import com.manyi.mall.widget.refreshview.NLPullRefreshView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
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


    List<AgencyBean> mList = new ArrayList<AgencyBean>();

    @AfterViews
    void init(){
        for (int i = 0 ;i<10;i++){
            AgencyBean bean = new AgencyBean();
            bean.setCompanyName("凯奇集团");
            bean.setAgencyedCityName("杭州  宁波...");
            bean.setConnectName("赵老师");
            bean.setConnectPhone("18917216840");
            bean.setImgUrl("http://nimei");
            mList.add(bean);
        }

        notifyListView();
    }
    private void notifyListView(){
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
                holder.agencyedTv = (TextView) view.findViewById(R.id.agencyedTv);
                view.setTag(holder);
            }else{
                holder = (ViewHolder) view.getTag();
            }
            AgencyBean bean = mList.get(i);
            holder.companyTv.setText(bean.getCompanyName());
            holder.agencyedTv.setText(bean.getAgencyedCityName());
            holder.connectTv.setText(bean.getConnectName());
            return view;
        }

        class ViewHolder{
            ImageView img;
            TextView companyTv,connectTv,agencyedTv;
        }
    }


}
