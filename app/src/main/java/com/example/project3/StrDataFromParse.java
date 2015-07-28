package com.example.project3;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StrDataFromParse extends AsyncTask<Void, Void,Integer> {
    Context contextOfProphilePage;
    Activity activity;
    Fragment fragment;

    ArrayList< Date> arriveDates = new ArrayList<Date>();
    ArrayList<Date> exitDates = new ArrayList<Date>();
    String userid;
    int counter = 0;
    Date currentDate;


    public StrDataFromParse(UserMainPage.PlaceholderFragment context,UserMainPage.PlaceholderFragment activity,String userid)
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
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                    "Shift");
            // Locate the column named "ranknum" in Parse.com and order list
            // by ascending
            query.orderByAscending("arrivalTime").whereEqualTo("worker", ParseUser.getCurrentUser());

            List<ParseObject> ob = query.find();
            numOfPages = ob.size();
            //Toast.makeText(contextOfProphilePage, "num of pages = : "+numOfPages, Toast.LENGTH_LONG).show();
            for (ParseObject saleObject : ob) {
                // Locate images in flag column

                Date dateEnter = (Date)saleObject.getDate("arrivalTime");
                Date dateExit = (Date)saleObject.getDate("leavingTime");
                if(dateEnter.getMonth() < currentDate.getMonth())
                    continue;
                if(dateEnter.getMonth() > currentDate.getMonth() )
                    break;

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
        ((UserMainPage.PlaceholderFragment)fragment).dateAndTimeList = new ArrayList<String>();
        if(arriveDates == null)
            ((UserMainPage.PlaceholderFragment)fragment).datesNTimesEnter = new ArrayList<Date>();
        else
            ((UserMainPage.PlaceholderFragment)fragment).datesNTimesEnter = arriveDates;
        if(exitDates == null)
            ((UserMainPage.PlaceholderFragment)fragment).datesNTimesExit = new ArrayList<Date>();
        else
            ((UserMainPage.PlaceholderFragment)fragment).datesNTimesExit = exitDates;


        for(Date dateArrive:arriveDates) {
            Date dateExit = exitDates.get(arriveDates.indexOf(dateArrive));
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



            String finalTimeAndDate = date + "  " + arrive  + " - " + exit;
            ((UserMainPage.PlaceholderFragment)fragment).dateAndTimeList.add(finalTimeAndDate);
        }
      /*  if(dateAndTimeList != null)
           ((UserMainPage.PlaceholderFragment)fragment).dateAndTimeList = dateAndTimeList;
        else
            ((UserMainPage.PlaceholderFragment)fragment).dateAndTimeList = new ArrayList<String>();*/

        ((UserMainPage.PlaceholderFragment)fragment).loadListDateFromParse(((UserMainPage.PlaceholderFragment)fragment).dateAndTimeList, ((UserMainPage.PlaceholderFragment) fragment).lv, (UserMainPage.PlaceholderFragment) fragment);



    }


}


