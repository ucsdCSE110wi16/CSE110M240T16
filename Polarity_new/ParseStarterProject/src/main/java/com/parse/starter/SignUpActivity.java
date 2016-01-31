/*
Author: Kendrick Mausolf
Description: This activity captures user input from the username, password, and email fields
in order to create a new Polarity account for the user.
Acknowledgements: Thomas Mushayi on teamtreehouse for his tutorial of ParseUser sign up stuff
 */

package com.parse.starter;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {

    public static final String TAG = SignUpActivity.class.getSimpleName();

    protected SignUpCallback mSignUpCallback = new SignUpCallback() {
        @Override
        public void done(ParseException e) {

            if(e != null){
                Log.e(TAG, e.getMessage());
                //TODO: Report failure to the user
            }//end if

        }//end done
    };//end callback

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }//end onCreate

    protected void signUp(String username, String email, String password){

        ParseUser newUser = new ParseUser();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.signUpInBackground(mSignUpCallback);

        //Did the user actually get created?
        if(newUser.isNew()){

            //TODO: Report success to the user

        }//end if

    }//end signUp

}//end class
