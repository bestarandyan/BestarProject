package com.manyi.mall.search;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.manyi.mall.R;
import com.manyi.mall.cachebean.search.HotSearchBean;

import java.util.List;

/**
 * Created by bestar on 2015/3/19.
 */
public class HotSearchAdapter extends BaseAdapter {
    List<HotSearchBean> mList;
    Context mContext;
    public HotSearchAdapter(Context context,List<HotSearchBean> list){
        this.mList = list;
        this.mContext = context;
    }
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
        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_hot_search,null);
        TextView contentTv  = (TextView) convertView.findViewById(R.id.contentTv);
        HotSearchBean hotSearchBean = mList.get(position);
        contentTv.setText(hotSearchBean.SearchWord);
        return convertView;
    }
}
