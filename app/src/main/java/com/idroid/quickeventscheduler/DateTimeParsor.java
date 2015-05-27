package com.idroid.quickeventscheduler;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Akshaya on 4/17/15.
 */
public class DateTimeParsor {

   public static String startTime;
    public static String endTime;
    public static String startDate;
    public static String endDate;

    public static void parseDate(String datetime) throws ParseException {


        System.out.println(datetime);
        String s= datetime;
        Log.e("datetime", s);
        Date date = new Date();
        Date date1 = new Date();
        DateFormat dateF = new SimpleDateFormat("dd MMM yyyy");
        Calendar googleCal = Calendar.getInstance();
        String[] token = s.split("[ ]+");

        for(int i =0; i<token.length;i++)
        {
            if(token[i].toString().length() < 10 ){

            }else {
                System.out.println(token.length + " " + token[i]);
                System.out.println(token[i].substring(2, 3));
                System.out.println(token[i].substring(5, 6));

                //Parsing the format MM.DD.YYYY
                if (token[i].substring(2, 3).contentEquals(".") && token[i].substring(5, 6).contentEquals(".")) {
                    googleCal.set(Integer.parseInt(token[i].substring(6, 10)), Integer.parseInt(token[i].substring(0, 2))-1, Integer.parseInt(token[i].substring(3, 5)));
                    System.out.println("date---------" + googleCal.get(Calendar.DATE));
                }
                //Parsing the format MM/DD/YYYY
                if (token[i].substring(2, 3).contentEquals("/") && token[i].substring(5, 6).contentEquals("/")) {
                    googleCal.set(Integer.parseInt(token[i].substring(6, 10)), Integer.parseInt(token[i].substring(0, 2))-1, Integer.parseInt(token[i].substring(3, 5)));
                    System.out.println("date---------" + googleCal.get(Calendar.DATE));
                }
                //Parsing the format MM-DD-YYYY
                if (token[i].substring(2, 3).contentEquals("-") && token[i].substring(5, 6).contentEquals("-")) {
                    System.out.println("Hello");
                    googleCal.set(Integer.parseInt(token[i].substring(6, 10)), Integer.parseInt(token[i].substring(0, 2))-1, Integer.parseInt(token[i].substring(3, 5)));
                    System.out.println("date---------" + googleCal.get(Calendar.DATE));
                }

            }
            //parsing for the format DD MM YYYY
            if(token[i].toString().length() >=  3) {
                if ((token[i].substring(0, 3).contentEquals("Dec")
                        || token[i].substring(0, 3).contentEquals("Jan")
                        || token[i].substring(0, 3).contentEquals("Feb")
                        || token[i].substring(0, 3).contentEquals("Mar")
                        || token[i].substring(0, 3).contentEquals("Apr")
                        || token[i].substring(0, 3).contentEquals("May")
                        || token[i].substring(0, 3).contentEquals("Jun")
                        || token[i].substring(0, 3).contentEquals("Jul")
                        || token[i].substring(0, 3).contentEquals("Aug")
                        || token[i].substring(0, 3).contentEquals("Sep")
                        || token[i].substring(0, 3).contentEquals("Oct")
                        || token[i].substring(0, 3).contentEquals("Nov"))
                        && (isInt(token[i-1].substring(0,2)))
                        && (isInt(token[i+1]))

                        ) {
                    String str = token[i - 1].substring(0, 2) + " " + token[i].substring(0,3) + " " + token[i + 1];
                    System.out.println("dd mmm yyyy--------" + str);
                    date = dateF.parse(str);
                    googleCal.setTime(date);

                }
            }

            //parsing for the format MM DD YYYY
            if((token[i].toString().length() >= 3)) {
                if ((token[i].substring(0, 3).contentEquals("Dec")
                        || token[i].substring(0, 3).contentEquals("Jan")
                        || token[i].substring(0, 3).contentEquals("Feb")
                        || token[i].substring(0, 3).contentEquals("Mar")
                        || token[i].substring(0, 3).contentEquals("Apr")
                        || token[i].substring(0, 3).contentEquals("May")
                        || token[i].substring(0, 3).contentEquals("Jun")
                        || token[i].substring(0, 3).contentEquals("Jul")
                        || token[i].substring(0, 3).contentEquals("Aug")
                        || token[i].substring(0, 3).contentEquals("Sep")
                        || token[i].substring(0, 3).contentEquals("Oct")
                        || token[i].substring(0, 3).contentEquals("Nov"))
                        && (isInt(token[i+1].substring(0,2)))
                        && (isInt(token[i+2]))
                        ) {
                    String str = token[i + 1].substring(0, 2) + " " + token[i].substring(0,3) + " " + token[i + 2];
                    date = dateF.parse(str);
                    googleCal.setTime(date);
                }
            }



        }

        //Time parsing
        for(int i = 0; i< token.length; i++){
            System.out.println(token[i].toString().length());
            if(token[i].toString().length() == 7) {
                if (token[i].substring(5, 7).contentEquals("am")
                        || token[i].substring(5, 7).contentEquals("pm")) {
                    if(token[i].substring(1, 3).contentEquals("pm") && token[i].substring(5, 7).contentEquals("pm"))
                    {
                        startTime = (Integer.parseInt(token[i].substring(0, 1)) + 12) + ":00:00";
                        endTime = (Integer.parseInt(token[i].substring(4, 5)) + 12) + ":00:00";
                    }
                    else if(token[i].substring(1,3).contentEquals("am") && token[i].substring(5, 7).contentEquals("pm")){
                        startTime  = token[i].substring(0, 1) + ":00:00";
                        endTime = (Integer.parseInt(token[i].substring(4, 5)) + 12) + ":00:00";
                    }
                    else if (token[i].substring(1,3).contentEquals("pm") && token[i].substring(5, 7).contentEquals("am")){
                        startTime = (Integer.parseInt(token[i].substring(0, 1)) + 12) + ":00:00";
                        endTime = token[i].substring(4, 5) + ":00:00";
                    }
                    else {
                        startTime = token[i].substring(0, 1) + ":00:00";
                        endTime = token[i].substring(4, 5) + ":00:00";
                    }
                    System.out.println("1");
                }
            }
            if(token[i].toString().length() == 9 ) {
                if (token[i].substring(7, 9).contentEquals("am")
                        || token[i].substring(7, 9).contentEquals("pm")) {

                    if(token[i].substring(2,4).contentEquals("pm") && token[i].substring(7, 9).contentEquals("pm"))
                    {
                        startTime = (Integer.parseInt(token[i].substring(0, 2)) + 12) + ":00:00";
                        endTime = (Integer.parseInt(token[i].substring(5,7)) + 12) + ":00:00";
                    }
                    else if(token[i].substring(2,4).contentEquals("am") && token[i].substring(7, 9).contentEquals("pm")){
                        startTime  = token[i].substring(0, 2) + ":00:00";
                        endTime = (Integer.parseInt(token[i].substring(5,7)) + 12) + ":00:00";
                    }
                    else if (token[i].substring(2,4).contentEquals("pm") && token[i].substring(7, 9).contentEquals("am")){
                        startTime = (Integer.parseInt(token[i].substring(0, 2)) + 12) + ":00:00";
                        endTime = token[i].substring(5,7) + ":00:00";
                    }
                    else {
                        startTime = token[i].substring(0, 2) + ":00:00";
                        endTime = token[i].substring(5,7) + ":00:00";
                    }


                    //startTime = token[i].substring(0, 2) + ":00:00";
                    //endTime = token[i].substring(5, 7) + ":00:00";
                    System.out.println("2");
                }
            }
            if(token[i].toString().length() == 8) {
                if (((token[i].substring(1, 3).contentEquals("am")
                        || token[i].substring(1, 3).contentEquals("pm")) && (token[i].substring(6, 8).contentEquals("am")
                        || token[i].substring(6, 8).contentEquals("pm")))) {


                    if(token[i].substring(1, 3).contentEquals("pm") && token[i].substring(6,8).contentEquals("pm"))
                    {
                        startTime = (Integer.parseInt(token[i].substring(0, 1)) + 12) + ":00:00";
                        endTime = (Integer.parseInt(token[i].substring(4, 6)) + 12) + ":00:00";
                    }
                    else if(token[i].substring(1,3).contentEquals("am") && token[i].substring(6,8).contentEquals("pm")){
                        startTime  = token[i].substring(0, 1) + ":00:00";
                        endTime = (Integer.parseInt(token[i].substring(4, 6)) + 12) + ":00:00";
                    }
                    else if (token[i].substring(1,3).contentEquals("pm") && token[i].substring(6,8).contentEquals("am")){
                        startTime = (Integer.parseInt(token[i].substring(0, 1)) + 12) + ":00:00";
                        endTime = token[i].substring(4, 6) + ":00:00";
                    }
                    else {
                        startTime = token[i].substring(0, 1) + ":00:00";
                        endTime = token[i].substring(4, 6) + ":00:00";
                    }
                    //startTime = token[i].substring(0, 1) + ":00:00";
                    //endTime = token[i].substring(4, 6) + ":00:00";
                    System.out.println("3");
                }
            }
            if(token[i].toString().length() == 8) {
                if (((token[i].substring(2, 4).contentEquals("am")
                        || token[i].substring(2, 4).contentEquals("pm")) && (token[i].substring(6, 8).contentEquals("am")
                        || token[i].substring(6, 8).contentEquals("pm")))) {

                    if(token[i].substring(2,4).contentEquals("pm") && token[i].substring(6,8).contentEquals("pm"))
                    {
                        startTime = (Integer.parseInt(token[i].substring(0, 2)) + 12) + ":00:00";
                        endTime = (Integer.parseInt(token[i].substring(5, 6)) + 12) + ":00:00";
                    }
                    else if(token[i].substring(2,4).contentEquals("am") && token[i].substring(6,8).contentEquals("pm")){
                        startTime  = token[i].substring(0, 2) + ":00:00";
                        endTime = (Integer.parseInt(token[i].substring(5, 6)) + 12) + ":00:00";
                    }
                    else if (token[i].substring(2,4).contentEquals("pm") && token[i].substring(6,8).contentEquals("am")){
                        startTime = (Integer.parseInt(token[i].substring(0, 2)) + 12) + ":00:00";
                        endTime = token[i].substring(5, 6) + ":00:00";
                    }
                    else {
                        startTime = token[i].substring(0, 2) + ":00:00";
                        endTime = token[i].substring(5, 6) + ":00:00";
                    }

                    //startTime = token[i].substring(0, 2) + ":00:00";
                    //endTime = token[i].substring(5, 6) + ":00:00";
                    System.out.println("4");
                }
            }
//.30
            if(token[i].toString().length() == 13) {
                if (token[i].substring(11, 13).contentEquals("am")
                        || token[i].substring(11, 13).contentEquals("pm")) {

                    if(token[i].substring(4,6).contentEquals("pm") && token[i].substring(11,13).contentEquals("pm"))
                    {
                        startTime = (Integer.parseInt(token[i].substring(0, 1)) + 12) +":"+token[i].substring(2,4)+ ":00";
                        endTime = (Integer.parseInt(token[i].substring(7,8)) + 12) +":"+token[i].substring(9,11)+ ":00";
                    }
                    else if(token[i].substring(4,6).contentEquals("am") && token[i].substring(11,13).contentEquals("pm")){
                        startTime  = token[i].substring(0, 1) +":"+token[i].substring(2,4)+ ":00";
                        endTime = (Integer.parseInt(token[i].substring(7,8)) + 12) +":"+token[i].substring(9,11)+ ":00";
                    }
                    else if (token[i].substring(4,6).contentEquals("pm") && token[i].substring(11,13).contentEquals("am")){
                        startTime = (Integer.parseInt(token[i].substring(0, 1)) + 12) +":"+token[i].substring(2,4)+ ":00";
                        endTime = token[i].substring(7,8) +":"+token[i].substring(9,11)+ ":00";
                    }
                    else {
                        startTime = token[i].substring(0, 1) +":"+token[i].substring(2,4)+ ":00";
                        endTime = token[i].substring(7,8) +":"+token[i].substring(9,11)+ ":00";
                    }
                    //startTime = token[i].substring(0, 1) +":"+token[i].substring(2,4)+ ":00";
                    //endTime = token[i].substring(7, 8) +":"+token[i].substring(9,11)+ ":00";
                    System.out.println("11");
                }
            }

            if(token[i].toString().length() == 15) {
                if (token[i].substring(13, 15).contentEquals("am")
                        || token[i].substring(13, 15).contentEquals("pm")) {


                    if(token[i].substring(5,7).contentEquals("pm") && token[i].substring(13,15).contentEquals("pm"))
                    {
                        startTime = (Integer.parseInt(token[i].substring(0, 2)) + 12) +":"+token[i].substring(3,5)+ ":00";
                        endTime = (Integer.parseInt(token[i].substring(8,10)) + 12) +":"+token[i].substring(11,13)+ ":00";
                    }
                    else if(token[i].substring(5,7).contentEquals("am") && token[i].substring(13,15).contentEquals("pm")){
                        startTime  = token[i].substring(0, 2) +":"+token[i].substring(3,5)+ ":00";
                        endTime = (Integer.parseInt(token[i].substring(8,10)) + 12) +":"+token[i].substring(11,13)+ ":00";
                    }
                    else if (token[i].substring(5,7).contentEquals("pm") && token[i].substring(13,15).contentEquals("am")){
                        startTime = (Integer.parseInt(token[i].substring(0, 2)) + 12) +":"+token[i].substring(3,5)+ ":00";
                        endTime = token[i].substring(8,10) +":"+token[i].substring(11,13)+ ":00";
                    }
                    else {
                        startTime = token[i].substring(0, 2) +":"+token[i].substring(3,5)+":00";
                        endTime = token[i].substring(8, 10) +":"+token[i].substring(11,13)+ ":00";
                    }


                    //startTime = token[i].substring(0, 2) +":"+token[i].substring(3,5)+":00";
                    // endTime = token[i].substring(8, 10) +":"+token[i].substring(11,13)+ ":00";
                    System.out.println("22");
                }
            }

            if(token[i].toString().length() == 14) {
                if (((token[i].substring(4, 6).contentEquals("am")
                        || token[i].substring(4, 6).contentEquals("pm")) && (token[i].substring(12, 14).contentEquals("am")
                        || token[i].substring(12, 14).contentEquals("pm")))) {

                    if(token[i].substring(4,6).contentEquals("pm") && token[i].substring(12,14).contentEquals("pm"))
                    {
                        startTime = (Integer.parseInt(token[i].substring(0, 1)) + 12) +":"+token[i].substring(2,4)+ ":00";
                        endTime = (Integer.parseInt(token[i].substring(7,9)) + 12) +":"+token[i].substring(10,12)+ ":00";
                    }
                    else if(token[i].substring(4,6).contentEquals("am") && token[i].substring(12,14).contentEquals("pm")){
                        startTime  = token[i].substring(0, 1) +":"+token[i].substring(2,4)+ ":00";
                        endTime = (Integer.parseInt(token[i].substring(7,9)) + 12) +":"+token[i].substring(10,12)+ ":00";
                    }
                    else if (token[i].substring(4,6).contentEquals("pm") && token[i].substring(12,14).contentEquals("am")){
                        startTime = (Integer.parseInt(token[i].substring(0, 1)) + 12) +":"+token[i].substring(2,4)+ ":00";
                        endTime = token[i].substring(7,9) +":"+token[i].substring(10,12)+ ":00";
                    }
                    else {
                        startTime = token[i].substring(0, 1) +":"+token[i].substring(2,4)+":00";
                        endTime = token[i].substring(7,9) +":"+token[i].substring(10,12)+ ":00";
                    }

                    //startTime = token[i].substring(0, 1) +":"+token[i].substring(2,4)+ ":00";
                    //endTime = token[i].substring(7, 9) +":"+token[i].substring(10,12)+ ":00";
                    System.out.println("33");
                }
            }

            if(token[i].toString().length() == 14) {
                if (((token[i].substring(5, 7).contentEquals("am")
                        || token[i].substring(5, 7).contentEquals("pm")) && (token[i].substring(12, 14).contentEquals("am")
                        || token[i].substring(12, 14).contentEquals("pm")))) {

                    if(token[i].substring(5,7).contentEquals("pm") && token[i].substring(12,14).contentEquals("pm"))
                    {
                        startTime = (Integer.parseInt(token[i].substring(0, 2)) + 12) +":"+token[i].substring(3,5)+ ":00";
                        endTime = (Integer.parseInt(token[i].substring(8,9)) + 12) +":"+token[i].substring(10,12)+ ":00";
                    }
                    else if(token[i].substring(5,7).contentEquals("am") && token[i].substring(12,14).contentEquals("pm")){
                        startTime  = token[i].substring(0, 2) +":"+token[i].substring(3,5)+ ":00";
                        endTime = (Integer.parseInt(token[i].substring(8,9)) + 12) +":"+token[i].substring(10,12)+ ":00";
                    }
                    else if (token[i].substring(5,7).contentEquals("pm") && token[i].substring(12,14).contentEquals("am")){
                        startTime = (Integer.parseInt(token[i].substring(0, 2)) + 12) +":"+token[i].substring(3,5)+ ":00";
                        endTime = token[i].substring(8,9) +":"+token[i].substring(10,12)+ ":00";
                    }
                    else {
                        startTime = token[i].substring(0, 2) +":"+token[i].substring(3,5)+ ":00";
                        endTime = token[i].substring(8, 9) +":"+token[i].substring(10,12)+ ":00";
                    }

                    //startTime = token[i].substring(0, 2) +":"+token[i].substring(3,5)+ ":00";
                    //endTime = token[i].substring(8, 9) +":"+token[i].substring(10,12)+ ":00";
                    System.out.println("44");
                }
            }

            //9-10.30
            if(token[i].toString().length() == 11) {
                if (((token[i].substring(1, 3).contentEquals("am")
                        || token[i].substring(1, 3).contentEquals("pm")) && (token[i].substring(9, 11).contentEquals("am")
                        || token[i].substring(9, 11).contentEquals("pm")))) {

                    if(token[i].substring(1,3).contentEquals("pm") && token[i].substring(9,11).contentEquals("pm"))
                    {
                        startTime = (Integer.parseInt(token[i].substring(0, 1)) + 12) +":00:00";
                        endTime = (Integer.parseInt(token[i].substring(4,6)) + 12) +":"+token[i].substring(7,9)+ ":00";
                    }
                    else if(token[i].substring(1,3).contentEquals("am") && token[i].substring(9,11).contentEquals("pm")){
                        startTime  = token[i].substring(0, 1) +":00:00";
                        endTime = (Integer.parseInt(token[i].substring(4,6)) + 12) +":"+token[i].substring(7,9)+ ":00";
                    }
                    else if (token[i].substring(1,3).contentEquals("pm") && token[i].substring(9,11).contentEquals("am")){
                        startTime = (Integer.parseInt(token[i].substring(0, 1)) + 12) +":00:00";
                        endTime = token[i].substring(4,6) +":"+token[i].substring(7,9)+ ":00";
                    }
                    else {
                        startTime = token[i].substring(0, 1) +":00:00";
                        endTime = token[i].substring(4,6) +":"+token[i].substring(7,9)+ ":00";
                    }

                    //startTime = token[i].substring(0, 2) +":"+token[i].substring(3,5)+ ":00";
                    //endTime = token[i].substring(8, 9) +":"+token[i].substring(10,12)+ ":00";
                    System.out.println("55");
                }
            }

            //9.30-10

            if(token[i].toString().length() == 11) {
                if (((token[i].substring(4, 6).contentEquals("am")
                        || token[i].substring(4, 6).contentEquals("pm")) && (token[i].substring(9, 11).contentEquals("am")
                        || token[i].substring(9, 11).contentEquals("pm")))) {

                    if(token[i].substring(4, 6).contentEquals("pm") && token[i].substring(9, 11).contentEquals("pm"))
                    {
                        startTime = (Integer.parseInt(token[i].substring(0, 1)) + 12)+ ":"+token[i].substring(2,4)+ ":00";
                        endTime = (Integer.parseInt(token[i].substring(7,9)) + 12) +":00:00";
                    }
                    else if(token[i].substring(4, 6).contentEquals("am") && token[i].substring(9, 11).contentEquals("pm")){
                        startTime  = token[i].substring(0, 1) +":"+token[i].substring(2,4)+":00";
                        endTime = (Integer.parseInt(token[i].substring(7,9)) + 12) +":00:00";
                    }
                    else if (token[i].substring(4, 6).contentEquals("pm") && token[i].substring(9,11).contentEquals("am")){
                        startTime = (Integer.parseInt(token[i].substring(0, 1)) + 12)+ ":"+token[i].substring(2,4)+ ":00";
                        endTime = token[i].substring(4,6) +":00:00";
                    }
                    else {
                        startTime = token[i].substring(0, 1) +":"+token[i].substring(2,4)+":00";
                        endTime = token[i].substring(7,9)+ ":00";
                    }

                    //startTime = token[i].substring(0, 2) +":"+token[i].substring(3,5)+ ":00";
                    //endTime = token[i].substring(8, 9) +":"+token[i].substring(10,12)+ ":00";
                    System.out.println("66");
                }


            }

            //10-9.30
            if(token[i].toString().length() == 11) {
                if (((token[i].substring(2, 4).contentEquals("am")
                        || token[i].substring(2, 4).contentEquals("pm")) && (token[i].substring(9, 11).contentEquals("am")
                        || token[i].substring(9, 11).contentEquals("pm")))) {

                    if(token[i].substring(2, 4).contentEquals("pm") && token[i].substring(9, 11).contentEquals("pm"))
                    {
                        startTime = (Integer.parseInt(token[i].substring(0, 2)) + 12)+ ":00:00";
                        endTime = (Integer.parseInt(token[i].substring(5,6)) + 12)+":"+token[i].substring(7,9)+":00";
                    }
                    else if(token[i].substring(2, 4).contentEquals("am") && token[i].substring(9, 11).contentEquals("pm")){
                        startTime  = token[i].substring(0, 2) +":00:00";
                        endTime = (Integer.parseInt(token[i].substring(5,6)) + 12) +":"+token[i].substring(7,9)+":00";
                    }
                    else if (token[i].substring(2, 4).contentEquals("pm") && token[i].substring(9,11).contentEquals("am")){
                        startTime = (Integer.parseInt(token[i].substring(0, 2)) + 12)+ ":00:00";
                        endTime = token[i].substring(5,6) +":"+token[i].substring(7,9)+":00";
                    }
                    else {
                        startTime = token[i].substring(0, 2) +":00:00";
                        endTime = token[i].substring(5,6)+":"+token[i].substring(7,9)+":00";
                    }

                    //startTime = token[i].substring(0, 2) +":"+token[i].substring(3,5)+ ":00";
                    //endTime = token[i].substring(8, 9) +":"+token[i].substring(10,12)+ ":00";
                    System.out.println("88");
                }


            }

            //10.30-9
            if(token[i].toString().length() == 11) {
                if (((token[i].substring(5, 7).contentEquals("am")
                        || token[i].substring(5, 7).contentEquals("pm")) && (token[i].substring(9, 11).contentEquals("am")
                        || token[i].substring(9, 11).contentEquals("pm")))) {

                    if(token[i].substring(5, 7).contentEquals("pm") && token[i].substring(9, 11).contentEquals("pm"))
                    {
                        startTime = (Integer.parseInt(token[i].substring(0, 2)) + 12)+":"+token[i].substring(3,5)+ ":00";
                        endTime = (Integer.parseInt(token[i].substring(8,9)) + 12)+":00:00";
                    }
                    else if(token[i].substring(5, 7).contentEquals("am") && token[i].substring(9, 11).contentEquals("pm")){
                        startTime  = token[i].substring(0, 2) +":"+token[i].substring(3,5)+":00";
                        endTime = (Integer.parseInt(token[i].substring(8,9)) + 12) +":00:00";
                    }
                    else if (token[i].substring(5, 7).contentEquals("pm") && token[i].substring(9,11).contentEquals("am")){
                        startTime = (Integer.parseInt(token[i].substring(0, 2)) + 12)+":"+token[i].substring(3,5)+ ":00";
                        endTime = token[i].substring(8,9) +":00:00";
                    }
                    else {
                        startTime = token[i].substring(0, 2) +":"+token[i].substring(3,5)+":00";
                        endTime = token[i].substring(8,9)+":00:00";
                    }

                    //startTime = token[i].substring(0, 2) +":"+token[i].substring(3,5)+ ":00";
                    //endTime = token[i].substring(8, 9) +":"+token[i].substring(10,12)+ ":00";
                    System.out.println("88");
                }


            }




        }
        System.out.println(startTime + " ---- " + endTime);
        System.out.println(googleCal.get(Calendar.YEAR)+"-"+googleCal.get(Calendar.MONTH)+"-"+googleCal.get(Calendar.DATE));

        startDate =  googleCal.get(Calendar.YEAR)+"-"+(googleCal.get(Calendar.MONTH)+1)+"-"+googleCal.get(Calendar.DATE);
        endDate = googleCal.get(Calendar.YEAR)+"-"+(googleCal.get(Calendar.MONTH)+1)+"-"+googleCal.get(Calendar.DATE);

    }

    public static boolean isInt(String s){
        try{
            Integer.parseInt(s);
        }catch (NumberFormatException n)
        {
            return false;
        }
        return true;
    }
}
