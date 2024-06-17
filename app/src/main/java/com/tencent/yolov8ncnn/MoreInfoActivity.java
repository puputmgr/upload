package com.tencent.yolov8ncnn;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class MoreInfoActivity extends Activity {
    private static final int REQUEST_IMAGE_SELECT = 101;

    private Yolov8Ncnn yolov8ncnn = new Yolov8Ncnn();
    private ImageView ivSelectedImage;
    private Button btnSelectImage;
    private Button btnDetectImage;
    private TextView tvDetectionResults;

    private Bitmap selectedBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_info_activity);

        if (getActionBar() != null) {
            getActionBar().hide();
        }

        ivSelectedImage = (ImageView) findViewById(R.id.iv_selected_image);
        btnSelectImage = (Button) findViewById(R.id.btn_select_image);
        btnDetectImage = (Button) findViewById(R.id.btn_detect_image);
        tvDetectionResults = (TextView) findViewById(R.id.tv_detection_results);

        btnSelectImage.setOnClickListener(v -> openImagePicker());
        btnDetectImage.setOnClickListener(v -> detectImage());

        loadModel();
    }

    private void loadModel() {
        boolean ret_init = yolov8ncnn.loadModel(getAssets(), 0, 0);
        if (!ret_init) {
            Log.e("MoreInfoActivity", "yolov8ncnn loadModel failed");
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_SELECT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_SELECT && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                InputStream imageStream = getContentResolver().openInputStream(imageUri);
                selectedBitmap = BitmapFactory.decodeStream(imageStream);
                ivSelectedImage.setImageBitmap(selectedBitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Unable to open image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void detectImage() {
        if (selectedBitmap != null) {
            String detectionResults = yolov8ncnn.detect(selectedBitmap);
            if (detectionResults != null) {
                // Menampilkan hasil deteksi pada TextView
                tvDetectionResults.setText(detectionResults);
                Toast.makeText(this, "Detection successful", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Detection failed", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please select an image first", Toast.LENGTH_SHORT).show();
        }
    }
}
