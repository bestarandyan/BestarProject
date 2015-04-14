package com.manyi.mall.wap;

import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.huoqiu.framework.app.SuperFragment;
import com.manyi.mall.BestarApplication;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.BaseResponse;
import com.manyi.mall.service.RequestServerFromHttp;
import com.manyi.mall.utils.JsonData;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

/**
 * Created by bestar on 2015/3/11.
 */
@EFragment(R.layout.fragment_pay_wap)
public class PayFragment extends SuperFragment{
    @ViewById(R.id.detailWebView)
    WebView mWebview;

    String mUrl = "";
    @FragmentArg
    String AgentPrice;
    @FragmentArg
    String CityID;
    @FragmentArg
    String AgentPeroid;
    @FragmentArg
    String ProviderID;

    RequestServerFromHttp request = new RequestServerFromHttp();

    @Click(R.id.wapBack)
    void back(){
        if (mWebview.canGoBack()){
            mWebview.goBack();
        }else{
            remove();
        }
    }
    @Background
    void getUrl(){
            String msg = request.getUrl();
            if (msg!=null && msg.contains("Pay")){
                mUrl = new JsonData().JsonUrl(msg,"Pay");
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
        //http://school.iiyey.com/Pay/Pay.aspx?ProviderID=11&&CustomerID=21&&AgentPrice=0.01&&CityID=1&&AgentPeroid=12
        String url = mUrl+"?ProviderID="+ProviderID+"&&CustomerID="+ BestarApplication.getInstance().getUserId()+"&&AgentPrice="+AgentPrice+"&&CityID="+CityID+"&&AgentPeroid="+AgentPeroid;
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
