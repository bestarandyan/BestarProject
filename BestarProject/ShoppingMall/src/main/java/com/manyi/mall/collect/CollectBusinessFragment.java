package com.manyi.mall.collect;

import android.widget.ListView;

import com.huoqiu.framework.app.SuperFragment;
import com.manyi.mall.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by bestar on 2015/1/25.
 */
@EFragment(R.layout.fragment_collect_business)
public class CollectBusinessFragment extends SuperFragment{
    @ViewById(R.id.collectListView)
    ListView mListView;

    @Click(R.id.collect_back)
    void back(){
        remove();
    }
}
