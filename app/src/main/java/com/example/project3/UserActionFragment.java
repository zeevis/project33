package com.example.project3;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserActionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserActionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserActionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ListView actionListView;
    ArrayList<String> actionsArr;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    UserMainPage parentActivity;
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserActionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserActionFragment newInstance(String param1, String param2) {
        UserActionFragment fragment = new UserActionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public UserActionFragment() {
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
        View v = inflater.inflate(R.layout.fragment_user_action, container, false);
        actionListView =(ListView)v.findViewById(R.id.actionListView);
        actionsArr = new ArrayList<String>();


        new userActionsDataFromParse(this,this,parentActivity.userid).execute();


        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        actionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                createDidTodayDialog(position);
            }
        });

    }

    public void createDidTodayDialog(final int position)
    {
        ///////////alertdialigprompt//////////

        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.dialog_for_finish_action, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);



        // set dialog message
        alertDialogBuilder.setTitle("")
                .setCancelable(false)
                .setPositiveButton("Save",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text
                                final String strFromListToList = actionsArr.get(position);
                                actionsArr.remove(position);
                                ArrayAdapter<String> adap = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, actionsArr);
                                actionListView.setAdapter(adap);


                                ParseQuery<ParseObject> query = ParseQuery.getQuery("TasksList");




                                query.whereEqualTo("worker", ParseUser.getCurrentUser());
                                String oid = "";
                                try {
                                    List<ParseObject> ob = query.find();
                                    for(ParseObject po : ob)
                                        oid = po.getObjectId();

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }








                                // Retrieve the object by id
                                    query.getInBackground(oid, new GetCallback<ParseObject>() {
                                        @Override
                                        public void done(ParseObject taskList, com.parse.ParseException e) {
                                            if (e == null) {
                                                // Now let's update it with some new data. In this case, only cheatMode and score
                                                // will get sent to the Parse Cloud. playerName hasn't changed.

                                                taskList.add("finishedTasks", strFromListToList);
                                                taskList.removeAll("tasks", Arrays.asList(strFromListToList));
                                                taskList.saveInBackground();
                                            }
                                        }
                                    });

                                    dialog.dismiss();
                                }
                            }

                            )
                                    .

                            setNegativeButton("Cancel",
                                                      new DialogInterface.OnClickListener() {
                                public void onClick (DialogInterface dialog,int id){
                                    dialog.cancel();
                                }
                            }

                            );

                            // create alert dialog
                            AlertDialog alertDialog = alertDialogBuilder.create();

                            // show it
                            alertDialog.show();


                            ////////////////////////////////////


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

        parentActivity = (UserMainPage)activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public ArrayList<String> loadListDateFromParse(ArrayList<String> array, ListView lv, Fragment fragment)
    {
        // array = new ArrayList<String>();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(fragment.getActivity(),android.R.layout.simple_list_item_1,array);
        lv.setAdapter(adapter);



        return array;
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
