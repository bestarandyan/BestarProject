package com.manyi.mall.collect;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.huoqiu.framework.app.SuperFragment;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.mine.CollectBean;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bestar on 2015/1/26.
 */
@EFragment(R.layout.fragment_my_collect)
public class CollectFragment extends SuperFragment {
    @ViewById(R.id.myCollectListView)
    ListView mListView;

    @ViewById(R.id.editBtn)
    TextView mEditBtn;

    @ViewById(R.id.bottomLayout)
    LinearLayout mBottomLayout;

    List<CollectBean> mList = new ArrayList<CollectBean>();

    boolean isEditing = false;
    @Override
    public void onAttach(Activity activity) {
        setBackOp(null);
        super.onAttach(activity);
    }
    @AfterViews
    void init(){
        for (int i = 0 ;i<10;i++){
            CollectBean bean = new CollectBean();
            bean.setCityName("上海");
            bean.setCompany("凯奇集团");
            bean.setIntroduce("中国最具权威幼儿园设计");
            bean.setClickCount(1234L);
            bean.setVisitCount(4567L);
            bean.setPraiseCount(9876L);
            mList.add(bean);
        }

        notifyListView();
    }
    private void notifyListView(){
        CollectListAdapter adapter = new CollectListAdapter();
        mListView.setAdapter(adapter);
    }
    @ItemClick(R.id.myCollectListView)
    void OnItemClick(int position){

    }

    @Click(R.id.editBtn)
    void edit(){
        if (mBottomLayout.getVisibility() == View.GONE){
            mBottomLayout.setVisibility(View.VISIBLE);
            isEditing = true;
            mEditBtn.setText("完成");
        }else{
            mBottomLayout.setVisibility(View.GONE);
            isEditing = false;
            mEditBtn.setText("编辑");
        }
        notifyListView();
    }

    class CollectListAdapter extends BaseAdapter{

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
                view = LayoutInflater.from(getActivity()).inflate(R.layout.item_collect_list,null);
                holder = new ViewHolder();
                holder.img  = (ImageView)view.findViewById(R.id.imgCollect);
                holder.companyTv = (TextView) view.findViewById(R.id.companyNameTv);
                holder.cityTv = (TextView) view.findViewById(R.id.cityNameTv);
                holder.introduceTv = (TextView) view.findViewById(R.id.introduceTv);
                holder.clickCountTv = (TextView) view.findViewById(R.id.clickCountTv);
                holder.visitCountTv = (TextView) view.findViewById(R.id.visitCountTv);
                holder.praiseTv = (TextView) view.findViewById(R.id.praiseCountTv);
                holder.checkBox = (CheckBox) view.findViewById(R.id.checkBox);
                view.setTag(holder);
            }else{
                holder = (ViewHolder) view.getTag();
            }
            CollectBean bean = mList.get(i);
            holder.companyTv.setText(bean.getCompany());
            holder.cityTv.setText(bean.getCityName());
            holder.introduceTv.setText(bean.getIntroduce());
            holder.clickCountTv.setText(String.valueOf(bean.getClickCount()));
            holder.visitCountTv.setText(String.valueOf(bean.getVisitCount()));
            holder.praiseTv.setText(String.valueOf(bean.getPraiseCount()));
            if (isEditing){
                holder.checkBox.setVisibility(View.VISIBLE);
            }else{
                holder.checkBox.setVisibility(View.GONE);
            }
            return view;
        }

        class ViewHolder{
            ImageView img;
            CheckBox checkBox;
            TextView companyTv,cityTv,introduceTv,clickCountTv,visitCountTv,praiseTv;
        }
    }


}
