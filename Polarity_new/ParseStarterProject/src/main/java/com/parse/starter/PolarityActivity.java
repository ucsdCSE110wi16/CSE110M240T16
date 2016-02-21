package com.parse.starter;


import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import java.util.ArrayList;

/** Global variables and methods go here
 *  Created by Lucas Pettit 02/10/2016
 */
public abstract class PolarityActivity extends Activity {

    //region Declare Variables

    // global variables - must be declared static
    static boolean com_userEventsLoaded;

    static String com_user, com_userID, com_eventID;
    static String com_eventName, com_eventLocation, com_eventTime, com_eventDescription;
    static String com_currentEventId;

    static EventModel com_currentEvent;

    static ArrayList<Model> com_modelList;
    static ArrayList<MovieModel> com_movieList;
    static ArrayList<FriendModel> com_invitedFriends;
    static ArrayList<EventModel> com_eventModelList;

    //endregion

    //region Initialize

    // initialize the variables. Only do this ONCE in MainActivity
    protected void initialize() {
        com_userEventsLoaded = false;

        com_user = "";
        com_userID = "";
        com_eventID = "";
        com_eventName = "";
        com_eventLocation = "";
        com_eventTime = "";
        com_eventDescription = "";
        com_currentEventId = "";

        com_currentEvent = null;

        com_movieList = new ArrayList<MovieModel>();
        com_invitedFriends = new ArrayList<FriendModel>();
        com_eventModelList = new ArrayList<EventModel>();
    }

    //endregion

    //region Notification

    protected void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_SHORT).show();
    } //displayToast

    //endregion


    //region Navigation

    protected void toActivity_AddMovies() {
        Intent intent = new Intent(this, AddMovies.class);
        startActivity(intent);
    } // toActivity_AddMovies

    protected void toActivity_CreateEvent() {
        Intent intent = new Intent(this, CreateEvent.class);
        startActivity(intent);
    } // toActivity_CreateEvent

    protected void toActivity_ForgotPassword() {
        Intent intent = new Intent(this, ForgotPassword.class);
        startActivity(intent);
    } // toActivity_ForgotPassword

    protected void toActivity_HubActivity() {
        Intent intent = new Intent(this, HubActivity.class);
        startActivity(intent);
    } // toActivity_HubActivity

    protected void toActivity_InviteFriends() {
        Intent intent = new Intent(this, InviteFriends.class);
        startActivity(intent);
    } // toActivity_InviteFriends

    protected void toActivity_Login() {
        Intent intent = new Intent(this, LogIn.class);
        startActivity(intent);
    } // toActivity_LogIn

    protected void toActivity_MainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    } // toActivity_MainActivity

    protected void toActivity_SignUp() {
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    } // toActivity_SignUp

    protected void toActivity_ViewEvent() {
        Intent intent = new Intent(this, ViewEventActivity.class);
        startActivity(intent);
    } // toActivity_ViewEvent

    protected void toActivity_Vote() {
        Intent intent = new Intent(this, VoteActivity.class);
        startActivity(intent);
    } // toActivity_Vote

    protected void toActivity_ViewInviteList() {
        Intent intent = new Intent(this, ViewInviteListActivity.class);
        startActivity(intent);
    } // toActivty_ViewInviteList

    //endregion

}
