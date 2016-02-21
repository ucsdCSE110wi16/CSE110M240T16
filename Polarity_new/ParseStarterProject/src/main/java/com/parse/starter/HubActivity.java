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
    EventAdapter adapter;

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
            fetchEvents();
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

    //region Helpers

    private void fetchEvents() {
        List<ParseObject> parseEventList = new LinkedList<ParseObject>();
        List<ParseObject> parseFriendList = new LinkedList<ParseObject>();
        LinkedList<String> userEventIds = new LinkedList<String>();
        PriorityQueue<EventModel> pqEvent;
        EventModelComparator comparator = new EventModelComparator();
        EventModel model;

        try {
            // FETCH * FROM InvitedFriends WHERE UserID IS com_userID
            parseEventList = ParseQuery.getQuery("FriendsInvited").whereEqualTo("UserID", com_userID).find();

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
            parseEventList = ParseQuery.getQuery("Event").whereEqualTo("UserID", com_userID).find();

            // Add all the event that we found in InvitedFriends to the list of events
            for(int i=0; i<userEventIds.size()-1; i++) {
                parseEventList.addAll(ParseQuery.getQuery("Events").whereEqualTo("objectId",
                        userEventIds.get(i)).find());
            }

            pqEvent = new PriorityQueue<>(parseEventList.size(), comparator);

            for (ParseObject obj : parseEventList) {
                // Create new EventModel
                model = new EventModel(com_userID, obj.getString("EventName"),
                        obj.getString("EventDiscription"), obj.getString("objectId"),
                        obj.getString("MovieQueueID"), obj.getDate("createdAt"));

                // Get all friends invited
                parseFriendList.clear();
                parseFriendList = ParseQuery.getQuery("InvitedFriends").whereEqualTo("EventID", model.getEventID()).find();
                model.setNumFriendsAttending(parseFriendList.size());

                // get all the friends attending & voted
                parseFriendList.clear();
                parseFriendList = ParseQuery.getQuery("InvitedFriends").whereEqualTo("Confirmation", 1).find();
                model.setNumFriendsVoted(parseFriendList.size());

                // get all the friends attending & NOT voted
                parseFriendList.clear();
                parseFriendList = ParseQuery.getQuery("InvitedFriends").whereEqualTo("Confirmation", 2).find();
                model.setNumFriendsAttending(parseFriendList.size());

                // Set Status
                switch(obj.getInt("Confirmation")) {
                    case 1:
                        model.status = EventModel.Status.Accepted;
                        break;
                    case 2:
                        model.status = EventModel.Status.AcceptedAndVoted;
                        break;
                }

                // add to eventModelList
                com_eventModelList.add(model);
            }


        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    //endregion

}

