package com.wind.control.model;

import com.wind.control.model.SceneInfoResultBean.SceneModel;
/**
 * Created by W010003373 on 2018/8/10.
 */

public class SceneList {
    private int imageId;
    private boolean isChecked;
    private String sceneName;
    private int triggerMode;
    private boolean isSaveSetting;
    private SceneModel sceneModel;
    private boolean isVisible = true;

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

    public void setSceneName(String sceneName){
        this.sceneName = sceneName;
    }

    public String  getSceneName(){
        return this.sceneName;
    }

    public void setTriggerMode(int triggerMode){
        this.triggerMode = triggerMode;
    }

    public int getTriggerMode(){
        return this.triggerMode;
    }

    public void setSaveSetting(boolean isSave){
        this.isSaveSetting = isSave;
    }

    public boolean getSaveSetting(){
        return this.isSaveSetting;
    }

    public void setSceneModel(SceneModel sceneModel){
        this.sceneModel = sceneModel;
    }

    public SceneModel getSceneModel(){
        return this.sceneModel;
    }

    public void setDelBtnVisible(boolean isVisible){
        this.isVisible = isVisible;
    }

    public boolean getDelBtnVisible(){
        return this.isVisible;
    }
}
