package com.ziqisu.acc10;
import org.jtransforms.fft.FloatFFT_1D;
import java.util.Arrays;
/**
 * Created by ziqisu on 5/31/16.
 */
import com.ziqisu.acc10.Data;
import java.util.ArrayList;

public class SampleData {
    protected String activity;
    float[] dataX= new float[512];
    float[] dataY= new float[512];
    float[] dataZ= new float[512];
    float[] FFTXreal= new float[257];
    float[] FFTYreal = new float[257];
    float[] FFTZreal = new float[257];

    protected  float[] FFTX = new float[512];
    protected  float[] FFTY = new float[512];
    protected  float[] FFTZ = new float[512];

    protected int num =0;
    protected Data[] computingdata = new Data[512];
    protected float[] rawdata = new float[1536];
    protected long[] time = new long[512];
    protected Data data = new Data();

    //data.time =0;



    public SampleData( float[] dataX,float[] dataY,float[] dataZ,
                       String activity,
                       float[] FFTX,float[] FFTY,float[] FFTZ,
                       Data[] computingdata,float[] rawdata,long[] time) {
        Arrays.fill(dataX,0);
        Arrays.fill(dataY,0);
        Arrays.fill(dataZ,0);
        this.dataX = dataX;
        this.dataY = dataY;
        this.dataZ = dataZ;
        this.activity = activity;
        this.FFTX= FFTX;
        this.FFTY = FFTY;
        this.FFTZ = FFTZ;
        //this.computingdata = computingdata;
        data.activity="";
        Arrays.fill(data.values,0);
        data.time = 0;
        Arrays.fill(computingdata,data);
        this.rawdata = rawdata;
        this.time = time;

    }

    public SampleData() {
    }

    public float[] getdataX() {
        return dataX;
    }

    public void setdataX(float[] data) {
        this.dataX = dataX;
    }
    public void changedataX(float a, int b){
        this.dataX[b]=a;
    }
    public void changedataY(float a, int b){
        this.dataY[b]=a;
    }
    public void changedataZ(float a, int b){
        this.dataZ[b]=a;
    }
    public float[] getdataY() {
        return dataY;
    }


    public void setdataY(float[] data) {
        this.dataY = dataY;
    }
    public float[] getdataZ() {
        return dataZ;
    }

    public void setdataZ(float[] dataZ) {
        this.dataZ = dataZ;
    }


    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity){
        this.activity =activity;
    }

    public float[] getFFTX(){
        return FFTX;
    }
    public void setFFTX(float[] FFTX){
        this.FFTX=FFTX;
    }
    public float[] getFFTY(){
        return FFTY;
    }
    public void setFFTY(float[] FFTY){
        this.FFTY=FFTY;
    }
    public float[] getFFTZ(){
        return FFTZ;
    }
    public void setFFTZ(float[] FFTZ){
        this.FFTZ=FFTZ;
    }

    public void setFFTXreal(float[] FFTXreal){
        this.FFTXreal=FFTXreal;
    }
    public float[] getFFTXreal(){
        return FFTXreal;
    }
    public void setFFTYreal(float[] FFTYreal){
        this.FFTYreal=FFTYreal;
    }
    public float[] getFFTYreal(){
        return FFTYreal;
    }
    public void setFFTZreal(float[] FFTZreal){
        this.FFTZreal=FFTZreal;
    }
    public float[] getFFTZreal(){
        return FFTZreal;
    }

}

