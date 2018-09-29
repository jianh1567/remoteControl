package com.wind.control.model;

import java.io.Serializable;

/**
 * 作者：Created by luow on 2018/7/2
 * 注释：请求响应体
 */
public class HttpResponse implements Serializable {

    /**
     * code	String	返回状态码
     * msg	String	返回信息说明
     */
    private String code;
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
