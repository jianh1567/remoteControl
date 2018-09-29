package com.wind.control.model;

/**
 * Created by 1003373 on 2018/8/30.
 */

public class SceneInfoQueryBean {
    private String phone;
    private int startpage;
    private int limitpage;
    private String token;

    public void setPhone(String phone){
        this.phone = phone;
    }

    public String getPhone(){
        return this.phone;
    }

    public void setStartpage(int startpage){
        this.startpage = startpage;
    }

    public int getStartpage(){
        return this.startpage;
    }

    public void setLimitpage(int limitpage){
        this.limitpage = limitpage;
    }

    public int getLimitpage(){
        return this.limitpage;
    }

    public void setToken(String token){
        this.token = token;
    }

    public String getToken(){
        return this.token;
    }
}
