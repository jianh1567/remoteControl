package com.wind.control.model;

import java.io.Serializable;

/**
 * Created by 1003373 on 2018/8/29.
 */

public class AddSceneInfo implements Serializable {
    private String scenename;
    private String time;
    private String phone;
    private String week;
    private String token;
    private SceneDeviceInfo[] d_id;

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

    public void setToken(String token){
        this.token = token;
    }

    public String getToken(){
        return this.token;
    }

    public void setD_id(SceneDeviceInfo[] sceneDeviceInfos){
        this.d_id = sceneDeviceInfos;
    }

    public SceneDeviceInfo[] getD_id(){
        return this.d_id;
    }

    public static class SceneDeviceInfo implements Serializable{
        private int id;
        private int sw;
        private String model;
        private String temperature;
        private String windspeed;

        public void setId(int id){
            this.id = id;
        }

        public int getId(){
           return  this.id = id;
        }

        public void setSw(int sw){
            this.sw = sw;
        }

        public int getSw(){
            return this.sw;
        }

        public void setModel(String mode){
            this.model = mode;
        }

        public String getModel(){
            return this.model;
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
    }
}
