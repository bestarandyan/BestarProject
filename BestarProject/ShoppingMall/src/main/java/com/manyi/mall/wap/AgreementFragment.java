package com.manyi.mall.wap;

import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.util.DialogBuilder;
import com.manyi.mall.BestarApplication;
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
 * Created by bestar on 2015/3/11.
 */
@EFragment(R.layout.fragment_pay_agreement)
public class AgreementFragment extends SuperFragment{
    @ViewById(R.id.contentView)
    TextView mContentView;

    @ViewById(R.id.checkBoxView)
    CheckBox mCheckBox;

    @ViewById(R.id.confirmBtn)
    Button mConfimBtn;

    @FragmentArg
    String ProviderId;

    RequestServerFromHttp request = new RequestServerFromHttp();

    @AfterViews
    void init(){
        getAgreement();
    }

    @Click(R.id.confirmBtn)
    void clickConfirmBtn(){
        if (mCheckBox.isChecked()){
            notifySelected (mCheckBox.isChecked());
        }else{
            DialogBuilder.showSimpleDialog("阅读并同意条款后再付款！",getActivity());
        }

    }
    @Background
    void getAgreement(){
        String msg = request.getAgreement(ProviderId);
        String contentStr = new JsonData().getStr(msg,"AgentAgreement");
        setContent(contentStr);
    }

    @UiThread
    void setContent(String content){
        mContentView.setText(content);
    }
}
