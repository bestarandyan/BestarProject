package com.manyi.mall;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.util.GeneratedClassUtils;
import com.manyi.mall.agency.MyAgencyListFragment;
import com.manyi.mall.common.Constants;
import com.manyi.mall.footprint.FootPrintListFragment;
import com.manyi.mall.collect.CollectFragment;
import com.manyi.mall.mine.MineFragment;
import com.manyi.mall.home.HomeFragment;

@EFragment(R.layout.fragment_main)
public class MainFragment extends SuperFragment<Object>{
	
	private FragmentManager mFragmentManager;
	
	@ViewById(android.R.id.tabhost)
	public FragmentTabHost mTabHost;
    String userType = "2";//园长  1代表商家

	@AfterViews
	public void init() {
        userType = BestarApplication.getInstance().getType();
        mFragmentManager = getActivity().getSupportFragmentManager();
        mTabHost.setup(getActivity(), mFragmentManager, R.id.realtabcontent);

        mTabHost.addTab(createSpec(Constants.TAB_1, "首页"), GeneratedClassUtils.get(HomeFragment.class), null);
        mTabHost.addTab(createSpec(Constants.TAB_2, "我的收藏"), GeneratedClassUtils.get(CollectFragment.class), null);
        if (userType.equals("2")){//园长
            mTabHost.addTab(createSpec(Constants.TAB_3, "我的足迹"), GeneratedClassUtils.get(FootPrintListFragment.class), null);
        }else{//商家
            mTabHost.addTab(createSpec(Constants.TAB_3, "我的代理"), GeneratedClassUtils.get(MyAgencyListFragment.class), null);
        }
        mTabHost.addTab(createSpec(Constants.TAB_4, "我的"), GeneratedClassUtils.get(MineFragment.class), null);

        mTabHost.setOnTabChangedListener(new OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                View mainTabLayout1 = mTabHost.getTabWidget().getChildTabViewAt(0);
                View mainTabLayout2 = mTabHost.getTabWidget().getChildTabViewAt(1);
                View mainTabLayout3 = mTabHost.getTabWidget().getChildTabViewAt(2);
                View mainTabLayout4 = mTabHost.getTabWidget().getChildTabViewAt(3);
                TextView tab1Tv = (TextView) mainTabLayout1.findViewById(R.id.main_tab_item);
                TextView tab2Tv = (TextView) mainTabLayout2.findViewById(R.id.main_tab_item);
                TextView tab3Tv = (TextView) mainTabLayout3.findViewById(R.id.main_tab_item);
                TextView tab4Tv = (TextView) mainTabLayout4.findViewById(R.id.main_tab_item);
                ImageView tab1iconView = (ImageView) mainTabLayout1.findViewById(R.id.iconView);
                ImageView tab2iconView = (ImageView) mainTabLayout2.findViewById(R.id.iconView);
                ImageView tab3iconView = (ImageView) mainTabLayout3.findViewById(R.id.iconView);
                ImageView tab4iconView = (ImageView) mainTabLayout4.findViewById(R.id.iconView);
                if (tabId.equals(Constants.TAB_1)) {
                    tab1Tv.setTextColor(Color.parseColor(Constants.SLECTED_TEXT_COLOR));
                    tab2Tv.setTextColor(Color.parseColor(Constants.UNSLECTED_TEXT_COLOR));
                    tab3Tv.setTextColor(Color.parseColor(Constants.UNSLECTED_TEXT_COLOR));
                    tab4Tv.setTextColor(Color.parseColor(Constants.UNSLECTED_TEXT_COLOR));
                    tab1iconView.setImageResource(R.drawable.home_h);
                    tab2iconView.setImageResource(R.drawable.collection);
                    tab3iconView.setImageResource(R.drawable.footprint);
                    tab4iconView.setImageResource(R.drawable.me);
                } else if (tabId.equals(Constants.TAB_2)) {
                    tab1Tv.setTextColor(Color.parseColor(Constants.UNSLECTED_TEXT_COLOR));
                    tab2Tv.setTextColor(Color.parseColor(Constants.SLECTED_TEXT_COLOR));
                    tab3Tv.setTextColor(Color.parseColor(Constants.UNSLECTED_TEXT_COLOR));
                    tab4Tv.setTextColor(Color.parseColor(Constants.UNSLECTED_TEXT_COLOR));
                    tab1iconView.setImageResource(R.drawable.home);
                    tab2iconView.setImageResource(R.drawable.collection_h);
                    tab3iconView.setImageResource(R.drawable.footprint);
                    tab4iconView.setImageResource(R.drawable.me);
                } else if (tabId.equals(Constants.TAB_3)) {
                    tab1Tv.setTextColor(Color.parseColor(Constants.UNSLECTED_TEXT_COLOR));
                    tab2Tv.setTextColor(Color.parseColor(Constants.UNSLECTED_TEXT_COLOR));
                    tab3Tv.setTextColor(Color.parseColor(Constants.SLECTED_TEXT_COLOR));
                    tab4Tv.setTextColor(Color.parseColor(Constants.UNSLECTED_TEXT_COLOR));
                    tab1iconView.setImageResource(R.drawable.home);
                    tab2iconView.setImageResource(R.drawable.collection);
                    tab3iconView.setImageResource(R.drawable.footprint_h);
                    tab4iconView.setImageResource(R.drawable.me);
                }else if (tabId.equals(Constants.TAB_4)) {
                    tab1Tv.setTextColor(Color.parseColor(Constants.UNSLECTED_TEXT_COLOR));
                    tab2Tv.setTextColor(Color.parseColor(Constants.UNSLECTED_TEXT_COLOR));
                    tab4Tv.setTextColor(Color.parseColor(Constants.SLECTED_TEXT_COLOR));
                    tab3Tv.setTextColor(Color.parseColor(Constants.UNSLECTED_TEXT_COLOR));
                    tab1iconView.setImageResource(R.drawable.home);
                    tab2iconView.setImageResource(R.drawable.collection);
                    tab3iconView.setImageResource(R.drawable.footprint);
                    tab4iconView.setImageResource(R.drawable.me_h);
                }
            }
        });

        mTabHost.getTabWidget().setDividerDrawable(null);

        mTabHost.setCurrentTab(0);
    }

	private TabSpec createSpec(String tag, String label) {
        View spec = View.inflate(getActivity(), R.layout.view_tab_indicator, null);
        TextView title = (TextView) spec.findViewById(R.id.main_tab_item);
        ImageView iconView = (ImageView) spec.findViewById(R.id.iconView);
        if (tag.equals(Constants.TAB_1)) {
            iconView.setImageResource(R.drawable.home_h);
            title.setTextColor(Color.parseColor(Constants.SLECTED_TEXT_COLOR));
		} else if (tag.equals(Constants.TAB_2)) {
            iconView.setImageResource(R.drawable.collection);
            title.setTextColor(Color.parseColor(Constants.UNSLECTED_TEXT_COLOR));
		} else if (tag.equals(Constants.TAB_3)) {
            iconView.setImageResource(R.drawable.footprint);
            title.setTextColor(Color.parseColor(Constants.UNSLECTED_TEXT_COLOR));
		}else if (tag.equals(Constants.TAB_4)) {
            iconView.setImageResource(R.drawable.me);
            title.setTextColor(Color.parseColor(Constants.UNSLECTED_TEXT_COLOR));
        }
		title.setText(label);

		return mTabHost.newTabSpec(tag).setIndicator(spec);
	}
}
