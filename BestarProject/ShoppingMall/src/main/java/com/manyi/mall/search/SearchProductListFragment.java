package com.manyi.mall.search;

import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.widget.pinnedlistview.PinnedHeaderListView;
import com.huoqiu.widget.pinnedlistview.SectionedBaseAdapter;
import com.manyi.mall.R;
import com.manyi.mall.utils.JsonData;
import com.manyi.mall.cachebean.mine.FootprintListResponse;
import com.manyi.mall.cachebean.search.OrderInfoBean;
import com.manyi.mall.cachebean.search.TypeProductBean;
import com.manyi.mall.interfaces.SelectItemClickListener;
import com.manyi.mall.interfaces.SelectViewCloseListener;
import com.manyi.mall.service.RequestServerFromHttp;
import com.manyi.mall.widget.filtrate.FiltrateView;
import com.manyi.mall.widget.refreshview.NLPullRefreshView;

import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;
import java.util.Map;

/**
 * Created by bestar on 2015/1/29.
 */
@EFragment(R.layout.fragment_search_product)
public class SearchProductListFragment extends SuperFragment  implements NLPullRefreshView.RefreshListener {
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
    @ViewById(R.id.searchEt)
    EditText mSearchEt;
    @ViewById(R.id.selectLayoutLine)
    View mLine;
    List<Map<String,Object>> mLists =null;
    List<TypeProductBean> mTypeLists =null;
    OrderInfoBean mOrderInfo = null;
    ProductSectionListAdapter mAdapter = null;
    ArrayAdapter arrayAdapter =null;
    String[] typeArray = null;
    String[] noopsycheArray = null;
    RequestServerFromHttp request = new RequestServerFromHttp();
    @AfterViews
    void init(){
        mRefreshableView.setRefreshListener(this);
    }

    @AfterTextChange(R.id.searchEt)
    void AfterTextChange(){
        if (mSearchEt.getText().toString().trim().length() > 0){
            getData();
            getDataType();
            getOrderInfo();
        }

    }

    @Click(R.id.search_back)
    void back(){
        remove();
    }

    @Background
    void getData(){
        String msg = request.searchProducts("1", "20", mSearchEt.getText().toString());
        mLists =  new JsonData().jsonFootprint(msg);
        notifyCheckedList(false);
    }

    @Background
    void getOrderInfo(){
        String msg = request.getOrderInfo();
        mOrderInfo =  new JsonData().jsonOrderInfo(msg);
        noopsycheArray = new String[mOrderInfo.OrderByField.size()];
        for (int i=0;i<mOrderInfo.OrderByField.size();i++){
            noopsycheArray[i] = mOrderInfo.OrderByField.get(i).Name;
        }
    }

    @Background
    void getDataType(){
        String msg = request.searchProductTypes(mSearchEt.getText().toString());
        mTypeLists =  new JsonData().jsonTypeProductList(msg);
        typeArray = new String[mTypeLists.size()];
        for (int i=0;i<mTypeLists.size();i++){
            typeArray[i] = mTypeLists.get(i).ClassName;
        }

    }

    @Background
    void getDataByInputAndType(){
//        String msg = request.searchProductByTypeAndInput("");

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
        mAdapter = new ProductSectionListAdapter();
        mCheckedListView.setAdapter(mAdapter);

    }
    @Override
    public void onRefresh(NLPullRefreshView view) {
        notifyCheckedList(true);
    }


    public class ProductSectionListAdapter extends SectionedBaseAdapter {

        FootprintListResponse responses;


        // 审核成功：文字颜色12c1c4 失败：8a000000
        @Override
        public Object getItem(int section, int position) {
            return ((List<Map<String,String>>)mLists.get(section).get("productList")).get(position);
        }

        @Override
        public long getItemId(int section, int position) {
            return position;
        }

        @Override
        public int getSectionCount() {
            return mLists.size();
        }

        @Override
        public int getCountForSection(int section) {
            return ((List<Map<String,String>>)mLists.get(section).get("productList")).size();
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
            final Map<String,String> response = ((List<Map<String,String>>)mLists.get(section).get("productList")).get(position);
            holder.productNameTv.setText(response.get("ProductName"));
            holder.moneyTv.setText(response.get("Price"));
            holder.clickCountTv.setText(response.get("ClickNum"));
            holder.visitCountTv.setText(response.get("ConsultNum"));
            holder.priaseCountTv.setText(response.get("PraiseNum"));
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
            String companyName = mLists.get(section).get("ProviderName").toString();
            String cityName = mLists.get(section).get("ProviderCityName").toString();
            holder.companyNameTv.setText(companyName);
            holder.cityNameTv.setText(cityName);
            return convertView;
        }
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



}
