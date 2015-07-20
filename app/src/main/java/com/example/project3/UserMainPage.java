package com.example.project3;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by maayan on 04/07/2015.
 */
public class UserMainPage extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks , UserInfoFragment.OnFragmentInteractionListener, UserActionFragment.OnFragmentInteractionListener {
    static String userid;
    boolean enterExitBtnWorks;
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    static boolean inOrOut = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.useractivity);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        Intent intent = getIntent();
        userid= intent.getExtras().getString("userid");
    }








    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments

        Log.wtf("aaa", ""+position);
        if (position == 0) {
                UserInfoFragment uif = new UserInfoFragment();

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, uif)
                        .commit();
        }
        else if (position == 1) {
            UserActionFragment uaf = new UserActionFragment();

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, uaf)
                    .commit();
        }
        else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                    .commit();
        }

    }
    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.manager, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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
    public void onFragmentInteraction(Uri uri) {

    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements LocationListener{
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        ArrayList dateAndTimeList;
        ArrayAdapter<String> adapter;
        String objectIdToRefresh;
        ArrayList<Date> datesNTimesEnter;
        ArrayList<Date> datesNTimesExit;

        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        String timeDataForTodayStr;

        Button enterExitBtn;
        TextView timeAndDateUserTv;
        ListView lv;
        UserMainPage parentActivity;

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_user_main, container, false);
            enterExitBtn = (Button)rootView.findViewById(R.id.enterExitBtn);
            timeAndDateUserTv = (TextView)rootView.findViewById(R.id.timeAndDateUserTv);
            lv = (ListView)rootView.findViewById(R.id.listView);


            new StrDataFromParse(this,this, parentActivity.userid).execute();

           // dateAndTimeList = new ArrayList<String>();



                SharedPreferences prefs = getActivity().getSharedPreferences("prefs", MODE_PRIVATE);
                String str = prefs.getString("ifInside", "0");
               if(prefs.getBoolean("inOrOut", false)) {
                   enterExitBtn.setText("Exit");
                   timeAndDateUserTv.setText(prefs.getString("ifInside", "0"));
                   timeDataForTodayStr = str;

               }















            return rootView;
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            enterExitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

/*
                    if(inOrOut) {
                        SharedPreferences prefs = getActivity().getSharedPreferences("prefs", MODE_PRIVATE);
                        String str = prefs.getString("ifInside", "0");
                        ((Button)v).setText("Exit");
                        timeAndDateUserTv.setText(str);
                    }*/

                    if(((Button)v).getText().equals("Enter")) {
                        Date date = new Date();
                        uploadListDateToParse(date, "arrivalTime",true,"",0);
                        DateFormat timeFormatter = new SimpleDateFormat("HH:mm");
                        String timeDateFormatted = timeFormatter.format(date.getTime());
                       // DateFormat dateFormatter = new SimpleDateFormat("MM/dd/YYYY");
                        String dateFormatted = date.toString().substring(0, 10);;

                        timeDataForTodayStr= dateFormatted +"    "+timeDateFormatted;
                        timeAndDateUserTv.setText(timeDataForTodayStr);
                        enterExitBtn.setText("Exit");


                        SharedPreferences.Editor editor = getActivity().getSharedPreferences("prefs", MODE_PRIVATE).edit();
                        editor.putString("ifInside", timeDataForTodayStr);
                        editor.putBoolean("inOrOut", true);
                        editor.commit();
                        inOrOut = true;
                        return;


                    }
                  if(((Button)v).getText().equals("Exit")) {
                      Date date = new Date();
                    //////  uploadListDateToParse(date, "leavingTime",false,"",0);
                      createDidTodayDialog( date);
                      DateFormat timeFormatter = new SimpleDateFormat("HH:mm");
                      String timeDateFormat = timeFormatter.format(date.getTime());
                      String finalTimeAndDate = timeAndDateUserTv.getText() + " - "+timeDateFormat;
                      dateAndTimeList.add(finalTimeAndDate);

                      adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,dateAndTimeList);
                      lv.setAdapter(adapter);
                      timeAndDateUserTv.setText("");
                      //timeAndDateUserTv.setText(timeAndDateUserTv.getText() + " - "+timeDateFormat);
                      enterExitBtn.setText("Enter");

                      SharedPreferences.Editor editor = getActivity().getSharedPreferences("prefs", MODE_PRIVATE).edit();
                      editor.putBoolean("inOrOut", false);
                      editor.commit();
                      inOrOut = false;

                    }
                }
            });

        }

        public void createDidTodayDialog(final Date date1)
        {
            ///////////alertdialigprompt//////////

            // get prompts.xml view
            LayoutInflater li = LayoutInflater.from(getActivity());
            View promptsView = li.inflate(R.layout.whatdidtoday_promptdialog, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    getActivity());

            // set prompts.xml to alertdialog builder
            alertDialogBuilder.setView(promptsView);

            double numForTransEt = 0;
            SharedPreferences prefs = getActivity().getSharedPreferences("prefs", MODE_PRIVATE);
           // String restoredText = prefs.getString("text", null);
           // if (restoredText != null) {
                String name = prefs.getString("etDialogTrans", "0");//"No name defined" is the default value.
                numForTransEt = Double.parseDouble(name);
            //}

            final EditText userInputDid = (EditText) promptsView
                .findViewById(R.id.editTextWhatDidToday);


            final EditText userInputTrans = (EditText) promptsView
                    .findViewById(R.id.editTextWhatTransportation);

            if(numForTransEt!= 0)
                userInputTrans.setText(""+numForTransEt);
            // set dialog message
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("Save",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // get user input and set it to result
                                    // edit text
                                    String inputDid = userInputDid.getText().toString();
                                    double inputTransport = Double.parseDouble(userInputTrans.getText().toString());



                                    uploadListDateToParse(date1, "leavingTime", false, inputDid, inputTransport);

                                    SharedPreferences.Editor editor =getActivity().getSharedPreferences("prefs", MODE_PRIVATE).edit();
                                    editor.putString("etDialogTrans", ""+inputTransport);
                                    editor.commit();



                                    dialog.dismiss();
                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();


            ////////////////////////////////////



        }


        public ArrayList<String> loadListDateFromParse(ArrayList<String> array, ListView lv, Fragment fragment)
        {
            // array = new ArrayList<String>();

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(fragment.getActivity(),android.R.layout.simple_list_item_1,array);
            lv.setAdapter(adapter);



            return array;
        }
        public void uploadParseObject()
        {

        }

        public void uploadListDateToParse(final Date date, final String column
                , final boolean trueEnterFalseExit,final String didToday, final double transport)
        {
            String g= "";
            if(trueEnterFalseExit) {
                final ParseObject dates = new ParseObject("Shift");
                dates.put("worker", ParseObject.createWithoutData(ParseUser.class, parentActivity.userid));
                dates.put(column, date);
                dates.saveInBackground(new SaveCallback() {
                    @Override
                  public void done(com.parse.ParseException e) {
                        //  Access the object id here
                        objectIdToRefresh = dates.getObjectId();
                        SharedPreferences.Editor editor = getActivity().getSharedPreferences("prefs", MODE_PRIVATE).edit();
                        editor.putString("objectIdToRefresh", objectIdToRefresh);
                        editor.commit();



                    }
                });
            }
            else{
                SharedPreferences prefs = getActivity().getSharedPreferences("prefs", MODE_PRIVATE);
                objectIdToRefresh = prefs.getString("objectIdToRefresh", "0");
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Shift");

// Retrieve the object by id
                query.getInBackground(objectIdToRefresh, new GetCallback<ParseObject>() {
                    @Override
                     public void done(ParseObject dates, com.parse.ParseException e) {
                        if (e == null) {
                            // Now let's update it with some new data. In this case, only cheatMode and score
                            // will get sent to the Parse Cloud. playerName hasn't changed.
                            dates.put(column, date);
                            dates.put("transportationPayments",transport );
                            dates.put("progressDescription", didToday);
                            dates.saveInBackground();
                        }
                    }
                });
            }
        }

        @Override
        public void setRetainInstance(boolean retain) {
            super.setRetainInstance(retain);
        }



        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((UserMainPage) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));

            parentActivity = (UserMainPage)activity;
        }

        @Override
        public void onLocationChanged(Location location) {

            final double lat = location.getLatitude();
            final double longitude = location.getLongitude();


            final Location location1 = location;


            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereEqualTo("objectId", userid);
            query.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    if (e == null) {
                        // The query was successful.
                        ParseGeoPoint workGeoPoint = objects.get(0).getParseGeoPoint("workPlace");

                        ParseGeoPoint currentPoint = new ParseGeoPoint(lat, longitude);

                        if ( Math.abs(workGeoPoint.distanceInKilometersTo(currentPoint)) >0.5) {
                            enterExitBtn.setEnabled(false);
                        } else {
                            enterExitBtn.setEnabled(true);
                        }
                    } else {
                        // Something went wrong.
                    }
                }
            });










        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

}
