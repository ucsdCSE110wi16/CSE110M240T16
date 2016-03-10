package com.parse.starter;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.robotium.solo.Solo;

/**
 * Created by PTRAN on 3/6/2016.
 */
public class HubEventsTest extends ActivityInstrumentationTestCase2<LogIn>{
    private Solo solo;

    public HubEventsTest(){
        super(LogIn.class);
    }

    @Override
    protected void setUp() throws Exception{
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
    }

    /*public void testAccessHubActivity(){
        solo.assertCurrentActivity("Are we on the login page?", LogIn.class);
        solo.typeText((EditText) solo.getView(R.id.login_tbUserName), "ptesting03");
        solo.typeText((EditText) solo.getView(R.id.login_tbPassword), "adol");
        solo.clickOnButton("Login");
        solo.waitForActivity("com.parse.starter.HubActivity",100);
        solo.assertCurrentActivity("The activity should be the HubActivity", HubActivity.class);
        solo.clickOnButton("Log Out");
        solo.waitForActivity("com.parse.starter.LogIn", 100);
        solo.assertCurrentActivity("The activity should be the LogIn", LogIn.class);
    }*/

    public void testCreateEvent(){
        solo.assertCurrentActivity("Are we on the login page?", LogIn.class);
        solo.typeText((EditText) solo.getView(R.id.login_tbUserName), "ptesting03");
        solo.typeText((EditText) solo.getView(R.id.login_tbPassword), "adol");
        solo.clickOnButton("Login");
        solo.waitForActivity("com.parse.starter.HubActivity", 100);
        solo.assertCurrentActivity("The activity should be the HubActivity", HubActivity.class);
        solo.searchText("No upcoming events");
        solo.clickOnButton("Create Event");
        solo.waitForActivity("com.parse.starter.CreateEvent", 100);
        solo.assertCurrentActivity("The activity should be the CreateEvent", CreateEvent.class);
        solo.clickOnButton("Create Event");
        solo.searchText("You must enter an event name");
        solo.typeText((EditText) solo.getView(R.id.createEvent_tbName), "Test Movie Event 02");
        solo.clickOnButton("Create Event");
        solo.searchText("You must enter a location");
        solo.typeText((EditText) solo.getView(R.id.createEvent_tbLocation), "ptesting03's house," +
                " NEVERLAND");
        solo.clickOnButton("Create Event");
        solo.searchText("You must enter a time");
        solo.clickOnView(solo.getView(R.id.createEvent_tbDate));
        solo.clickOnView(solo.getView(R.id.createEvent_tbDate));
        solo.setDatePicker(0, 2016, 11, 17);
        solo.clickOnText("OK");
        solo.clickOnView(solo.getView(R.id.createEvent_tbTime));
        solo.clickOnView(solo.getView(R.id.createEvent_tbTime));
        solo.setTimePicker(0, 18, 30);
        solo.clickOnText("OK");
        solo.typeText((EditText) solo.getView(R.id.createEvent_tbDescription), "This is a test event.");
        solo.clickOnButton("Create Event");
        solo.searchText("You must invite friends");
        solo.clickOnButton("Invite Friends");
        solo.waitForActivity("com.parse.starter.InviteFriends", 100);
        solo.assertCurrentActivity("The activity should be the ViewUsers", ViewUsers.class);
        solo.clickOnView(solo.getView(R.id.viewUsers_btnAddFriends));
        solo.assertCurrentActivity("The activity should be AddFriends", AddFriends.class);
        solo.scrollListToLine(0, 4);
        solo.clickInList(1);
        solo.clickOnView(solo.getView(R.id.addFriends_btnAddFriends));
        solo.searchText("Successful!");
        solo.clickOnView(solo.getView(R.id.addFriends_btnBack));
        solo.waitForActivity("com.parse.starter.InviteFriends", 100);
        solo.assertCurrentActivity("The activity should now be the ViewUsers", ViewUsers.class);
        solo.clickOnCheckBox(0);
        solo.clickOnView(solo.getView(R.id.viewUsers_btnActionShort));
        solo.waitForActivity("com.parse.starter.CreateEvent", 100);
        solo.clickOnButton("Create Event");
        solo.searchText("You must add movies");
        solo.clickOnButton("Add Movies");
        solo.waitForActivity("com.parse.starter.AddMovies", 100);
        solo.assertCurrentActivity("The activity should be the AddMovies", AddMovies.class);
        solo.typeText((EditText) solo.getView(R.id.addMovies_tbSearch), "Test Movie");
        solo.clickOnView(solo.getView(R.id.addMovies_btnSearch));
        solo.clickOnView(solo.getView(R.id.addMovies_btnBack));
        solo.waitForActivity("com.parse.starter.CreateEvent", 100);
        solo.clickOnButton("Create Event");
        solo.waitForActivity("com.parse.starter.HubActivity", 100);
        solo.assertCurrentActivity("The activity should be HubActivity", HubActivity.class);
        try{
            Thread.sleep(1000);
        }
        catch(InterruptedException ex){
            Thread.currentThread().interrupt();
        }
        solo.clickOnButton("Log Out");
        solo.waitForActivity("com.parse.starter.LogIn", 100);
        solo.assertCurrentActivity("The activity should be the LogIn", LogIn.class);
        solo.typeText((EditText) solo.getView(R.id.login_tbUserName), "phtran");
        solo.typeText((EditText) solo.getView(R.id.login_tbPassword), "rxzero0079");
        solo.clickOnButton("Login");
        solo.waitForActivity("com.parse.starter.HubActivity", 100);
        solo.assertCurrentActivity("The activity should be the HubActivity", HubActivity.class);
        try{
            Thread.sleep(1000);
        }
        catch(InterruptedException ex){
            Thread.currentThread().interrupt();
        }
        solo.clickOnButton("Log Out");
        solo.waitForActivity("com.parse.starter.LogIn", 100);
        solo.assertCurrentActivity("The activity should be the LogIn", LogIn.class);
    }
}
