package com.example.project3;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.opencsv.CSVWriter;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MonthlyFinalReportFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MonthlyFinalReportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MonthlyFinalReportFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ArrayList<String> users;
    ListView usersList;
    Button sendMailBtn;
    Uri u;
    boolean todelete = false;
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MonthlyFinalReportFragment.
     */
    static String g = "";
    // TODO: Rename and change types and number of parameters
    public static MonthlyFinalReportFragment newInstance(String param1, String param2) {
            MonthlyFinalReportFragment fragment = new MonthlyFinalReportFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MonthlyFinalReportFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        users = getArguments().getStringArrayList("usersForReport");
        View v =  inflater.inflate(R.layout.fragment_monthly_final_report, container, false);
        usersList =(ListView)v.findViewById(R.id.finalListOfUsersforReport);
        sendMailBtn = (Button)v.findViewById(R.id.sendMonthlyReportMailBtn);




        return v;
    }


    public  void clearCsv() {
        String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
        String fileName = "AnalysisData.csv";
        String filePath = baseDir + "/"/*File.separator*/ + fileName;
        File f = new File(filePath );


        FileWriter fw = null;
        try {
            fw = new FileWriter(f, false);

        PrintWriter pw = new PrintWriter(fw, false);
        pw.flush();
        pw.close();
        fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void getList(Date chosenDate,ArrayList<String> users)
    {

      clearCsv();
        ArrayList<String> dateAndTimeList = new ArrayList<String>();

            try {

                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                        "Shift");
                query.orderByAscending("arrivalTime");//.whereEqualTo("worker", ParseUser.getCurrentUser());
                query.include("worker");
                List<ParseObject> ob = query.find();
                // numOfPages = ob.size();
                //Toast.makeText(contextOfProphilePage, "num of pages = : "+numOfPages, Toast.LENGTH_LONG).show();

                for(String uid:users) {
                    dateAndTimeList.add(uid);
                    csvSave("worker", "Date", "arrived", "left", "total hours","progress","transportation payments");
                    for (ParseObject po : ob) {
                        if(po.getParseObject("worker")!=null) {
                            if (po.getParseObject("worker").getString("username") != null)
                                if (po.getParseObject("worker").getString("username").equals(uid)) {
                                    // Locate images in flag column
                                    String arrive = "", date = "", exit = "",action="",totalHours="",trans="";
                                    double enterHour=0,exitHour=0,enterMin = 0,exitMin= 0;
                                    Date dateEnter = (Date) po.getDate("arrivalTime");
                                    Date dateExit = (Date) po.getDate("leavingTime");


                                    if (dateEnter.getMonth() < chosenDate.getMonth())
                                        continue;
                                    if (dateEnter.getMonth() > chosenDate.getMonth())
                                        break;

                                    action = (String) po.getString("progressDescription");
                                    trans = ""+(double)po.getDouble("transportationPayments");


                                    DateFormat timeFormatter = new SimpleDateFormat("HH:mm");

                                    if (dateEnter != null) {
                                        arrive = timeFormatter.format(dateEnter.getTime());
                                        date = dateEnter.toString().substring(0, 10);
                                        enterHour = dateEnter.getHours();
                                        enterMin = dateEnter.getMinutes();
                                    }
                                    if (dateExit != null) {
                                        exit = timeFormatter.format(dateExit.getTime());
                                        exitHour = dateExit.getHours();
                                        exitMin = dateExit.getMinutes();

                                    }
                                    totalHours = (exitHour + exitMin / 60 - (enterHour + enterMin / 60)) +"";

                                    String finalTimeAndDate = date + "  " + arrive + " - " + exit;
                                    u = csvSave(uid, date, arrive, exit, totalHours,action,trans);

                                    dateAndTimeList.add(finalTimeAndDate);
                                }
                        }

                    }
                }
            } catch (ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();

            }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,dateAndTimeList);
        usersList.setAdapter(adapter);

    }

    public Uri  csvSave(String name,String date, String enter,String exit,String totalHours,String action ,String transport)
    {
        String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
        String fileName = "AnalysisData.csv";
        String filePath = baseDir + "/"/*File.separator*/ + fileName;
        File f = new File(filePath );
        CSVWriter writer;




// File exist
        try {
        if(f.exists() && !f.isDirectory()){
            FileWriter mFileWriter = null;

                mFileWriter = new FileWriter(f , true);

            writer = new CSVWriter(mFileWriter);
        }
        else {
            writer = new CSVWriter(new FileWriter(f));
        }
        String[] data = {name,date,enter, exit,totalHours,action,transport};

        writer.writeNext(data);

        writer.close();
        }
        catch (IOException e) {
        e.printStackTrace();
    }
        f.setReadable(true, false);
        Uri u = Uri.fromFile(f);
        return  u;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
       //final Uri u =  csvSave("a","b","c","d" ,"e");
       // todelete = true;
        Date chosenDate = new Date();
        getList(chosenDate, users);
        sendMailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{"zeevis@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
                i.putExtra(Intent.EXTRA_TEXT, "body of email");
                i.putExtra(Intent.EXTRA_STREAM, u);
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getActivity(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }


            }
        });

       /* if(todelete)
        {
            File fdelete = new File(u.getPath());
            if (fdelete.exists()) {
                fdelete.delete();
            }
            todelete = false;
        }*/

    }

/*
    @Override
    public void onPause() {
        super.onPause();
        if(todelete)
        {
            File fdelete = new File(u.getPath());
            if (fdelete.exists()) {
                fdelete.delete();
            }
            todelete = false;
        }
    }
    */

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
