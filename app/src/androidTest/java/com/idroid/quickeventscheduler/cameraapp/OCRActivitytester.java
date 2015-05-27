package com.idroid.quickeventscheduler.cameraapp;

import android.os.Environment;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;

import com.idroid.quickeventscheduler.OCRActivity;

/**
 * Created by sunil on 4/23/15.
 */
public class OCRActivitytester extends ActivityInstrumentationTestCase2<OCRActivity> {

private OCRActivity activity;

    public OCRActivitytester(){

        super(OCRActivity.class);
    }

    @Override
    public void setUp() throws Exception {

        super.setUp();
        setActivityInitialTouchMode(true);

        activity = this.getActivity();

    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @MediumTest  //Function to test OCR conversion against plan background.
    //image should be saved first in storage card and set the expected text before running the this test
    public void testOCRConversionForPlaneBackground() throws Exception{

        String expectedString = "public";

        String actualString = activity.performOCR(Environment.getExternalStorageDirectory() + "/QES" + "/Test" + ".jpg");

        assertEquals(expectedString,actualString);

    }

    @MediumTest  //Function to test OCR conversion against animated background (light background)
    //image should be saved first in storage card and set the expected text before running the this test
    public void testOCRConversionForAnimatedBackground() throws Exception{

        String expectedString = "WORK";

        String actualString = activity.performOCR(Environment.getExternalStorageDirectory() + "/QES" + "/Test2" + ".jpg");

        assertEquals(expectedString,actualString);

    }






}
