package com.example.android.qrgenerator;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    Button btnGenerate;
    Button btnSave;
    private static final String REMINDER_JOB_TAG = "task_reminder_tag";
    static ImageView QRImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.edt_value);
        btnGenerate = findViewById(R.id.start);
        btnSave = findViewById(R.id.save);
        QRImage = findViewById(R.id.QR_Image);
        final Driver driver = new GooglePlayDriver(this);
        final FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Job constraintReminderJob = dispatcher.newJobBuilder()
                        .setService(Dispatcher.class)
                        .setLifetime(Lifetime.FOREVER)
                        .setRecurring(true)
                        .setTag(REMINDER_JOB_TAG)
                        .setTrigger(Trigger.executionWindow(
                                10,
                                30))
                        .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                        .setReplaceCurrent(false)
                        .setConstraints(Constraint.ON_ANY_NETWORK)
                        .build();
                dispatcher.mustSchedule(constraintReminderJob);
            }
        });
        JobDispatcher.scheduleUpdateReminder(this);
    }

   public static void updateUI(Bitmap b) {
       QRImage.setImageBitmap(b);
    }

}
