package com.parse.starter;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LogIn extends AppCompatActivity implements View.OnKeyListener{

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_log_in);

        // Set elements
        btnLogin = (Button) findViewById(R.id.main_btnLogin);
        btnCreateAccount = (Button) findViewById(R.id.main_btnCreateAccount);
        btnForgotPassword = (Button) findViewById(R.id.main_btnForgotPassword);
        userName = (EditText) findViewById(R.id.main_tbUserName);
        password = (EditText) findViewById(R.id.main_tbPassword);
        txtInfo = (TextView) findViewById(R.id.main_tbInfo);

        btnLogin.setOnClickListener(btnLogin_Click());
        btnCreateAccount.setOnClickListener(btnCreateAccount_Click());
        btnForgotPassword.setOnClickListener(btnForgotPassword_Click());
        userName.setOnKeyListener(this);

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

                try {
                    ParseUser.logIn(userName.getText().toString(), password.getText().toString());

                    // save variables to global file
                    //Common com = ((Common)getApplicationContext());
                    //com._username = userName.getText().toString();
                    //com._userKey = ParseUser.getCurrentUser().getObjectId().toString();

                    goToHub();
                } catch (ParseException e) {
                    btnLogin.setEnabled(true);
                    btnCreateAccount.setEnabled(true);
                    btnForgotPassword.setEnabled(true);
                    txtInfo.setText(e.getMessage());
                }
            }
        };
    } // btnLogin_Click

    private View.OnClickListener btnCreateAccount_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSignUpScreen();
            }
        };
    } // btnCreateAccount_Click

    private View.OnClickListener btnForgotPassword_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToForgotPassword();
            }
        };
    } // btnForgotPassword_Click

    //endregion

    //region Page Transfers

    public void goToForgotPassword() {
        Intent intentObject = new Intent(this, ForgotPassword.class);
        startActivity(intentObject);
    }

    public void goToHub() {
        Intent intentObject = new Intent(this, HubActivity.class);
        startActivity(intentObject);
    }

    public void goToSignUpScreen() {
        Intent intentObject = new Intent(this, SignUp.class);
        startActivity(intentObject);
    }

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



