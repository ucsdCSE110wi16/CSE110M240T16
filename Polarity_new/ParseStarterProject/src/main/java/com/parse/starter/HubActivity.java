package com.parse.starter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;

public class HubActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub);
    }

    public void onLogoutClick(View view){

        //TODO:
        // Log out of user account


        Intent intentObject = new Intent(this, MainActivity.class);
        startActivity(intentObject);
    }
}
