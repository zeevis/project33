package com.example.project3;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WorkerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WorkerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkerFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ListView usersList;
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WorkerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WorkerFragment newInstance(String param1, String param2) {
        WorkerFragment fragment = new WorkerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public WorkerFragment() {
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
        View v =  inflater.inflate(R.layout.fragment_worker, container, false);
        usersList = (ListView)v.findViewById(R.id.usersList);
       return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListOfUsers();

        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {


                ParseQuery<ParseUser> query = ParseUser.getQuery();
                query.whereEqualTo("username",parent.getItemAtPosition(position).toString());
                query.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                   public void done(List<ParseUser> objects, com.parse.ParseException e) {
                        if (e == null) {
                            // The query was successful.
                            final String userid =objects.get(0).getObjectId();


                            ParseUser user = objects.get(0);



                            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                                    "TasksList");
                            query.include("worker");
                            // Locate the column named "ranknum" in Parse.com and order list
                            // by ascending


                            List<ParseObject> ob = null;
                            try {
                                ob = query.find();
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }

                             for (ParseObject po : ob) {
                                 // Locate images in flag column

                                 if (po.getParseObject("worker") != null)
                                     if( po.getParseObject("worker").getString("username")!=null )
                                         if( po.getParseObject("worker").getString("username").equals(parent.getItemAtPosition(position).toString()) ) {
                                           createEditUserDialog(userid, po.getObjectId());

                                 }

                             }






























/*

                                ParseQuery<ParseUser> innerQuery = ParseUser.getQuery();
                            innerQuery.equalTo("username", userid);

                            ParseQuery query = new ParseQuery("TasksList");
                            query.whereMatchesQuery("worker", innerQuery);
                            query.findInBackground(new FindCallback<ParseUser>(){


                                @Override
                                public void done(List<ParseUser> objects, com.parse.ParseException e) {
                                    // resultList now contains the class A objects which match the class B query.
                                    createEditUserDialog(userid, objects.get(0).getObjectId());
                                }


                            });
*/

                            /*

                            ParseQuery<ParseObject> query = ParseQuery.getQuery("TaskList");
                            query.whereEqualTo("worker",user.getParseUser() );
                            query.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                 public void done(List<ParseObject> list, com.parse.ParseException e) {
                                    if (e == null) {

                                       // if(list.get(0)!=null)
                                         createEditUserDialog(userid, list.get(0).getObjectId());
                                        } else {

                                    }
                                }
                            });
*/







                        } else {
                            // Something went wrong.
                        }
                    }
                });
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

    public void createEditUserDialog(final String userid, final String tasksListObjId)
    {
        ///////////alertdialigprompt//////////

        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.edit_user_prompt_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);


        SharedPreferences prefs = getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        // String restoredText = prefs.getString("text", null);
        // if (restoredText != null) {
        String action = prefs.getString("action"+userid, "0");//"No name defined" is the default value.
        float lat = prefs.getFloat("lat" + userid, 0);
        float lng = prefs.getFloat("lng" + userid, 0);




        final EditText actionEt = (EditText) promptsView
                .findViewById(R.id.actionEditOldUser);


        final EditText latEt = (EditText) promptsView
                .findViewById(R.id.latEditOldUser);

        final EditText lngEt = (EditText) promptsView
                .findViewById(R.id.lngEditOldUser);

        actionEt.setText(action);
        latEt.setText(lat+"");
        lngEt.setText(lng+"");

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setTitle("Edit " + userid)
                .setPositiveButton("Save",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text


                                final float lat = Float.parseFloat(latEt.getText().toString());
                                final float lng = Float.parseFloat(lngEt.getText().toString());


                                ParseQuery<ParseObject> query = ParseQuery.getQuery("TasksList");

                                // Retrieve the object by id
                                ///////////////////////////
                                query.getInBackground(tasksListObjId, new GetCallback<ParseObject>() {
                                    @Override
                                    public void done(ParseObject actions, com.parse.ParseException e) {
                                        if (e == null) {
                                            // Now let's update it with some new data. In this case, only cheatMode and score
                                            // will get sent to the Parse Cloud. playerName hasn't changed.
                                            actions.put("worker", ParseObject.createWithoutData(ParseUser.class, userid));
                                            actions.add("tasks", Arrays.asList(actionEt.getText().toString()));
                                            actions.put("workPlace", new ParseGeoPoint(lat, lng));
                                            actions.saveInBackground();
                                        }
                                    }
                                });

/*
                                final ParseObject actions = new ParseObject("TasksList");

                                actions.put("worker", ParseObject.createWithoutData(ParseUser.class, userid));
                                actions.add("tasks", Arrays.asList(actionEt.getText().toString()));
                                actions.put("workPlace", new ParseGeoPoint(lat, lng));
                                actions.saveInBackground();
*/
                                SharedPreferences.Editor editor = getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE).edit();
                                editor.putString("action" + userid, actionEt.getText().toString());
                                editor.putFloat("lat" + userid, lat);
                                editor.putFloat("lng" + userid, lng);
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
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,users);
                    usersList.setAdapter(adapter);

                } else {
                    // Something went wrong.
                }
            }
        });


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
