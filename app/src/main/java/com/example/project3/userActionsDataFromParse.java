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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zeev on 7/11/2015.
 */
public class userActionsDataFromParse extends AsyncTask<Void, Void,Integer> {
    Context contextOfProphilePage;
    Activity activity;
    Fragment fragment;

    static ArrayList< Date> arriveDates = new ArrayList<Date>();
    static ArrayList<Date> exitDates = new ArrayList<Date>();

    static ArrayList<String> actionsArr = new ArrayList<String>();

    String userid;
    int counter = 0;


    public userActionsDataFromParse(UserActionFragment context,UserActionFragment activity,String userid)
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
                    "TasksList");
            // Locate the column named "ranknum" in Parse.com and order list
            // by ascending
            query.whereEqualTo("worker", ParseUser.getCurrentUser());

            List<ParseObject> ob = query.find();
            numOfPages = ob.size();
            //Toast.makeText(contextOfProphilePage, "num of pages = : "+numOfPages, Toast.LENGTH_LONG).show();
            for (ParseObject Object : ob) {
                // Locate images in flag column


                actionsArr = (ArrayList)Object.getList("tasks");





            }
        } catch (ParseException e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return numOfPages;
    }

    @Override
    protected void onPostExecute(Integer result) {

        (( UserActionFragment)fragment).actionsArr = actionsArr;
        (( UserActionFragment)fragment).loadListDateFromParse(actionsArr, (( UserActionFragment)fragment).actionListView,( UserActionFragment)fragment);

    }


}


