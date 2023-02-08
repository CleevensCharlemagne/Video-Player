package com.example.videoplayer;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.SharedPreferencesKt;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AllowAccessActivity extends AppCompatActivity {

    public static final int STORAGE_PERMISSION = 1;
    public static final int REQUEST_PERMISSION_SETTING = 12;
    Button allow_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allow_access);

        allow_btn = findViewById(R.id.allow_access);

        SharedPreferences preferences = getSharedPreferences("AllowAccess", MODE_PRIVATE);

        String value = preferences.getString("Allow", "");
        if (value.equals("OK")) {
            Intent intent = new Intent(AllowAccessActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("Allow", "OK");
            editor.apply();
        }

        allow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Checking if user granted permission or not
                if (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // If user granted permission
                    Intent intent = new Intent(AllowAccessActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // If user didn't grant permission
                    ActivityCompat.requestPermissions(AllowAccessActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION);
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    boolean showRationale = shouldShowRequestPermissionRationale(permission);
                    // If user clicked on never ask again
                    if (!showRationale) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(this);
                        alert.setTitle("Allow Permission")
                                .setMessage("For playing videos with this App, you must allow access to all videos files on your device."
                                        + "\n\n" + "Now folow the steps below" + "\n\n"
                                        + "Open settings form the button bellow" + "\n"
                                        + "Click on Permissions" + "\n"
                                        + "Allow access for storage")
                                .setPositiveButton("Open settings", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //Send user to phone's settings in order to grant or deny the access.
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                                        intent.setData(uri);
                                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                                    }
                                }).create().show();

                    }
                    // User just clicked on denied
                    else {
                        ActivityCompat.requestPermissions(AllowAccessActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION);
                    }
                } else {
                    Intent intent = new Intent(AllowAccessActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();
        //If user grant the permission in the settings but not in the dialog, after clicking back to the app, drive the to the next activity
        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            Intent intent = new Intent(AllowAccessActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}