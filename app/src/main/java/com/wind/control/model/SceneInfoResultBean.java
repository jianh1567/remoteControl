package com.wind.control.model;

import java.io.Serializable;

/**
 * Created by 1003373 on 2018/8/30.
 */

public class SceneInfoResultBean implements Serializable{
    private int code;
    private SceneModel[] scenemodel;

    public void setCode(int code){
        this.code = code;
    }

    public int getCode(){
        return this.code;
    }

    public void setScenemodel(SceneModel[] scenemodel){
        this.scenemodel = scenemodel;
    }

    public SceneModel[] getSceneModel(){
        return this.scenemodel;
    }

    public static class  SceneModel implements Serializable {
        private String week;
        private String phone;
        private String scenename;
        private String time;
        private int scenemodel_id;
        private DeviceModel[] devicemodel;

        public void setWeek(String week){
            this.week = week;
        }

        public String getWeek(){
            return this.week;
        }

        public void setPhone(String phone){
            this.phone = phone;
        }

        public String getPhone(){
            return this.phone;
        }

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

        public void setScenemodel_id(int id){
            this.scenemodel_id = id;
        }

        public int getScenemodel_id(){
            return this.scenemodel_id;
        }

        public void setDevicemodel(DeviceModel[] devicemodel){
            this.devicemodel = devicemodel;
        }

        public DeviceModel[] getDevicemodel(){
            return this.devicemodel;
        }

        public static class DeviceModel implements Serializable{
            private int devicemodel_id;
            private String ir_devicename;
            private int sw;
            private int brandid;
            private String name;
            private String temperature;
            private String windspeed;
            private String model;
            private String type;
            private String title;
            private String mac;
            private int categoryid;
            private int d_id;

            public void setDevicemodel_id(int id){
                this.devicemodel_id = id;
            }

            public int getDevicemodel_id(){
                return this.devicemodel_id;
            }

            public void setIr_devicename(String name){
                this.ir_devicename = name;
            }

            public String getIr_devicename(){
                return this.ir_devicename;
            }

            public void setSw(int sw){
                this.sw = sw;
            }

            public int getSw(){
                return this.sw;
            }

            public void setBrandid(int id){
                this.brandid = id;
            }

            public int getBrandid(){
                return this.brandid;
            }

            public void setName(String name){
                this.name = name;
            }

            public String getName(){
                return this.name;
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

            public void setType(String type){
                this.type = type;
            }

            public String getType(){
                return this.type;
            }

            public void setTitle(String title){
                this.title = title;
            }

            public String getTitle(){
                return this.title;
            }

            public void setMac(String mac){
                this.mac = mac;
            }

            public String getMac(){
                return this.mac;
            }

            public void setCategoryid(int id){
                this.categoryid = id;
            }

            public int getCategoryid(){
                return this.categoryid;
            }

            public void setD_id(int id) {
                this.d_id = id;
            }

            public int getD_id(){
                return this.d_id;
            }
        }
    }
}
