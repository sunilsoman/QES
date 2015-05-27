package com.idroid.quickeventscheduler;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

 public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder sHolder;
    private Camera previewCamera;
    private Camera.Parameters cameraParameters;
    private byte[] imageBuffer;

    public CameraPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public CameraPreview(Context context) {
        super(context);
        initialize();
    }

    public void initialize() {
        //surface holder to get notified when it is installed created and destroyed
        sHolder = getHolder();
        sHolder.addCallback(this);
        sHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }


    public void surfaceCreated(SurfaceHolder holder) {
        
		//open camera and draw
        try {
            previewCamera = getCameraInstance();
            cameraParameters = previewCamera.getParameters();
        }
        catch (RuntimeException exception) {
            
            Toast.makeText(getContext(), "Camera broken 1",Toast.LENGTH_LONG).show();
            
        }

        try {
            previewCamera.setPreviewDisplay(holder);
            updateImageBufferSize();
            previewCamera.addCallbackBuffer(imageBuffer);
            previewCamera.setPreviewCallbackWithBuffer(new PreviewCallback() {
                public synchronized void onPreviewFrame(byte[] data, Camera c) {

                    if (previewCamera != null) { 
                        previewCamera.addCallbackBuffer(imageBuffer);
                    }
                }
            });
        } catch (Exception exception) {
            
            releaseCamera();
            
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {

        previewCamera.stopPreview();
        releaseCamera();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {

        cameraParameters.setColorEffect("mono");
        previewCamera.setParameters(cameraParameters);

        updateImageBufferSize();
        previewCamera.startPreview();
    }

    public Parameters getCameraParameters(){
        return previewCamera.getParameters();
    }

    public void setCameraFocus(AutoFocusCallback autoFocus){
       // if(previewCamera!=null) {
            if (previewCamera.getParameters().getFocusMode().equals(previewCamera.getParameters().FOCUS_MODE_AUTO) ||
                    previewCamera.getParameters().getFocusMode().equals(previewCamera.getParameters().FOCUS_MODE_MACRO)) {
                previewCamera.autoFocus(autoFocus);
         //   }
        }
    }

    public static Camera getCameraInstance() {
        Camera camera = null;
        try {
            camera = Camera.open(); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return camera; 
    }

    private void releaseCamera() {
        if (previewCamera != null) {
            previewCamera.release();        
            previewCamera = null;
        }
    }

	//it returns the cropped bitmap iamge
    public Bitmap getCropImage(int x, int y, int width, int height) {
        System.gc();
        Bitmap bImage = null;
        Camera.Size size = cameraParameters.getPreviewSize();

        YuvImage imageYUV = new YuvImage(imageBuffer, ImageFormat.NV21, size.width, size.height, null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageYUV.compressToJpeg(new Rect(x, y, width, height), 100, baos); 
        bImage = BitmapFactory.decodeByteArray(baos.toByteArray(), 0, baos.size()); 
        if (bImage != null) {
            
        } else {
            
        }
        imageYUV = null;
        baos = null;
        System.gc();
        return bImage;
    }

	//buffer to store image data.
    private void updateImageBufferSize() {
        imageBuffer = null;
        System.gc();
        
        int height = previewCamera.getParameters().getPreviewSize().height;
        int width = previewCamera.getParameters().getPreviewSize().width;
        int bitsPerPixel = ImageFormat.getBitsPerPixel(previewCamera.getParameters().getPreviewFormat());
        imageBuffer = new byte[height * width * bitsPerPixel / 8];
        
    }

}