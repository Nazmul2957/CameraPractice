package com.example.camerapractice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Picture;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class MainActivity extends AppCompatActivity {

    public Camera mCamera;
    public CameraPreview mPreview;

    FrameLayout frameLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button_capture);
        frameLayout = findViewById(R.id.camera_preview);

        permission();

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //captureimage();
            }
        }, 0, 1000);

        mCamera = Camera.open();
        mPreview = new CameraPreview(this, mCamera);
        frameLayout.addView(mPreview);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             captureimage(v);

            }
        });

    }


    Camera.PictureCallback mpicturecallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File picturefile = getoutputmediafile();
            if (picturefile == null) {
                return;
            } else {
                try {
                    FileOutputStream fos = new FileOutputStream(picturefile);
                    fos.write(data);
                    fos.close();

                    mCamera.startPreview();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private File getoutputmediafile() {
            String state = Environment.getExternalStorageState();
            if (!state.equals(Environment.MEDIA_MOUNTED)) {
                return null;

            } else {
                File folder_uri = new File(Environment.getExternalStorageDirectory() + File.separator + "GUI");
                if (!folder_uri.exists()) {
                    folder_uri.mkdirs();
                }
                File outputfile = new File(folder_uri, "temp.jpge");
                return outputfile;
            }
        }
    };

    public void captureimage(View v) {
        if (mCamera != null) {
            mCamera.takePicture(null, null, mpicturecallback);
        }

    }

    public void permission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CAMERA}, 101);
        }

    }
}

