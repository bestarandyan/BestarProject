package com.manyi.mall.service;

import com.huoqiu.framework.rest.AppAuthInterceptor;
import com.huoqiu.framework.rest.Loading;
import com.huoqiu.framework.rest.RequestMapping;
import com.huoqiu.framework.rest.Response;
import com.huoqiu.framework.rest.RestService;
import com.manyi.mall.R;
import com.manyi.mall.cachebean.user.LoginRequest;
import com.manyi.mall.cachebean.user.LoginResponse;
import com.manyi.mall.cachebean.user.ModifyLoginPwdRequest;
import com.manyi.mall.cachebean.user.RegistNextRequest;
import com.manyi.mall.cachebean.user.RegistRequest;
import com.manyi.mall.cachebean.user.RegisterAgainRequest;
import com.manyi.mall.cachebean.user.ResetPasswordRequest;
import com.manyi.mall.cachebean.user.UpdateUserPublicNumRequest;

@RequestMapping(value = "/uc", interceptors = AppAuthInterceptor.class)
public interface UcService extends RestService {

    //
    @Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.login_relative_layout)
    @RequestMapping("/userLogin.rest")
    public LoginResponse login(LoginRequest request);



    @RequestMapping("/userLogin.rest")
    public LoginResponse splashLogin(LoginRequest request);

    @Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.register_relative_layout)
    @RequestMapping("/checkMobile.rest")
    public Response check(RegistRequest request);


    @Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.register_next_relative_layout)
    @RequestMapping("/regist.rest")
    public Response register(RegistNextRequest request);



    @Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.update_password_relative_layout)
    @RequestMapping("/modifyLoginPwd.rest")
    public Response modifyLoginPwd(ModifyLoginPwdRequest req);


    @Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.register_next_relative_layout)
    @RequestMapping("/againRegist.rest")
    public Response againRegist(RegisterAgainRequest request);



    /**
     * 发布标识清除
     */
    @Loading(value = R.id.loading, layout = R.layout.loading, container = R.id.release_fragment)
    @RequestMapping("/updateUserPublicNum.rest")
    public Response updatePublicNum(UpdateUserPublicNumRequest req);




    // @Loading(value = R.id.loading, layout = R.layout.loading)
    // @RequestMapping("/hisTaskDetail.rest")
    // @Accept(MediaType.APPLICATION_JSON)
    // public HisTaskDetailResponse getFinishedTaskInfo(
    // HisTaskDetailRequest request);
    //
    // @Loading(value = R.id.loading, layout = R.layout.loading)
    // @RequestMapping("/taskDetails.rest")
    // @Accept(MediaType.APPLICATION_JSON)
    // public ToDaysTaskDetailsResponse getToDayTaskInfo(
    // ToDaysTaskDetailsRequest request);
    //
    // @RequestMapping("/taskLookFail.rest")
    // @Accept(MediaType.APPLICATION_JSON)
    // public Response taskNotAs(TaskLookFailRequest request);
}