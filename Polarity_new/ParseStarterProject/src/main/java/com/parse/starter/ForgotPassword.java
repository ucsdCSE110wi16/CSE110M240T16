package com.parse.starter;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ForgotPassword extends PolarityActivity {

    Button btnBack, btnSendResetEmail;
    EditText tbUserName, tbEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        btnBack = (Button) findViewById(R.id.forgotPassword_btnBack);
        btnSendResetEmail = (Button) findViewById(R.id.forgotPassword_btnSendResetEmail);
        tbUserName = (EditText) findViewById(R.id.forgotPassword_tbUserName);
        tbEmail = (EditText) findViewById(R.id.forgotPassword_tbEmail);

//        btnBack.setOnClickListener(btnBack_Click());
    }

    protected View.OnClickListener btnBack_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toActivity_Login();
            }
        };
    } // btnBack_Click

}
