package com.example.project3;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MonthlyReportFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MonthlyReportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MonthlyReportFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    Button getReportBtn;
    ListView usersList;
    DatePicker dp;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MonthlyReportFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MonthlyReportFragment newInstance(String param1, String param2) {
        MonthlyReportFragment fragment = new MonthlyReportFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MonthlyReportFragment() {
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
        View v =  inflater.inflate(R.layout.fragment_monthly_report, container, false);
        usersList = (ListView)v.findViewById(R.id.workersNameForMRListV);
        getReportBtn = (Button)v.findViewById(R.id.GetReportBtn);
        Toast.makeText(getActivity(),ParseUser.getCurrentUser().getUsername(),Toast.LENGTH_LONG).show();
        dp = (DatePicker)v.findViewById(R.id.datePicker);


        try {
            Field f[] = dp.getClass().getDeclaredFields();
            for (Field field : f) {
                if (field.getName().equals("mDaySpinner")||field.getName().equals("mYearSpinner")) {
                    field.setAccessible(true);
                    Object yearPicker = new Object();
                    yearPicker = field.get(dp);
                    ((View) yearPicker).setVisibility(View.GONE);
                }
            }
        }
        catch (SecurityException e) {
            Log.d("ERROR", e.getMessage());
        }
        catch (IllegalArgumentException e) {
            Log.d("ERROR", e.getMessage());
        }
        catch (IllegalAccessException e) {
            Log.d("ERROR", e.getMessage());
        }


        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListOfUsers();

      //  usersList.seto
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void getListOfUsers()
    {

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        final ArrayList<String> users = new ArrayList<String>();
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, com.parse.ParseException e) {
                if (e == null) {
                    // The query was successful.
                    for (ParseUser pu: objects)
                    {
                        String username =  pu.getUsername();
                        users.add(username);


                    }
                   // ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,users);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_checked, users);

                    usersList.setAdapter(adapter);
                    usersList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

                    getReportBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            SparseBooleanArray sp = usersList.getCheckedItemPositions();
                            ArrayList<String> finalUsersToPublish = new ArrayList<String>();
                            String str = "";
                            for (int i = 0; i < sp.size(); i++) {
                                finalUsersToPublish.add(users.get(sp.keyAt(i)));
                            }

                            Bundle bundle = new Bundle();
                            bundle.putStringArrayList("usersForReport", finalUsersToPublish);
                            bundle.putStringArrayList("chosenMonth", new ArrayList<String>() {{
                                add("" + dp.getMonth());
                            }});



                            MonthlyFinalReportFragment mfrf = new MonthlyFinalReportFragment();
                            mfrf.setArguments(bundle);
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.container, mfrf)
                                    .addToBackStack(null)
                                    .commit();



                        }
                    });



                } else {
                    // Something went wrong.
                }
            }
        });


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
