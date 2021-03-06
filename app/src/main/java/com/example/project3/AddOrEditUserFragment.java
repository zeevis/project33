package com.example.project3;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddOrEditUserFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddOrEditUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddOrEditUserFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    EditText usernameEt,passwordEt,emailEdit,latEt,lngEt;
    AutoCompleteTextView actionAC;
    Button saveBtn;
    ScrollView sv;
    boolean saveOrUpdate;
    String username;
    ArrayList<String> taskList;
   Manager parentActivity;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddOrEditUserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddOrEditUserFragment newInstance(String param1, String param2) {
        AddOrEditUserFragment fragment = new AddOrEditUserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public AddOrEditUserFragment() {
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
        final View v =  inflater.inflate(R.layout.fragment_add_or_edit_user, container, false);
        usernameEt = (EditText)v.findViewById(R.id.usernameEditEt);
        passwordEt = (EditText)v.findViewById(R.id.passwordEditEt);
        actionAC = (AutoCompleteTextView)v.findViewById(R.id.actionEditAutoCompleteTextView);
        emailEdit = (EditText)v.findViewById(R.id.emailEditEt);
        latEt = (EditText)v.findViewById(R.id.geoPointLatEt);
        lngEt = (EditText)v.findViewById(R.id.geoPointLngEt);
        saveBtn = (Button)v.findViewById(R.id.saveUserDetailsbutton);
        taskList = new ArrayList<String>();


        getListOfTasksForAutoComplete();
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!((Manager) getActivity()).saveOrUpdate) {
                    if (!isValidEmail(emailEdit.getText().toString())) {
                        Toast.makeText(getActivity(), "email is invalid", Toast.LENGTH_LONG).show();
                        return;
                    }


                    final ParseUser user = new ParseUser();
                    user.setUsername(usernameEt.getText().toString());
                    user.setPassword(passwordEt.getText().toString());
                    user.setEmail(emailEdit.getText().toString());

                    // other fields can be set just like with ParseObject
                    // user.put("isManager", true);

                    user.signUpInBackground(new SignUpCallback() {

                        @Override
                        public void done(com.parse.ParseException e) {
                            if (e == null) {
                                Log.i("wowwowowow", "sucsess"); // Hooray! Let them use the app now.
                                float lat = latEt.getText().toString().equals("") ? 0 : Float.parseFloat(latEt.getText().toString());
                                float lng = lngEt.getText().toString().equals("") ? 0 : Float.parseFloat(lngEt.getText().toString());
                                SharedPreferences.Editor editor = getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE).edit();
                                editor.putString("action" + user.getObjectId(), actionAC.getText().toString());
                                editor.putFloat("lat" + user.getObjectId(), lat);
                                editor.putFloat("lng" + user.getObjectId(), lng);
                                editor.commit();

                                logoutLogin();

                                final ParseObject actions = new ParseObject("TasksList");
/////////////////////////////////////////////////////////////////////
                                actions.put("worker", ParseObject.createWithoutData(ParseUser.class, user.getObjectId()));
                                actions.add("tasks", actionAC.getText().toString());
                                actions.put("workPlace", new ParseGeoPoint(lat, lng));
                                actions.saveInBackground();
                                Toast.makeText(getActivity(), "saved", Toast.LENGTH_LONG).show();
                            } else {
                                Log.i("!!!!!!!!!!!!!!!111", "no good " + e.toString());
                                // Sign up didn't succeed. Look at the c
                                // to figure out what went wrong
                            }
                        }


                    });

                }



            }
        });



        super.onViewCreated(view, savedInstanceState);

    }

    public void getListOfTasksForAutoComplete()
    {
        try {
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                    "StaticTasks");
            query.orderByAscending("taskName");//.whereEqualTo("worker", ParseUser.getCurrentUser());
            List<ParseObject> ob = query.find();
            String task="";
            for (ParseObject po : ob) {
                task =  po.getString("taskName")+",";
                taskList.add(task);
            }



            String[] arr = taskList.toArray(new String[taskList.size()]);



            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_dropdown_item_1line,  arr);

            actionAC.setAdapter(adapter);
        }catch (ParseException e){

        }
    }

    public void logoutLogin()
    {
        ParseUser.logOut();
       ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null

        ParseUser.logInInBackground(((Manager)getActivity()).username, ((Manager)getActivity()).password);

    }
    public boolean isValidEmail(CharSequence target) {
        if (target == null) {
            {
                return false;
            }
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
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
        parentActivity = (Manager) activity;
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
