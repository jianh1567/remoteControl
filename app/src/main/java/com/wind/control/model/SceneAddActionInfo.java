package com.wind.control.model;

/**
 * Created by 1003373 on 2018/8/27.
 */

public class SceneAddActionInfo {
    private String actionName;
    private int status;
    private int id;
    private String model;
    private String temp;
    private String speed;


    public SceneAddActionInfo(String name, int status, int id, String model, String temp, String speed){
        this.actionName = name;
        this.status = status;
        this.id = id;
        this.model = model;
        this.temp = temp;
        this.speed = speed;
    }

    public void setActionName(String name){
        this.actionName = name;
    }

    public String getActionName(){
        return this.actionName;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
       return this.id;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus(){
        return this.status;
    }

    public void setModel(String model){
        this.model = model;
    }

    public String getModel(){
        return this.model;
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
}
