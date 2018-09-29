package com.wind.control.okhttp.service;

/**
 * 作者：Created by luow on 2018/8/23
 * 注释：
 */
public class BindDeviceRequest {
    private String phone;
    private String type;
    private String mac;
    private String name;
    private String title;
    private String token;
    private String ir_devicename;
    private int categoryid;
    private int brandid;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(int categoryid) {
        this.categoryid = categoryid;
    }

    public int getBrandid() {
        return brandid;
    }

    public void setBrandid(int brandid) {
        this.brandid = brandid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIr_devicename() {
        return ir_devicename;
    }

    public void setIr_devicename(String ir_devicename) {
        this.ir_devicename = ir_devicename;
    }
}
