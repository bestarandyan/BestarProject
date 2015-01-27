package com.manyi.mall.footprint;

import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huoqiu.framework.analysis.ManyiAnalysis;
import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.util.StringUtil;
import com.huoqiu.widget.pinnedlistview.PinnedHeaderListView;
import com.huoqiu.widget.pinnedlistview.SectionedBaseAdapter;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.mine.FootprintListResponse;
import com.manyi.mall.widget.refreshview.NLPullRefreshView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@EFragment(R.layout.fragment_footprintlist)
public class FootPrintListFragment extends SuperFragment  implements NLPullRefreshView.RefreshListener {
    @ViewById(R.id.refreshable_view)
    NLPullRefreshView mRefreshableView;

    @ViewById(R.id.footprintListView)
    PinnedHeaderListView mCheckedListView;

    FootprintSectionListAdapter mAdapter = null;
    FootprintListResponse mResponse;
    @AfterViews
    void init(){
        mRefreshableView.setRefreshListener(this);
        mResponse = new FootprintListResponse();
        List<FootprintListResponse.CheckedListResponse> list = new ArrayList<>();
        for (int i=0;i<10;i++){
            FootprintListResponse.CheckedListResponse listResponse = new FootprintListResponse.CheckedListResponse();
            listResponse.setRecordCount(10);
            listResponse.setResultDateStr("12345678");
            List<FootprintListResponse.CheckedResponse> responses = new ArrayList<>();
            for (int j=0;j<3;j++){
                FootprintListResponse.CheckedResponse response = new FootprintListResponse.CheckedResponse();
                response.setBuilding("1-0");
                response.setBuildingNameStr("asdfafadsf");
                response.setEstateName("曹杨二村");
                response.setHouseState(1);
                response.setHouseStateStr("已租");
                response.setPublishDate("20140404");
                response.setTypeName("adsfaf");
                response.setSubEstateName("adfadf");
                response.setStatusStr("asdfadfadsf");
                response.setStatus(1);
                responses.add(response);
            }
            listResponse.setExamineRecodList(responses);
            list.add(listResponse);
        }
        mResponse.setResult(list);
        notifyCheckedList(false);
    }

    @UiThread
    void notifyCheckedList(boolean isRefresh) {
        if (isRefresh) {
            mRefreshableView.finishRefresh();
        }
        TextView emptyView = new TextView(getActivity());
        emptyView.setText("您还没有查看任何产品哦！先去看看吧！");
        emptyView.setTextColor(Color.parseColor("#333333"));
        emptyView.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        emptyView.setLayoutParams(layoutParams);
        mCheckedListView.setEmptyView(emptyView);
        if (mResponse == null || mResponse.getErrorCode() != 0 || mResponse.getResult() == null) {
            return;
        }
        mAdapter = new FootprintSectionListAdapter(mResponse);
        mCheckedListView.setAdapter(mAdapter);

    }
    @Override
    public void onRefresh(NLPullRefreshView view) {

    }


    public class FootprintSectionListAdapter extends SectionedBaseAdapter {

        FootprintListResponse responses;

        // 选择颜色 ： 12c1c4
        public FootprintSectionListAdapter(FootprintListResponse responses) {
            this.responses = responses;
        }

        // 审核成功：文字颜色12c1c4 失败：8a000000
        @Override
        public Object getItem(int section, int position) {
            return responses.getResult().get(section).getExamineRecodList().get(position);
        }

        @Override
        public long getItemId(int section, int position) {
            return position;
        }

        @Override
        public int getSectionCount() {
            return responses.getResult().size();
        }

        @Override
        public int getCountForSection(int section) {
            return responses.getResult().get(section).getExamineRecodList().size();
        }

        @Override
        public View getItemView(final int section, final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_select_config_content, null);
                holder.contentTv = (TextView) convertView.findViewById(R.id.contentTv);
                holder.dateTv = (TextView) convertView.findViewById(R.id.sendDate);
                holder.stateTv = (TextView) convertView.findViewById(R.id.stateTv);
                holder.hotCityTv = (TextView) convertView.findViewById(R.id.hotCity);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            // Map<String,String> map = ((ArrayList<Map<String,String>>)mList.get(section).get("itemList")).get(position);
            final FootprintListResponse.CheckedResponse response = responses.getResult().get(section).getExamineRecodList().get(position);
            String content;
            String name = null;
            if (!StringUtil.isEmptyOrNull(response.getSubEstateName())) {
                name = response.getEstateName() + response.getSubEstateName();
            } else {
                name = response.getEstateName();
            }

            if (("").equals(response.getBuildingNameStr())) {
                content = name;
            } else {
                content = name + "·" + response.getBuildingNameStr();
            }

            String dateString = response.getPublishDate() + "  " + response.getHouseStateStr();
            int state = response.getStatus();
            String stateStr = response.getStatusStr();
            holder.contentTv.setText(content);
            holder.dateTv.setText(dateString);
            if (state == 1) {// 审核成功
                holder.stateTv.setTextColor(Color.parseColor("#12c1c4"));
            } else if (state == 3) {// 审核失败
                holder.stateTv.setTextColor(Color.parseColor("#8a000000"));
            }
            holder.stateTv.setText(stateStr);

            int isHot = response.getHot();
            if (isHot == 1) {
                holder.hotCityTv.setVisibility(View.VISIBLE);
                holder.hotCityTv.setText("[热点小区]");
            } else {
                holder.hotCityTv.setVisibility(View.GONE);
            }
            convertView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    ManyiAnalysis.onEvent(getActivity(), "CheckedRecordItemClick");
                }
            });
            return convertView;
        }

        public class ViewHolder {
            TextView contentTv, dateTv, stateTv, hotCityTv;
        }

        public class SectionHolder {
            TextView titleTv;
        }

        @Override
        public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
            SectionHolder holder = null;
            if (convertView == null) {
                holder = new SectionHolder();
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_select_config_title, null);
                holder.titleTv = (TextView) convertView.findViewById(R.id.titleTv);
                convertView.setTag(holder);
            } else {
                holder = (SectionHolder) convertView.getTag();
            }
            String countString = " 审核成功" + responses.getResult().get(section).getRecordCount() + "条";
            String title = responses.getResult().get(section).getResultDateStr();

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            Date localDate = new Date(System.currentTimeMillis());// 获取当前时间
            String dateString = formatter.format(localDate);
            Date dateData = null;
            try {
                dateData = formatter.parse(title);
                localDate = formatter.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }

                title = responses.getResult().get(section).getResultDateStr();
            holder.titleTv.setText(title + countString);
            return convertView;
        }
    }
}