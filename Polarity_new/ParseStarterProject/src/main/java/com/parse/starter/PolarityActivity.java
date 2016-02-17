package com.parse.starter;


import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import com.parse.starter.MovieEvent;
import java.util.ArrayList;

/** Global variables and methods go here
 *  Created by Lucas Pettit 02/10/2016
 */
public abstract class PolarityActivity extends Activity {

    //region Declare Variables

    // global variables - must be declared static
    static String com_user, com_userID, com_eventID;
    static String com_eventName, com_eventLocation, com_eventTime, com_eventDescription;

    static ArrayList<Model> com_movieList;
    static ArrayList<Model> com_invitedFriends;
    static ArrayList<Model> com_eventQueue;

    static ArrayList<MovieEvent> com_userEvents;
    //endregion

    //region Initialize

    // initialize the variables. Only do this ONCE in MainActivity
    protected void initialize() {
        com_user = "";
        com_userID = "";
        com_eventID = "";
        com_eventName = "";
        com_eventLocation = "";
        com_eventTime = "";
        com_eventDescription = "";

        com_movieList = new ArrayList<Model>();
        com_invitedFriends = new ArrayList<Model>();
        com_eventQueue = new ArrayList<Model>();

        com_userEvents = new ArrayList<MovieEvent>();
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

    //endregion

}
