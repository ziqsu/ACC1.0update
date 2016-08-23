package com.ziqisu.acc10;

/**
 * Created by ziqisu on 5/31/16.
 */
public class Energy {
    static float EnergySum;
    public static float computeEnergy(float[] X){
        EnergySum=0f;
        for(int i=1; i<X.length/2;i++){
            EnergySum=EnergySum+X[i]*X[i];

            //EnergySum=EnergySum+X[i]*X[i];
        }
        // because the length of X is 512
        return EnergySum/512;

    }

}
