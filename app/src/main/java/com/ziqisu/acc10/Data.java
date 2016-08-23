package com.ziqisu.acc10;

/**
 * Created by ziqisu on 5/18/16.
 */
 
//create class data to store accelerometer data, time and activity type
public class Data {
    protected String activity;
    protected float[] values;
    protected long time;
    
    
    public Data(long time, float[] values, String activity) {
        this.time = time;
        this.values = values;
        this.activity = activity;
    }

    public Data() {

    }

    public float[] getValues() {
        return values;
    }

    public void setValues(float[] values) {
        this.values = values;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getActivity() {
        return activity;
    }
}
