package com.wind.control.okhttp.service;

/**
 * 作者：Created by luow on 2018/6/29
 * 注释：登录请求参数
 */
public class LoginRequest {

    private String phone;
    private String password;
    private String identifyCode;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIdentifyCode() {
        return identifyCode;
    }

    public void setIdentifyCode(String identifyCode) {
        this.identifyCode = identifyCode;
    }

}

