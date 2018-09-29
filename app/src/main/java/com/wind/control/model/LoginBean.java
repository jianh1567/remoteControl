package com.wind.control.model;

/**
 * 作者：Created by luow on 2018/7/2
 * 注释：登录返回参数
 */
public class LoginBean {

    /**
     * code : 1000
     * msg : 登录成功!
     * token : eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJBUFAiLCJ1c2VyX2lkIjoiMTg2NTU1MjAxOTMiLCJpc3MiOiJTZXJ2aWNlIiwiZXhwIjoxNTM3NDk3MzExLCJpYXQiOjE1MzY2MzMzMTF9.Y92I8cUv92l3PCobBQ9o8CCQ7uSZlcP7GFo5kReOwY8
     */

    private int code;
    private String msg;
    private String token;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
