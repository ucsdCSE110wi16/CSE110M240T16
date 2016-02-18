package com.parse.starter;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class HubActivity extends PolarityActivity {

    public static final String TAG = SignUp.class.getSimpleName();

    Button btnLogOut, btnCreateEvent;
    ListView lvEventQueue;
    TextView txtEventQueue;
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
        txtEventQueue = (TextView) findViewById(R.id.upcomingEvents_txtInfo);



        // If com_userEvents is not populated yet, then populate it
        if(com_userEvents.size() == 0) {
            List<ParseObject> parseEventList = new LinkedList<ParseObject>();
            List<ParseObject> parseFriendList = new LinkedList<ParseObject>();
            LinkedList<String> userEventIds = new LinkedList<String>();
            PriorityQueue<MovieEvent> pqEvent = new PriorityQueue<MovieEvent>();

            try {
                parseEventList = ParseQuery.getQuery("InvitedFriends").whereEqualTo("UserID", com_userID).find();
                for(ParseObject obj : parseEventList) {
                    userEventIds.add(obj.getString("EventID"));
                }
                parseEventList.clear();
                parseEventList = ParseQuery.getQuery("Events").whereEqualTo("UserID", com_userID).find();
                for(int i=0; i<userEventIds.size()-1; i++) {
                    parseEventList.addAll(ParseQuery.getQuery("Events").whereEqualTo("objectId",
                            userEventIds.get(i)).find());
                }


                for (ParseObject obj : parseEventList) {
                    //TODO: input all the desired value into a new MovieEvent object and stuff it in a pq
                }
            } catch (ParseException e) {
                Log.e(TAG, e.getMessage());
            }
        }


        // check if the queue is empty (this means the user wasn't invited to anything)
        if(com_userEvents.size() == 0){
           txtEventQueue.setText("No upcoming events");
        }
        else{
            txtEventQueue.setText("");
        }

        // Set this stuff up either way, it'll just be empty if there are no events
        adapter = new CustomAdapter(getApplicationContext(), com_eventModelList);
        lvEventQueue.setAdapter(adapter);

    }

    //region Button Clicks

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

