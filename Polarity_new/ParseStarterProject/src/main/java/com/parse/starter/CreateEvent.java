package com.parse.starter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class CreateEvent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
    }

    public void toHubActivity(View view){
        Intent intentObject = new Intent(this, HubActivity.class);
        startActivity(intentObject);
    }

    public void toAddMovies(View view){
        Intent intent = new Intent(this, AddMovies.class);
        startActivity(intent);
    }

    public void toInviteFriends(View view){
        Intent intent = new Intent(this, InviteFriends.class);
        startActivity(intent);
    }

    public void eventCreatedToHubActivity(View view){
        Intent intent = new Intent(this, HubActivity.class);
        startActivity(intent);
    }
}
