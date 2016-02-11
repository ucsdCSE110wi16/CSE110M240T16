package com.parse.starter;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateEvent extends PolarityActivity {

    Button btnBack, btnAddMovie, btnInviteFriends, btnCreateEvent;
    EditText tbEventName, tbEventLocation, tbEventTime, tbEventDescription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        btnBack = (Button) findViewById(R.id.inviteFriends_btnBack);
        btnAddMovie = (Button) findViewById(R.id.inviteFriends_btnSelectAll);
        btnInviteFriends = (Button) findViewById(R.id.inviteFriends_btnInviteFriends);
        btnCreateEvent = (Button) findViewById(R.id.createEvent_btnCreateEvent);

        btnBack.setOnClickListener(btnBack_Click());
        btnAddMovie.setOnClickListener(btnAddMovie_Click());
        btnInviteFriends.setOnClickListener(btnInviteFriends_Click());
        btnCreateEvent.setOnClickListener(btnCreateEvent_Click());
    }

    //region Button Clicks

    protected View.OnClickListener btnAddMovie_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toActivity_AddMovies();
            }
        };
    } // btnAddMovie_Click

    protected View.OnClickListener btnInviteFriends_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toActivity_InviteFriends();
            }
        };
    } // btnInviteFriends_Click

    protected View.OnClickListener btnCreateEvent_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toActivity_HubActivity();
            }
        };
    } // btnCreateEvent_Click

    protected View.OnClickListener btnBack_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toActivity_HubActivity();
            }
        };
    } // btnBack_Click

    //endregion

}
