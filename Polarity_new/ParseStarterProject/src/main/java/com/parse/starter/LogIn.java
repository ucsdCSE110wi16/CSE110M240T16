package com.parse.starter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseUser;

public class LogIn extends AppCompatActivity {

    //region Create Variables

    private Button btnLogin, btnCreateAccount, btnForgotPassword;
    private EditText userName, password;
    TextView txtInfo;

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

    } // onCreate

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
                }
                catch (ParseException e) {
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

    public void goToForgotPassword(){
        Intent intentObject = new Intent(this,ForgotPassword.class);
        startActivity(intentObject);
    }

    public void goToHub(){
        Intent intentObject = new Intent(this, HubActivity.class);
        startActivity(intentObject);
    }

    public void goToSignUpScreen(){
        Intent intentObject = new Intent(this, SignUp.class);
        startActivity(intentObject);
    }

    //endregion
}



