package com.manyi.mall;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.FrameLayout;

import com.huoqiu.framework.backstack.BackOpFragmentActivity;
import com.huoqiu.framework.backstack.Op;
import com.huoqiu.framework.rest.Configuration;
import com.huoqiu.framework.util.DialogBuilder;
import com.huoqiu.framework.util.GeneratedClassUtils;
import com.huoqiu.framework.util.NetworkUtil;
import com.huoqiu.framework.util.StringUtil;
import com.manyi.mall.cachebean.NotificationBean;
import com.manyi.mall.common.push.Constants;
import com.manyi.mall.common.push.Notifier;
import com.manyi.mall.common.util.DBUtil;
import com.manyi.mall.user.LoginFragment;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;

@WindowFeature({ Window.FEATURE_NO_TITLE })
@EActivity(R.layout.activity_start)
public class StartActivity extends BaseActivity {
	@ViewById(R.id.start_activity)
	public FrameLayout mLayout;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        if (!NetworkUtil.isOpenNetwork(this)){
            DialogBuilder.showSimpleDialog("未检测到网络连接", "确定", this, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            return ;
        }
		// 通知
		if (getIntent() != null && getIntent().getStringExtra(Constants.TAG) != null) {
			Intent intent = getIntent();
			NotificationBean bean = NotificationBean.getInstance();
			bean.msgtype = intent.getStringExtra(Constants.NOTIFICATION_MSGTYPE);
			bean.message = intent.getStringExtra(Constants.NOTIFICATION_MESSAGE);
			Notifier.sendReadedInfo(intent.getStringExtra(Constants.PACKET_ID), intent.getStringExtra(Constants.NOTIFICATION_FROM));
		}
		if (savedInstanceState == null) {
			initIp();
//			initBaseException();
			initFragment();

		}
	}

	/**
	 * 获取测试Ip
	 */
	private void initIp() {
			Configuration.DEFAULT.hostname = "shopapp.iiyey.com";
			Configuration.DEFAULT.port = 80;
		Configuration.DEFAULT.protocol = "http";
		Configuration.DEFAULT.path = "/rest";
	}

	/**
	 * 初始化异常处理
	 */
//	private void initBaseException(){
//        Intent targetIntent = new Intent();
//        targetIntent.setClass(AppConfig.application ,GeneratedClassUtils.get(StartActivity.class));
//        PendingIntent intent = PendingIntent.getActivity(this.getBaseContext(), 0x200, targetIntent, getIntent().getFlags());
//        BaseUncaughtExceptionHandler baseException = BaseUncaughtExceptionHandler.getInstance();
//        baseException.setmIntent(intent);
//    }

	private void initFragment() {
		LoginFragment loginFragment = GeneratedClassUtils.getInstance(LoginFragment.class);
		Bundle extra = getIntent().getBundleExtra("bundle");
        loginFragment.tag = LoginFragment.class.getName();
		Bundle bundle = new Bundle();
		if (extra == null || extra.getBoolean("isEnterFromLauncher", true)) {
			// startFragment.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out, R.anim.anim_fragment_close_in,
			// R.anim.anim_fragment_close_out);

			bundle.putBoolean("NotCheckNewVersion", false);
		} else {
			bundle.putBoolean("NotCheckNewVersion", true);
		}
        loginFragment.setManager(getSupportFragmentManager());
        loginFragment.setArguments(bundle);
        loginFragment.setBackOp(new Op() {

			@Override
			public void setTag(String tag) {
			}

			@Override
			public void perform(BackOpFragmentActivity activity) {
				finish();
			}

			@Override
			public String getTag() {
				return LoginFragment.class.getName();
			}
		});
        loginFragment.show();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		cancelNotice();
	}

	/** 取消通知栏 */
	public void cancelNotice() {
		NotificationManager notificationMgr = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
		try {
			notificationMgr.cancelAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
