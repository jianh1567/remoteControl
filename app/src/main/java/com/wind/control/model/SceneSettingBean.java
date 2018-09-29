package com.wind.control.model;

import java.io.Serializable;

/**
 * Created by W010003373 on 2018/7/17.
 */

public class SceneSettingBean implements Serializable {
    private int code;
    private String msg;

    public int getCode(){
        return this.code;
    }

    public void setCode(int code){
        this.code = code;
    }

    public String getMessage(){
        return this.msg;
    }

    public void setMessage(String message){
        this.msg = message;
    }
}
