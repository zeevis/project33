package com.example.project3;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zeev on 7/13/2015.
 */
public class DataFromParseToManagerMain extends AsyncTask<Void, Void,Integer> {
    Context contextOfProphilePage;
    Activity activity;
    Fragment fragment;

    static ArrayList< Date> arriveDates = new ArrayList<Date>();
    static ArrayList<Date> exitDates = new ArrayList<Date>();
    static ArrayList<String> names = new ArrayList<String>();

    static ArrayList<String>  onlyArrived = new ArrayList<>();



    String userid;
    int counter = 0;
    Date currentDate;


    public DataFromParseToManagerMain(Manager.PlaceholderFragment context,Manager.PlaceholderFragment activity,String userid)
    {
        fragment = context;
        //this.activity = activity;
        currentDate = new Date();
        this.userid = userid;
    }
    @Override
    protected void onPreExecute() {

        super.onPreExecute();

    }

    @Override
    protected Integer doInBackground(Void... params) {
            // Create the array
            Integer numOfPages = 0;
            try {
            // Locate the class table named "Country" in Parse.com

                ParseQuery<ParseUser> uquery = ParseUser.getQuery();
                final ArrayList<String> users = new ArrayList<String>();
                uquery.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> objects, com.parse.ParseException e) {
                        if (e == null) {

                            for(ParseUser pu:objects)
                            {
                                if(pu.getBoolean("isInShift"))
                                {
                                   Date dateArrive= pu.getDate("arrivedAt");
                                    DateFormat timeFormatter = new SimpleDateFormat("HH:mm");

                                    if(dateArrive!=null) {
                                        String arrive = timeFormatter.format(dateArrive.getTime());
                                        String date = dateArrive.toString().substring(0, 10);
                                        String name = pu.getUsername();
                                        String finalStr = name +":  "+  date + "  " + arrive;
                                        onlyArrived.add(finalStr);
                                    }



                                }
                            }
                            ((Manager.PlaceholderFragment)fragment).loadListDateFromParse(onlyArrived, ((Manager.PlaceholderFragment) fragment).onlyEnterLv, (Manager.PlaceholderFragment) fragment);

                        } else {

                        }
                    }});






            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                    "Shift");
                query.include("worker");
            // Locate the column named "ranknum" in Parse.com and order list
            // by ascending
            query.orderByAscending("arrivalTime");//.whereEqualTo("worker", ParseUser.getCurrentUser());

            List<ParseObject> ob = query.find();
            numOfPages = ob.size();
            //Toast.makeText(contextOfProphilePage, "num of pages = : "+numOfPages, Toast.LENGTH_LONG).show();
            for (ParseObject po : ob) {
                // Locate images in flag column


                Date dateEnter = (Date)po.getDate("arrivalTime");
                Date dateExit = (Date)po.getDate("leavingTime");



                if(dateEnter!=null && dateEnter.getMonth() < currentDate.getMonth())
                    continue;


                if(dateEnter!=null && dateEnter.getDate() < currentDate.getDate())
                     continue;

                if(dateEnter!=null && dateEnter.getDate() > currentDate.getDate() )
                    break;

                if(po.getParseObject("worker")!=null) {
                    String n = "" + po.getParseObject("worker").getString("username");
                    names.add(n);
                }
                else
                    names.add("");
                arriveDates.add(dateEnter);
                exitDates.add(dateExit);




				/*
				image.getDataInBackground(new GetDataCallback() {

                    @Override
                    public void done(byte[] imageInBytes, ParseException pEx) {
                        // TODO Auto-generated method stub
                    	Bitmap bmp = BitmapFactory.decodeByteArray(imageInBytes, 0, imageInBytes.length);
                        picList.add(bmp);


                    }
                });*/

            }
        } catch (ParseException e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return numOfPages;
    }

    @Override
    protected void onPostExecute(Integer result) {
        //ProphilePage.listOfSales = sales;
        // ((ProphilePage) contextOfProphilePage).salesFromListToTextView(sales);
        ArrayList<String> onlyEnterList = new ArrayList<String>();
        ArrayList<String> enterAndExitList = new ArrayList<String>();
      /*  if(arriveDates == null)
            ((Manager.PlaceholderFragment)fragment).datesNTimesEnter = new ArrayList<Date>();
        else
            ((UserMainPage.PlaceholderFragment)fragment).datesNTimesEnter = arriveDates;
        if(exitDates == null)
            ((Manager.PlaceholderFragment)fragment).datesNTimesExit = new ArrayList<Date>();
        else
            ((Manager.PlaceholderFragment)fragment).datesNTimesExit = exitDates;
*/

        for(Date dateArrive:arriveDates) {
            Date dateExit = exitDates.get(arriveDates.indexOf(dateArrive));
            String name = names.get(arriveDates.indexOf(dateArrive));
            String arrive ="";
            String exit="";
            String date ="";



            DateFormat timeFormatter = new SimpleDateFormat("HH:mm");

            if(dateArrive!=null) {
                arrive = timeFormatter.format(dateArrive.getTime());
                date = dateArrive.toString().substring(0, 10);
            }
            if(dateExit!=null)
                exit = timeFormatter.format(dateExit.getTime());



            String finalTimeAndDate =name +":  "+  date + "  " + arrive  + " - " + exit;
            enterAndExitList.add(finalTimeAndDate);

           /*

            if(exit.equals(""))
                onlyEnterList.add(finalTimeAndDate);
            else
                enterAndExitList.add(finalTimeAndDate);*/
        }

        //.whereEqualTo("worker", ParseUser.getCurrentUser());
      //  ((Manager.PlaceholderFragment)fragment).loadListDateFromParse(onlyEnterList, ((Manager.PlaceholderFragment) fragment).onlyEnterLv, (Manager.PlaceholderFragment) fragment);
        ((Manager.PlaceholderFragment)fragment).loadListDateFromParse(enterAndExitList, ((Manager.PlaceholderFragment) fragment).enterAndExitLv, (Manager.PlaceholderFragment) fragment);



    }


}


