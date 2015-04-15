package com.manyi.mall;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.FrameLayout;

import com.baidu.android.pushservice.CustomPushNotificationBuilder;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
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
import com.manyi.mall.push.Utils;
import com.manyi.mall.user.LoginFragment;

import org.androidannotations.annotations.Background;
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
        setPush();
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
    @Background
    public void setPush(){
        // Push: 以apikey的方式登录，一般放在主Activity的onCreate中。
        // 这里把apikey存放于manifest文件中，只是一种存放方式，
        // 您可以用自定义常量等其它方式实现，来替换参数中的Utils.getMetaValue(PushDemoActivity.this,
        // "api_key")
//        ！！ 请将AndroidManifest.xml 104行处 api_key 字段值修改为自己的 api_key 方可使用 ！！
//        ！！ ATTENTION：You need to modify the value of api_key to your own at row 104 in AndroidManifest.xml to use this Demo !!
        PushManager.startWork(this,
                PushConstants.LOGIN_TYPE_API_KEY,
                Utils.getMetaValue(this, "api_key"));
        // Push: 如果想基于地理位置推送，可以打开支持地理位置的推送的开关
        // PushManager.enableLbs(getApplicationContext());

        // Push: 设置自定义的通知样式，具体API介绍见用户手册，如果想使用系统默认的可以不加这段代码
        // 请在通知推送界面中，高级设置->通知栏样式->自定义样式，选中并且填写值：1，
        // 与下方代码中 PushManager.setNotificationBuilder(this, 1, cBuilder)中的第二个参数对应
        CustomPushNotificationBuilder cBuilder = new CustomPushNotificationBuilder(
                this, R.layout.notification_custom_builder,
                R.id.notification_icon,
                R.id.notification_title,
                R.id.notification_text
        );
        cBuilder.setNotificationFlags(Notification.FLAG_AUTO_CANCEL);
        cBuilder.setNotificationDefaults(Notification.DEFAULT_SOUND
                | Notification.DEFAULT_VIBRATE);
        cBuilder.setStatusbarIcon(this.getApplicationInfo().icon);
        cBuilder.setLayoutDrawable(R.drawable.launcher_icon);
        PushManager.setNotificationBuilder(this, 1, cBuilder);
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
