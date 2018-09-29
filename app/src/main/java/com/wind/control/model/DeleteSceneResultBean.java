package com.wind.control.model;

/**
 * Created by 1003373 on 2018/9/3.
 */

public class DeleteSceneResultBean {
    private int code;
    private String msg;

    public void setCode(int code){
        this.code = code;
    }

    public int getCode(){
        return this.code;
    }

    public void setMessage(String msg){
        this.msg = msg;
    }

    public String getMessage(){
        return this.msg;
    }
}
