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

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class HubActivity extends PolarityActivity {

    public static final String TAG = HubActivity.class.getSimpleName();

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
        if(com_eventModelList.size() == 0) {
                    List<ParseObject> parseEventList = new LinkedList<ParseObject>();
                    List<ParseObject> parseFriendList = new LinkedList<ParseObject>();
                    LinkedList<String> userEventIds = new LinkedList<String>();
                    PriorityQueue<EventModel> pqEvent = new PriorityQueue<EventModel>();
                    EventModel model;

                    try {
                        // FETCH * FROM InvitedFriends WHERE UserID IS com_userID
                        parseEventList = ParseQuery.getQuery("InvitedFriends").whereEqualTo("UserID", com_userID).find();

                        // debug output
                        Log.d(TAG, "Fetched all events user is invited to. Size = " + parseEventList.size());

                        // push all the eventId's onto an array of eventIds
                        for(ParseObject obj : parseEventList) {
                            // if the user hasn't already decided not to go to the event
                            if(obj.getInt("Confirmation") != 3) {
                                userEventIds.add(obj.getString("EventID"));
                            }
                        }

                        // clear the parseEventList
                        parseEventList.clear();
                        // FETCH * FROM Events WHERE UserID IS com_userID
                        parseEventList = ParseQuery.getQuery("Events").whereEqualTo("UserID", com_userID).find();

                        // debug
                        Log.d(TAG, "Fetched all events user is hosting. Size = " + parseEventList.size());

                        // Add all the event that we found in InvitedFriends to the list of events
                        for(int i=0; i<userEventIds.size()-1; i++) {
                            parseEventList.addAll(ParseQuery.getQuery("Events").whereEqualTo("objectId",
                                    userEventIds.get(i)).find());
                        }

                        // debug
                        Log.d(TAG, "Total size of events to be put on queue = " + parseEventList.size());

                        for (ParseObject obj : parseEventList) {
                            // Create new EventModel
                            model = new EventModel(com_userID, obj.getString("EventName"),
                                    obj.getString("EventDiscription"), obj.getString("objectId"),
                                    obj.getString("MovieQueueID"), obj.getDate("createdAt"));

                            // debug
                            Log.d(TAG, "Created new model. ID = " + model.getEventID());


                            // Get all friends invited
                            parseFriendList.clear();
                            parseFriendList = ParseQuery.getQuery("InvitedFriends").whereEqualTo("EventID", model.getEventID()).find();
                            model.setNumFriendsAttending(parseFriendList.size());

                            // debug
                            Log.d(TAG, "model has " + model.getNumFriendsInvited() + " friends invited");

                            // get all the friends attending & voted
                            parseFriendList.clear();
                            parseFriendList = ParseQuery.getQuery("InvitedFriends").whereEqualTo("Confirmation", 1).find();
                            model.setNumFriendsVoted(parseFriendList.size());

                            // debug
                            Log.d(TAG, "model has " + model.getNumFriendsVoted() + " friends voted");

                            // get all the friends attending & NOT voted
                            parseFriendList.clear();
                            parseFriendList = ParseQuery.getQuery("InvitedFriends").whereEqualTo("Confirmation", 2).find();
                            model.setNumFriendsAttending(parseFriendList.size());

                            // debug
                            Log.d(TAG, "model has " + model.getNumFriendsAttending() + " friends attending");

                            // add to eventModelList
                            com_eventModelList.add(model);
                        }

        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
        }
    }


        // check if the queue is empty (this means the user wasn't invited to anything)
        if(com_eventModelList.size() == 0){
           txtEventQueue.setText("No upcoming events");
        }
        else{
            txtEventQueue.setText("");
        }

        // Set this stuff up either way, it'll just be empty if there are no events
        adapter = new EventAdapter(getApplicationContext(), com_eventModelList);
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

