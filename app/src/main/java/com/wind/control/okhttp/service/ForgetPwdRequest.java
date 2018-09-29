package com.wind.control.okhttp.service;

/**
 * 作者：Created by luow on 2018/7/20
 * 注释：
 */
public class ForgetPwdRequest {

    private String phone;
    private String veri_code;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVeri_code() {
        return veri_code;
    }

    public void setVeri_code(String veri_code) {
        this.veri_code = veri_code;
    }
}
