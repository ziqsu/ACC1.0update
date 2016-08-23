package com.ziqisu.acc10;

import android.os.Environment;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by ziqisu on 6/16/16.
 */
public class StoreRawData extends Thread{
    /*protected SampleData s;
    protected Data[] datalist;
    protected SampleData sampledata;
    protected Data d;*/

    public void run(){
        String state;
        state = Environment.getExternalStorageState();


        // to check whether or not we have external storage
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // get the directory and create a folder named AccData
            File Root = Environment.getExternalStorageDirectory();
            File Dir = new File(Root.getAbsolutePath() + "/RawData");
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
                DataOutputStream steam = new DataOutputStream(new FileOutputStream(file));
                final StringBuilder sb = new StringBuilder();
                while (MainActivity.start) {
                    //use stringbuilder to create a line of data
                    SampleData sampledata = FFT.RawData.take();
                    //Data[] datalist = sampledata.computingdata;
                    sb.setLength(0);
                    sb.append("start a new sample\n");
                    //sb.append(datalist);
                    sb.append("the activity is: "+sampledata.activity+"\n");
                    sb.append("time: \n");
                    for (int i = 0; i < 512; i++) {
                        sb.append(sampledata.time[i]);
                        sb.append(";");
                    }
                    sb.append("\n");
                    steam.writeBytes(sb.toString());
                    sb.setLength(0);
                    sb.append("dataX: \n");
                    for(int i = 0;i<512;i++){
                        sb.append(sampledata.dataX[i]);
                        sb.append(";");
                    }
                    sb.append("\n");
                    steam.writeBytes(sb.toString());
                    sb.setLength(0);
                    sb.append("dataY: \n");
                    for(int i = 0;i<512;i++){
                        sb.append(sampledata.dataY[i]);
                        sb.append(";");
                    }
                    sb.append("\n");
                    steam.writeBytes(sb.toString());
                    sb.setLength(0);
                    sb.append("dataZ: \n");
                    for(int i = 0;i<512;i++){
                        sb.append(sampledata.dataZ[i]);
                        sb.append(";");
                    }
                    sb.append("\n");
                    steam.writeBytes(sb.toString());


                        /*sb.append(i);
                        sb.append(": ");
                        //d = datalist[i];
                        sb.append(sampledata.time[i]);
                        sb.append(";ACC;");
                        sb.append(sampledata.rawdata[3*i]);
                        sb.append(";");
                        sb.append(sampledata.rawdata[3*i+1]);
                        sb.append(";");
                        sb.append(sampledata.rawdata[3*i+2]);
                        sb.append(";the raw data is:");
                        sb.append(sampledata.rawdata);

                        //sb.append(datalist[i].getValues()[0]);
                        for (int j = 0; j < 3; j++) {
                            sb.append(datalist[i].getValues()[j]);
                            sb.append(";");
                        }
                        //sb.append(";");
                        sb.append(sampledata.activity);
                        sb.append("\n");
                        //write one line of data into file
                        steam.writeBytes(sb.toString());*/
                    //}

                }
                steam.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }
    }

}
