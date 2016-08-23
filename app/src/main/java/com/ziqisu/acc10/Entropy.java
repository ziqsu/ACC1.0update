package com.ziqisu.acc10;
import java.lang.*;
import java.util.ArrayList;

/**
 * Created by ziqisu on 5/31/16.
 */
import android.util.Log;
/*
calculate the entropy of the full forward fft data.
both function input is raw data which length should be 512
matlab code for this function:

function [ h ] = f_entropy( frame )

N = length(frame);
fftdata = abs(fft(frame));
r = fftdata(2:end)'; %/sum(fftdata(2:end));
p = hist(r, N-1);
p(p == 0) = [];
p = p ./ (N-1);
h = -sum(p.*log2(p));

end

or function:

function [ h ] = f_entropy( frame )

N = length(frame);
fftdata = abs(fft(frame));
r = fftdata(2:end)'; %/sum(fftdata(2:end));

p = qhist(r, N-1);
p(p == 0) = [];
p = p ./ (N-1);
h = -sum(p.*log2(p));

end

function h = qhist(r, N)
h = zeros(N, 1);
m = min(r);
M = max(r);
step = abs(M - m) / N;
for k = 1:N
   ix = 1 + floor((r(k) - m) / step);
   h(ix) = h(ix) + 1;
end
end
 */
public class Entropy {
    static double EntropySum;
    public static double computeEntropy(float[] S){
        EntropySum=0;
        int a;
        int b;
        Double c;
        float step;
        float min =S[1];
        float max =S[1];
        /*
        calculate min and max in the fft data, note that the first data point
        does not include in calculating entropy. Because the first data point
        is the sum of of all the other fft data in the same array.
         */
        for(int i=1;i<S.length;i++){
            if(S[i]>max){
                max = S[i];
            }
            if(S[i]<min){
                min=S[i];
            }
        }
        /*
        get binwidth of the array
         */
        step=Math.abs(max-min)/511;
        int[] alist= new int[511];
        ArrayList<Double> finalList = new ArrayList<Double>();
        for(int i=0;i<511;i++){
            alist[i]=0;
        }
        double[] list = new double[512];

        for(int i=1;i<S.length;i++){
            //c=Math.ceil((2*(S[i]-min)/step-1)/2);
            //get the index number of each data point
            c=Math.floor((S[i]-min)/step);
            a=c.intValue();

            if(a>=510){
                alist[510]=alist[510]+1;
            }else if(a<=0){
                alist[0]=alist[0]+1;
            }else{
                alist[a]=alist[a]+1;
            }


        }
        /*createList(min,max,list);
        for(int i=1;i<S.length;i++){
            a=binarySearch(list,S[i],step);
            alist[a]++;
        }*/


        /*StringBuilder sb = new StringBuilder();
        sb.setLength(0);
        for (int i=0;i<alist.length;i++){
            sb.append(alist[i]);
            sb.append(" ");
        }
        Log.i("list with 0: ",sb.toString());*/
        /*
        remove zero in the alist and calculate remaining number possibility
        in order to get p to calculate entropy.
         */
        for(int i=0;i<alist.length;i++){
            if(alist[i] != 0){
                Double d;
                d = (double) alist[i]/511;
                finalList.add(d);
                //Log.i(Integer.toString(i) + "th element is:", Double.toString((double) alist[i]/256) );

            }
        }
        Log.i("finallist is: ",finalList.toString());
        for(int i=0;i<finalList.size();i++){
            double p;
            p=finalList.get(i);
            //calculate entropy sum. Be aware that that entropy sum is negative.
            EntropySum= EntropySum-(  p * (  (double)Math.log( Math.abs(p) )/(double)Math.log(2) )  );

        }

        return EntropySum;

    }

    public static int binarySearch(double[] a, float b, double step){
        int index=-1;
        int high=511;
        int low =0;
        int mid =0;
        while(index ==-1){
            mid = (int) Math.floor((high+low)/2);
            if(a[mid]<b && b<=a[mid+1]){
                index = mid;
            }else if(a[mid]<b && a[mid+1]<b){
                low = mid;
            }else if(a[mid]>b){
                high = mid;
            }
        }


        return index;
    }

    public static double[] createList(double min, double max,double[] list){
        double step= (max-min)/511;
        for(int i =1; i<list.length;i++){
            list[i] = min + (i*2-1)*step/2;
        }
        list[0]=min-1;
        list[list.length-1]=max+1;

        return list;
    }
}
