package com.wind.control.model;

/**
 * 作者：Created by luow on 2018/8/11
 * 注释：
 */
public class DeviceBean {

    private String ProductName;
    private String DeviceName;
    private String phone;
    private String token;
    private int cmd[];

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getDeviceName() {
        return DeviceName;
    }

    public void setDeviceName(String deviceName) {
        DeviceName = deviceName;
    }

    public int[] getCmd() {
        return cmd;
    }

    public void setCmd(int[] cmd) {
        this.cmd = cmd;
    }

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
