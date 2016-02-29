package com.parse.starter;


import android.app.Activity;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

/** Global variables and methods go here
 *  Created by Lucas Pettit 02/10/2016
 */
public abstract class PolarityActivity extends Activity {

    //region Declare Variables

    // global variables - must be declared static
    static boolean com_userEventsLoaded;

    static String android_id;
    static String com_user, com_userID, com_eventID;
    static String com_eventName, com_eventLocation, com_eventDate, com_eventTime, com_eventDescription;
    static String com_currentEventId;
    static String com_previousActivity; // flag for deletion

    static EventModel com_currentEvent;

    static ArrayList<String> com_friendIdList;
    static ArrayList<Model> com_modelList;
    static ArrayList<MovieModel> com_movieList;
    static ArrayList<FriendModel> com_invitedFriends;
    static ArrayList<EventModel> com_eventModelList;

    static Stack<String> com_activityHistory;

    static HashMap<String, Class> com_activities;

    //endregion

    //region Initialize

    // initialize the variables. Only do this ONCE in MainActivity
    protected void initialize() {

        if(android_id == null || android_id.length() == 0){
            android_id = Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        }

        com_userEventsLoaded = false;

        com_user = "";
        com_userID = "";
        com_eventID = "";
        com_eventName = "";
        com_eventLocation = "";
        com_eventTime = "";
        com_eventDate = "";
        com_eventDescription = "";
        com_currentEventId = "";
        com_previousActivity = LogIn.class.getSimpleName();

        com_currentEvent = null;

        com_modelList = new ArrayList<Model>();
        com_movieList = new ArrayList<MovieModel>();
        com_invitedFriends = new ArrayList<FriendModel>();
        com_friendIdList = new ArrayList<String>();
        com_eventModelList = new ArrayList<EventModel>();

        com_activityHistory = new Stack<String>();

        // Create and fill HashMap of all classes for navigation
        com_activities = new HashMap<String, Class>();
        com_activities.put(LogIn.class.getSimpleName(), LogIn.class);
        com_activities.put(AddFriends.class.getSimpleName(), AddFriends.class);
        com_activities.put(AddMovies.class.getSimpleName(), AddMovies.class);
        com_activities.put(CreateEvent.class.getSimpleName(), CreateEvent.class);
        com_activities.put(EventConfirmationActivity.class.getSimpleName(), EventConfirmationActivity.class);
        com_activities.put(ForgotPassword.class.getSimpleName(), ForgotPassword.class);
        com_activities.put(HubActivity.class.getSimpleName(), HubActivity.class);
        com_activities.put(InviteFriends.class.getSimpleName(), InviteFriends.class);
        com_activities.put(SignUp.class.getSimpleName(), SignUp.class);
        com_activities.put(ViewEventActivity.class.getSimpleName(), ViewEventActivity.class);
        com_activities.put(VoteActivity.class.getSimpleName(), VoteActivity.class);
        com_activities.put(ViewUsers.class.getSimpleName(), ViewUsers.class);
    }

    //endregion

    //region Notification

    protected void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_SHORT).show();
    } //displayToast

    //endregion

    //region Navigation

    protected void goToActivity(String FromActivitySimpleName, String ToActivitySimpleName) {
        if(com_activities.containsKey(ToActivitySimpleName) && com_activities.containsKey(FromActivitySimpleName)) {
            com_activityHistory.push(FromActivitySimpleName);
            Intent intent = new Intent(this, com_activities.get(ToActivitySimpleName));
            startActivity(intent);
        }
        else {
            Log.e(PolarityActivity.class.getSimpleName(), FromActivitySimpleName + " and/or "
                    + ToActivitySimpleName + " are not a key in com_activities");
        };
    } // goToActivity

    protected View.OnClickListener btnBackOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToPrevActivity();
            }
        };
    } // btnBackOnClickListener

    protected void returnToPrevActivity() {
        if(!com_activityHistory.isEmpty()) {
            if(com_activities.containsKey(com_activityHistory.peek())) {
                Intent intent = new Intent(this, com_activities.get(com_activityHistory.pop()));
                startActivity(intent);
                return;
            }
            else {
                Log.e(PolarityActivity.class.getSimpleName(), "Corrupt activity history data found");
            }
        }
        else {
            Log.e(PolarityActivity.class.getSimpleName(), "Attempting to backtrack with an empty activity history");
        }

        // in case there was an error, redirect user to Hub or Login
        if(com_userID.compareTo("") != 0 || !com_userID.isEmpty()) {
            Intent intent = new Intent(this, com_activities.get(HubActivity.class.getSimpleName()));
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(this, com_activities.get(LogIn.class.getSimpleName()));
            startActivity(intent);
        }
    } // returnToPrevActivity

    //endregion

}
