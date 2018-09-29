package com.wind.control.model;

/**
 * Created by W010003373 on 2018/7/14.
 */

public class TimerDateInfo {
    private String mDate;
    private boolean isChecked;

    public TimerDateInfo(String date, boolean isCheck){
        mDate =date;
        isChecked = isCheck;
    }

    public void setDate(String date){
        mDate = date;
    }

    public void setChecked(boolean isCheck){
        isChecked = isCheck;
    }

    public String getDate(){
        return this.mDate;
    }

    public boolean getChecked(){
        return isChecked;
    }
}
