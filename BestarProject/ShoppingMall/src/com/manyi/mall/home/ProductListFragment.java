package com.manyi.mall.home;

import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.widget.pinnedlistview.PinnedHeaderListView;
import com.huoqiu.widget.pinnedlistview.SectionedBaseAdapter;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.mine.FootprintListResponse;
import com.manyi.mall.interfaces.SelectItemClickListener;
import com.manyi.mall.interfaces.SelectViewCloseListener;
import com.manyi.mall.widget.filtrate.FiltrateView;
import com.manyi.mall.widget.refreshview.NLPullRefreshView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bestar on 2015/1/29.
 */
@EFragment(R.layout.fragment_product_list)
public class ProductListFragment extends SuperFragment  implements NLPullRefreshView.RefreshListener {
    @ViewById(R.id.filtrateView)
    FiltrateView mFiltrateView;

    @ViewById(R.id.selectLayout1)
    TextView mSelectLayout1;

    @ViewById(R.id.selectLayout2)
    TextView mSelectLayout2;

    @ViewById(R.id.refreshable_view)
    NLPullRefreshView mRefreshableView;

    @ViewById(R.id.searchListView)
    PinnedHeaderListView mCheckedListView;

    @ViewById(R.id.selectLayoutLine)
    View mLine;

    ProductSectionListAdapter mAdapter = null;
    FootprintListResponse mResponse;
    ArrayAdapter arrayAdapter =null;
    String[] typeArray = new String[]{"床具","空调设备","课桌椅","户外大型游乐","厨房设备"};
    String[] noopsycheArray = new String[]{"创办时间从早到晚","创办时间从晚到早","价格从高到低","价格从低到高","级别从高到低","级别从低到高"};
    @AfterViews
    void init(){
        mRefreshableView.setRefreshListener(this);
        mResponse = new FootprintListResponse();
        List<FootprintListResponse.CheckedListResponse> list = new ArrayList<>();
        for (int i=0;i<10;i++){
            FootprintListResponse.CheckedListResponse listResponse = new FootprintListResponse.CheckedListResponse();
            listResponse.setCityName("上海");
            listResponse.setCompanyName("凯奇集团");
            List<FootprintListResponse.CheckedResponse> responses = new ArrayList<>();
            for (int j=0;j<3;j++){
                FootprintListResponse.CheckedResponse response = new FootprintListResponse.CheckedResponse();
                response.setClickCount(1000L);
                response.setHasVoucher(1);
                response.setImgUrl("adfadsfaf");
                response.setPriaseCount(344L);
                response.setPrice(453L);
                response.setProductName("教学用书");
                response.setVisitCount(8976L);
                responses.add(response);
            }
            listResponse.setExamineRecodList(responses);
            list.add(listResponse);
        }
        mResponse.setResult(list);
        notifyCheckedList(false);
    }

    @Click(R.id.selectLayout1)
    void selectLayout1(){
        initSelect1Data();
        if (mFiltrateView.isAdded()){
            mFiltrateView.closeSelectView();
            mSelectLayout1.setTextColor(Color.parseColor("#80000000"));
        }else{
            mSelectLayout1.setTextColor(getResources().getColor(R.color.app_theme_color));
            mFiltrateView.addSelectLayout(getActivity(),arrayAdapter,mLine,new SelectItemClickListener() {
                @Override
                public void ItemClick(int position) {
                    mFiltrateView.closeSelectView();
                }
            });
            mFiltrateView.setOnSelectCloseListener(new SelectViewCloseListener() {
                @Override
                public void OnClose() {
                    mSelectLayout1.setTextColor(Color.parseColor("#80000000"));
                }
            });
        }

    }
@Click(R.id.selectLayout2)
    void selectLayout2(){
        initSelect2Data();
        if (mFiltrateView.isAdded()){
            mFiltrateView.closeSelectView();
            mSelectLayout2.setTextColor(Color.parseColor("#80000000"));
        }else{
            mSelectLayout2.setTextColor(getResources().getColor(R.color.app_theme_color));
            mFiltrateView.addSelectLayout(getActivity(),arrayAdapter,mLine,new SelectItemClickListener() {
                @Override
                public void ItemClick(int position) {
                    mFiltrateView.closeSelectView();
                }
            });
            mFiltrateView.setOnSelectCloseListener(new SelectViewCloseListener() {
                @Override
                public void OnClose() {
                    mSelectLayout2.setTextColor(Color.parseColor("#80000000"));
                }
            });
        }

    }

    private void initSelect1Data(){
        arrayAdapter = new ArrayAdapter(getActivity(),R.layout.item_select_list,R.id.selectContent,typeArray);
    }

    private void initSelect2Data(){
        arrayAdapter = new ArrayAdapter(getActivity(),R.layout.item_select_list,R.id.selectContent,noopsycheArray);
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
        mAdapter = new ProductSectionListAdapter(mResponse);
        mCheckedListView.setAdapter(mAdapter);

    }
    @Override
    public void onRefresh(NLPullRefreshView view) {
        notifyCheckedList(true);
    }


    public class ProductSectionListAdapter extends SectionedBaseAdapter {

        FootprintListResponse responses;

        // 选择颜色 ： 12c1c4
        public ProductSectionListAdapter(FootprintListResponse responses) {
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
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_footprint_list_product, null);
                holder.productNameTv = (TextView) convertView.findViewById(R.id.companyNameTv);
                holder.moneyTv = (TextView) convertView.findViewById(R.id.moneyTv);
                holder.clickCountTv = (TextView) convertView.findViewById(R.id.clickCountTv);
                holder.visitCountTv = (TextView) convertView.findViewById(R.id.visitCountTv);
                holder.priaseCountTv = (TextView) convertView.findViewById(R.id.praiseCountTv);
                holder.imageView = (ImageView) convertView.findViewById(R.id.imgCollect);
                holder.voucherImg = (ImageView) convertView.findViewById(R.id.voucherImg);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            // Map<String,String> map = ((ArrayList<Map<String,String>>)mList.get(section).get("itemList")).get(position);
            final FootprintListResponse.CheckedResponse response = responses.getResult().get(section).getExamineRecodList().get(position);
            holder.productNameTv.setText(response.getProductName());
            holder.moneyTv.setText(String.valueOf(response.getPrice()));
            holder.clickCountTv.setText(String.valueOf(response.getClickCount()));
            holder.visitCountTv.setText(String.valueOf(response.getVisitCount()));
            holder.priaseCountTv.setText(String.valueOf(response.getPriaseCount()));
            convertView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                }
            });
            return convertView;
        }

        public class ViewHolder {
            TextView productNameTv, moneyTv, clickCountTv,visitCountTv,priaseCountTv;
            ImageView imageView;
            ImageView voucherImg;
        }

        public class SectionHolder {
            TextView companyNameTv,cityNameTv;
        }

        @Override
        public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
            SectionHolder holder = null;
            if (convertView == null) {
                holder = new SectionHolder();
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_footprint_list_sections, null);
                holder.companyNameTv = (TextView) convertView.findViewById(R.id.companyNameTv);
                holder.cityNameTv = (TextView) convertView.findViewById(R.id.cityNameTv);
                convertView.setTag(holder);
            } else {
                holder = (SectionHolder) convertView.getTag();
            }
            String companyName = responses.getResult().get(section).getCompanyName();
            String cityName = responses.getResult().get(section).getCityName();
            holder.companyNameTv.setText(companyName);
            holder.cityNameTv.setText(cityName);
            return convertView;
        }
    }

}