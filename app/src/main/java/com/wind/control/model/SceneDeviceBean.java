package com.wind.control.model;

/**
 * Created by 1003373 on 2018/8/22.
 */

public class SceneDeviceBean {
    private int deviceImageId;
    private int deviceName;

    public SceneDeviceBean(int deviceName, int deviceImageId){
        this.deviceName = deviceName;
        this.deviceImageId = deviceImageId;
    }

    public void setDeviceImageId(int id){
        this.deviceImageId = id;
    }

    public int getDeviceImageId(){
        return this.deviceImageId;
    }

    public void setDeviceName(int name){
        this.deviceName = name;
    }

    public int getDeviceName(){
        return this.deviceName;
    }
}
