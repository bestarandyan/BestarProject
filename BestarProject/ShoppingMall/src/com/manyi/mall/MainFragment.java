package com.manyi.mall;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.util.GeneratedClassUtils;
import com.manyi.mall.agency.AgencyFragment;
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

	@AfterViews
	public void init() {
        mFragmentManager = getActivity().getSupportFragmentManager();
        mTabHost.setup(getActivity(), mFragmentManager, R.id.realtabcontent);

        mTabHost.addTab(createSpec(Constants.TAB_1, "首页"), GeneratedClassUtils.get(HomeFragment.class), null);
        mTabHost.addTab(createSpec(Constants.TAB_2, "我的收藏"), GeneratedClassUtils.get(CollectFragment.class), null);
        if (false){
            mTabHost.addTab(createSpec(Constants.TAB_3, "我的足迹"), GeneratedClassUtils.get(FootPrintListFragment.class), null);
        }else{
            mTabHost.addTab(createSpec(Constants.TAB_3, "我的代理"), GeneratedClassUtils.get(AgencyFragment.class), null);
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
                if (tabId.equals(Constants.TAB_1)) {
//                    searchTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tabbar_ic_search_pre, 0, 0, 0);
                    tab1Tv.setTextColor(Color.parseColor(Constants.SLECTED_TEXT_COLOR));
//                    postTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tabbar_ic_post_org, 0, 0, 0);
                    tab2Tv.setTextColor(Color.parseColor(Constants.UNSLECTED_TEXT_COLOR));
//                    mineTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tabbar_ic_agent_org, 0, 0, 0);
                    tab3Tv.setTextColor(Color.parseColor(Constants.UNSLECTED_TEXT_COLOR));
                    tab4Tv.setTextColor(Color.parseColor(Constants.UNSLECTED_TEXT_COLOR));
                } else if (tabId.equals(Constants.TAB_2)) {
//                    searchTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tabbar_ic_search_org, 0, 0, 0);
                    tab1Tv.setTextColor(Color.parseColor(Constants.UNSLECTED_TEXT_COLOR));
//                    postTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tabbar_ic_post_pre, 0, 0, 0);
                    tab2Tv.setTextColor(Color.parseColor(Constants.SLECTED_TEXT_COLOR));
//                    mineTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tabbar_ic_agent_org, 0, 0, 0);
                    tab3Tv.setTextColor(Color.parseColor(Constants.UNSLECTED_TEXT_COLOR));
                    tab4Tv.setTextColor(Color.parseColor(Constants.UNSLECTED_TEXT_COLOR));
                } else if (tabId.equals(Constants.TAB_3)) {
//                    searchTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tabbar_ic_search_org, 0, 0, 0);
                    tab1Tv.setTextColor(Color.parseColor(Constants.UNSLECTED_TEXT_COLOR));
//                    postTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tabbar_ic_post_org, 0, 0, 0);
                    tab2Tv.setTextColor(Color.parseColor(Constants.UNSLECTED_TEXT_COLOR));
//                    mineTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tabbar_ic_agent_pre, 0, 0, 0);
                    tab3Tv.setTextColor(Color.parseColor(Constants.SLECTED_TEXT_COLOR));
                    tab4Tv.setTextColor(Color.parseColor(Constants.UNSLECTED_TEXT_COLOR));
                }else if (tabId.equals(Constants.TAB_4)) {
//                    searchTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tabbar_ic_search_org, 0, 0, 0);
                    tab1Tv.setTextColor(Color.parseColor(Constants.UNSLECTED_TEXT_COLOR));
//                    postTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tabbar_ic_post_org, 0, 0, 0);
                    tab2Tv.setTextColor(Color.parseColor(Constants.UNSLECTED_TEXT_COLOR));
//                    mineTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tabbar_ic_agent_pre, 0, 0, 0);
                    tab4Tv.setTextColor(Color.parseColor(Constants.SLECTED_TEXT_COLOR));
                    tab3Tv.setTextColor(Color.parseColor(Constants.UNSLECTED_TEXT_COLOR));
                }
            }
        });

        mTabHost.getTabWidget().setDividerDrawable(null);

        mTabHost.setCurrentTab(0);
    }

	private TabSpec createSpec(String tag, String label) {
        View spec = View.inflate(getActivity(), R.layout.view_tab_indicator, null);
        TextView title = (TextView) spec.findViewById(R.id.main_tab_item);
//        if (tag.equals(Constants.TAB_SEATCH_HOUSE)) {
//			title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tabbar_ic_search_org, 0, 0, 0);
//		} else if (tag.equals(Constants.TAB_POST)) {
//			title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tabbar_ic_post_org, 0, 0, 0);
//		} else if (tag.equals(Constants.TAB_MINE)) {
//			title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tabbar_ic_agent_org, 0, 0, 0);
//		}
		title.setText(label);

		return mTabHost.newTabSpec(tag).setIndicator(spec);
	}
}
