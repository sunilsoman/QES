package com.idroid.quickeventscheduler.cameraapp;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.ViewAsserts;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.idroid.quickeventscheduler.CameraActivity;
import com.idroid.quickeventscheduler.MainActivity;
import com.idroid.quickeventscheduler.R;

/**
 * Created by sunil on 4/23/15.
 */
public class MainActivityTester extends ActivityInstrumentationTestCase2<MainActivity> {

    MainActivity activity;
    LinearLayout linear;
    Button scheduleButton;
    public MainActivityTester(){

        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {

        super.setUp();
        setActivityInitialTouchMode(true);

        activity = this.getActivity();
        linear = (LinearLayout)activity.findViewById(R.id.linear);
        scheduleButton = (Button)activity.findViewById(R.id.capturePicture);

    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }


   // Test to check whether CaptureActivity is started on capture button click
    @MediumTest
    public void testCaptureEvent_click() throws Exception{

        // Set up an ActivityMonitor
        Instrumentation.ActivityMonitor myCameraActivityMonitor =
                getInstrumentation().addMonitor(CameraActivity.class.getName(),
                        null, false);

        // Validate that MyAccelerometer is started
        TouchUtils.clickView(this, scheduleButton);
        Thread.sleep(2000);
        CameraActivity myCameraActivity = (CameraActivity) myCameraActivityMonitor.waitForActivityWithTimeout(20000);
        assertNotNull("CameraActivity is null",myCameraActivity );
        assertEquals("Monitor for CameraActivity has not been called",
                1, myCameraActivityMonitor.getHits());
        assertEquals("Activity is of wrong type",CameraActivity.class, myCameraActivity.getClass());

        // Remove the ActivityMonitor
        getInstrumentation().removeMonitor(myCameraActivityMonitor);
        myCameraActivity.finish();
    }

    // Test to check presence of capture button
    @MediumTest
    public void testCaptureEventButtonPresence() throws Exception{

        final View decorView = activity.getWindow().getDecorView();
        ViewAsserts.assertOnScreen(decorView, scheduleButton);


    }






}
