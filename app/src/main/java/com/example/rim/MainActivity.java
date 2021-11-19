package com.example.rim;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.Output;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.datatype.Duration;

public class MainActivity extends AppCompatActivity {
    ImageView disp,disp2;
    int img_no1 = 1,img_no2=2;
    String img_nm;
    TextView ind;
    Button cap,process,pic2;
    String pic1_path,pic2_path;
    Bitmap pic1,pict2;

    int[] coordinates1,coordinates2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        disp = findViewById(R.id.img);
        disp2 = findViewById(R.id.pic2_img);
        cap = findViewById(R.id.cap);
        pic2 = findViewById(R.id.pic2);
        process = findViewById(R.id.process);
        ind =findViewById(R.id.ind);

        pic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(intent.resolveActivity(getPackageManager())!=null) {
                    File photoFile=null;
                    try {
                        photoFile = create(img_no2);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(photoFile!=null)
                    {
                        Uri uri = FileProvider.getUriForFile(MainActivity.this,"com.example.rim",photoFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
                        startActivityForResult(intent,2);
                    }
                }

            }
        });

        cap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takepic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(takepic.resolveActivity((getPackageManager()))!=null) {
                    File photoFile = null;
                    try {
                        photoFile = create(img_no1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(photoFile !=null ) {
                        Uri photoUri = FileProvider.getUriForFile(MainActivity.this,"com.example.rim",photoFile);
                        takepic.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
                        startActivityForResult(takepic,1);
                    }
                }
            }
        });
        process.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Imgprocess().execute(pic1);
            }
        });

    }
    class Imgprocess extends AsyncTask<Bitmap,Bitmap,Bitmap>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ind.setVisibility(View.VISIBLE);
        }

        @Override
        protected Bitmap doInBackground(Bitmap... bitmaps) {
            if(pic1!=null) {
                pic1 = Image_Processing.imgprcss(pic1).copy(Bitmap.Config.ARGB_8888, true);
                coordinates1 = Image_Processing.coordinates();
            }
            if(pict2!=null) {
                pict2 = Image_Processing.imgprcss(pict2).copy(Bitmap.Config.ARGB_8888, true);
                coordinates2 = Image_Processing.coordinates();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
//            ind.setVisibility(View.INVISIBLE);
            Delete(pic1_path);
            save(img_no1);
            disp(disp,pic1_path);

            Delete(pic2_path);
            save(img_no2);
            disp(disp2,pic2_path);
            double i = Math.pow((coordinates1[0]-coordinates2[0]),2);
            double j = Math.pow((coordinates1[1]-coordinates2[1]),2);
            double dist = Math.sqrt(i+j);
            ind.setText("Distance(pixels):"+dist);
        }

    }
    private void save(int img_no)
    {
        File photo = null;
        try {
            photo = create(img_no);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(photo!=null) {
            try {
                FileOutputStream fos = new FileOutputStream(photo);
                if(img_no == img_no1) {
                    pic1.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                }
                if(img_no == img_no2)
                {
                    pict2.compress(Bitmap.CompressFormat.JPEG,100,fos);
                }
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private File create(int img_no) throws IOException {
        String file_name = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        img_nm = file_name;
        File strdir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File img = File.createTempFile(file_name,".jpg",strdir);
        if(img_no == 1) {
            pic1_path = img.getAbsolutePath();
        }
        if(img_no == 2)
        {
            pic2_path = img.getAbsolutePath();
        }
        return img;
    }

    private Bitmap disp(ImageView disp,String path)
    {
        Bitmap pic1;
        int wd = disp.getWidth();
        int ht = disp.getHeight();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        int imgwd = options.outWidth;
        int imght = options.outHeight;
        int sf = Math.min(imgwd/wd,imght/ht);
        options.inJustDecodeBounds = false;
        options.inSampleSize = sf;
        options.inPurgeable = true;
        pic1 = BitmapFactory.decodeFile(path,options);
        disp.setImageBitmap(pic1);
        return pic1;
    }

    private void Delete(String path)
    {
        if(path!=null) {
            File fDel = new File(path);
            Toast.makeText(getApplicationContext(), "loc:" + path, Toast.LENGTH_SHORT).show();
            if (fDel.exists()) {
                if (fDel.delete()) {
                    Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Not Deleted", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == 1)
        {
            Log.d("d","ss");
            pic1=disp(disp,pic1_path).copy(Bitmap.Config.ARGB_8888,true);
        }
        if(resultCode == RESULT_OK && requestCode == 2)
        {
            Log.d("d","ss");
            pict2 = disp(disp2,pic2_path).copy(Bitmap.Config.ARGB_8888,true);
        }
    }
}
