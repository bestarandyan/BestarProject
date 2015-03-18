package com.manyi.mall.wap;

import android.app.Activity;
import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.huoqiu.framework.app.SuperFragment;
import com.manyi.mall.R;
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
 * Created by bestar on 2015/3/11.
 */
@EFragment(R.layout.fragment_product_wap)
public class DetailProductFragment extends SuperFragment{
    @ViewById(R.id.detailWebView)
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
    @Background
    void getUrl(){
        RequestServerFromHttp request = new RequestServerFromHttp();
        String appkey = getActivity().getSharedPreferences("appkey", Activity.MODE_PRIVATE).getString("appkey", "");
        if (appkey!=null && appkey.length()>0){
            String msg = request.getUrl(appkey);
            if (msg!=null && msg.contains("ProductInfo")){
                mUrl = new JsonData().JsonUrl(msg,"ProductInfo");
                if (mUrl!=null && mUrl.length()>0){
                    loadUrl();
                }
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
