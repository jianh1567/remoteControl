package com.wind.control.model;

import java.io.Serializable;

/**
 * Created by 1003373 on 2018/8/27.
 */

public class SceneActionInfo implements Serializable{
    private String actionName;
    private int status;
    private String modle;
    private String temp;
    private String speed;
    private int deviceModelId;
    private int changetype;
    private int d_id;
    private String deviceType;

    public SceneActionInfo(String name, int status, String modle, String temp,
                           String speed, int deviceModelId, int changetype, int d_id, String deviceType){
        this.actionName = name;
        this.status = status;
        this.modle = modle;
        this.temp = temp;
        this.speed = speed;
        this.deviceModelId = deviceModelId;
        this.changetype = changetype;
        this.d_id = d_id;
        this.deviceType = deviceType;
    }

    public void setActionName(String name){
        this.actionName = name;
    }

    public String getActionName(){
        return this.actionName;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus(){
        return this.status;
    }

    public void setModle(String modle){
        this.modle = modle;
    }

    public String getModle(){
        return this.modle;
    }

    public void setTemp(String temp){
        this.temp = temp;
    }

    public String getTemp(){
        return this.temp;
    }

    public void setSpeed(String speed){
        this.speed = speed;
    }

    public String getSpeed(){
        return this.speed;
    }

    public void setDeviceModelId(int id){
        this.deviceModelId = id;
    }

    public int getDeviceModelId(){
        return this.deviceModelId;
    }

    public void setChangetype(int type){
        this.changetype = type;
    }

    public int getChangetype(){
        return this.changetype;
    }

    public void setD_id(int id){
        this.d_id = id;
    }

    public int getD_id(){
        return this.d_id;
    }

    public void setDeviceType(String type){
        this.deviceType = type;
    }

    public String getDeviceType(){
        return this.deviceType;
    }
}
