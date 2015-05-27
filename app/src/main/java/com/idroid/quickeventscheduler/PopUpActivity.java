package com.idroid.quickeventscheduler;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


public class PopUpActivity extends Activity {


    private RadioGroup rgroup;
    private RadioButton rEvent;
    private RadioButton rVenue;
    private RadioButton rDateAndTime;
    private Button btnSave;
    private Button btnDiscard;
    private TextView ocrResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up);

     ocrResult = (TextView)findViewById(R.id.tvConvertedText);

     rEvent = (RadioButton)findViewById(R.id.name);
     rVenue = (RadioButton)findViewById(R.id.venue);
     rDateAndTime = (RadioButton)findViewById(R.id.date);

      getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pop_up, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {


        super.onPause();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //function registers the button listeners
        addListenerOnButton();

        Log.e("OCR result", CameraActivity.ocrResult);
        ocrResult.setText(CameraActivity.ocrResult);

    }

    public void addListenerOnButton() {

        rgroup = (RadioGroup) findViewById(R.id.radio);

        btnSave = (Button) findViewById(R.id.bSave);

        btnDiscard = (Button)findViewById(R.id.bDiscard);

        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                // Saving event attributes based on radiobutton selection by the user
                if(rEvent.isChecked())
                {
                    CameraActivity.event= ocrResult.getText().toString();


                }else if(rVenue.isChecked())
                {
                    CameraActivity.venue= ocrResult.getText().toString();

                }
                else if(rDateAndTime.isChecked())
                {
                    CameraActivity.dateandtime= ocrResult.getText().toString();

                }

                onPause();
            }

        });


        btnDiscard.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Clearing the garbage data obtained after Ocr conversion.
                ocrResult.setText(null);
                CameraActivity.timeStamp =null;
                finish();
            }
        });

    }
}
