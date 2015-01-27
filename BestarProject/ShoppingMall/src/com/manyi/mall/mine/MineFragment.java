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
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huoqiu.framework.analysis.ManyiAnalysis;
import com.huoqiu.framework.app.AppConfig;
import com.huoqiu.framework.app.SuperFragment;
import com.huoqiu.framework.exception.ClientException;
import com.huoqiu.framework.exception.RestException;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.DialogBuilder;
import com.huoqiu.framework.util.GeneratedClassUtils;
import com.huoqiu.framework.util.StringUtil;
import com.huoqiu.widget.filedownloader.FileDownloadListener;
import com.manyi.mall.R;
import com.manyi.mall.StartActivity;
import com.manyi.mall.cachebean.mine.MineHomeRequest;
import com.manyi.mall.cachebean.mine.MineHomeResponse;
import com.manyi.mall.cachebean.user.AutoUpdateResponse;
import com.manyi.mall.cachebean.user.VersionRequest;
import com.manyi.mall.common.Constants;
import com.manyi.mall.service.CommonService;
import com.manyi.mall.service.UcService;
import com.manyi.mall.user.BandBankCardFragment;
import com.manyi.mall.user.UpdateLoginPwdFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.springframework.util.StringUtils;

import java.io.File;

@EFragment(R.layout.fragment_mine)
public class MineFragment extends SuperFragment<Object> implements android.content.DialogInterface.OnClickListener {

    private UcService mUserService;
    private CommonService mAppLoadService;
    private MineHomeResponse mRes;

    private AutoUpdateResponse mResponse;

    @AfterViews
    void loadDate() {

        getData();
    }

    @Override
    public void onAttach(Activity activity) {
        setBackOp(null);
        super.onAttach(activity);
    }

    @Background
    void getData() {
        try {
            MineHomeRequest req = new MineHomeRequest();
            int uid = getActivity().getSharedPreferences(Constants.LOGIN_TIMES, 0).getInt("uid", 0);
            req.setUid(uid);
            mRes = mUserService.Minehome(req);

            setText();

        } catch (Exception e) {
            throw e;
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
    @Click(R.id.mine_exit)
    void exit() {
        ManyiAnalysis.onEvent(getActivity(), "LogoutClick");
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
        FeedBackFragment feedBackFragment = GeneratedClassUtils.getInstance(FeedBackFragment.class);
        feedBackFragment.tag = MineAwardFragment.class.getName();
        feedBackFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
                R.anim.anim_fragment_close_out);
        feedBackFragment.setManager(getFragmentManager());
        feedBackFragment.setContainerId(R.id.main_container);
        feedBackFragment.show(SHOW_ADD_HIDE);
    }


    @Click(R.id.mine_bonus)
    void mineBonus() {
        if (CheckDoubleClick.isFastDoubleClick())
            return;
        ManyiAnalysis.onEvent(getActivity(), "MyRewardsClick");

        MineAwardFragment mineAwardFragBment = GeneratedClassUtils.getInstance(MineAwardFragment.class);
        mineAwardFragBment.tag = MineAwardFragment.class.getName();
        mineAwardFragBment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
                R.anim.anim_fragment_close_out);
        mineAwardFragBment.setManager(getFragmentManager());
        mineAwardFragBment.setContainerId(R.id.main_container);
        mineAwardFragBment.setSelectListener(new SelectListener<Object>() {

            @Override
            public void onSelected(Object t) {
                getData();
            }

            @Override
            public void onCanceled() {
            }
        });
        mineAwardFragBment.show(SHOW_ADD_HIDE);
    }


    @Click(R.id.check_update)
    @Background
    void checkUpdate() {

        try {
            ManyiAnalysis.onEvent(getActivity(), "CheckUpdateClick");
            if (CheckDoubleClick.isFastDoubleClick())
                return;
            mResponse = new AutoUpdateResponse();
            VersionRequest versionRequest = new VersionRequest();
            versionRequest.setVersion(AppConfig.versionName);
            mResponse = mAppLoadService.getUpdateResponse2(versionRequest);
            // 服务器版本大于当前版本
            if (mResponse != null && StringUtils.hasLength(mResponse.getUrl()) && !mResponse.getVersion().equals(AppConfig.versionName)) {
                String title = /* Const.APP_UPDATE_TITLE + */"发现新版本:" + mResponse.getVersion();
                String msg = mResponse.getMessage().toString();
                // String msg1 = "2.0版本已发布,更新内容：\n\n 1. 加了好多东西\n 2.修了好多Bug";
                msg = msg.replace("\\n", "\n");
                updateSuccess(title, msg, mResponse.getUrl(), mResponse.isIfForced());
            } else {
                updateFailed(mResponse);
            }
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
    void updateFailed(AutoUpdateResponse response) {
        if (getActivity() == null || getActivity().isFinishing()) {
            return;
        }

        if (response == null) {
            DialogBuilder.showSimpleDialog("服务失败了！", getBackOpActivity());
        } else {
            // 已经是最新版本了
            DialogBuilder.showSimpleDialog(response.getMessage(), getBackOpActivity());
        }
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
        CommonProblemFragment commonProblemFragment = GeneratedClassUtils.getInstance(CommonProblemFragment.class);
        commonProblemFragment.tag = CommonProblemFragment.class.getName();
        commonProblemFragment.setManager(getFragmentManager());
        commonProblemFragment.setContainerId(R.id.main_container);
        commonProblemFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
                R.anim.anim_fragment_close_out);

        commonProblemFragment.show(SHOW_ADD_HIDE);
    }


    private void showDialog(String msgString) {
        DialogBuilder.showSimpleDialog(msgString, getActivity());
    }
}
