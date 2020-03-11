package com.indatacore.skyAnalytics.skyID.demo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.indatacore.skyAnalytics.skyID.DocumentAnalyzer;
import com.indatacore.skyAnalytics.skyID.tools.Country;
import com.indatacore.skyAnalytics.skyID.tools.Language;

import java.util.Locale;

public class ActivityToCallDocumentAnalysisFunctionality extends AppCompatActivity {

    String Token;
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
        setContentView(R.layout.activity_document_analysis);
        textView_label = findViewById(R.id.textView_label);
        textView_1 = findViewById(R.id.textView_1);
        Token ="XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public void ClearTextViews() {
        textView_label.setText("");
        textView_1.setText("");
    }

    public void launchDocOcerizationActivity(View v) {

        if(hasPermissions(this, PERMISSIONS)){
            ClearTextViews();
            Intent in = new Intent(this, DocumentAnalyzer.class);
            startActivityForResult(in, DocumentAnalyzer.RequestCode, Token, Language.FRENCH, Country.MOROCCO, "0102");
        }else
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        Log.d("onActivityResult","Result1");
        if (requestCode == DocumentAnalyzer.RequestCode) {
            if(resultCode == DocumentAnalyzer.RESULT_OK){

                String StatusCode   = data.getStringExtra("StatusCode");
                String StatusLabel  = data.getStringExtra("StatusLabel");
                String RequestedFiles     = data.getStringExtra("RequestedFiles");
                String RequestedInformations     = data.getStringExtra("RequestedInformations");

                textView_label.setText(String.format(Locale.getDefault(), "StatusCode: %s \nStatusLabel: %s \n", StatusCode , StatusLabel));
                textView_1.setText(String.format(Locale.getDefault(), "RequestedInformations: %s \nRequestedFiles: %s \n", RequestedInformations , RequestedFiles));

            } else  if (resultCode == DocumentAnalyzer.RESULT_Not_OK){

                String StatusCode  = data.getStringExtra("StatusCode");
                String StatusLabel  = data.getStringExtra("StatusLabel");
                Log.d("Response","CodeStatus: "+ StatusCode+" -- StatusLabel: "+StatusLabel);

                textView_label.setText(String.format(Locale.getDefault(), "StatusCode: %s \nStatusLabel: %s \n", StatusCode , StatusLabel));
                textView_1.setText("");

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(mClss != null) {
                        Intent intent = new Intent(this, mClss);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(this, "Please grant camera permission to use the Sky Documment Analysis", Toast.LENGTH_SHORT).show();
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


    public void startActivityForResult(Intent intent, int requestCode, String token, Language language, Country country, String serviceID) {
        intent.putExtra("requestCode", requestCode);
        intent.putExtra("Language", language.toString());
        intent.putExtra("Country", country.toString());
        intent.putExtra("ServiceID", serviceID);
        intent.putExtra("Token", token);
        startActivityForResult(intent, requestCode);
    }


}
