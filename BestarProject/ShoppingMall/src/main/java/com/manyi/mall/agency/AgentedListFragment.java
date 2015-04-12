package com.manyi.mall.agency;

import android.widget.ListView;
import android.widget.TextView;

import com.huoqiu.framework.app.SuperFragment;
import com.manyi.mall.R;
import com.manyi.mall.service.RequestServerFromHttp;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

/**
 * Created by bestar on 2015/4/12.
 */
@EFragment(R.layout.fragment_agented_list)
public class AgentedListFragment extends SuperFragment {
    @ViewById(R.id.titleTv)
    TextView mTitleView;

    @ViewById(R.id.agentedListView)
    ListView mListView;

    @FragmentArg
    String providerId

    @Background
    void getList(){
        RequestServerFromHttp requestServerFromHttp = new RequestServerFromHttp();
        String msg = requestServerFromHttp.getAgentedList(providerId,"0","0","100");
    }
    @Click(R.id.backbtn)
    void back(){
        remove();
    }
}
