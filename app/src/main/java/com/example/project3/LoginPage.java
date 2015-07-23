package com.example.project3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by maayan on 04/07/2015.
 */

public class LoginPage extends ActionBarActivity {
    EditText username;
    EditText password;
    Button logInBusiness;
    ParseObject users;
    static final int USER_ID = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OpenParse.getOpenPares(this);

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            // do stuff with the user
            if (currentUser.getBoolean("isManager")) {
                Intent intent = new Intent(LoginPage.this, Manager.class);
                intent.putExtra("userid", currentUser.getObjectId());
                setResult(RESULT_OK, intent);
                startActivity(intent);
            } else {
                Intent intent = new Intent(LoginPage.this, UserMainPage.class);
                intent.putExtra("userid", currentUser.getObjectId());
                setResult(RESULT_OK, intent);
                startActivity(intent);
            }
        } else {

            setContentView(R.layout.activity_login_page);
            username = (EditText) findViewById(R.id.username);
            password = (EditText) findViewById(R.id.password);
            logInBusiness = (Button) findViewById(R.id.logInAsBusinessBtn);

            //73EWZuGtnf



/*
				ParseUser user = new ParseUser();
				user.setUsername("d");
				user.setPassword("2");
				//user.setEmail("email@example.com");

				// other fields can be set just like with ParseObject
				user.put("isManager", true);

				user.signUpInBackground(new SignUpCallback() {

                    @Override
                    public void done(com.parse.ParseException e) {
                        if (e == null) {
                            Log.i("wowwowowow", "sucsess"); // Hooray! Let them use the app now.
                        } else {
                            Log.i("!!!!!!!!!!!!!!!111", "no good");
                            // Sign up didn't succeed. Look at the ParseException
                            // to figure out what went wrong
                        }
                    }


				});

*/


            logInBusiness.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub


                    ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
                        @Override
                        public void done(ParseUser parseUser, com.parse.ParseException e) {
                            if (parseUser != null) {
                                if (parseUser.getBoolean("isManager")) {
                                    Intent intent = new Intent(LoginPage.this, Manager.class);
                                    intent.putExtra("userid", parseUser.getObjectId());
                                    setResult(RESULT_OK, intent);
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(LoginPage.this, UserMainPage.class);
                                    intent.putExtra("userid", parseUser.getObjectId());
                                    setResult(RESULT_OK, intent);
                                    startActivity(intent);
                                }


                            } else {
                                Toast.makeText(LoginPage.this, "your username or password are invalid", Toast.LENGTH_LONG).show();
                            }
                        }


                    });


                }
            });

        }


    }
}
