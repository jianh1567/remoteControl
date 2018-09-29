package com.wind.control.okhttp.service;

/**
 * 作者：Created by luow on 2018/6/29
 * 注释：验证码请求参数
 */
public class GetCodeRequest {

    private String phone;
    private String target;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}

