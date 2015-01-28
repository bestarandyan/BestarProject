package com.manyi.mall.mine;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.exception.ClientException;
import com.huoqiu.framework.exception.RestException;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.DialogBuilder;
import com.huoqiu.framework.util.GeneratedClassUtils;
import com.huoqiu.widget.filedownloader.FileDownloadListener;
import com.manyi.mall.R;
import com.manyi.mall.StartActivity;
import com.manyi.mall.common.Constants;
import com.manyi.mall.service.CommonService;
import com.manyi.mall.service.UcService;
import com.manyi.mall.widget.switchView.ToggleButton;

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
    private UcService mUserService;
    private CommonService mAppLoadService;

    @AfterViews
    void loadDate() {
        //切换开关
        mSwitchBtn.toggle();
        //开关切换事件
        mSwitchBtn.setOnToggleChanged(new ToggleButton.OnToggleChanged(){
            @Override
            public void onToggle(boolean on) {
                if (on){
                    Toast.makeText(getActivity(),"开",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(),"关",Toast.LENGTH_SHORT).show();
                }
            }
        });

//        mSwitchBtn.setToggleOn();
//        mSwitchBtn.setToggleOff();
    }

    @Override
    public void onAttach(Activity activity) {
        setBackOp(null);
        super.onAttach(activity);
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
                SharedPreferences sharedPreferences2 = getBackOpActivity().getSharedPreferences(Constants.LOGIN_TIMES, Context.MODE_PRIVATE);
                Editor editor = sharedPreferences2.edit();

                editor.putString("password", null);
                editor.putString("name", null);
                editor.putString("realName", null);
                editor.putBoolean("isSkip", false);
                editor.putInt("cityId", 0);
                editor.putString("cityName", null);
                editor.commit();

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

    }

    @Click(R.id.Layout1)
    void layoutClick() {
        if (CheckDoubleClick.isFastDoubleClick())
            return;

    }


    @Click(R.id.check_update)
    @Background
    void checkUpdate() {

        try {
            if (CheckDoubleClick.isFastDoubleClick())
                return;
        } catch (final RestException ex) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    restErrorProcess(ex.getMessage(), RestException.class.cast(ex).getErrorCode());
                }
            });

        } catch (final ClientException ex) {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    unexpectedErrorProcess(ex.getMessage());
                    ex.printStackTrace();
                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }

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

    @Click(R.id.payHelpTv)
    public void gotoCommonProblem() {

    }


    private void showDialog(String msgString) {
        DialogBuilder.showSimpleDialog(msgString, getActivity());
    }
}
