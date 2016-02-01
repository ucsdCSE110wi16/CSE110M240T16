package com.parse.starter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class HubActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub);
    }

    public void toMainActivity(View view){
        Intent intentObject = new Intent(this, MainActivity.class);
        startActivity(intentObject);
    }

    public void toCreateEvent(View view){
        Intent intentObject = new Intent(this, CreateEvent.class);
        startActivity(intentObject);
    }

}
