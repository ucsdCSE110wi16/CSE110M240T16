package com.parse.starter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HubActivity extends PolarityActivity {

    public static final String TAG = SignUp.class.getSimpleName();

    Button btnLogOut, btnCreateEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub);

        btnLogOut = (Button) findViewById(R.id.hubActivity_btnLogOut);
        btnCreateEvent = (Button) findViewById(R.id.hubActivity_btnCreateEvent);

        btnLogOut.setOnClickListener(btnLogOut_Click());
        btnCreateEvent.setOnClickListener(btnCreateEvent_Click());

    }

    //region btnLogOut_Click

    protected View.OnClickListener btnLogOut_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toActivity_Login();
            }
        };
    } // btnLogOut

    protected View.OnClickListener btnCreateEvent_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toActivity_CreateEvent();
            }
        };
    } // btnCreateEvent

    //endregion

}
