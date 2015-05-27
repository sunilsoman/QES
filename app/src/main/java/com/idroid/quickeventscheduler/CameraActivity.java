package com.idroid.quickeventscheduler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Size;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CameraActivity extends Activity implements SensorEventListener {

    private CameraPreview cameraPreview;
    public static String timeStamp;
    private BoxTouchView boxTouchView;
    Button saveButton, convertButton, discardButton, doneButton;
    TextView convertedTextTV;
    private boolean autoFocus = true;

    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private boolean mInitialized = false;
    private float acceloLastX = 0;
    private float acceloLastY = 0;
    private float acceloLastZ = 0;

    public static int screenHeight;
    public static int screenWidth;
    public static int buttonHeight;
    private boolean invalidate = false;
    public static boolean buttonListener;
    private File imageLocation;
    OCRActivity ocr;

    public static String event;

    public static String venue;

    public static String dateandtime;

    public static String ocrResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_camera);

        // the accelerometer sensor is used for autofocus
        sensorManager = (SensorManager) getSystemService(Context.
                SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.
                TYPE_ACCELEROMETER);

        // get the width and height of the window
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenHeight = displaymetrics.heightPixels;
        screenWidth = displaymetrics.widthPixels;
        
        cameraPreview = (CameraPreview) findViewById(R.id.cameraPreview);
        boxTouchView = (BoxTouchView) findViewById(R.id.boxTouchView);
        
        convertButton = (Button) findViewById(R.id.bConvert);
        
        doneButton = (Button) findViewById(R.id.bDone);
        doneButton.setEnabled(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Log.i(TAG, "onDestroy()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Log.i(TAG, "onPause()");
        sensorManager.unregisterListener(this);
        doneButton.setEnabled(false);
        //finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
		doneButton.setEnabled(false);
		
		//register accelerometer sensor 
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_UI);

		//check if all event details are gathered to create event.
       if (CameraActivity.event !=null && CameraActivity.dateandtime !=null && CameraActivity.venue!=null) {
           doneButton.setEnabled(true);
       }

	   //convert button to perform ocr
            convertButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (autoFocus) {
                        autoFocus = false;

                        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

                        Thread tGetPic = new Thread(new Runnable() {
                            public void run() {
                                Double[] ratio = getRatio();
                                int left = (int) (ratio[1] * (double) boxTouchView.getXTopLeft());
                                
                                int top = (int) (ratio[0] * (double) boxTouchView.getYTopLeft());

                                int right = (int) (ratio[1] * (double) boxTouchView.getXBottomRight());

                                int bottom = (int) (ratio[0] * (double) boxTouchView.getYBottomRight());

                                saveImage(cameraPreview.getCropImage(left, top, right, bottom));
                                autoFocus = true;
                            }
                        });
                        tGetPic.start();
                    }
                    delayInSeconds(3);

                    ocr = new OCRActivity();

                    if(timeStamp!=null) {

                        Log.d("Path", "location " + Environment.getExternalStorageDirectory() + "/QES" + "/IMG_" + timeStamp + ".jpg");
                        ocrResult = ocr.performOCR(Environment.getExternalStorageDirectory() + "/QES" + "/IMG_" + timeStamp + ".jpg");

                        if (ocrResult == null || ocrResult == "" || ocrResult == " " || ocrResult == "  ") {
                            Toast.makeText(getApplicationContext(),
                                    "Conversion failed. Please capture the information again", Toast.LENGTH_SHORT)
                                    .show();
                        } else {
							//starting popup actvity to save the event details
                            Intent intent = new Intent(CameraActivity.this, PopUpActivity.class);
                            startActivity(intent);
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),
                                "Conversion failed. Please capture the information again", Toast.LENGTH_SHORT)
                                .show();

                    }


                }
            });

		//done button to create event in the calendar
        doneButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CameraActivity.this, CalendarActivity.class);
                startActivity(intent);
                finish();
            }
        });





     //  }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //Log.i(TAG, "onRestart()");


    }

    @Override
    protected void onStop() {
        super.onStop();
        //Log.i(TAG, "onStop()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Log.i(TAG, "onStart()");
    }

    // autofocus call back feature
    private AutoFocusCallback myAutoFocusCallback = new AutoFocusCallback(){

        public void onAutoFocus(boolean autoFocusSuccess, Camera arg1) {
            autoFocus = true;
        }};

    // get ratio between screen size and pixels of the image
    public Double[] getRatio(){
        Size size = cameraPreview.getCameraParameters().getPreviewSize();
        double ratioHeight = (double)size.height/(double)screenHeight;
        double ratioWidth = (double)size.width/(double)screenWidth;
        Double[] ratio = {ratioHeight,ratioWidth};
        return ratio;
    }

    // release the application
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK){
            CameraActivity.venue= null;
            CameraActivity.dateandtime = null;
            CameraActivity.event= null;
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

   //used to set autofocus of the camera
    public void onSensorChanged(SensorEvent event) {

        if (invalidate == true){
            boxTouchView.invalidate();
            invalidate = false;
        }
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        if (!mInitialized){
            acceloLastX = x;
            acceloLastY = y;
            acceloLastZ = z;
            mInitialized = true;
        }
        float changeInX  = Math.abs(acceloLastX - x);
        float changeInY = Math.abs(acceloLastY - y);
        float changeInZ = Math.abs(acceloLastZ - z);

        if (changeInX > .5 && autoFocus){ 
            autoFocus = false;
            cameraPreview.setCameraFocus(myAutoFocusCallback);
        }
        if (changeInY > .5 && autoFocus){ 
            autoFocus = false;
            cameraPreview.setCameraFocus(myAutoFocusCallback);
        }
        if (changeInZ > .5 && autoFocus){ 
            autoFocus = false;
            cameraPreview.setCameraFocus(myAutoFocusCallback);
        }

        acceloLastX = x;
        acceloLastY = y;
        acceloLastZ = z;

    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub

    }

    /**
     * Create a File for saving an image or video
     */
    private File getOutputMediaFile(String type) {

        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "QES");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("Camera", "failed to create directory");
                return null;
            }
        }
        // Create a media file name

        File mediaFile;
        if (type.contentEquals("image")) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

	//method to save image captured
    private boolean saveImage(Bitmap b) {
        imageLocation = getOutputMediaFile("image");
        FileOutputStream fosImage = null;
        try {
            fosImage = new FileOutputStream(imageLocation);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        b.compress(CompressFormat.JPEG, 100, fosImage);
        if (b != null) {
            int h = b.getHeight();
            int w = b.getWidth();
            //Log.i(TAG, "savePhoto(): Bitmap WxH is " + w + "x" + h);
        } else {
            //Log.i(TAG, "savePhoto(): Bitmap is null..");
            return false;
        }
        return true;
    }

	//cmethod to get delay in seconds
    private void delayInSeconds(long seconds){
        try {
            Thread.currentThread().sleep(seconds * 1000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        buttonHeight = convertButton.getHeight();
        Log.d("button", "height " + buttonHeight);
    }
}