package com.idroid.quickeventscheduler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by as on 5/1/2015.
 */
public class StartBackground extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startbackground);
        Thread timer = new Thread() {
            public void run() {
                try{
                    sleep(2000);
                }catch(InterruptedException ie){
                    ie.printStackTrace();
                }finally {
                    Intent openMainActivity = new Intent(StartBackground.this,MainActivity.class);
                    startActivity(openMainActivity);
                }
            }
        };
        timer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
