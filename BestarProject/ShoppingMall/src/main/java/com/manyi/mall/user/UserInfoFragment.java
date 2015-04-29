package com.manyi.mall.user;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.GeneratedClassUtils;
import com.manyi.mall.BestarApplication;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.user.UserInfoResponse;
import com.manyi.mall.service.RequestServerFromHttp;
import com.manyi.mall.user.ForgetPasswordFragment;
import com.manyi.mall.user.UpdatePswFragment;
import com.manyi.mall.utils.JsonData;
import com.manyi.mall.utils.UploadUtil;
import com.manyi.mall.widget.GalleryWidget.ImageLoaderConfig;
import com.manyi.mall.widget.imageView.CircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.w3c.dom.Text;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by bestar on 2015/3/4.
 */
@EFragment(R.layout.fragment_user_info)
public class UserInfoFragment extends SuperFragment implements UploadUtil.OnUploadProcessListener {
    @ViewById(R.id.fragment_userInfo)
    RelativeLayout mParentLayout;
    @ViewById(R.id.headImgView)
    public static CircleImageView mHeadImgView;
    UserInfoResponse mUserInfoResponse =null;
    private static final String TAG = "uploadImage";

    /**
     * 去上传文件
     */
    protected static final int TO_UPLOAD_FILE = 1;
    /**
     * 上传文件响应
     */
    protected static final int UPLOAD_FILE_DONE = 2;  //
    /**
     * 选择文件
     */
    public static final int TO_SELECT_PHOTO = 3;
    /**
     * 上传初始化
     */
    private static final int UPLOAD_INIT_PROCESS = 4;
    /**
     * 上传中
     */
    private static final int UPLOAD_IN_PROCESS = 5;
    public static String picPath = null;
    /***
     * 这里的这个URL是我服务器的javaEE环境URL
     */
    private static String requestURL = "http://shopapp.iiyey.com/customer.aspx?appKey=abcd&&method=uploadHeadimg&&CustomerID=21";
    public String tempPicUrl = "";
    @ViewById(R.id.real_name_et)
    TextView mRealNameTv;

    @ViewById(R.id.pswTv)
    TextView mPswTv;

    @Click(R.id.userInfoBack)
    void back() {
        remove();
    }
    private ProgressDialog progressDialog;
    @Click(R.id.headViewLayout)
    void gotoUpdateHead() {
        Intent intent = new Intent(getActivity(),SelectPicActivity_.class);
        startActivityForResult(intent, TO_SELECT_PHOTO);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!tempPicUrl.equals(picPath) && picPath!=null && picPath.length()>0){
            setHeadImg(picPath);
        }
    }

    public void setHeadImg(String picUrl){
        picPath = picUrl;
        tempPicUrl = picUrl;
        Bitmap bm = BitmapFactory.decodeFile(picPath);
        mHeadImgView.setImageBitmap(bm);
        uploadImg();
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TO_UPLOAD_FILE:
                    toUploadFile();
                    break;
                case UPLOAD_INIT_PROCESS:
                    break;
                case UPLOAD_IN_PROCESS:
                    break;
                case UPLOAD_FILE_DONE:
                    String result = "响应码："+msg.arg1+"\n响应信息："+msg.obj+"\n耗时："+ UploadUtil.getRequestTime()+"秒";
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }

    };
    @Background
    void uploadImg(){
        if(picPath!=null)
        {
            handler.sendEmptyMessage(TO_UPLOAD_FILE);
        }else{
            Toast.makeText(getActivity(), "上传的文件路径出错", Toast.LENGTH_LONG).show();
        }
    }
    /**
     * 上传服务器响应回调
     */
    @Override
    public void onUploadDone(int responseCode, String message) {
        progressDialog.dismiss();
        Message msg = Message.obtain();
        msg.what = UPLOAD_FILE_DONE;
        msg.arg1 = responseCode;
        msg.obj = message;
        handler.sendMessage(msg);
    }

    @Override
    public void onUploadProcess(int uploadSize) {
        Message msg = Message.obtain();
        msg.what = UPLOAD_IN_PROCESS;
        msg.arg1 = uploadSize;
        handler.sendMessage(msg );
    }

    @Override
    public void initUpload(int fileSize) {
        Message msg = Message.obtain();
        msg.what = UPLOAD_INIT_PROCESS;
        msg.arg1 = fileSize;
        handler.sendMessage(msg );
    }

    private void toUploadFile()
    {
        progressDialog.setMessage("正在上传文件...");
        progressDialog.show();
        String fileKey = "pic";
        UploadUtil uploadUtil = UploadUtil.getInstance();;
        uploadUtil.setOnUploadProcessListener(this);  //设置监听器监听上传状态

        Map<String, String> params = new HashMap<String, String>();
        params.put("orderId", "11111");
        uploadUtil.uploadFile( picPath,fileKey, requestURL,params);
    }
    @AfterViews
    void init(){
        requestURL = "http://shopapp.iiyey.com/customer.aspx?appKey="+ BestarApplication.getInstance().getAppkey()+"&&method=uploadHeadimg&&CustomerID="+BestarApplication.getInstance().getUserId()+"";
        progressDialog = new ProgressDialog(getActivity());
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getActivity()));
        initOption();
        getUserInfo();
    }

    @Background
    void getUserInfo(){
        RequestServerFromHttp request = new RequestServerFromHttp();
        String msg =  request.getUserInfo();
        mUserInfoResponse = new JsonData().jsonUserInfo(msg);
        if (mUserInfoResponse!=null){
            setViewValue();
        }
    }
    ImageLoadingListener imageLoadingListener = new ImageLoadingListener() {
        @Override
        public void onLoadingStarted(String imageUri, View view) {
        }

        @Override
        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            ((ImageView)view).setImageResource(R.drawable.head);
        }

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage == null){
                ((ImageView)view).setImageResource(R.drawable.head);
            }

        }

        @Override
        public void onLoadingCancelled(String imageUri, View view) {
        }
    };
    DisplayImageOptions options;
    public void initOption(){

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.take_photos_list_no__thumbnail)
                .showImageForEmptyUri(R.drawable.take_photos_list_no__thumbnail)
                .showImageOnFail(R.drawable.take_photos_list_no__thumbnail)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(20))
                .build();
    }
    @Override
    public void onDestroy() {
        ImageLoader.getInstance().clearMemoryCache();
        ImageLoader.getInstance().clearDiskCache();
        super.onDestroy();
    }
    @UiThread
    void setViewValue(){
        mRealNameTv.setText(mUserInfoResponse.UserName);
        mPswTv.setText(mUserInfoResponse.Password);
        ImageLoader.getInstance().displayImage(mUserInfoResponse.PortraitPath,mHeadImgView, ImageLoaderConfig.options_agent,imageLoadingListener, ImageLoaderConfig.imgProgressListener);
    }
    @Click(R.id.pswLayout)
    void gotoUpdatePsw() {
        if (CheckDoubleClick.isFastDoubleClick()) {
            return;
        }
        if (CheckDoubleClick.isFastDoubleClick())
            return;
        UpdatePswFragment fragment = GeneratedClassUtils.getInstance(UpdatePswFragment.class);
        fragment.tag = UpdatePswFragment.class.getName();

        fragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
                R.anim.anim_fragment_close_out);
        fragment.setManager(getFragmentManager());
        fragment.show(SHOW_ADD_HIDE);
    }

    @Click(R.id.moreInfoLayout)
    void gotoUpdateMoInfo() {
        if (CheckDoubleClick.isFastDoubleClick())
            return;
        MoreUserInfoFragment fragment = GeneratedClassUtils.getInstance(MoreUserInfoFragment.class);
        fragment.tag = MoreUserInfoFragment.class.getName();
        Bundle bundle = new Bundle();
        bundle.putSerializable("mUserInfoResponse",mUserInfoResponse);
        fragment.setArguments(bundle);
        fragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
                R.anim.anim_fragment_close_out);
        fragment.setManager(getFragmentManager());
        fragment.show(SHOW_ADD_HIDE);
    }


}