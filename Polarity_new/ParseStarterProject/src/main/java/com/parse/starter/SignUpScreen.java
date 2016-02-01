package com.parse.starter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_screen);
    }

    public static final String TAG = SignUpActivityToBeDeleted.class.getSimpleName();

    protected SignUpCallback mSignUpCallback = new SignUpCallback() {
        @Override
        public void done(ParseException e) {

            if(e != null){
                Log.e(TAG, e.getMessage());
                //TODO: Report failure to the user
            }//end if

        }//end done
    };//end callback

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
}
