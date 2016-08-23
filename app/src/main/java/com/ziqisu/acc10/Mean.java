package com.ziqisu.acc10;

/**
 * Created by ziqisu on 5/31/16.
 */
public class Mean {
    /*
    calculate mean of the data
     */

    static float Meansum;
    public static float computeMean(float[] S){
        Meansum=0f;
        for(int i=0; i<S.length;i++){
            Meansum=Meansum+S[i];
        }
        return Meansum/S.length;
    }
}
