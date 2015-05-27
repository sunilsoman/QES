package com.idroid.quickeventscheduler;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.IOException;

/**
 * Created by sunil on 3/4/15.
 */
public class OCRActivity extends Activity{

    String imagePath;
    String extractedText;
    public Bitmap ocrbitmap;


    public String performOCR(final String path) {

        imagePath = path;

        if(imagePath!= null) {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;

            //Extracting the image from storage memory and converting it onto bitmap
            ocrbitmap = BitmapFactory.decodeFile(imagePath, options);

            try {


                ExifInterface exif = new ExifInterface(imagePath);
                int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

                Log.v("Error", "Orient: " + exifOrientation);

                int rotate = 0;
                switch (exifOrientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotate = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotate = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        rotate = 270;
                        break;

                }

                Log.v("Error", "Rotation: " + rotate);

                if (rotate != 0) {

                    // Getting width & height of the given image.
                    int w = ocrbitmap.getWidth();
                    int h = ocrbitmap.getHeight();

                    // Setting pre rotate
                    Matrix mtx = new Matrix();
                    mtx.preRotate(rotate);

                    // Rotating Bitmap
                    ocrbitmap = Bitmap.createBitmap(ocrbitmap, 0, 0, w, h, mtx, false);

                    // tesseract req. ARGB_8888
                    ocrbitmap = ocrbitmap.copy(Bitmap.Config.ARGB_8888, true);
                }
            } catch (IOException e) {
                Log.e("Error", "Rotate or coversion failed: " + e.toString());

            }


            TessBaseAPI baseApi = new TessBaseAPI();

            baseApi.setDebug(true);

            //initializing trained data to baseApi
            baseApi.init("/storage/sdcard0/Android/data/com.idroid.quickeventscheduler/files/mounted/", "eng");

            baseApi.setImage(ocrbitmap);

            extractedText = baseApi.getUTF8Text();

            baseApi.end();


            Log.v("Result", "OCR Result: " + extractedText);

            return extractedText;
        }

        return null;
    }
}
