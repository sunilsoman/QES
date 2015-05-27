package com.idroid.quickeventscheduler;

/**
 * Created by Akshaya on 4/21/2015.
 */

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
public class CalendarParse {

    private JSONObject resultJSON;
    private JSONObject dataJSON;
    private JSONArray lang;
    public static String Id1;
    public static String newId;
    private String jsonString;
    public CalendarParse(String data){
        jsonString = data;
    }//end of constructor

    // processing the output obtained from getallcalendar to get calendar ID
    public void processData() throws JSONException{

        try {
          System.out.println(" **** ObjMetrics().ProcessData Start ******\n");
          resultJSON = (JSONObject) new JSONParser().parse(jsonString);
          lang= (JSONArray) resultJSON.get("items");
            for(int i=0; i<lang.size(); i++){
                //System.out.println("\n The " + i + " element of the array: "+lang.get(i));
                JSONObject jz = (JSONObject) lang.get(i);
                Id1 = "" + ((JSONObject)lang.get(i)).get("id");
                String Summary1 = "" + ((JSONObject)lang.get(i)).get("summary");
                if(Id1.substring(Id1.length()-10,Id1.length()).contentEquals("@gmail.com")) {
                String valueId = "" + ((JSONObject) lang.get(i)).get("id");
                    newId = Id1;
                    System.out.println("Calendar identification " + Id1);
                }
              }


            System.out.println(" **** ObjMetrics().ProcessData END ****** \n");

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }//end of method start


}
