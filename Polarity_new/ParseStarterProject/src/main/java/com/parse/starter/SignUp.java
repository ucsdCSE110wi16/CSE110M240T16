package com.parse.starter;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUp extends PolarityActivity {

    //region Declare Variables

    // some tag thing for writing error messages to log screen
    public static final String TAG = SignUp.class.getSimpleName();

    // make local references to display elements
    EditText userName, email, password, rePassword;
    Button btnRegister;
    TextView txtInfo;

    // basic variables
    String pw1_input, pw2_input, eMessage;
    boolean hasUsername = false;
    boolean usernameUnique = false;
    boolean hasEmail = false;
    boolean hasPassword = false;
    boolean passwordsMatch = false;

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialize the page
        setContentView(R.layout.activity_sign_up);

        // this will initializes the variables on the page
        userName = (EditText) findViewById(R.id.createEvent_tbName);
        email = (EditText) findViewById(R.id.createEvent_tbLocation);
        password = (EditText) findViewById(R.id.main_tbPassword);
        rePassword = (EditText) findViewById(R.id.tbRePassword);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        txtInfo = (TextView) findViewById(R.id.txtInfo);

        // set the text color because i dont know how to do it in the designer screen
        txtInfo.setTextColor(getResources().getColor(android.R.color.holo_red_dark));

        // userName LooseFocus
        userName.setOnFocusChangeListener(usernameFocusChanged());

        // email LooseFocus
        email.setOnFocusChangeListener(emailFocusChanged());

        // password LooseFocus
        password.setOnFocusChangeListener(pwFocusChanged());

        // rePassword LooseFocus
        rePassword.setOnFocusChangeListener(pwFocusChanged());

        // rePassword textChaned
        rePassword.addTextChangedListener(pwTextChanged());

        // btnRegister Click
        btnRegister.setOnClickListener(btnRegisterClick());
    }

    //region EditText Events

    protected View.OnFocusChangeListener usernameFocusChanged() {
        return new View.OnFocusChangeListener() {
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
        };
    } // usernameFocusChanged

    protected View.OnFocusChangeListener emailFocusChanged() {
        return new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    if(email.getText().length() == 0) hasEmail = false;
                    else hasEmail = true;
                    setTxtInfoMessage();
                }
            }
        };
    } // emailFocusChanged

    protected View.OnFocusChangeListener pwFocusChanged() {
        return new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    passwordsMatch();
                    setTxtInfoMessage();
                }
            }
        };
    } // pwFocusChanged

    protected TextWatcher pwTextChanged() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordsMatch();
                setTxtInfoMessage();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
    } // pwTextChanged

    //endregion

    //region Button Click Events

    protected View.OnClickListener btnRegisterClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp(userName.getText().toString(), email.getText().toString(), password.getText().toString());
            }
        };
    } // btnRegisterClick

    //endregion

    //region Helper Methods

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
        ParseUser.getCurrentUser().logOutInBackground();

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

    protected void passwordsMatch() {
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
    } // passwordsMatch

    protected void signUp(String username, String email, String password) {

        ParseUser.getCurrentUser().logOutInBackground();
        ParseUser user = new ParseUser();

        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    // Sign Up was successful!
                    toActivity_HubActivity();
                }//end if
                else{
                    // sign-up failed :(
                    txtInfo.setText(e.getMessage());
                    Log.e(TAG, e.getMessage());
                }//end else
            }
        });
    } // signUp

    //endregion

}
