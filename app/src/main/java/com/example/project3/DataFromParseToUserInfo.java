package com.example.project3;

import android.app.Activity;
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

/**
 * Created by zeev on 7/13/2015.
 */
public class DataFromParseToUserInfo extends AsyncTask<Void, Void,Integer> {

    Activity activity;
    Fragment fragment;

    static ArrayList< Date> arriveDates = new ArrayList<Date>();
    static ArrayList<Date> exitDates = new ArrayList<Date>();
    String userid;
    int counter = 0;


    public DataFromParseToUserInfo(UserInfoFragment context,UserInfoFragment activity,String userid)
    {
        fragment = context;
        //this.activity = activity;

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

                arriveDates.add(dateEnter);
                exitDates.add(dateExit);



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
        ArrayList<String> dateAndTimeList = new ArrayList<String>();
      /*  if(arriveDates == null)
            ((UserInfoFragment)fragment).datesNTimesEnter = new ArrayList<Date>();
        else
            ((UserInfoFragment)fragment).datesNTimesEnter = arriveDates;
        if(exitDates == null)
            ((UserInfoFragment)fragment).datesNTimesExit = new ArrayList<Date>();
        else
            ((UserInfoFragment)fragment).datesNTimesExit = exitDates;
*/

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
            dateAndTimeList.add(finalTimeAndDate);
        }
        if(dateAndTimeList != null)
            ((UserInfoFragment)fragment).dateAndTimeList = dateAndTimeList;
        else
            ((UserInfoFragment)fragment).dateAndTimeList = new ArrayList<String>();

        ((UserInfoFragment)fragment).loadListDateFromParse(dateAndTimeList, ((UserInfoFragment) fragment).allInfoLv, (UserInfoFragment) fragment);



    }


}


