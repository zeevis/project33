package com.example.project3;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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





        return v;
    }

    public void getList(Date chosenDate)
    {

        ArrayList<String> dateAndTimeList = new ArrayList<String>();
        try {

            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                    "Shift");
            query.orderByAscending("arrivalTime").whereEqualTo("worker", ParseUser.getCurrentUser());

            List<ParseObject> ob = query.find();
           // numOfPages = ob.size();
            //Toast.makeText(contextOfProphilePage, "num of pages = : "+numOfPages, Toast.LENGTH_LONG).show();
            for (ParseObject po : ob) {
                // Locate images in flag column
                String arrive="",date="",exit="";
                Date dateEnter = (Date)po.getDate("arrivalTime");
                Date dateExit = (Date)po.getDate("leavingTime");
                if(dateEnter.getMonth() < chosenDate.getMonth())
                    continue;
                if(dateEnter.getMonth() > chosenDate.getMonth() )
                    break;

                DateFormat timeFormatter = new SimpleDateFormat("HH:mm");

                if(dateEnter!=null) {
                    arrive = timeFormatter.format(dateEnter.getTime());
                    date = dateEnter.toString().substring(0, 10);
                }
                if(dateExit!=null)
                    exit = timeFormatter.format(dateExit.getTime());



                String finalTimeAndDate = date + "  " + arrive  + " - " + exit;
                dateAndTimeList.add(finalTimeAndDate);





            }
        } catch (ParseException e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        for(String u:users )
        {

        }
    }

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
