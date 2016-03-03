package com.example.wenzhao.helpinghand.ble.pro.Database;

import java.util.ArrayList;
import java.util.List;

public class ChildInfo {
    private long id;
    private String name;
    private String weakArm;
    private String activityName;
    private boolean ableToTalk;
    private List<Double> finalRatio;

    public ChildInfo(){
        this.finalRatio = new ArrayList<Double>();
    }

    public ChildInfo(String name, String weakArm, boolean ableToTalk, String activityName){
        this.name = name;
        this.weakArm = weakArm;
        this.ableToTalk = ableToTalk;
        this.activityName = activityName;
        this.finalRatio = new ArrayList<Double>();
    }

    public ChildInfo(long id,String name, String weakArm, boolean ableToTalk, String activityName){
        this.id = id;
        this.name = name;
        this.weakArm = weakArm;
        this.ableToTalk = ableToTalk;
        this.activityName = activityName;
        this.finalRatio = new ArrayList<Double>();
    }
    //setter
    public void setAbleToTalk(boolean ableToTalk) {
        this.ableToTalk = ableToTalk;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWeakArm(String weakArm) {
        this.weakArm = weakArm;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public void setFinalRatio(){
        finalRatio = new ArrayList<Double>();
    }
    //getter
    public boolean isAbleToTalk() {
        return ableToTalk;
    }

    public List<Double> getFinalRatio() {
        return finalRatio;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getWeakArm() {
        return weakArm;
    }

    public String getActivityName() {
        return activityName;
    }
}
