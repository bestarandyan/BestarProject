package com.manyi.mall.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.baidu.location.LocationClient;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.user.UserLocationRequest;
import com.manyi.mall.common.Constants;
import com.manyi.mall.common.location.ManyiLoacationMannger;
import com.manyi.mall.common.location.ManyiLoacationMannger.OnLocationReceivedListener;
import com.manyi.mall.common.location.ManyiLocation;
import com.manyi.mall.service.UcService;
import com.manyi.mall.service.UserLocationService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;

import java.io.File;

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


	private UcService mUserService;

	@FragmentArg
	public File mCodeFile; // 身份证上传文件
	@FragmentArg
	public File mCardFile; // 名片上传文件

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
