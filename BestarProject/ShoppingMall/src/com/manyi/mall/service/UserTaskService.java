package com.manyi.mall.service;

import org.androidannotations.annotations.rest.Accept;
import org.androidannotations.api.rest.MediaType;

import com.huoqiu.framework.rest.AppAuthInterceptor;
import com.huoqiu.framework.rest.Loading;
import com.huoqiu.framework.rest.RequestMapping;
import com.huoqiu.framework.rest.Response;
import com.huoqiu.framework.rest.RestService;

import com.manyi.mall.cachebean.user.UploadFileRequest;

@RequestMapping(value = "/userTask", interceptors = AppAuthInterceptor.class)
public interface UserTaskService extends RestService {


	/**
	 * taskUploadSingleFile.rest
	 */
	@RequestMapping("/taskUploadSingleFile.rest")
	@Accept(MediaType.APPLICATION_JSON)
	public Response taskUploadSingleFile(UploadFileRequest request);

}
