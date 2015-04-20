package com.manyi.mall.wap;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.DialogBuilder;
import com.huoqiu.framework.util.GeneratedClassUtils;
import com.manyi.mall.BestarApplication;
import com.manyi.mall.R;
import com.manyi.mall.agency.AddAgencyFragment;
import com.manyi.mall.agency.ConsultListFragment;
import com.manyi.mall.cachebean.BaseResponse;
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

    @ViewById(R.id.confirmBtn)
    Button mConfirmBtn;

    String mUrl = "";
    @FragmentArg
    String ProviderID;
    @FragmentArg
    String CustomerID;
    @FragmentArg
    String ProductID;
    String userType ="2";
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
            if (msg!=null && msg.contains("ProductInfo")){
                mUrl = new JsonData().JsonUrl(msg,"ProductInfo");
                if (mUrl!=null && mUrl.length()>0){
                    loadUrl();
                }
            }
    }

    @Click(R.id.confirmBtn)
    void clickAgency(){
        if (userType.equals("2")){//园长
            gotoConsultList();
            addConsult();
        }else{
            gotoAgenyList();
        }
    }
    @Background
    void addConsult(){
        String msg = request.addConsult(ProviderID);
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
    private void gotoConsultList(){
        if (CheckDoubleClick.isFastDoubleClick())
            return;
        ConsultListFragment fragment = GeneratedClassUtils.getInstance(ConsultListFragment.class);
        Bundle bundle = new Bundle();
        bundle.putString("providerId", ProviderID);
        fragment.setArguments(bundle);
        fragment.tag = ConsultListFragment.class.getName();
        fragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
                R.anim.anim_fragment_close_out);
        fragment.setManager(getFragmentManager());
        fragment.show(SHOW_ADD_HIDE);
    }
    @Click(R.id.collectBtn)
    void clickEdit(){
        addCollect();
    }

    @Background
    void addCollect(){
        String msg = request.addCollect(ProviderID);
        BaseResponse baseResponse = new JsonData().JsonBase(msg);
        showDialogCollect(baseResponse.getMessage());
    }
    @UiThread
    void showDialogCollect(String msg){
        DialogBuilder.showSimpleDialog(msg,getActivity());
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
        CustomerID = BestarApplication.getInstance().getUserId();
        String url = mUrl+"?ProductID="+ProductID+"&&ProviderID="+ProviderID+"&&CustomerID="+CustomerID;
        mWebview.loadUrl(url);
    }

    @AfterViews
    void init(){
        userType = BestarApplication.getInstance().getType();
        if (userType.equals("2")){//园长
            mConfirmBtn.setText("我要咨询");
        }else{
            mConfirmBtn.setText("我要代理");
        }
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
