package com.idroid.quickeventscheduler;

import com.temboo.Library.Google.Calendar.CreateCalendar.CreateCalendarInputSet;
import com.temboo.Library.Google.Calendar.CreateCalendar.CreateCalendarResultSet;
import com.temboo.Library.Google.Calendar.CreateEvent;
import com.temboo.Library.Google.Calendar.CreateEvent.CreateEventInputSet;
import com.temboo.Library.Google.Calendar.CreateEvent.CreateEventResultSet;
import com.temboo.Library.Google.Calendar.GetAllCalendars;
import com.temboo.Library.Google.Calendar.GetAllCalendars.GetAllCalendarsInputSet;
import com.temboo.Library.Google.Calendar.GetAllCalendars.GetAllCalendarsResultSet;
import com.temboo.Library.Google.Calendar.GetCalendar.GetCalendarInputSet;
import com.temboo.Library.Google.Calendar.GetCalendar.GetCalendarResultSet;
import com.temboo.Library.Google.OAuth.FinalizeOAuth;
import com.temboo.Library.Google.OAuth.InitializeOAuth;
import com.temboo.core.TembooException;
import com.temboo.core.TembooSession;

import java.text.ParseException;

/**
 * Created by Akshaya on 4/17/15.
 */
public class CalendarEvent {

    private TembooSession session;
    //initialize OAuth
    private InitializeOAuth initializeOAuthChoreo;
    private InitializeOAuth.InitializeOAuthInputSet initializeOAuthInputs;
    private InitializeOAuth.InitializeOAuthResultSet initializeOAuthResults;
    public static Integer flag=0;
    public static Integer flag1=0;
    //finalize OAuth
    private FinalizeOAuth finalizeOAuthChoreo;
    private FinalizeOAuth.FinalizeOAuthInputSet finalizeOAuthInputs;
    private FinalizeOAuth.FinalizeOAuthResultSet finalizeOAuthResults;

    private final String CLIENTID = "121540133138-640bvvbf4jgjpt88rl3um9ka4s46htn2.apps.googleusercontent.com";
    private final String CLIENTSECRET = "udyVehE4LYPRnRaImvLDWX5o";
    private final String REFRESHTOKEN="1/ELl0UCIKb4T_bDWQQ7wLr3CE6TUFfHNrbUh9_x6gkngMEudVrK5jSpoR30zcRFq6";
    private final String SCOPE="https://www.googleapis.com/auth/calendar";
    public static String CALENDARID = CalendarActivity.str;
    private CalendarParse metJSON;
    private String userResultJSON = null;
    private String metricsResultJSON = null;


    private String AuthorizationURL = "111";
    private String CallBackID = "222";
    private String AccessToken = "444";
    private String expires="22";
    private String RefreshToken = "100";
    private String NewAccessToken;


    private CreateEvent createEventChoreo;
    private CreateEvent.CreateEventInputSet createEventInputs ;
    private CreateEvent.CreateEventResultSet createEventResults;
    private GetCalendarInputSet getCalendarInputs;
    private GetCalendarResultSet getCalendarResults;
    private CreateCalendarInputSet createCalendarInputs;
    private CreateCalendarResultSet createCalendarResults;
    public CalendarEvent(){}

    public void oauth_choreo() {
        // Instantiate the Choreo, using a previously instantiated TembooSession

        try {
            //The session values are given by the temboo website after
            // making a user and signing up
            session = new TembooSession("akshayaiyer", "myFirstApp", "b24d3213f4f74ad8ad9ae51894e4e9b6");

            initializeOAuthChoreo = new InitializeOAuth(session);


            // Get an InputSet object for the choreo
            initializeOAuthInputs = initializeOAuthChoreo.newInputSet();

            // Set inputs
            initializeOAuthInputs.set_ClientID(CLIENTID);
            initializeOAuthInputs.set_Scope(SCOPE);


            // Execute Choreo
            initializeOAuthResults = initializeOAuthChoreo.execute(initializeOAuthInputs);

            AuthorizationURL = initializeOAuthResults.get_AuthorizationURL();
            CallBackID = initializeOAuthResults.get_CallbackID();


            System.out.println("---------------5-----------------"); //for debugging

        } catch (TembooException e) {
            e.printStackTrace();
        }
    }

    public void oauth_finalize() {
        try {
            session = new TembooSession("akshayaiyer", "myFirstApp", "b24d3213f4f74ad8ad9ae51894e4e9b6");
            finalizeOAuthChoreo = new FinalizeOAuth(session);

            // Get an InputSet object for the choreo
            finalizeOAuthInputs = finalizeOAuthChoreo.newInputSet();

            // Set inputs
            finalizeOAuthInputs.set_ClientID(CLIENTID);
            finalizeOAuthInputs.set_ClientSecret(CLIENTSECRET);
            finalizeOAuthInputs.set_CallbackID(CallBackID);



            // Execute Choreo
            finalizeOAuthResults = finalizeOAuthChoreo.execute(finalizeOAuthInputs);
            AccessToken=finalizeOAuthResults.get_AccessToken();
            RefreshToken = finalizeOAuthResults.get_RefreshToken();
            GetAllCalendars getAllCalendarsChoreo = new GetAllCalendars(session);

// Get an InputSet object for the choreo
            GetAllCalendarsInputSet getAllCalendarsInputs = getAllCalendarsChoreo.newInputSet();

// Set inputs
            getAllCalendarsInputs.set_ClientID(CLIENTID);
            getAllCalendarsInputs.set_ClientSecret(CLIENTSECRET);
            getAllCalendarsInputs.set_RefreshToken(RefreshToken);
            GetAllCalendarsResultSet getAllCalendarsResults = getAllCalendarsChoreo.execute(getAllCalendarsInputs);
            //getSpo2();

            System.out.println("---------------6----------------"); //for debugging

            AccessToken = getAllCalendarsResults.get_NewAccessToken();


            expires = finalizeOAuthResults.get_Expires();





       } catch (TembooException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

 //For obtaining Calendar ID
    public String getallcalendar(String Calendarid) {
        GetAllCalendars getAllCalendarsChoreo = new GetAllCalendars(session);

// Get an InputSet object for the choreo
        GetAllCalendarsInputSet getAllCalendarsInputs = getAllCalendarsChoreo.newInputSet();

// Set inputs
        getAllCalendarsInputs.set_ClientID(CLIENTID);
        getAllCalendarsInputs.set_ClientSecret(CLIENTSECRET);
        getAllCalendarsInputs.set_RefreshToken(RefreshToken);

// Execute Choreo
        try {
            GetAllCalendarsResultSet getAllCalendarsResults = getAllCalendarsChoreo.execute(getAllCalendarsInputs);
            metricsResultJSON = getAllCalendarsResults.get_Response(); //get JSON string result

            System.out.println("Results :--: '" + metricsResultJSON + "'"); //for debugging
        } catch (TembooException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Calendarid;
    }


    //Creating an event
    public void createEvent(){

        CreateEvent createEventChoreo = new CreateEvent(session);
        // Get an InputSet object for the choreo
        CreateEventInputSet createEventInputs = createEventChoreo.newInputSet();

        createEventInputs.set_ClientID(CLIENTID);
        createEventInputs.set_ClientSecret(CLIENTSECRET);
        createEventInputs.set_RefreshToken(RefreshToken);
        createEventInputs.set_CalendarID(CalendarParse.newId);

        try {
            DateTimeParsor.parseDate(CameraActivity.dateandtime);
        } catch (ParseException e) {
            e.printStackTrace();
        }



   /*     System.out.println("NewID-->"+CalendarParse.newId );
        System.out.println("Event "+CameraActivity.event);
        System.out.println("Venue "+CameraActivity.venue);
        System.out.println("StartDate "+DateTimeParsor.startDate);
        System.out.println("StartTime "+DateTimeParsor.startTime);
        System.out.println("Enddate "+DateTimeParsor.endDate);
        System.out.println("EndTime "+DateTimeParsor.endTime);*/


        createEventInputs.set_EventTitle(CameraActivity.event);
        createEventInputs.set_StartDate(DateTimeParsor.startDate);
        createEventInputs.set_StartTime(DateTimeParsor.startTime);
        createEventInputs.set_EndDate(DateTimeParsor.endDate);
        createEventInputs.set_EndTime(DateTimeParsor.endTime);
        createEventInputs.set_EventLocation(CameraActivity.venue);



        // Execute Choreo
        try {
            CreateEventResultSet createEventResults = createEventChoreo.execute(createEventInputs);
        } catch (TembooException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }

    }

    public String getCLIENTID() {
        return CLIENTID;
    }

    public String getCLIENTSECRET() {
        return CLIENTSECRET;
    }

    public String getAuthorizationURL() {
        return AuthorizationURL;
    }

    public void setAuthorizationURL(String authorizationURL) {
        AuthorizationURL = authorizationURL;
    }

    public String getCallBackID() {
        return CallBackID;
    }

    public void setCallBackID(String callBackID) {
        CallBackID = callBackID;
    }

    public String getAccessToken() {
        return AccessToken;
    }

    public void setAccessToken(String accessToken) {
        AccessToken = accessToken;
    }

    public String getNewAccessToken() {
        return NewAccessToken;
    }

    public void setNewAccessToken(String newaccessToken) {
        NewAccessToken = newaccessToken;
    }

    public String getRefreshToken() {
        return RefreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        RefreshToken = refreshToken;
    }

    public String getMetricsResultJSON() {
        return metricsResultJSON;
    }

    public void setMetricsResultJSON(String metricsResultJSON) {
        this.metricsResultJSON = metricsResultJSON;
    }


    public CalendarParse getMetJSON() {
        return metJSON;
    }

    public void setMetJSON(CalendarParse metJSON) {
        this.metJSON = metJSON;
    }



}
