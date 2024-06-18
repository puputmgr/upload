package com.tencent.yolov8ncnn;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
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
import java.util.Arrays;
import java.util.Locale;

public class ImageDetectionActivity extends Activity {
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
        setContentView(R.layout.image_detection_activity);

        if (getActionBar() != null) {
            getActionBar().hide();
        }

        ivSelectedImage = (ImageView) findViewById(R.id.iv_selected_image);
        btnSelectImage = (Button) findViewById(R.id.btn_select_image);
        btnDetectImage = (Button) findViewById(R.id.btn_detect_image);
        tvDetectionResults = (TextView) findViewById(R.id.tv_detection_results);

        btnSelectImage.setOnClickListener(v -> openImagePicker());
        btnDetectImage.setOnClickListener(v -> handleDetectImage());

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

//    private void detectImage() {
//        if (selectedBitmap != null) {
//            String detectionResults = yolov8ncnn.detect(selectedBitmap);
//            if (detectionResults != null) {
//                // Menampilkan hasil deteksi pada TextView
//                tvDetectionResults.setText(detectionResults);
//                Toast.makeText(this, "Detection successful", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(this, "Detection failed", Toast.LENGTH_SHORT).show();
//            }
//        } else {
//            Toast.makeText(this, "Please select an image first", Toast.LENGTH_SHORT).show();
//        }
//    }

    private void handleDetectImage() {
        if (selectedBitmap == null) {
            Toast.makeText(this, "Please select an image first", Toast.LENGTH_SHORT).show();
            return;
        }

        float[] results = yolov8ncnn.detectFromBitmap(selectedBitmap);
        Log.d("ImageDetectionActivity", "RESULTS = " + Arrays.toString(results));

        if (results.length == 0) {
            Toast.makeText(this, "Detection failed", Toast.LENGTH_SHORT).show();
            return;
        }

        int numObjects = results.length / 6;

        Bitmap mutableBitmap = selectedBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(mutableBitmap);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);

        Paint textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(50);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        textPaint.setStyle(Paint.Style.FILL);

        Paint bgPaint = new Paint();
        bgPaint.setColor(Color.RED);
        bgPaint.setStyle(Paint.Style.FILL);


        int padding = 20;

        for (int i = 0; i < numObjects; i++) {
            int label = (int) results[i * 6];
            float confidence = results[i * 6 + 1];
            float x = results[i * 6 + 2];
            float y = results[i * 6 + 3];
            float width = results[i * 6 + 4];
            float height = results[i * 6 + 5];

            canvas.drawRect(x, y, x + width, y + height, paint);

            String labelText = String.format(Locale.US, "%s %.2f%%", getLabelText(label), confidence * 100);
            Rect textBounds = new Rect();
            textPaint.getTextBounds(labelText, 0, labelText.length(), textBounds);

            float textX = x + padding;

            canvas.drawRect(textX - padding, y - textBounds.height() - 2 * padding, textX + textBounds.width() + padding, y, bgPaint);

            canvas.drawText(labelText, textX, y - padding, textPaint);
            tvDetectionResults.setText(getString(R.string.txt_result_detect, labelText));
            Toast.makeText(this, "Detection successful", Toast.LENGTH_SHORT).show();
        }

        ivSelectedImage.setImageBitmap(mutableBitmap);
    }

    private String getLabelText(int label) {
        String[] classNames = {
                "A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"
        };
        if (label >= 0 && label < classNames.length) {
            return classNames[label];
        } else {
            return "Unknown";
        }
    }
}
