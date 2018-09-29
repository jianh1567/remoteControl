package com.wind.control.okhttp.request;

/**
 * 作者：Created by luow on 2018/9/13
 * 注释：
 */
public class DeviceRegisterRequest{

    private String phone;
    private String token;
    private String ProductKey;
    private String DeviceName;
    private String triadTitle;

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

    public String getProductKey() {
        return ProductKey;
    }

    public void setProductKey(String productKey) {
        ProductKey = productKey;
    }

    public String getDeviceName() {
        return DeviceName;
    }

    public void setDeviceName(String deviceName) {
        DeviceName = deviceName;
    }

    public String getTriadTitle() {
        return triadTitle;
    }

    public void setTriadTitle(String triadTitle) {
        this.triadTitle = triadTitle;
    }
}
