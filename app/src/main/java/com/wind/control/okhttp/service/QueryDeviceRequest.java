package com.wind.control.okhttp.service;

/**
 * 作者：Created by luow on 2018/8/23
 * 注释：
 */
public class QueryDeviceRequest {

  private String phone;
  private String token;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
