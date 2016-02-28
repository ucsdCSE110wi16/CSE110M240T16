package com.parse.starter;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class LogIn extends PolarityActivity implements View.OnKeyListener{

    //region Create Variables

    public static final String TAG = SignUp.class.getSimpleName();

    private Button btnLogin, btnCreateAccount, btnForgotPassword;
    private EditText userName, password;
    TextView txtInfo;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    //endregion

    @Override
    public void onBackPressed(){
        //DON'T DO IT, I'll phone children's aid
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // check to see if any user has logged in on this device before
        try {
            ParseObject object = ParseQuery.getQuery("_User").whereEqualTo("androidId", android_id).getFirst();
            boolean autoLogin = object.getBoolean("autoLogin");
            //If the user previously logged out manually, they don't want to be logged in automatically
            if(autoLogin) {
                com_userID = object.getObjectId();
                com_user = object.getString("username");
                goToActivity(HubActivity.class.getSimpleName(), HubActivity.class.getSimpleName());
            }//end if
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
        }

        setContentView(R.layout.activity_log_in);

        // Set elements
        btnLogin = (Button) findViewById(R.id.main_btnLogin);
        btnCreateAccount = (Button) findViewById(R.id.main_btnCreateAccount);
        btnForgotPassword = (Button) findViewById(R.id.main_btnForgotPassword);
        userName = (EditText) findViewById(R.id.main_tbUserName);
        password = (EditText) findViewById(R.id.main_tbPassword);
        txtInfo = (TextView) findViewById(R.id.main_tbInfo2);

        btnLogin.setOnClickListener(btnLogin_Click());
        btnCreateAccount.setOnClickListener(btnCreateAccount_Click());
        btnForgotPassword.setOnClickListener(btnForgotPassword_Click());
        userName.setOnKeyListener(this);
        userName.setOnClickListener(editText_click());
        password.setOnClickListener(editText_click());

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    } // onCreate

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {
        if(event.getAction() == KeyEvent.ACTION_DOWN) {
            // check if userName has focus AND the ENTER key was pressed
            if(userName.hasFocus() && keyCode == KeyEvent.KEYCODE_ENTER) {
                password.requestFocus(); // puts onto a focus queue
                view.clearFocus(); // removes current focus causing the app to pull password's focus request from queue
                return true;
            }
        }
        return false; // pass on to listeners
    } // onKey

    private View.OnClickListener editText_click(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "entering editText_click");
                txtInfo.setText("");
            }
        };
    }

    //region Button Clicks

    private View.OnClickListener btnLogin_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // disable buttons so user wont spaz-click it while logging in
                btnLogin.setEnabled(false);
                btnCreateAccount.setEnabled(false);
                btnForgotPassword.setEnabled(false);

                ParseUser.getCurrentUser().logOut();

                ParseUser.logInInBackground(userName.getText().toString(),
                       password.getText().toString(), new LogInCallback() {
                             @Override
                             public void done(ParseUser user, ParseException e) {
                                 if(e == null && user != null){

                                     // save variables to global file
                                     com_user = ParseUser.getCurrentUser().getUsername();
                                     com_userID = ParseUser.getCurrentUser().getObjectId();

                                     ParseUser.getCurrentUser().put("androidId", android_id);
                                     ParseUser.getCurrentUser().put("autoLogin", true);
                                     ParseUser.getCurrentUser().saveInBackground();

                                     toActivity_HubActivity();

                                 }//end if
                                 else if(user == null){
                                     btnLogin.setEnabled(true);
                                     btnCreateAccount.setEnabled(true);
                                     btnForgotPassword.setEnabled(true);
                                     txtInfo.setText(e.getMessage());

                                     /* Ego and Superego can display some message
                                      * here if there should be a specific message indicating
                                      * that the username or password were invalid
                                      */
                                 }//end else if
                                 else{
                                     btnLogin.setEnabled(true);
                                     btnCreateAccount.setEnabled(true);
                                     btnForgotPassword.setEnabled(true);
                                     txtInfo.setText(e.getMessage());

                                     /* Ego and Superego can display some message here if there
                                      * should be a specific message indicating that some internal
                                      * error occurred
                                      */
                                 }//end else

                             }//end callback
                });//end logInInBackground


            }
        };
    } // btnLogin_Click

    private View.OnClickListener btnCreateAccount_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toActivity_SignUp();
            }
        };
    } // btnCreateAccount_Click

    private View.OnClickListener btnForgotPassword_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toActivity_ForgotPassword();
            }
        };
    } // btnForgotPassword_Click

    //endregion

    //region Auto-Generated Stuff

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "LogIn Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.parse.starter/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "LogIn Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.parse.starter/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    //endregion
}



