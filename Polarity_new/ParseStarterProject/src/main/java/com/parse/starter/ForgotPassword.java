package com.parse.starter;

import android.content.Intent;
import android.content.IntentSender;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class ForgotPassword extends PolarityActivity {

    static String TAG = ForgotPassword.class.getSimpleName();

    Button btnBack, btnSendResetEmail;
    EditText tbUserName, tbEmail;

    String username, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        username = "";
        email = "";

        btnBack = (Button) findViewById(R.id.forgotPassword_btnBack);
        btnSendResetEmail = (Button) findViewById(R.id.forgotPassword_btnSendResetEmail);
        tbUserName = (EditText) findViewById(R.id.forgotPassword_tbUserName);
        tbEmail = (EditText) findViewById(R.id.forgotPassword_tbEmail);

        btnBack.setOnClickListener(btnBackOnClickListener());
        btnSendResetEmail.setOnClickListener(btnSendResetEmail_Click());
    }

    //region Button Clicks

   protected View.OnClickListener btnSendResetEmail_Click() {
       return new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               if(tbUserName.getText().length() == 0) {
                    displayToast("You must enter a username");
                   return;
               }
               if(tbEmail.getText().length() == 0) {
                   displayToast("You must enter a password");
                   return;
               }

               username = tbUserName.getText().toString();
               email = tbEmail.getText().toString();

               ParseQuery.getQuery("_User").whereEqualTo("username", username).getFirstInBackground(new GetCallback<ParseObject>() {
                   @Override
                   public void done(ParseObject object, ParseException e) {
                       if (e != null) {
                           Log.e(TAG, e.getMessage());
                           displayToast("Unable to send email");
                           return;
                       }

                       if (object == null) {
                           Log.e(TAG, "No user found");
                           displayToast("Invalid username");
                           return;
                       }

                       if(object.getString("email").compareTo(email) == 0) {
                           sendEmail(object);
                       }
                       else displayToast("Invalid email");
                   }
               });
           }
       };
   }

    //endregion

    protected void sendEmail(ParseObject object) {
        Log.d(TAG, "Send Email");

        String[] TO = {email};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");

        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Polarity Forgot Credentials");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Your Polarity login information was requested.\n\n"
                + "Username: " + object.getString("username")
                + "password" + object.getString("password"));

        try {
            startActivity(Intent.createChooser(emailIntent, "Polarity Credential Request"));
            finish();
            displayToast("Email sent!");
            Log.d(TAG, "Finished sending email");
        }
        catch(android.content.ActivityNotFoundException e) {
            displayToast("Unable to send email");
            Log.e(TAG, e.getMessage());
        }
    }
}
