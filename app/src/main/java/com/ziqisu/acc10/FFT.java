package com.ziqisu.acc10;

/**
 * Created by ziqisu on 5/26/16.
 */
import org.jtransforms.fft.FloatFFT_1D;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.lang.InterruptedException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.Arrays;


import android.os.Environment;
import android.util.Log;



class FFT extends Thread{

    /*
    these three sampledata are used to record 512 sensor data repeatly

     */
    protected static SampleData SampleData=new SampleData();
    protected static SampleData SampleData1=new SampleData();
    protected static SampleData SampleData2=new SampleData();
    protected static Data d = new Data();

    // these two blockingqueue are used to
    final static BlockingQueue<SampleData> blockingQueue = new ArrayBlockingQueue<SampleData>(5);
    final static BlockingQueue<SampleData> RawData = new ArrayBlockingQueue<SampleData>(10);

    public static int Switch = 0;
    protected static int count =0;


    public float[] computeFFT (float[] DATA){
        FloatFFT_1D fft = new FloatFFT_1D(DATA.length);
        float[] FFT = new float[DATA.length*2];
        //float[] FFTfull = new float[DATA.length*2];
        for(int i =0; i<DATA.length;i++){
            FFT[i]=DATA[i];
        }
        fft.realForwardFull(FFT);

        return FFT;

    }



    public static void Addition (float[] S,SampleData d, int a){
        //d= new Data();
        try{
            d.dataX[a] = S[0];
            d.dataY[a] = S[1];
            d.dataZ[a] = S[2];
            d.rawdata[3*a]=S[0];
            d.rawdata[3*a+1]=S[1];
            d.rawdata[3*a+2]=S[2];
            d.time[a]=System.currentTimeMillis();


        }catch(NullPointerException e){
            e.printStackTrace();
        }catch(ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
        }

    }
    public static float[] getAbsolute(float[] S){
        float[] Re = new float[S.length/2];
        float[] Im = new float[S.length/2];
        float[] Abs = new float[S.length/2];
        Log.i("S length is: ",Integer.toString(S.length));
        for(int k=0;k<S.length/2;k++){
            Re[k]=S[2*k];
            Im[k]=S[2*k+1];
        }
        /*Im[0]=0;
        Im[S.length/2]=0;
        Re[0]=S[0];
        Re[S.length/2]=S[1];*/
        for(int j=0;j<Abs.length;j++){
            Abs[j]= (float) Math.sqrt( Math.pow(Re[j],2)+Math.pow(Im[j],2));
        }
        return Abs;
    }

    public static void addElement(float[] s,String activity){
        float[] S = s;
        //d = new Data();
        if(Switch ==0){
            Addition(S,SampleData,SampleData.num);

            SampleData.num++;

        }if(Switch==1){
            Addition(S,SampleData,SampleData.num);
            Addition(S,SampleData1,SampleData1.num);

            SampleData.num++;
            SampleData1.num++;

        }if(Switch==2) {

            Addition(S,SampleData1,SampleData1.num);
            Addition(S,SampleData2,SampleData2.num);

            SampleData1.num++;
            SampleData2.num++;

        }if(Switch==3){
            Addition(S,SampleData2,SampleData2.num);
            Addition(S,SampleData,SampleData.num);

            SampleData.num++;
            SampleData2.num++;

        }
    }

    public void run(){
        String state;
        state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)) {
            File Root = Environment.getExternalStorageDirectory();
            File Dir = new File(Root.getAbsolutePath() + "/DATA+Feature Computation");
            // if the folder does not exist, we create the folder
            if (!Dir.exists()) {
                Dir.mkdir();
            }
            // create file name according to data and time
            DateFormat df = new SimpleDateFormat("ddMMyyyy,HH:mm");
            String date = df.format(Calendar.getInstance().getTime());
            date = date + ".txt";
            File file = new File(Dir, date);
            try {
                DataOutputStream STEAM = new DataOutputStream(new FileOutputStream(file));
                final StringBuilder sb = new StringBuilder();
                /*sb.append("@relation activityrecognition\n \n" +
                        "@attribute MeanX numeric\n@attribute EnergyX numeric\n@attribute EntropyX numeric\n" +
                        "@attribute CorrletiondataXY numeric\n@attribute CorrelationFFTXY numeric\n"+
                        "@attribute MeanY numeric\n@attribute EnergyY numeric\n@attribute EntropyY numeric\n" +
                        "@attribute CorrletiondataYZ numeric\n@attribute CorrelationFFTYZ numeric\n"+
                        "@attribute MeanZ numeric\n@attribute EnergyZ numeric\n@attribute EntropyZ numeric\n"+"" +
                        "@attribute CorrletiondataXZ numeric\n@attribute CorrelationFFTXZ numeric\n"+
                        "@attribute class {Run,Drive,Sit,Climb Stairs,Walk}\n\n@data\n\n");
                STEAM.writeBytes(sb.toString());*/

                SampleData computedata;
                SampleData rawdata;
                while (MainActivity.start) {

                    computedata = blockingQueue.take();

                    while(computedata.activity==null ||computedata.activity =="noactivity" ||computedata.activity.length()<2 ||count<5){
                        computedata=blockingQueue.take();
                        count++;
                    }

                    RawData.put(computedata);
                    sb.setLength(0);
                    computedata.FFTX = computeFFT(computedata.dataX);
                    computedata.FFTY = computeFFT(computedata.dataY);
                    computedata.FFTZ = computeFFT(computedata.dataZ);
                    computedata.FFTXreal = getAbsolute(computedata.FFTX);
                    computedata.FFTYreal = getAbsolute(computedata.FFTY);
                    computedata.FFTZreal = getAbsolute(computedata.FFTZ);
                    sb.append("\n FFTXreal:\n");
                    for(int j =0;j<computedata.FFTXreal.length;j++){
                        //computedata.FFTXreal[j] =0.01f*computedata.FFTXreal[j];
                        sb.append(computedata.FFTXreal[j]);
                        sb.append(",");
                    }
                    sb.append("\n FFTYreal:\n");
                    for(int j =0;j<computedata.FFTYreal.length;j++){
                        //computedata.FFTYreal[j] =0.01f*computedata.FFTYreal[j];
                        sb.append(computedata.FFTYreal[j]);
                        sb.append(",");
                    }
                    sb.append("\n FFTZreal:\n");
                    for(int j =0;j<computedata.FFTZreal.length;j++){
                        //computedata.FFTZreal[j] =0.01f*computedata.FFTZreal[j];
                        sb.append(computedata.FFTZreal[j]);
                        sb.append(",");
                    }
                    sb.append("\n Feature: \n");


                    sb.append(Mean.computeMean(computedata.dataX));
                    sb.append(",");
                    sb.append(Energy.computeEnergy(computedata.FFTXreal));
                    sb.append(",");
                    sb.append(Entropy.computeEntropy(computedata.FFTXreal));
                    sb.append(",");
                    sb.append(Correlation.computeCorrelation(computedata.dataX, computedata.dataY));
                    sb.append(",");
                    sb.append(Correlation.computeCorrelation(computedata.FFTXreal,computedata.FFTYreal));
                    sb.append(",");
                    sb.append(Mean.computeMean(computedata.dataY));
                    sb.append(",");
                    sb.append( Energy.computeEnergy(computedata.FFTYreal));
                    sb.append(",");
                    sb.append(Entropy.computeEntropy(computedata.FFTYreal));
                    sb.append(",");
                    sb.append(Correlation.computeCorrelation(computedata.dataY, computedata.dataZ));
                    sb.append(",");
                    sb.append(Correlation.computeCorrelation(computedata.FFTYreal,computedata.FFTZreal));
                    sb.append(",");
                    sb.append(Mean.computeMean(computedata.dataZ));
                    sb.append(",");
                    sb.append(Energy.computeEnergy(computedata.FFTZreal));
                    sb.append(",");
                    sb.append(Entropy.computeEntropy(computedata.FFTZreal));
                    sb.append(",");
                    sb.append(Correlation.computeCorrelation(computedata.dataX, computedata.dataZ));
                    sb.append(",");
                    sb.append(Correlation.computeCorrelation(computedata.FFTXreal,computedata.FFTZreal));
                    sb.append(",");

                    sb.append(computedata.activity);

                    sb.append("\n");
                    STEAM.writeBytes(sb.toString());
                }
                STEAM.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }



}

