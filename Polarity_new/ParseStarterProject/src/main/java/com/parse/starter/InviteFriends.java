package com.parse.starter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class InviteFriends extends PolarityActivity {

    Button btnBack, btnHome, btnSelectAll, btnInviteFreinds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friends);

        btnBack = (Button) findViewById(R.id.inviteFriends_btnBack);
        btnHome = (Button) findViewById(R.id.inviteFriends_btnHome);
        btnSelectAll = (Button) findViewById(R.id.inviteFriends_btnSelectAll);
        btnInviteFreinds = (Button) findViewById(R.id.inviteFriends_btnInviteFriends);

        btnBack.setOnClickListener(btnBack_Click());
        btnHome.setOnClickListener(btnHome_Click());
        btnSelectAll.setOnClickListener(btnSelectAll_Click());
        btnInviteFreinds.setOnClickListener(btnInviteFriends_Click());

    }


    //region Button Clicks

    protected View.OnClickListener btnBack_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toActivity_CreateEvent();
            }
        };
    } // btnBack_Click

    protected View.OnClickListener btnHome_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toActivity_HubActivity();
            }
        };
    } // btnHome_Click

    protected View.OnClickListener btnSelectAll_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };
    } // btnSelectAll_Click

    protected View.OnClickListener btnInviteFriends_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toActivity_CreateEvent();
            }
        };
    } // btnInviteFriends_Click

    //endregion


}
