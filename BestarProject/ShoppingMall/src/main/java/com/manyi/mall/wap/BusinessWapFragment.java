package com.manyi.mall.wap;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.GeneratedClassUtils;
import com.manyi.mall.BestarApplication;
import com.manyi.mall.R;
import com.manyi.mall.agency.AddAgencyFragment;
import com.manyi.mall.agency.AgentedListFragment;
import com.manyi.mall.agency.GetAgencyProvinceFragment;
import com.manyi.mall.cachebean.GetProvinceResponse;
import com.manyi.mall.utils.JsonData;
import com.manyi.mall.service.RequestServerFromHttp;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

/**
 * Created by bestar on 2015/3/6.
 */
@EFragment(R.layout.fragment_business_wap)
public class BusinessWapFragment extends SuperFragment{
    @ViewById(R.id.businessWebView)
    WebView mWebview;

    String mUrl = "";
    @FragmentArg
    String ProviderID;
    @FragmentArg
    String CustomerID;

    @Click(R.id.wapBack)
    void back(){
        if (mWebview.canGoBack()){
            mWebview.goBack();
        }else{
            remove();
        }
    }

    @Click(R.id.agencyBtn)
    void clickAgency(){
        String userType = BestarApplication.getInstance().getType();
        if (userType.equals("2")){//园长
            gotoAgentedList();
        }else{
            gotoAgenyList();
        }
    }

    private void gotoAgenyList(){
        if (CheckDoubleClick.isFastDoubleClick())
            return;
        AddAgencyFragment fragment = GeneratedClassUtils.getInstance(AddAgencyFragment.class);
        Bundle bundle = new Bundle();
        bundle.putString("providerId", ProviderID);
        fragment.setArguments(bundle);
        fragment.tag = AddAgencyFragment.class.getName();
        fragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
                R.anim.anim_fragment_close_out);
        fragment.setManager(getFragmentManager());
        fragment.show(SHOW_ADD_HIDE);
    }
    private void gotoAgentedList(){
        if (CheckDoubleClick.isFastDoubleClick())
            return;
        AgentedListFragment fragment = GeneratedClassUtils.getInstance(AgentedListFragment.class);
        Bundle bundle = new Bundle();
        bundle.putString("providerId", ProviderID);
        fragment.setArguments(bundle);
        fragment.tag = AgentedListFragment.class.getName();
        fragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
                R.anim.anim_fragment_close_out);
        fragment.setManager(getFragmentManager());
        fragment.show(SHOW_ADD_HIDE);
    }
    @Background
    void getUrl(){
        RequestServerFromHttp request = new RequestServerFromHttp();
            String msg = request.getUrl();
            if (msg!=null && msg.contains("WebIndex")){
                mUrl = new JsonData().JsonUrl(msg,"WebIndex");
                if (mUrl!=null && mUrl.length()>0){
                    loadUrl();
                }
            }
    }

    @Override
    public boolean canFragmentGoback(int from) {
        if (mWebview.canGoBack()){
            mWebview.goBack();
            return false;
        }else{
            return super.canFragmentGoback(from);
        }
    }

    @UiThread
    void loadUrl(){
        String url = mUrl+"?ProviderID="+ProviderID+"&&CustomerID="+CustomerID;
        mWebview.loadUrl(url);
    }

    @AfterViews
    void init(){
        getUrl();
        mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }
        });
    }
}
