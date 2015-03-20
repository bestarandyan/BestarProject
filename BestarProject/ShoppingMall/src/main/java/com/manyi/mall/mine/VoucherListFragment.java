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
import com.manyi.mall.cachebean.mine.VoucherBean;
import com.manyi.mall.widget.refreshview.NLPullRefreshView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bestar on 2015/1/29.
 */
@EFragment(R.layout.fragment_my_voucher)
public class VoucherListFragment extends SuperFragment implements NLPullRefreshView.RefreshListener{
    @ViewById(R.id.myVoucherListView)
    ListView mListView;

    @ViewById(R.id.refreshable_view)
    NLPullRefreshView mRefreshView;

    List<VoucherBean> mList = new ArrayList<VoucherBean>();

    @AfterViews
    void init(){
        mRefreshView.setRefreshListener(this);
        for (int i = 0 ;i<10;i++){
            VoucherBean bean = new VoucherBean();
            bean.setImgUrl("");
            bean.setFaceValue(100f);
            bean.setValid("2015-12-30");
            bean.setVoucherName("生日礼券");
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
                view = LayoutInflater.from(getActivity()).inflate(R.layout.item_voucher_list,null);
                holder = new ViewHolder();
                holder.voucherValueTv = (TextView) view.findViewById(R.id.voucherValueTv);
                holder.voucherNameTv = (TextView) view.findViewById(R.id.voucherNameTv);
                holder.voucherValidTv = (TextView) view.findViewById(R.id.validTv);
                view.setTag(holder);
            }else{
                holder = (ViewHolder) view.getTag();
            }
            VoucherBean bean = mList.get(i);
            holder.voucherValueTv.setText("￥"+bean.getFaceValue());
            holder.voucherNameTv.setText(bean.getVoucherName());
            holder.voucherValidTv.setText(bean.getValid());
            return view;
        }

        class ViewHolder{
            TextView voucherValueTv,voucherNameTv,voucherValidTv;
        }
    }

    @Click(R.id.daijinquanback)
    void back(){
        remove();
    }

}
