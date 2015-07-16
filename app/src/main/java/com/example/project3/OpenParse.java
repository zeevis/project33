package com.example.project3;

import android.content.Context;

import com.parse.Parse;

/**
 * Created by maayan on 04/07/2015.
 */
public class OpenParse {

    private OpenParse(Context c) {
        Parse.enableLocalDatastore(c);
        Parse.initialize(c, "StL1Iz9EgoOGQYPr5Rs1ywDxcWOMQqYzH6hIhsNE", "SngtCgnX3ookYYufo11FL3WSS0JtOwlE51BeWgf7");


    }

    private static OpenParse logic;

    public static OpenParse getOpenPares(Context c) {
        if (logic == null)
            logic = new OpenParse(c);
        return logic;
    }

}