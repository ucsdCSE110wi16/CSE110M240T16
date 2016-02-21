package com.parse.starter;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class HubActivity extends PolarityActivity {

    public static final String TAG = HubActivity.class.getSimpleName();

    // region declare variables
    Button btnLogOut, btnCreateEvent;
    ListView lvEventQueue;
    TextView txtEventQueue;
    EventAdapter adapter;
    // endregion

    // region prevent going back to log out screen
    @Override
    public void onBackPressed(){
        // prevent user from going back to the login screen without logging out
    }
    // endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub);

        // region findViewById
        btnLogOut = (Button) findViewById(R.id.hubActivity_btnLogOut);
        btnCreateEvent = (Button) findViewById(R.id.hubActivity_btnCreateEvent);

        btnLogOut.setOnClickListener(btnLogOut_Click());
        btnCreateEvent.setOnClickListener(btnCreateEvent_Click());

        lvEventQueue = (ListView) findViewById(R.id.hubActivity_lvEventQueue);
        txtEventQueue = (TextView) findViewById(R.id.upcomingEvents_txtInfo);

        lvEventQueue.setOnItemClickListener(lvEventQueue_Click());

        // endregion

        // If com_userEvents is not populated yet, then populate it
        if(com_eventModelList.size() == 0) {
            fetchEvents();
        }

        // region empty list status message
        // check if the queue is empty (this means the user wasn't invited to anything)
        if(com_eventModelList.size() == 0){
           txtEventQueue.setText("No upcoming events");
        }
        else{
            txtEventQueue.setText("");
        }
        // endregion

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

    //region ListView Click

    private AdapterView.OnItemClickListener lvEventQueue_Click() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                com_currentEvent = (EventModel) lvEventQueue.getItemAtPosition(position);
                if(com_currentEvent == null) {
                    Log.e(TAG, "Event returned NULL");
                    displayToast("Unable to open event");
                }
                toActivity_ViewEvent();
            }
        };
    }

    //endregion

    //region Helpers

    private void fetchEvents() {

        List<ParseObject> parseEventList = new LinkedList<ParseObject>();
        List<ParseObject> parseFriendList = new LinkedList<ParseObject>();
        LinkedList<String> userEventIds = new LinkedList<String>();
        EventModel model;
        int num_voted = 0, num_attending = 0;

        try {
            // FETCH * FROM InvitedFriends WHERE UserID IS com_userID
            parseEventList = ParseQuery.getQuery("InvitedFriends").whereEqualTo("UserID", com_userID).find();

            // debug output
            Log.d(TAG, "Fetched all events user is invited to. Size = " + parseEventList.size());
            Log.d(TAG, "com_userID = " + com_userID);

            // push all the eventId's onto an array of eventIds
            for(ParseObject obj : parseEventList) {
                // if the user hasn't already decided not to go to the event
                if(obj.getInt("Confirmation") != 3) {
                    userEventIds.add(obj.getString("EventID"));
                    Log.d(TAG, "Added Event[" + obj.getString("EventID") + "] to queue");
                }
                else Log.d(TAG, "Skipped 1 event");
            }

            // clear the parseEventList
            parseEventList.clear();

            // Add all the event that we found in InvitedFriends to the list of events
            for(String Id : userEventIds) {
                parseEventList.addAll(ParseQuery.getQuery("Event").whereEqualTo("objectId", Id).find());
            }

                Log.d(TAG, "Found " + parseEventList.size() + " non-denied events");

                for (ParseObject obj : parseEventList) {
                    // Create new EventModel
                    model = new EventModel(com_userID, obj.getString("EventName"),
                            obj.getString("EventDiscription"), obj.getString("EventLocation"),
                            obj.getObjectId(), obj.getString("MovieQueueID"),
                            obj.getDate("EventDate"));

                    // Get all friends invited
                    parseFriendList.clear();

                    Log.d(TAG, "Working with Event[" + model.getEventID() + "]");

                    parseFriendList = ParseQuery.getQuery("InvitedFriends").whereEqualTo("EventID", model.getEventID()).find();
                    model.setNumFriendsAttending(parseFriendList.size());

                    Log.d(TAG, "Event[" + model.getEventID() + "] Number of friends invited = " + model.getNumFriendsAttending());

                    num_voted = 0;
                    num_attending = 0;
                    for(ParseObject person : parseFriendList) {
                        // check if the person is the user

                        Log.d(TAG, "person[" + person.getString("UserID") + "] Status=" + person.getInt("Confirmation"));

                        if(person.getString("UserID").compareTo(com_userID) == 0) {

                            Log.d(TAG, "User FOUND");

                            num_attending++;
                            switch(person.getInt("Confirmation")) {
                                case 2:
                                    num_voted++;
                                    model.status = EventModel.Status.AcceptedAndVoted;
                                    break;
                                case 1:
                                    model.status = EventModel.Status.Accepted;
                            }
                        }
                        // the person is a friend
                        else {
                            switch(person.getInt("Confirmation")) {
                                case 2: num_voted++;
                                case 1: num_attending++;
                            }
                        }
                    }

                    model.setNumFriendsVoted(num_voted);
                    model.setNumFriendsAttending(num_attending);

                    // add to eventModelList
                    com_eventModelList.add(model);
                }

            // sort the list
            Collections.sort(com_eventModelList, new EventModelComparator());

        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    //endregion

}

