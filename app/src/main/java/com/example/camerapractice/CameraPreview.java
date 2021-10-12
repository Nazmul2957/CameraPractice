package com.example.camerapractice;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.List;

import static android.content.ContentValues.TAG;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder mHolder;
    private android.hardware.Camera mCamera;

    public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        Camera.Parameters parameters = mCamera.getParameters();
        List<Camera.Size> sizes = parameters.getSupportedPictureSizes();
        Camera.Size msize=null;

        for (Camera.Size size:sizes){
            msize=size;
        }

        if (this.getResources().getConfiguration().orientation!= Configuration.ORIENTATION_LANDSCAPE)
        {
            parameters.set("orientation","portraite");
            mCamera.setDisplayOrientation(90);
            parameters.setRotation(90);
        }
        else {
            parameters.set("orientation","portraite");
            mCamera.setDisplayOrientation(0);
            parameters.setRotation(0);
        }
        parameters.setPictureSize(msize.width,msize.height);
        mCamera.setParameters(parameters);

        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        if (mHolder.getSurface() == null) {
            return;
        }
        mCamera.stopPreview();

        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e) {
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

        mCamera.stopPreview();
        mCamera.release();

    }
}
