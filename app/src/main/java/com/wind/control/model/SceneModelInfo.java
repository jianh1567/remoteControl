package com.wind.control.model;

/**
 * Created by W010003373 on 2018/8/10.
 */

public class SceneModelInfo {
    private int imageId;
    private int descbId;
    private boolean isChecked;

    public void setImageId(int id){
        this.imageId = id;
    }

    public int getImageId(){
        return this.imageId;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public boolean getChecked(){
        return this.isChecked;
    }

    public void setDescbId(int id){
        this.descbId = id;
    }

    public int getDescbId(){
        return this.descbId;
    }
}
