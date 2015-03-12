package com.manyi.mall.service;

import com.huoqiu.framework.rest.AppAuthInterceptor;
import com.huoqiu.framework.rest.RequestMapping;
import com.huoqiu.framework.rest.RestService;

@RequestMapping(value = "/common", interceptors = AppAuthInterceptor.class)
public interface CommonService extends RestService {


}
