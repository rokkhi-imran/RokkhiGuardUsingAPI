package com.rokkhi.rokkhiguard;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.rokkhi.rokkhiguard.Model.Guards;
import com.rokkhi.rokkhiguard.Utils.UniversalImageLoader;

import de.hdodenhof.circleimageview.CircleImageView;

public class GuardQRCodeActivity extends AppCompatActivity {

    protected CircleImageView propicGuardImage;
    protected TextView guardNameTV;
    protected ImageView qrCodeImageView;
    protected ProgressBar progressbarQR;
    Guards guards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_guard_qrcode);
        initView();

        //get data from intent
         guards = (Guards) getIntent().getSerializableExtra("GuardInfo");

        UniversalImageLoader.setImage(guards.getG_pic(), propicGuardImage, null, "");
        guardNameTV.setText(guards.getG_name());


        new Handler().postDelayed(new Runnable() {
            // Using handler with postDelayed called runnable run method
            @Override
            public void run() {

                Bitmap bitmap = null;
                try {
                    bitmap = TextToImageEncode(guards.getBuild_id() + guards.getG_uid());
                } catch (WriterException e) {
                    e.printStackTrace();
                }

                qrCodeImageView.setImageBitmap(bitmap);
                progressbarQR.setVisibility(View.GONE);

            }
        }, 1*1000);


    }



    private void initView() {
        propicGuardImage = (CircleImageView) findViewById(R.id.propic_guard_image);
        guardNameTV = (TextView) findViewById(R.id.guardNameTV);
        qrCodeImageView = (ImageView) findViewById(R.id.qrCodeImageView);
        progressbarQR = (ProgressBar) findViewById(R.id.progressbarQR);
    }


    private Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    500, 500, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.black) : getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }
}
