package com.manyi.mall.mine;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.widget.EditText;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.DialogBuilder;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.BaseResponse;
import com.manyi.mall.service.RequestServerFromHttp;
import com.manyi.mall.utils.JsonData;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.apache.http.protocol.RequestExpectContinue;

/**
 * Created by bestar on 2015/1/29.
 */
@EFragment(R.layout.fragment_feedback)
public class FeedbackFragment extends SuperFragment {
    @ViewById(R.id.feedbackContent)
    EditText mFeedBackEt;

    ProgressDialog mProgressDialog;

    void showDialog(){
        mProgressDialog = new ProgressDialog(getActivity(),ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage("正在发送您的反馈，请稍候...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }
    void dismissDialog(){
        if (mProgressDialog!=null){
            mProgressDialog.dismiss();
        }
    }
    @Click(R.id.submitBtn)
    void clickSubmit(){
        if (CheckDoubleClick.isFastDoubleClick()){
            return;
        }
        if (mFeedBackEt.getText().toString().trim().length() == 0){
            DialogBuilder.showSimpleDialog("请输入反馈内容！",getActivity());
        }else{
            showDialog();
            addFeedBack(mFeedBackEt.getText().toString().trim());
        }
    }

    @Background
    void addFeedBack(String msg){
        RequestServerFromHttp request = new RequestServerFromHttp();
        String msgStr = request.addFeedBack(mFeedBackEt.getText().toString());
        BaseResponse response = new JsonData().JsonBase(msgStr);
        if (response.getCode().equals("1")){
            showDialog("添加成功");
        }else{
            failed();
        }
        dismissDialog();
    }
    @UiThread
    void showDialog(String msg){
        DialogBuilder.showSimpleDialog(msg,getActivity(),new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                remove();
            }
        });
    }
    @UiThread
    void failed(){
        DialogBuilder.showCancelableToast(getActivity(),"添加失败");
    }
    @Click(R.id.feedbackBackBtn)
    void back(){
        remove();
    }

}
