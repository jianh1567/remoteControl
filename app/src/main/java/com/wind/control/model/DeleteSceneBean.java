package com.wind.control.model;

/**
 * Created by 1003373 on 2018/9/3.
 */

public class DeleteSceneBean {
    private int scenemodel_id;
    private String phone;
    private String token;

    public void setScenemodel_id(int id){
        this.scenemodel_id = id;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }

    public void setToken(String token){
        this.token = token;
    }

    public String getToken(){
        return this.token;
    }
}
