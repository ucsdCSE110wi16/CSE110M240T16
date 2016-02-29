package com.parse.starter;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class HubActivity extends PolarityActivity {

    public static final String TAG = HubActivity.class.getSimpleName();

    // region declare variables
    Button btnLogOut, btnCreateEvent, btnAddFriends;
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

        // region Initialize Vars
        com_activityHistory.clear(); // clear this just incase there was a bug

        btnLogOut = (Button) findViewById(R.id.hubActivity_btnLogOut);
        btnCreateEvent = (Button) findViewById(R.id.hubActivity_btnCreateEvent);
        btnAddFriends = (Button) findViewById(R.id.hubActivity_btnAddFriends);

        btnLogOut.setOnClickListener(btnLogOut_Click());
        btnCreateEvent.setOnClickListener(btnCreateEvent_Click());
        btnAddFriends.setOnClickListener(btnAddFriends_Click());

        lvEventQueue = (ListView) findViewById(R.id.hubActivity_lvEventQueue);
        txtEventQueue = (TextView) findViewById(R.id.hubActivity_txtInfo);

        lvEventQueue.setOnItemClickListener(lvEventQueue_Click());

        // endregion

        adapter = new EventAdapter(getApplicationContext(), com_eventModelList);
        lvEventQueue.setAdapter(adapter);

        // If com_userEvents is not populated yet, then populate it
        if(com_eventModelList.size() == 0) {
            fetchEvents();
        }
        if(com_friendIdList.size() == 0) fetchFriends();

        // check if the queue is empty (this means the user wasn't invited to anything)
        if(com_eventModelList.size() == 0){
           txtEventQueue.setText("No upcoming events");
        }
        else{
            txtEventQueue.setText("");
        }
    }

    //region Button Clicks

    protected View.OnClickListener btnLogOut_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If the user manually logs out, this means that they should have to manually log
                //in again
                ParseQuery.getQuery("_User").whereEqualTo("objectId", com_userID).getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        if(object != null && e == null){
                            object.put("autoLogin", false);
                            object.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e != null) {
                                        Log.e(TAG, e.getMessage());
                                    }//end if
                                }//end done
                            });//end SaveCallback
                            goToActivity(TAG, LogIn.class.getSimpleName());
                        }//end if
                        else{
                            Log.e(TAG, e.getMessage());
                        }
                    }//end done
                });//end GetCallback
            }
        };
    } // btnLogOut

    protected View.OnClickListener btnCreateEvent_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivity(TAG, CreateEvent.class.getSimpleName());
            }
        };
    } // btnCreateEvent

    protected View.OnClickListener btnAddFriends_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivity(TAG, ViewUsers.class.getSimpleName());
            }
        };
    } // btnAddFriends_Click

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
                com_currentEventId = com_currentEvent.getEventID();
                goToActivity(TAG, ViewEventActivity.class.getSimpleName());
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
            ParseQuery<ParseObject> query = ParseQuery.getQuery("InvitedFriends");
            parseEventList = query.whereEqualTo("UserID", com_userID).find();

            for(ParseObject obj : parseEventList) {
                    if(obj.getInt("Confirmation") != 3) {
                        userEventIds.add(obj.getString("EventID"));
                    }
            }

            // clear the parseEventList
            parseEventList.clear();

            // Add all the event that we found in InvitedFriends to the list of events
            for(String Id : userEventIds) {
                parseEventList.add(ParseQuery.getQuery("Event").whereEqualTo("objectId", Id).getFirst());
            }

            for (ParseObject obj : parseEventList) {

                // this should make it so it skips old events
                if(obj.getDate("EventDate").before(new Date())) continue;

                // Create new EventModel
                model = new EventModel(com_userID, obj.getString("EventName"),
                        obj.getString("EventDiscription"), obj.getString("EventLocation"),
                        obj.getObjectId(), obj.getString("MovieQueueID"),
                        obj.getDate("EventDate"), obj.getString("EventStartTime"));

                switch(obj.getInt("Confirmation")) {
                    case 0:
                        model.status = EventModel.Status.Unanswered;
                        break;
                    case 1:
                        model.status = EventModel.Status.Accepted;
                    case 2:
                        model.status = EventModel.Status.AcceptedAndVoted;
                }

                // Get all friends invited
                parseFriendList.clear();

                parseFriendList = ParseQuery.getQuery("InvitedFriends").whereEqualTo("EventID", model.getEventID()).find();
                model.setNumFriendsAttending(parseFriendList.size());

                Log.d(TAG, "Event[" + model.getEventID() + "] Number of friends invited = " + model.getNumFriendsAttending());

                num_voted = 0;
                num_attending = 0;
                for(ParseObject person : parseFriendList) {
                    // check if the person is the user

                    Log.d(TAG, "person[" + person.getString("UserID") + "] Status=" + person.getInt("Confirmation"));

                    model.isHost = false;
                    if(person.getString("UserID").compareTo(com_userID) == 0) {

                        Log.d(TAG, "User FOUND");

                        model.isHost = true;

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
    } // fetchEvents

    private void fetchFriends() {
        ParseQuery.getQuery("FriendsFollowing").whereEqualTo("UserID", com_userID).findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                List<ParseObject> userList = new LinkedList<ParseObject>();

                for(ParseObject friend : objects) {
                    ParseQuery.getQuery("_User").whereEqualTo("objectId", friend.getString("FriendID")).getFirstInBackground(addFriend());
                }
            }
        });
    } // fetchFriends

    private com.parse.GetCallback<ParseObject> addFriend() {
        return new com.parse.GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject obj, ParseException e) {
                if(obj != null && e == null) {
                    com_friendIdList.add(obj.getObjectId());
                }//end if
                else{
                    Log.e(TAG, e.getMessage());
                }
            }
        };
    } // addFriend

    //endregion

}

