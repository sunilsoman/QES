package com.idroid.quickeventscheduler;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;


public class MainActivity extends ActionBarActivity {


    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;

    public static final int MEDIA_TYPE_IMAGE = 1;

    private static final String IMAGE_DIRECTORY_NAME = "OCR_Camera_Directory";

    /** Download filename for orientation and script detection (OSD) data. */
    static final String OSD_FILENAME = "tesseract-ocr-3.01.osd.tar";

    /** Destination filename for orientation and script detection (OSD) data. */
    static final String OSD_FILENAME_BASE = "osd.traineddata";

    /** Resource to use for data file downloads. */
    static final String DOWNLOAD_BASE = "http://tesseract-ocr.googlecode.com/files/";

    public Button btnCapturePicture;

    private ProgressDialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (dialog != null) {
            dialog.dismiss();
        }
        dialog = new ProgressDialog(this);


        btnCapturePicture = (Button) findViewById(R.id.capturePicture);


        btnCapturePicture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                initiateOCR();
            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Downloads the trained data files if they are not present in the storage card
    public void initiateOCR()
    {
        File storageDirectory = getExternalFilesDir(Environment.MEDIA_MOUNTED);


        File file = new File(Environment.getExternalStorageDirectory() + "/Android/data/com.idroid.quickeventscheduler/files/mounted/tessdata/eng.traineddata" );

        if (!file.exists()) {
            new OCRInit(this, dialog, "eng", "English", TessBaseAPI.OEM_CUBE_ONLY)
                    .execute(storageDirectory.toString());
        }
        else{

            captureImage();
        }

    }

    //Starts the capture activity once the OCR engine is setup.
    public void captureImage() {

        Intent iCameraActivity = new Intent(MainActivity.this, CameraActivity.class);

        startActivity(iCameraActivity);

    }

}

