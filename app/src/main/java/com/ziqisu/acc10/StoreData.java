package com.ziqisu.acc10;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.lang.Float;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Environment;
import android.util.Log;
import android.widget.Switch;


/**
 * Created by ziqisu on 5/18/16.
 */


public class StoreData extends Thread implements SensorEventListener {
    //get private blocking queue to store data, time and activity type
    final static BlockingQueue<Data> queue = new ArrayBlockingQueue<Data>(100);
    final static BlockingQueue<Data> objects = new ArrayBlockingQueue<>(100);
    protected String activity = "";
    SampleData s;
    static float[] sensorValue = new float[3];


    //create a method to add sensor data x,y,z, time and activity type into queue
    public static void enqueue(float[] value, String activity){
        try {
            Data data;
            //if object is empty, we get value and activity from input
            if (objects.isEmpty()) {
                data = new Data(System.currentTimeMillis(), value, activity);
            } else {
                //if object is not empty, we take data from object
                data = objects.take();
                data.time = System.currentTimeMillis();
                data.values = value;
                data.activity = activity;
            }
            queue.put(data);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    
    //this method will create directory, file and store data into the file
    @Override
    public void run(){
        String state;
        state = Environment.getExternalStorageState();

        // to check whether or not we have external storage
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // get the directory and create a folder named AccData
            File Root = Environment.getExternalStorageDirectory();
            File Dir = new File(Root.getAbsolutePath() + "/AccData");
            // if the folder does not exist, we create the folder
            if (!Dir.exists()) {
                Dir.mkdir();
            }
            // create file name according to data and time
            DateFormat df = new SimpleDateFormat("ddMMyyyy,HH:mm");
            String date = df.format(Calendar.getInstance().getTime());
            date = date + ".txt";
            File file = new File(Dir, date);
            try{
                DataOutputStream steam = new DataOutputStream(new FileOutputStream(file));
                final StringBuilder sb = new StringBuilder();
                while(MainActivity.start){
                    //use stringbuilder to create a line of data
                    sb.setLength(0);
                    Data data = queue.take();
                    sb.append(data.time);
                    sb.append(";ACC;");
                    for (int i = 0; i < data.values.length; i++) {
                        sb.append(data.values[i]);

                        if (i < data.values.length - 1) sb.append(";");
                    }
                    sb.append(";");
                    sb.append(data.activity);
                    sb.append("\n");
                    //write one line of data into file
                    steam.writeBytes(sb.toString());
                    objects.put(data);
                }
                //close file
                steam.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        //if we press start, we will collect the accelerometer data and activity type
        //float[] sensorValue = new float[3];
        sensorValue = event.values;
        StoreData.enqueue(sensorValue,activity);
        FFT.addElement(sensorValue,activity);
        //s=new SampleData();


        if (FFT.SampleData.num == 0 && FFT.SampleData1.num == 0 &&
                FFT.SampleData2.num == 0 ) {
            FFT.Switch = 0;
            FFT.SampleData.activity=activity;
            //FFT.addElement(event.values);
        }else if (FFT.SampleData.num == 256 && FFT.SampleData1.num == 0 ) {
            //FFT.Switch = 1;
            //FFT.SampleData2.num=0;
            if(FFT.SampleData2.num==512){
                //s=FFT.SampleData2;
                try {
                    s=FFT.SampleData2;
                    Log.e("switch1:","SampleData2 put in to blockingqueue+ "+s.num);
                    FFT.blockingQueue.put(s);
                    FFT.SampleData2.num=0;
                    //FFT.SampleData2.computingdata.clear();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //FFT.SampleData2.num=0;
            }
            FFT.SampleData1.activity=activity;
            FFT.Switch = 1;

            //FFT.SampleData1.activity=activity;
            //FFT.addElement(event.values);
        }else if (FFT.SampleData.num == 512 && FFT.SampleData1.num == 256 ) {
            try{
                s = FFT.SampleData;
                Log.e("switch2:","SampleData put in to blockingqueue"+s.num);
                FFT.blockingQueue.put(s);
                //FFT.SampleData.computingdata.clear();
                FFT.SampleData.num=0;
                FFT.SampleData2.activity=activity;


            }catch(InterruptedException e){
                e.printStackTrace();
            }
            FFT.Switch = 2;
            //FFT.addElement(event.values);
        }else if (FFT.SampleData1.num== 512 && FFT.SampleData2.num == 256 ) {
            try{
                s = FFT.SampleData1;
                Log.e("switch3:","SampleData1 put in to blockingqueue + "+s.num);
                FFT.blockingQueue.put(s);
                FFT.SampleData1.num = 0;
                //FFT.SampleData1.computingdata.clear();
                FFT.SampleData.activity=activity;

            }catch(InterruptedException e){
                e.printStackTrace();
            }
            FFT.Switch = 3;
            //FFT.SampleData1.num = 0;
            //FFT.addElement(event.values);
        }


        //FFT.addElement(sensorValue,activity);





    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}

