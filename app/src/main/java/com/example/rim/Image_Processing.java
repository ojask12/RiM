package com.example.rim;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

public class Image_Processing {
    static int i1,j1,i2,j2;
    public static Bitmap imgprcss(Bitmap pic)
    {
        Bitmap testpic = pic.copy(Bitmap.Config.ARGB_8888,true);
        Bitmap crp = null;
        int ht = testpic.getHeight();
        int wd = testpic.getWidth();
        i2=0;
        j2=0;
        i1=wd;
        j1=ht;

        int r=180;
        int g=80;
        int b=20;
        for(int i =0 ;i < wd;i++)
        {
            for(int j = 0;j< ht;j++)
            {
                int ctr =0 ;
                int pix = testpic.getPixel(i,j);
                if((Color.red(pix)<r)||(Color.blue(pix)>g || Color.green(pix)>b))
                {
                    testpic.setPixel(i,j,Color.rgb(0,0,0));
                }
                else {
                    ctr++;
                    if(i2<i)
                    {
                        Log.d("mx","x2:"+i2);
                        i2=i;
                    }
                    if(i1>i)
                    {
                        Log.d("mx","x1:"+i1);
                        i1=i;
                    }
                    if(j2<j)
                    {
                        j2=j;
                    }
                    if(j1>j)
                    {
                        j1=j;
                    }
                }
                if(ctr==1)
                {
                    Log.d("pix","r:"+Color.red(pix)+"g:"+Color.green(pix)+"b:"+Color.blue(pix)+"i:"+i+"j:"+j);
                }
            }
        }

        if(((i2-i1)>0) && ((j2-j1)>0))
        {
            Log.d("mx","i2-i1:"+(i2-i1));


            crp = Bitmap.createBitmap(i2-i1,j2-j1,Bitmap.Config.ARGB_8888);
            for(int i = i1;i<i2;i++)
            {

                for(int j=j1;j<j2;j++)
                {
                    int pix1 = pic.getPixel(i,j);
                    crp.setPixel(i-i1,j-j1,Color.rgb(Color.red(pix1),Color.green(pix1),Color.blue(pix1)));

                }
            }
            int i =  (i2-i1)/2;
            int j =  (j2-j1)/2;
            for(int k = 0;k<(i2-i1);k++)
            {
                crp.setPixel(k,j,Color.rgb(120,120,120));
            }
            for(int k = 0;k<(j2-j1);k++)
            {
                crp.setPixel(i,k,Color.rgb(120,120,120));
            }

            return crp;
        }


        return testpic;
    }

    public static int[] coordinates()
    {
        int i = i1 + (i2-i1)/2;
        int j = j1 + (j2-j1)/2;
        return new int[]{i,j};
    }
}
