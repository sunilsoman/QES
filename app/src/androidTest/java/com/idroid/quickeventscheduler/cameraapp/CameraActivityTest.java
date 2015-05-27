package com.idroid.quickeventscheduler.cameraapp;

import android.app.Instrumentation;
import android.content.pm.ActivityInfo;
import android.os.Environment;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.suitebuilder.annotation.MediumTest;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.idroid.quickeventscheduler.BoxTouchView;
import com.idroid.quickeventscheduler.CalendarActivity;
import com.idroid.quickeventscheduler.CameraActivity;
import com.idroid.quickeventscheduler.CameraPreview;
import com.idroid.quickeventscheduler.PopUpActivity;
import com.idroid.quickeventscheduler.R;

/**
 * Created by as on 4/23/2015.
 */
public class CameraActivityTest extends ActivityInstrumentationTestCase2<CameraActivity> {

    private CameraActivity testCameraActivity;
    private Button testConvertButton, testDoneButton;
    private CameraPreview testCameraPreview;
    private BoxTouchView testBoxTouchView;
    private RelativeLayout testRL;

   /* public CameraActivityTest(Class<CameraActivity> activityClass) {
        super(activityClass);
    }*/


    public CameraActivityTest() {
        super(CameraActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        //Sets the initial touch mode for the Activity under test.
        setActivityInitialTouchMode(false);

        CameraActivity.event = "Android workshop";
        CameraActivity.venue = "ECE 232";
        CameraActivity.dateandtime = "2015-05-01";

        //Get a reference to the Activity under test, starting it if necessary.
        testCameraActivity = getActivity();
        testBoxTouchView = new BoxTouchView(this.getActivity().getBaseContext());
        testRL = (RelativeLayout) testCameraActivity.findViewById(R.id.rlParent);
        testConvertButton = (Button) testCameraActivity.findViewById(R.id.bConvert);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        //if()
    }

    @MediumTest
    public void testCameraActivity() throws Exception {
        assertNotNull("CameraActivity is null ", testCameraActivity);

    }

    @MediumTest
    public void testCropBox() throws Exception {
        assertNotNull("CameraActivity is null ", testBoxTouchView);

    }

    @MediumTest
    public void testCameraOrientation() throws Exception {
        assertNotNull("CameraActivity is null ", testCameraActivity);
        assertNotNull("layout is null", testRL);
        assertTrue("Check orientation", testRL.getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Thread.sleep(2000);
    }

    @MediumTest
    public void testConvertButton_click() throws Exception{

        // Set up an ActivityMonitor
        Instrumentation.ActivityMonitor myPopUpActivityMonitor =
                getInstrumentation().addMonitor(PopUpActivity.class.getName(),
                        null, false);

        // Validate that MyAccelerometer is started
        TouchUtils.clickView(this, testConvertButton);
        Thread.sleep(2000);
        PopUpActivity myPopUpActivity = (PopUpActivity) myPopUpActivityMonitor.waitForActivityWithTimeout(2000);
        assertNotNull("PopUpActivity is null",myPopUpActivity );
        assertEquals("Monitor for MyPoUpActivity has not been called",
                1, myPopUpActivityMonitor.getHits());
        assertEquals("Activity is of wrong type",PopUpActivity.class, myPopUpActivity.getClass());
        assertNotNull("Image is null", Environment.getExternalStorageDirectory() + "/QES" + "/IMG_" + testCameraActivity.timeStamp + ".jpg");

        // Remove the ActivityMonitor
        getInstrumentation().removeMonitor(myPopUpActivityMonitor);
        myPopUpActivity.finish();
    }

    @MediumTest
    public void testSaveButton_click() throws Exception{

        // Set up an ActivityMonitor
        Instrumentation.ActivityMonitor myPopUpActivityMonitor =
                getInstrumentation().addMonitor(PopUpActivity.class.getName(),
                        null, false);

        // Validate that MyAccelerometer is started
        TouchUtils.clickView(this, testConvertButton);
        Thread.sleep(2000);
        PopUpActivity myPopUpActivity = (PopUpActivity) myPopUpActivityMonitor.waitForActivityWithTimeout(2000);
        assertNotNull("PopUpActivity is null",myPopUpActivity );
        assertEquals("Monitor for MyPoUpActivity has not been called",
                1, myPopUpActivityMonitor.getHits());
        assertEquals("Activity is of wrong type",PopUpActivity.class, myPopUpActivity.getClass());

        final Button testSaveButton = (Button) myPopUpActivity.findViewById(R.id.bSave);
        TouchUtils.clickView(this, testSaveButton);
        getInstrumentation().removeMonitor(myPopUpActivityMonitor);

        myPopUpActivity.finish();
        assertNotNull("PopUpActivity is null",testCameraActivity );
        // Remove the ActivityMonitor

    }

    @MediumTest
    public void testDiscardButton_click() throws Exception{

        // Set up an ActivityMonitor
        Instrumentation.ActivityMonitor myPopUpActivityMonitor =
                getInstrumentation().addMonitor(PopUpActivity.class.getName(),
                        null, false);


        // Validate that MyAccelerometer is started
        TouchUtils.clickView(this, testConvertButton);
        Thread.sleep(2000);
        PopUpActivity myPopUpActivity = (PopUpActivity) myPopUpActivityMonitor.waitForActivityWithTimeout(2000);
        assertNotNull("PopUpActivity is null",myPopUpActivity );
        assertEquals("Monitor for MyPoUpActivity has not been called",
                1, myPopUpActivityMonitor.getHits());
        assertEquals("Activity is of wrong type",PopUpActivity.class, myPopUpActivity.getClass());

        final Button testDiscardButton = (Button) myPopUpActivity.findViewById(R.id.bDiscard);
        TouchUtils.clickView(this, testDiscardButton);
        getInstrumentation().removeMonitor(myPopUpActivityMonitor);

        myPopUpActivity.finish();
        assertNotNull("PopUpActivity is null",testCameraActivity );
        // Remove the ActivityMonitor



    }

    /*@MediumTest
    public void testDoneButton_click() throws Exception{

        // Set up an ActivityMonitor
        Instrumentation.ActivityMonitor myCalendarActivityMonitor =
                getInstrumentation().addMonitor(CalendarActivity.class.getName(),
                        null, false);

        final Button testDoneButton = (Button) testCameraActivity.findViewById(R.id.bDone);
        // Validate that MyAccelerometer is started
        TouchUtils.clickView(this, testDoneButton);
        Thread.sleep(2000);
        CalendarActivity myCalendarActivity = (CalendarActivity) myCalendarActivityMonitor.waitForActivityWithTimeout(2000);
        assertNotNull("PopUpActivity is null",myCalendarActivity );
        assertEquals("Monitor for MyPoUpActivity has not been called",
                1, myCalendarActivityMonitor.getHits());
        assertEquals("Activity is of wrong type",CalendarActivity.class, myCalendarActivity.getClass());

        getInstrumentation().removeMonitor(myCalendarActivityMonitor);

        myCalendarActivity.finish();
        // Remove the ActivityMonitor

    }*/


}
