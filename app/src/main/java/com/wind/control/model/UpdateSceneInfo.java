package com.wind.control.model;

import java.io.Serializable;

/**
 * Created by 1003373 on 2018/9/7.
 */

public class UpdateSceneInfo implements Serializable {
    private String scenename;
    private String time;
    private String phone;
    private String week;
    private int scenemodel_id;
    private String token;
    private DeviceModel[] devicemodel;


    public void setScenename(String name){
        this.scenename = name;
    }

    public String getScenename(){
        return this.scenename;
    }

    public void setTime(String time){
        this.time = time;
    }

    public String getTime(){
        return this.time;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }

    public String getPhone(){
        return this.phone;
    }

    public void setWeek(String week){
        this.week = week;
    }

    public String getWeek(){
        return this.week;
    }

    public void setScenemodel_id(int id){
        this.scenemodel_id = id;
    }

    public int getScenemodel_id(){
        return this.scenemodel_id;
    }

    public void setToken(String token){
        this.token = token;
    }

    public String getToken(){
        return this.token;
    }

    public void setDevicemodel(DeviceModel[] devicemodel){
        this.devicemodel = devicemodel;
    }

    public DeviceModel[] getDevicemodels(){
        return this.devicemodel;
    }

    public static class DeviceModel implements Serializable{
        private int changetype;
        private int devicemodel_id;
        private int sw;
        private String temperature;
        private String windspeed;
        private String model;
        private int d_id;

        public void setChangetype(int changetype){
            this.changetype = changetype;
        }

        public int getChangetype(){
            return this.changetype;
        }

        public void setDeviceModelId(int id){
            this.devicemodel_id = id;
        }

        public int getDeviceModelId(){
            return this.devicemodel_id;
        }

        public void setSw(int sw){
            this.sw = sw;
        }

        public int getSw(){
            return this.sw;
        }

        public void setTemperature(String temperature){
            this.temperature = temperature;
        }

        public String getTemperature(){
            return this.temperature;
        }

        public void setWindspeed(String windspeed){
            this.windspeed = windspeed;
        }

        public String getWindspeed(){
            return this.windspeed;
        }

        public void setModel(String model){
            this.model = model;
        }

        public String getModel(){
            return this.model;
        }

        public void setD_id(int id){
            this.d_id = id;
        }

        public int getD_id(){
            return this.d_id;
        }
    }
}
