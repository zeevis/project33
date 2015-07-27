package com.example.project3;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AllTasksFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AllTasksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllTasksFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    EditText addTaskAutoComplete;
    Button AddToAutoCompleteListBtn;
    ListView lv;
    ArrayList<String> taskList;
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllTasksFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllTasksFragment newInstance(String param1, String param2) {
        AllTasksFragment fragment = new AllTasksFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public AllTasksFragment() {
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
        View v = inflater.inflate(R.layout.fragment_all_tasks, container, false);

        addTaskAutoComplete = (EditText)v.findViewById(R.id.addTaskForAutoCompleteEt);
        AddToAutoCompleteListBtn = (Button)v.findViewById(R.id.AddToAutoCompleteListBtn);
        taskList = new ArrayList<String>();
        lv = (ListView)v.findViewById(R.id.tasksToAutoCompLv);

        AddToAutoCompleteListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ParseObject po = new ParseObject("StaticTasks");
                po.put("taskName", addTaskAutoComplete.getText().toString());
                po.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(com.parse.ParseException e) {
                        if (e == null) {


                            taskList.add(addTaskAutoComplete.getText().toString());
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,taskList);
                            lv.setAdapter(adapter);
                        } else {
                            Log.i("didnt secceed ", e.toString());
                        }
                        addTaskAutoComplete.setText("");

                    }
                });

            }
        });

        return v;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getList();
    }

    public void getList()
    {
        try {
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                    "StaticTasks");
            query.orderByAscending("taskName");//.whereEqualTo("worker", ParseUser.getCurrentUser());
            List<ParseObject> ob = query.find();

            for (ParseObject po : ob) {
               String task =  po.getString("taskName");
                taskList.add(task);
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,taskList);
            lv.setAdapter(adapter);
        }catch (ParseException e){

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
