package com.idroid.quickeventscheduler;
/**
 * Created by Akshaya on 4/17/15.
 */
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;


public class CalendarActivity extends Activity {

    private Button Click;
    private Button Calendar;
    private WebView webview;
    public static CalendarEvent oauth;
    public static String calendarid;


    public static String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        oauth = new CalendarEvent();
        webview = (WebView) findViewById(R.id.webView2);
        webview.setWebViewClient(new MyWebViewClient());
        webview.getSettings().setJavaScriptEnabled(true);
        if (checkNetwork()) {
                    new ObjWeb().execute("");


                }
              }


    //Checking internet connection
    public boolean checkNetwork() {
        System.out.println("\t\t ******* Check Intenet Connection ********");

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            // fetch data
            System.out.println("\t\t ******* Is Connected ********");
            return true;

        } else {
            // display error
            System.out.println("\t\t ******* Error Connection ********");
            return false;
        }
    }//End Method checkNetwork


    //initial authorization
    private class ObjWeb extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
           oauth.oauth_choreo();
              return null;
        }


        @Override
        protected void onPostExecute(String result) {
             webview.loadUrl(oauth.getAuthorizationURL());
                new ObjToken().execute("");

           }


    }// end of class ObjWeb

   //final authorization
    private class ObjToken extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            oauth.oauth_finalize();
              return null;
        }

        @Override
        protected void onPostExecute(String result) {
            new ObjJsonReading().execute("");

        }
    }//end of class ObjToken


    //Obtaining the calendar ID
    private class ObjJsonReading extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
           oauth.getallcalendar(str);
              return null;
        }

        @Override
    protected void onPostExecute(String result) {

      System.out.println("\t **********  JSON   *************");
         CalendarParse metJSON = new CalendarParse(oauth.getMetricsResultJSON());
         System.out.println("\t **********  CHECKING ID   *************---------" + metJSON.newId );

            try {
                metJSON.processData();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            new ObjGetReading().execute("");

        }
    }


    //creating event
    private class ObjGetReading extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
           System.out.println("check id1--------- ");
              oauth.createEvent();
                  return null;
        }


        @Override
        protected void onPostExecute(String result) {
            Toast toast = Toast.makeText(CalendarActivity.this, "Event is scheduled.", Toast.LENGTH_SHORT);
            toast.show();
            onPause();

        }
    }


       //Web page for logging in
       public class MyWebViewClient extends WebViewClient {

            public MyWebViewClient() {
                super();
                // start anything you need to
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // Do something to the urls, views, etc.
            }

        }// end of MyWebViewClient

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
