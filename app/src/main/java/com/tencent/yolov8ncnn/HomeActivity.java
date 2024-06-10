package com.tencent.yolov8ncnn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.cardview.widget.CardView;

public class HomeActivity extends Activity {

    private CardView cvCameraDetection;
    private CardView cvAbjad;
    private CardView cvCallingName;
    private CardView cvKosakata;
    private CardView cvMoreInfo;
    private CardView cvAbout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        if (getActionBar() != null) {
            getActionBar().hide();
        }

        cvCameraDetection = (CardView) findViewById(R.id.cv_camera_detection);
        cvAbjad = (CardView) findViewById(R.id.cv_abjad);
        cvCallingName = (CardView) findViewById(R.id.cv_calling_name);
        cvKosakata = (CardView) findViewById(R.id.cv_kosakata);
        cvMoreInfo = (CardView) findViewById(R.id.cv_more_info);
        cvAbout = (CardView) findViewById(R.id.cv_about);

        cvCameraDetection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, DetectorActivity.class);
                startActivity(intent);
            }
        });

        cvAbjad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AbjadBisindoActivity.class);
                startActivity(intent);
            }
        });

        cvCallingName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, CallingNameActivity.class);
                startActivity(intent);
            }
        });

        cvKosakata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, KosakataActivity.class);
                startActivity(intent);
            }
        });

        cvMoreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MoreInfoActivity.class);
                startActivity(intent);
            }
        });

        cvAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });
    }
}
