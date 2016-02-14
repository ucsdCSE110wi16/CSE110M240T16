package com.parse.starter;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class HubActivity extends PolarityActivity {

    public static final String TAG = SignUp.class.getSimpleName();

    Button btnLogOut, btnCreateEvent;
    ListView lvEventQueue;
    TextView eventQueueTxt;
    ArrayList<Model> eventQueue;
    CustomAdapter adapter;

    @Override
    public void onBackPressed(){
        //DON'T DO IT, I'll phone children's aid
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub);

        btnLogOut = (Button) findViewById(R.id.hubActivity_btnLogOut);
        btnCreateEvent = (Button) findViewById(R.id.hubActivity_btnCreateEvent);

        btnLogOut.setOnClickListener(btnLogOut_Click());
        btnCreateEvent.setOnClickListener(btnCreateEvent_Click());

        lvEventQueue = (ListView) findViewById(R.id.hubActivity_lvEventQueue);
        eventQueueTxt = (TextView) findViewById(R.id.upcomingEvents_txtInfo);

        if(com_eventQueue.size() == 0){
           eventQueueTxt.setText("No upcoming events");
        }
        else{
            eventQueueTxt.setText("");
        }

        eventQueue = com_eventQueue;
        adapter = new CustomAdapter(getApplicationContext(), eventQueue);
        lvEventQueue.setAdapter(adapter);

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
