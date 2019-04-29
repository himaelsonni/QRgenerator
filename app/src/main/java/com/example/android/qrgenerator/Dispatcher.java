package com.example.android.qrgenerator;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.Random;

public class Dispatcher extends JobService {
   // ImageView QRImage;

    @Override
    public boolean onStartJob(final JobParameters job) {
        Background background=new Background()
        {
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);


            jobFinished(job,false);

            }
        };
        background.execute();


        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }

    public static class Background extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {

            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            try {
                String random = random();
                BitMatrix bitMatrix = multiFormatWriter.encode(random, BarcodeFormat.QR_CODE, 600, 600);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                MainActivity.updateUI(bitmap);
            } catch (WriterException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(10);
        char tempChar;
        for (int i = 0; i < randomLength; i++) {
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }
}
