package com.manyi.mall.wap;

import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.huoqiu.framework.app.SuperFragment;
import com.manyi.mall.R;
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
 * Created by bestar on 2015/3/6.
 */
@EFragment(R.layout.fragment_app_download_wap)
public class AppDownLoadFragment extends SuperFragment{
    @ViewById(R.id.businessWebView)
    WebView mWebview;
    @FragmentArg
    String shareUrl;



    @Click(R.id.wapBack)
    void back(){
        if (mWebview.canGoBack()){
            mWebview.goBack();
        }else{
            remove();
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
        mWebview.loadUrl(shareUrl);
    }

    @AfterViews
    void init(){
        loadUrl();
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
