package com.parse.starter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class SignUpScreen extends AppCompatActivity {

    // global vars
    public static final String TAG = SignUpScreen.class.getSimpleName();

    EditText userName, email, password, rePassword;
    Button btnRegister;
    TextView txtInfo;

    String pw1_input, pw2_input, eMessage;
    boolean hasUsername = false;
    boolean usernameUnique = false;
    boolean hasEmail = false;
    boolean hasPassword = false;
    boolean passwordsMatch = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialize the page
        setContentView(R.layout.activity_sign_up_screen);

        // this will initializes the variables on the page
        userName = (EditText) findViewById(R.id.tbUserName);
        email = (EditText) findViewById(R.id.tbEmail);
        password = (EditText) findViewById(R.id.tbPassword);
        rePassword = (EditText) findViewById(R.id.tbRePassword);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        txtInfo = (TextView) findViewById(R.id.txtInfo);

        // set the text color because i dont know how to do it in the designer screen
        txtInfo.setTextColor(getResources().getColor(android.R.color.holo_red_dark));


        // the rest of the code in onCreate tells the program what to do when
        // different events are raised

        // userName LooseFocus
        userName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if(userName.getText().length() == 0) {
                        hasUsername = false;
                    }
                    else if(!usernameAvailable(userName.getText().toString())) {
                        hasUsername = true;
                        usernameUnique = false;
                    }
                    else {
                        hasUsername = true;
                        usernameUnique = true;
                    }
                    setTxtInfoMessage();
                }
            }
        });

        // email LooseFocus
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    if(email.getText().length() == 0) hasEmail = false;
                    else hasEmail = true;
                    setTxtInfoMessage();
                }
            }
        });

        // password LooseFocus
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    pw1_input = password.getText().toString();
                    pw2_input = rePassword.getText().toString();
                    if(password.getText().length() == 0) hasPassword = false;
                    else if(!pw1_input.equals(pw2_input)) {
                        hasPassword = true;
                        passwordsMatch = false;
                    }
                    else {
                        hasPassword = true;
                        passwordsMatch = true;
                    }
                    setTxtInfoMessage();
                }
            }
        });

        // rePassword LooseFocus
        rePassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    pw1_input = password.getText().toString();
                    pw2_input = rePassword.getText().toString();

                    if(password.getText().length() == 0) hasPassword = false;
                    else if(!pw1_input.equals(pw2_input)) {
                        hasPassword = true;
                        passwordsMatch = false;
                    }
                    else {
                        hasPassword = true;
                        passwordsMatch = true;
                    }
                    setTxtInfoMessage();
                }
            }
        });

        // btnRegister Click
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRegisterClick(); // call method that handles btnRegister Click
            }
        });
    }

    protected void btnRegisterClick() {
        eMessage = "";
        if (usernameUnique) {
            signUp(userName.getText().toString(), email.getText().toString(), password.getText().toString());

            // if it makes it here then there was an error
            txtInfo.setText(eMessage);
        }
        else {
            setTxtInfoMessage();
        }
    } // btnRegisterClick

    protected void setTxtInfoMessage() {
        String message = "";
        if(!hasUsername) message = "You must enter a username";
        else if(!usernameUnique) message = "Username " + userName.getText().toString() + " is taken";
        else if(!hasEmail) message = "You must enter an email";
        else if(!hasPassword) message = "You must enter a password";
        else if(!passwordsMatch) message = "Passwords do not match";

        txtInfo.setText(message);

    }  // setTbInfoMessage

    protected boolean usernameAvailable(String username) {
        // do this to keep parse from throwing invalidSessionToken error
        ParseUser.getCurrentUser().logOut();

        // get all rows where column.username = username
        ParseQuery<ParseObject> userQuery;
        userQuery = ParseQuery.getQuery("_User");

        // if the list has any elements in it, then the username exists
        try {
            userQuery.whereEqualTo("username", username);

            if (userQuery.count() > 0) {
                return false;
            } else {
                return true;
            }
        } catch (ParseException e) {
            txtInfo.setText(e.getMessage());
            Log.e(TAG, e.getMessage());
            return false;
        }
    } // usernameAvailable

    protected void signUp(String username, String email, String password) {

        ParseUser.getCurrentUser().logOut();
        ParseUser user = new ParseUser();

        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);

        try {
            user.signUp();
            setContentView(R.layout.activity_hub);
        } catch (ParseException e) {
            // sign-up failed :(
            txtInfo.setText(e.getMessage());
            Log.e(TAG, e.getMessage());
        }
    } // signUp

    public void toMainActivity(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void toMainActivityAfterRegistration(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
