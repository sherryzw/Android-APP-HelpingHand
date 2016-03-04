package com.example.wenzhao.helpinghand.ble.pro.Database;

import android.app.Activity;

import java.lang.Double;import java.lang.String;import java.util.ArrayList;
import java.util.List;

public class ChildInfo {
    private long id;
    private String activity;
    private Double ratio;

    public ChildInfo(){

    }

    public ChildInfo( String activity, double finalRatio){
        this.activity = activity;
        this.ratio = finalRatio;
    }

    public ChildInfo(long id, String activity,double finalRatio ){
        this.id = id;
        this.activity = activity;
        this.ratio = finalRatio;
    }
    //setter

    public void setId(long id) {
        this.id = id;
    }

    public void setActivity(String activityName) {
        this.activity = activityName;
    }

    public void setFinalRatio(double finalRatio ){
        this.ratio = finalRatio;
    }
    //getter

    public double getFinalRatio() {
        return ratio;
    }

    public long getId() {
        return id;
    }

    public String getActivity(){
        return activity;
    }

}
