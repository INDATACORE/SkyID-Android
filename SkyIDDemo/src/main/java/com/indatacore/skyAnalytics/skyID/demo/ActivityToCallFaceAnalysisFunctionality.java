package com.indatacore.skyAnalytics.skyID.demo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.indatacore.skyAnalytics.skyID.DocumentAnalyzer;
import com.indatacore.skyAnalytics.skyID.FacebasedAuthenticator;
import com.indatacore.skyAnalytics.skyID.tools.Language;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class ActivityToCallFaceAnalysisFunctionality extends AppCompatActivity {

    String Token, CINFaceFile;
    private Class<?> mClss;
    int PERMISSION_ALL = 1;
    TextView textView_label, textView_1;
    private static final int CAMERA_PERMISSION = 1;
    String[] PERMISSIONS = {
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.INTERNET,
            android.Manifest.permission.ACCESS_NETWORK_STATE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_analysis);
        textView_label = findViewById(R.id.textView_label);
        textView_1 = findViewById(R.id.textView_1);
        Token = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // ClearTextViews();
    }

    public void ClearTextViews() {
        textView_label.setText("");
        textView_1.setText("");
    }


    public void launchDocOcerizationActivity(View v) {
        if (hasPermissions(this, PERMISSIONS)) {
            ClearTextViews();
            CINFaceFile = saveBitmap(getBitmapFromAssets(this, "DocumentFile.png"), "157669365934713892976772571946__DocumentFile", 80);
            Intent in = new Intent(this, FacebasedAuthenticator.class);
            startActivityForResult(in, DocumentAnalyzer.RequestCode, Token, Language.FRENCH, CINFaceFile);
        } else
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == DocumentAnalyzer.RequestCode) {
            if (resultCode == DocumentAnalyzer.RESULT_OK) {

                String StatusCode = data.getStringExtra("StatusCode");
                String StatusLabel = data.getStringExtra("StatusLabel");
                String FaceAuthenticationResult = data.getStringExtra("FaceAuthenticationResult");
                textView_label.setText("StatusCode: " + StatusCode + "\nStatusLabel: " + StatusLabel + "\n");
                textView_1.setText(("\n FaceAuthenticationResult:   " + FaceAuthenticationResult));

            } else if (resultCode == DocumentAnalyzer.RESULT_Not_OK){

                String StatusCode = data.getStringExtra("StatusCode");
                String StatusLabel = data.getStringExtra("StatusLabel");
                textView_label.setText("CodeStatus: " + StatusCode + "\nCodeLabel: " + StatusLabel + "\n");
                textView_1.setText("");

            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (mClss != null) {
                        Intent intent = new Intent(this, mClss);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(this, "Please grant camera permission to use the SkyID", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void startActivityForResult(Intent intent, int requestCode, String Token, Language language, String DocumentFile) {
        intent.putExtra("RequestCode", requestCode);
        intent.putExtra("DocumentFile", DocumentFile);
        intent.putExtra("Language", language.getIso());
        intent.putExtra("Token", Token);
        startActivityForResult(intent, requestCode);
    }

    public static Bitmap getBitmapFromAssets(Context context, String filename) {
        Bitmap bitmap;
        AssetManager asm = context.getAssets();
        try {
            InputStream is = asm.open(filename);
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap.copy(bitmap.getConfig(),true);
    }

    public static String saveBitmap(Bitmap pictureBitmap, String name, int compressQuality) {
        File file;
        try {
            String imageFileName = name + ".jpg";
            String path = Environment.getExternalStorageDirectory().toString();
            File folder = new File(path + "/SkyID");
            if (!folder.exists()) folder.mkdirs();
            file = new File(folder, imageFileName);
            OutputStream fOut = new FileOutputStream(file);
            pictureBitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            return "";
        }
        return file.getAbsolutePath();
    }


}
