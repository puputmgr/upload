package com.tencent.yolov8ncnn;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

public class CallingNameActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calling_name_activity);
        if (getActionBar() != null) {
            getActionBar().hide();
        }
        ImageView ivBackButton = (ImageView) findViewById(R.id.iv_back_button);
        ivBackButton.setOnClickListener(v -> finish());
    }
}
