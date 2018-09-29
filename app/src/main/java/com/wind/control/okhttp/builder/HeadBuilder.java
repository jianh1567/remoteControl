package com.wind.control.okhttp.builder;

import com.wind.control.okhttp.OkHttpUtils;
import com.wind.control.okhttp.request.OtherRequest;
import com.wind.control.okhttp.request.RequestCall;


/**
 * Created by zhy on 16/3/2.
 */
public class HeadBuilder extends GetBuilder
{
    @Override
    public RequestCall build()
    {
        return new OtherRequest(null, null, OkHttpUtils.METHOD.HEAD, url, tag, params, headers,id).build();
    }
}
