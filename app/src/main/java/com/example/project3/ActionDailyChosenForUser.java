package com.example.project3;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ActionDailyChosenForUser.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ActionDailyChosenForUser#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ActionDailyChosenForUser extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ListView chosenDayyActionLv;
    Date dateToCompare;
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ActionDailyChosenForUser.
     */
    // TODO: Rename and change types and number of parameters
    public static ActionDailyChosenForUser newInstance(String param1, String param2) {
        ActionDailyChosenForUser fragment = new ActionDailyChosenForUser();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ActionDailyChosenForUser() {
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
        View v= inflater.inflate(R.layout.fragment_action_daily_chosen_for_user, container, false);

        chosenDayyActionLv = (ListView)v.findViewById(R.id.chosenDayyActionLv);
        String dateToCompareStr = getArguments().getString("dateToCompareStr");


        DateFormat format = new SimpleDateFormat("E MMM dd", Locale.ENGLISH);
        try {
            dateToCompare = format.parse(dateToCompareStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return  v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        presentDailyActions(dateToCompare);
    }

    public void presentDailyActions(final Date dateToCompare)
    {
        final ArrayList<String> dailyActionArray = new ArrayList<String>();
        //final Date dateToCompare = new Date();///*parent.getItemAtPosition(position).toString().substring(0,9);*/ null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Shift");
        query.include("worker");
        query.whereEqualTo("worker", ParseUser.getCurrentUser());
        query.orderByAscending("arrivalTime");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, com.parse.ParseException e) {
                if (e == null) {
                    for(ParseObject po :list)
                    {

                        int dtc_month = dateToCompare.getMonth();
                        int po_month = ((Date)po.getDate("arrivalTime")).getMonth();


                        int dtc_date = dateToCompare.getDate();
                        int po_date =  ((Date)po.getDate("arrivalTime")).getDate();


                        if(dtc_month== po_month && dtc_date==po_date) {
                            String dailyAction =  (String)po.getString("progressDescription");
                            dailyActionArray.add(dailyAction);


                        }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,dailyActionArray);
                    chosenDayyActionLv.setAdapter(adapter);


                } else {

                }
            }
        });
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
