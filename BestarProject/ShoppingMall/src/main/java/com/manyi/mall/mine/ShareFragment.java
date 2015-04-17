package com.manyi.mall.mine;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.util.DeviceUtil;
import com.manyi.mall.R;
import com.manyi.mall.common.util.QRCodeEncoder;
import com.manyi.mall.common.util.QRContents;
import com.manyi.mall.service.RequestServerFromHttp;
import com.manyi.mall.utils.HardwareInfo;
import com.manyi.mall.utils.JsonData;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by Otto on 2015/1/31.
 */
@EFragment(R.layout.fragment_share)
public class ShareFragment extends SuperFragment {
    @ViewById(R.id.bottomShareLayout)
    LinearLayout mBottomShareLayout;

    @ViewById(R.id.transLayout)
    LinearLayout mTransLayout;
    @FragmentArg
    String shareUrl;

    Bitmap mShareBitmap;

    private static final String APP_ID = "wx07c86deb26c737d5";
    private IWXAPI mApi;

    private void regToWx(){
        mApi = WXAPIFactory.createWXAPI(getActivity(), APP_ID, true);
        mApi.registerApp(APP_ID);
    }

    @Click(R.id.wechatShareBtn)
    void shareToWeiXin(){
        WXTextObject textObject = new WXTextObject();
        textObject.text = "幼宝精品";

        WXWebpageObject webpageObject = new WXWebpageObject();
        webpageObject.webpageUrl = shareUrl;

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = webpageObject;
        msg.description = "幼宝精品,一款幼儿园商城一战式服务软件!!!";
        msg.title="亚太商城";
        msg.setThumbImage(BitmapFactory.decodeResource(getResources(),R.drawable.launcher_icon));

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = req.WXSceneSession;

        mApi.sendReq(req);
    }

    @Click(R.id.pyqShareBtn)
    void shareToWeiXinCircle(){
        WXTextObject textObject = new WXTextObject();
        textObject.text = "幼宝精品";

        WXWebpageObject webpageObject = new WXWebpageObject();
        webpageObject.webpageUrl = shareUrl;

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = webpageObject;
        msg.description = "幼宝精品,一款幼儿园商城一战式服务软件!!!";
        msg.title="亚太商城";
        msg.setThumbImage(BitmapFactory.decodeResource(getResources(),R.drawable.launcher_icon));

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = req.WXSceneTimeline;

        mApi.sendReq(req);
    }

    @Click(R.id.msgShareBtn)
    void shareToMsg(){
        shareInfo2SMS("亚太商城（幼宝精品）下载app,享受幼儿园一站式服务\n"+shareUrl);
    }

    @Click(R.id.codeShareBtn)
    void shareToCode(){
        onCodeImage();
    }
    /**
     * 查看二维码
     */
    public void onCodeImage() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_detalis_code_image, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.code_image);
        getShareBitmap();
        imageView.setImageBitmap(mShareBitmap);
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).setView(view).create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.setCancelable(true);
        alertDialog.show();
    }

    /**
     * 生成二维码分享图片
     */
    public void getShareBitmap() {
        Bundle shareBundle = new Bundle();
        shareBundle.putString(QRContents.URL_KEY, shareUrl);
        QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(shareUrl, null, QRContents.Type.TEXT,
                BarcodeFormat.QR_CODE.toString(),
                (Integer.valueOf((int) (HardwareInfo.getScreenWidth(getActivity()) * 0.7))));
        try {
            mShareBitmap = qrCodeEncoder.encodeAsBitmap();
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
    /**
     * 短信分享功能是否可用
     *
     * @return boolean
     */
    public boolean isSMSIntentAvailable() {
        PackageManager packageManager = getActivity().getPackageManager();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (DeviceUtil.getSDKVersionInt() < 19) {
            intent.putExtra("sms_body", "");
            intent.setType("vnd.android-dir/mms-sms");
        } else {
            Uri uri = Uri.parse("smsto:");
            intent = new Intent(Intent.ACTION_SENDTO, uri);
            intent.putExtra("sms_body", "");
        }
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    /**
     * 分享到短信
     *
     * @param content 短信内容文本
     */
    public void shareInfo2SMS(String content) {
        if(!isSMSIntentAvailable()){
            Toast.makeText(getActivity(),"您的手机好像不支持短信功能哦~", Toast.LENGTH_LONG);
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (DeviceUtil.getSDKVersionInt() < 19) {
            intent.setType("vnd.android-dir/mms-sms");
        } else {
            Uri uri = Uri.parse("smsto:");
            intent = new Intent(Intent.ACTION_SENDTO, uri);
        }
        intent.putExtra("sms_body", content);
        getActivity().startActivity(intent);
    }

    @AfterViews
    void initView(){
        regToWx();
        Animation animation = AnimationUtils.loadAnimation(getActivity(),R.anim.slide_in_from_bottom);
        mBottomShareLayout.startAnimation(animation);

        Animation scaleAnimation = AnimationUtils.loadAnimation(getActivity(),R.anim.alpha_in);
        mTransLayout.setAnimation(scaleAnimation);
    }

    @Click(R.id.transLayout)
    void dismissShare(){
        Animation animation = AnimationUtils.loadAnimation(getActivity(),R.anim.slide_out_to_bottom);
        mBottomShareLayout.startAnimation(animation);

        Animation scaleAnimation = AnimationUtils.loadAnimation(getActivity(),R.anim.alpha_out);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                remove();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mTransLayout.setAnimation(scaleAnimation);


    }

}
