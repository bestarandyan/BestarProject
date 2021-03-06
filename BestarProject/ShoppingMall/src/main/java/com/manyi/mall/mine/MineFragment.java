package com.manyi.mall.mine;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.android.pushservice.CustomPushNotificationBuilder;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.exception.RestException;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.DialogBuilder;
import com.huoqiu.framework.util.GeneratedClassUtils;
import com.huoqiu.framework.util.ManyiUtils;
import com.huoqiu.widget.filedownloader.FileDownloadListener;
import com.manyi.mall.BestarApplication;
import com.manyi.mall.R;
import com.manyi.mall.StartActivity;
import com.manyi.mall.cachebean.user.UserInfoResponse;
import com.manyi.mall.push.Utils;
import com.manyi.mall.service.CommonService;
import com.manyi.mall.service.RequestServerFromHttp;
import com.manyi.mall.user.UserInfoFragment;
import com.manyi.mall.utils.JsonData;
import com.manyi.mall.wap.AppDownLoadFragment;
import com.manyi.mall.widget.GalleryWidget.ImageLoaderConfig;
import com.manyi.mall.widget.imageView.CircleImageView;
import com.manyi.mall.widget.switchView.ToggleButton;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.File;

@EFragment(R.layout.fragment_mine)
public class MineFragment extends SuperFragment<Object> implements android.content.DialogInterface.OnClickListener {
    @ViewById(R.id.switchBtn)
    ToggleButton mSwitchBtn;
    private CommonService mAppLoadService;
    @ViewById(R.id.headLayout)
    CircleImageView mHeadImgView;

    @ViewById(R.id.Layout1)
    TextView mLayout1;

    @ViewById(R.id.currentVersionTv)
    TextView mViewsionTv;
    UserInfoResponse mUserInfoResponse =null;
    @ViewById(R.id.nameTV)
    TextView mNameTv;


    String type;

    @Override
    public void onResume() {
        super.onResume();
        mNameTv.setText(BestarApplication.getInstance().getRealName());
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

    @Click(R.id.payHelpTv)
    void clickPlayHelpLayout(){
        if (CheckDoubleClick.isFastDoubleClick())
            return;
        DialogBuilder.showSimpleDialog("拨打服务专线：0571-8602-0252 ","确定","取消",getBackOpActivity(),new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onCallPhoneStartIntent("057186020252");
            }
        });
    }
    /**
     * 拨打电话
     *
     * @param uri
     */
    public void onCallPhoneStartIntent(String uri) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + uri));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    @UiThread
    void setViewValue(){
        mNameTv.setText(mUserInfoResponse.RealName);
        BestarApplication.getInstance().setRealName(mUserInfoResponse.RealName);
        ImageLoader.getInstance().displayImage(mUserInfoResponse.PortraitPath,mHeadImgView, ImageLoaderConfig.options_agent,imageLoadingListener, ImageLoaderConfig.imgProgressListener);
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
    @AfterViews
    void loadDate() {
        type = BestarApplication.getInstance().getType();
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getActivity()));
        initOption();
        getUserInfo();
        if (type.equals("1")) {//渠道
            mLayout1.setText("代理付款明细");
        } else {//园长
            mLayout1.setText("我的代金券");
        }
        getUrl();
        mViewsionTv.setText(ManyiUtils.getVersion(getActivity()));

        //切换开关
        mSwitchBtn.toggle();
        setPush();
        //开关切换事件
        mSwitchBtn.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on) {
                    setPush();
                } else {
                    PushManager.stopWork(getActivity());
                }
            }
        });
    }


    @Background
    public void setPush() {
        // Push: 以apikey的方式登录，一般放在主Activity的onCreate中。
        // 这里把apikey存放于manifest文件中，只是一种存放方式，
        // 您可以用自定义常量等其它方式实现，来替换参数中的Utils.getMetaValue(PushDemoActivity.this,
        // "api_key")
//        ！！ 请将AndroidManifest.xml 104行处 api_key 字段值修改为自己的 api_key 方可使用 ！！
//        ！！ ATTENTION：You need to modify the value of api_key to your own at row 104 in AndroidManifest.xml to use this Demo !!
        PushManager.startWork(getActivity(),
                PushConstants.LOGIN_TYPE_API_KEY,
                Utils.getMetaValue(getActivity(), "api_key"));
        // Push: 如果想基于地理位置推送，可以打开支持地理位置的推送的开关
        // PushManager.enableLbs(getApplicationContext());

        // Push: 设置自定义的通知样式，具体API介绍见用户手册，如果想使用系统默认的可以不加这段代码
        // 请在通知推送界面中，高级设置->通知栏样式->自定义样式，选中并且填写值：1，
        // 与下方代码中 PushManager.setNotificationBuilder(this, 1, cBuilder)中的第二个参数对应
        CustomPushNotificationBuilder cBuilder = new CustomPushNotificationBuilder(
                getActivity(), R.layout.notification_custom_builder,
                R.id.notification_icon,
                R.id.notification_title,
                R.id.notification_text
        );
        cBuilder.setNotificationFlags(Notification.FLAG_AUTO_CANCEL);
        cBuilder.setNotificationDefaults(Notification.DEFAULT_SOUND
                | Notification.DEFAULT_VIBRATE);
        cBuilder.setStatusbarIcon(getActivity().getApplicationInfo().icon);
        cBuilder.setLayoutDrawable(R.drawable.launcher_icon);
        PushManager.setNotificationBuilder(getActivity(), 1, cBuilder);
    }

    @Override
    public void onAttach(Activity activity) {
        setBackOp(null);
        super.onAttach(activity);
    }

    @Click(R.id.headLayout)
    void gotoUserInfo() {
        if (CheckDoubleClick.isFastDoubleClick())
            return;
        UserInfoFragment fragment = GeneratedClassUtils.getInstance(UserInfoFragment.class);
        fragment.tag = UserInfoFragment.class.getName();

        fragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
                R.anim.anim_fragment_close_out);
        fragment.setManager(getFragmentManager());
        fragment.show(SHOW_ADD_HIDE);
    }

    String shareUrl;

    @Background
    void getUrl() {
        RequestServerFromHttp request = new RequestServerFromHttp();
        String msg = request.getUrl();
        if (msg != null && msg.contains("AppDownLoad")) {
            shareUrl = new JsonData().JsonUrl(msg, "AppDownLoad");
        }
    }

    @SuppressLint({"ResourceAsColor", "InlinedApi"})
    @UiThread
    void setText() {
        try {
            PackageManager packageManager = getActivity().getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(getActivity().getPackageName(), 0);
            String version = packInfo.versionName;
//            nowVersions.setText("当前版本" + " " + version);
        } catch (Exception e) {

        }

    }

    @SuppressLint("WorldReadableFiles")
    @Click(R.id.mine_exit_btn)
    void exit() {
        if (CheckDoubleClick.isFastDoubleClick())
            return;
        DialogBuilder.showSimpleDialog("您确定退出?", "确定", "取消", getBackOpActivity(), new DialogInterface.OnClickListener() {

            @SuppressLint({"CommitPrefEdits", "WorldWriteableFiles"})
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // @SuppressWarnings({ })
                SharedPreferences mySharedPreferences = getActivity().getSharedPreferences("appkey", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = mySharedPreferences.edit();
                editor.putString("appkey", "");
                editor.commit();

                SharedPreferences userInfo = getActivity().getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor1 = userInfo.edit();
                editor1.putString("userName", "");
                editor1.putString("password", "");
                editor1.commit();

                Intent intent = new Intent(getBackOpActivity(), GeneratedClassUtils.get(StartActivity.class));
                Bundle bundle = new Bundle();
                bundle.putBoolean("isEnterFromLauncher", false);
                intent.putExtra("bundle", bundle);
                startActivity(intent);

                getBackOpActivity().finish();

            }
        });
    }

    @Click(R.id.feedback)
    void feedBack() {
        if (CheckDoubleClick.isFastDoubleClick())
            return;
        FeedbackFragment feedbackFragment = GeneratedClassUtils.getInstance(FeedbackFragment.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("phone", mLoginUsername.getText().toString().trim());
//        voucherListFragment.setArguments(bundle);
        feedbackFragment.tag = FeedbackFragment.class.getName();

        feedbackFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
                R.anim.anim_fragment_close_out);
        feedbackFragment.setManager(getFragmentManager());
        feedbackFragment.show(SHOW_ADD_HIDE);
    }

    @Click(R.id.Layout1)
    void layoutClick() {
        if (CheckDoubleClick.isFastDoubleClick())
            return;

        if (type.equals("1")) {//渠道
            gotoAgencyPay();
        } else {
            gotoVoucher();
        }

    }

    private void gotoVoucher() {
        VoucherListFragment voucherListFragment = GeneratedClassUtils.getInstance(VoucherListFragment.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("phone", mLoginUsername.getText().toString().trim());
//        voucherListFragment.setArguments(bundle);
        voucherListFragment.tag = VoucherListFragment.class.getName();

        voucherListFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
                R.anim.anim_fragment_close_out);
        voucherListFragment.setManager(getFragmentManager());
        voucherListFragment.show(SHOW_ADD_HIDE);
    }

    private void gotoAgencyPay() {
        AgencyPayListFragment agencyPayListFragment = GeneratedClassUtils.getInstance(AgencyPayListFragment.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("phone", mLoginUsername.getText().toString().trim());
//        agencyPayListFragment.setArguments(bundle);
        agencyPayListFragment.tag = AgencyPayListFragment.class.getName();

        agencyPayListFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
                R.anim.anim_fragment_close_out);
        agencyPayListFragment.setManager(getFragmentManager());
        agencyPayListFragment.show(SHOW_ADD_HIDE);
    }

    private void gotoShare() {
        ShareFragment shareFragment = GeneratedClassUtils.getInstance(ShareFragment.class);
        Bundle bundle = new Bundle();
        bundle.putString("shareUrl", shareUrl);
        shareFragment.setArguments(bundle);
        shareFragment.tag = ShareFragment.class.getName();

//        shareFragment.setCustomAnimations(R.anim.slide_in_from_bottom, R.anim.slide_out_to_top, R.anim.slide_in_from_top,
//                    R.anim.slide_out_to_bottom);
        shareFragment.setManager(getFragmentManager());
        shareFragment.show(SHOW_ADD_HIDE);
    }

    @Click(R.id.shareApp)
    void shareApp() {
        if (CheckDoubleClick.isFastDoubleClick())
            return;
        gotoShare();
    }

    @Click(R.id.supportUsTv)
    void supportUs() {
        if (CheckDoubleClick.isFastDoubleClick())
            return;

    }

    @Click(R.id.aboutUsTv)
    void aboutUs() {
        if (CheckDoubleClick.isFastDoubleClick())
            return;
        AboutUsFragment fragment = GeneratedClassUtils.getInstance(AboutUsFragment.class);
        fragment.tag = AboutUsFragment.class.getName();
        fragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
                R.anim.anim_fragment_close_out);
        fragment.setManager(getFragmentManager());
        fragment.show(SHOW_ADD_HIDE);
    }


    @Click(R.id.check_update)
    @Background
    void checkUpdate() {
        if (CheckDoubleClick.isFastDoubleClick())
            return;
        AppDownLoadFragment shareFragment = GeneratedClassUtils.getInstance(AppDownLoadFragment.class);
        Bundle bundle = new Bundle();
        bundle.putString("shareUrl", shareUrl);
        shareFragment.setArguments(bundle);
        shareFragment.tag = AppDownLoadFragment.class.getName();

//        shareFragment.setCustomAnimations(R.anim.slide_in_from_bottom, R.anim.slide_out_to_top, R.anim.slide_in_from_top,
//                    R.anim.slide_out_to_bottom);
        shareFragment.setManager(getFragmentManager());
        shareFragment.show(SHOW_ADD_HIDE);
    }


    @UiThread
    public void checkEnable() {
//        mCheckUpdateLayout.setEnabled(true);
    }

    @UiThread
    public void checkUnable() {
//        mCheckUpdateLayout.setEnabled(false);

    }

    @UiThread
    void updateSuccess(String title, String message, final String url, boolean isForced) {
        doUpdate(title, message, url, isForced);
    }

    /**
     * 更新程序
     */
    private void doUpdate(String title, String message, final String url, boolean isForced) {
        if (isForced) {
            new AlertDialog.Builder(getActivity()).setPositiveButton("立即更新", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    doDownload(url);
                    // getActivity().finish();
                }
            }).setMessage(message).setCancelable(false).show();
        } else {
            Dialog dialog = new AlertDialog.Builder(getActivity()).setTitle(title).setMessage(message)
                    .setPositiveButton(("立即更新"), new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            doDownload(url);
                        }
                    }).setNegativeButton(("以后再说"), new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create();
            dialog.show();
        }
    }

    private ProgressDialog mProgress;

    /**
     * 显示更新界面
     */
    private void doDownload(String url) {
        mProgress = new ProgressDialog(getActivity());
        mProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        // mProgress.setTitle("正在下载");

        mProgress.setCancelable(false);

        mProgress.setMessage("正在下载");
        try {
            new FileDownloadListener(getActivity(), mProgress, url, callBack, R.drawable.launcher_icon, "房源宝", "FYB");
        } catch (Exception e) {
            e.printStackTrace();
            mProgress.dismiss();
            DialogBuilder.showSimpleDialog("版本更新失败，请手动检测更新。", getActivity());
        }
    }

    FileDownloadListener.DownloadCallBack callBack = new FileDownloadListener.DownloadCallBack() {

        @Override
        public void downloadSuccess(File file) {
            // 下载完成后退出进度条
            mProgress.dismiss();
            // 安装apk
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // 执行的数据类型
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void downloadFail() {
            mProgress.dismiss();
            Toast.makeText(getActivity(), "下载过程中出现错误", Toast.LENGTH_SHORT).show();
        }
    };

    private void restErrorProcess(String detailMessage, int errorCode) {
        if (errorCode == RestException.INVALID_TOKEN) {
            DialogBuilder.showSimpleDialog(detailMessage, getActivity());
        } else {
            DialogBuilder.showSimpleDialog(detailMessage, getActivity());
        }
    }

    private void unexpectedErrorProcess(String detailMessage) {
        DialogBuilder.showSimpleDialog(detailMessage, getActivity());
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "4007009922"));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    private void showDialog(String msgString) {
        DialogBuilder.showSimpleDialog(msgString, getActivity());
    }
}
