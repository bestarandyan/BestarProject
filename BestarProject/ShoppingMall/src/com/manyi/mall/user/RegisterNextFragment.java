package com.manyi.mall.user;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.huoqiu.framework.backstack.AbsOp;
import com.huoqiu.framework.backstack.BackOpFragmentActivity;
import com.huoqiu.framework.exception.RestException;
import com.huoqiu.framework.util.AdvancedBitmapUtils;
import com.huoqiu.framework.util.CheckDoubleClick;
import com.huoqiu.framework.util.DialogBuilder;
import com.huoqiu.framework.util.FileUtils;
import com.huoqiu.framework.util.GeneratedClassUtils;
import com.huoqiu.framework.util.ManyiUtils;
import com.huoqiu.framework.util.StringUtil;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.search.AreaAndTownMessage;
import com.manyi.mall.cachebean.search.City;
import com.manyi.mall.cachebean.user.RegistNextRequest;
import com.manyi.mall.cachebean.user.RegisterAgainRequest;
import com.manyi.mall.cachebean.user.UserLocationRequest;
import com.manyi.mall.common.CommonConfig;
import com.manyi.mall.common.Constants;
import com.manyi.mall.common.location.LBSHelper;
import com.manyi.mall.common.location.ManyiLoacationMannger;
import com.manyi.mall.common.location.ManyiLoacationMannger.OnLocationReceivedListener;
import com.manyi.mall.common.location.ManyiLocation;
import com.manyi.mall.common.util.AESUtil;
import com.manyi.mall.search.CityBaseFragment;
import com.manyi.mall.service.UcService;
import com.manyi.mall.service.UserLocationService;
import com.manyi.mall.widget.touchview.ImageLoaderConfig;

@EFragment(R.layout.fragment_register_next)
public class RegisterNextFragment extends ImageLoaderFragment {
	public static final int TAKE_CODE_PHOTO = 1003;
	public static final int TAKE_CARD_PHOTO = 1004;

	@FragmentArg
	public String mPhoneNumber; // 电话号码
	@FragmentArg
	public String mSmsCaptcha; // 验证码
	@FragmentArg
	public String mRegisterPwd; // 密码
	@FragmentArg
	public String mConfirmPwd; // 确认密码

	// 审核失败 重新注册 回填信息

	@FragmentArg
	public String mRealName;
	@FragmentArg
	public int mCityId;// 城市ID
	@FragmentArg
	public String mCityName;// 城市
	@FragmentArg
	public int mAreaId;// 区域ID
	@FragmentArg
	public String mAreaName;// 区域名称
	@FragmentArg
	public int mTownId;// 区域ID
	@FragmentArg
	public String mTownName;// 区域名称
	@FragmentArg
	public String mCode;// 身份证号码
	@FragmentArg
	public String mSpreadName;// 邀请码
	@FragmentArg
	public String mRemark;// 失败原因
	@FragmentArg
	public int mUserId; // 用户ID
	@FragmentArg
	public boolean isRegisterAgain; // 是否是重新注册

	private AreaAndTownMessage mAreaAndTownMessage;

	private UcService mUserService;

	@FragmentArg
	public File mCodeFile; // 身份证上传文件
	@FragmentArg
	public File mCardFile; // 名片上传文件

	private City mCity;
	public LocationClient mLocationClient = null;
	private boolean isFirstEnter = true;

	private UserLocationService userLocationService;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@AfterViews
	void init() {
		ManyiLoacationMannger mLocation = new ManyiLoacationMannger(getActivity(), 0);
		mLocation.setOnLocationReceivedListener(new OnLocationReceivedListener() {

			@Override
			public void onReceiveLocation(ManyiLocation location) {

				if (location == null)
					return;
				String cityName = location.getCity();
				if (!StringUtil.isEmptyOrNull(cityName)) {
					City city = LBSHelper.getCityFromCityName(getActivity(), cityName);
					if (city != null && mCity == null && !isRegisterAgain) {
						mCity = city;
					}
				}

				sendLocation(location);
			}

			@Override
			public void onFailedLocation(String msg) {
				// TODO Auto-generated method stub
				
			}
		});
		mLocation.start();
	}

	@Background
	void sendLocation(ManyiLocation location) {
		SharedPreferences sharedPreferences = getBackOpActivity().getSharedPreferences(Constants.LOGIN_TIMES, Context.MODE_PRIVATE);
		Integer userId = sharedPreferences.getInt("uid", 1);
		UserLocationRequest request = new UserLocationRequest();
		request.setLatitude(String.valueOf(location.getLatitude()));
		request.setLongitude(String.valueOf(location.getLongitude()));
		request.setUserId(userId);
		try {
			 userLocationService.sendUserLocation(request);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}



}
