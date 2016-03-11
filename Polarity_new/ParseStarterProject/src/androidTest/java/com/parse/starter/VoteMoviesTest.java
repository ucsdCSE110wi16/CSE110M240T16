package com.parse.starter;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.robotium.solo.Solo;

/**
 * Created by PTRAN on 3/10/2016.
 */
public class VoteMoviesTest extends ActivityInstrumentationTestCase2<LogIn>{
    private Solo solo;

    public VoteMoviesTest(){
        super(LogIn.class);
    }

    @Override
    protected void setUp() throws Exception{
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testVoting(){
        solo.assertCurrentActivity("Are we on the login page?", LogIn.class);
        solo.typeText((EditText) solo.getView(R.id.login_tbUserName), "ptesting04");
        solo.typeText((EditText) solo.getView(R.id.login_tbPassword), "adol");
        solo.clickOnButton("Login");

        solo.waitForActivity("com.parse.starter.HubActivity", 100);
        solo.assertCurrentActivity("The activity should be the HubActivity", HubActivity.class);
        try{
            Thread.sleep(1000);
        }
        catch(InterruptedException ex){
            Thread.currentThread().interrupt();
        }

        solo.clickInList(1);
        solo.waitForActivity("parse.starter.ViewEventActivity", 100);
        solo.assertCurrentActivity("The activity should be the ViewEventActivity", ViewEventActivity.class);
        try{
            Thread.sleep(1000);
        }
        catch(InterruptedException ex){
            Thread.currentThread().interrupt();
        }

        solo.clickOnView(solo.getView(R.id.viewEvent_btnViewInviteList));
        solo.waitForActivity("parse.starter.ViewUsers", 100);
        solo.assertCurrentActivity("The activity should be the ViewUsers", ViewUsers.class);
        try{
            Thread.sleep(1000);
        }
        catch(InterruptedException ex){
            Thread.currentThread().interrupt();
        }

        solo.clickOnView(solo.getView(R.id.viewUsers_btnBack));
        solo.waitForActivity("parse.starter.ViewEventActivity", 100);
        solo.assertCurrentActivity("The activity should be the ViewEventActivity", ViewEventActivity.class);
        try{
            Thread.sleep(1000);
        }
        catch(InterruptedException ex){
            Thread.currentThread().interrupt();
        }

        solo.clickOnView(solo.getView(R.id.viewEvent_btnVoteOnMovies));
        solo.waitForActivity("parse.starter.VoteActivity", 100);
        solo.assertCurrentActivity("The activity should be VoteActivity", VoteActivity.class);
        try{
            Thread.sleep(1000);
        }
        catch(InterruptedException ex){
            Thread.currentThread().interrupt();
        }

        solo.clickOnView(solo.getView(R.id.vote_btnBack));
        solo.waitForActivity("parse.starter.ViewEventActivity", 100);
        solo.assertCurrentActivity("The activity should be the ViewEventActivity", ViewEventActivity.class);
        try{
            Thread.sleep(1000);
        }
        catch(InterruptedException ex){
            Thread.currentThread().interrupt();
        }

        solo.clickOnView(solo.getView(R.id.viewEvent_btnAcceptInvite));
        solo.searchText("Invite accepted");
        try{
            Thread.sleep(1000);
        }
        catch(InterruptedException ex){
            Thread.currentThread().interrupt();
        }

        solo.clickOnView(solo.getView(R.id.viewEvent_btnVoteOnMovies));
        solo.waitForActivity("parse.starter.VoteActivity", 100);
        solo.assertCurrentActivity("The activity should be VoteActivity", VoteActivity.class);
        try{
            Thread.sleep(1000);
        }
        catch(InterruptedException ex){
            Thread.currentThread().interrupt();
        }

        solo.clickInList(1);
        solo.clickInList(2);
        solo.clickInList(3);
        solo.clickInList(4);
        solo.clickInList(5);
        try{
            Thread.sleep(1000);
        }
        catch(InterruptedException ex){
            Thread.currentThread().interrupt();
        }

        solo.clickOnView(solo.getView(R.id.vote_btnVote));
        solo.waitForActivity("parse.starter.ViewEventActivity", 100);
        solo.assertCurrentActivity("The activity should be the ViewEventActivity", ViewEventActivity.class);
        try{
            Thread.sleep(1000);
        }
        catch(InterruptedException ex){
            Thread.currentThread().interrupt();
        }

        solo.clickOnView(solo.getView(R.id.viewEvent_btnVoteOnMovies));
        solo.waitForActivity("parse.starter.VoteActivity", 100);
        solo.assertCurrentActivity("The activity should be the VoteActivity", VoteActivity.class);
        try{
            Thread.sleep(1000);
        }
        catch(InterruptedException ex){
            Thread.currentThread().interrupt();
        }

        solo.clickOnView(solo.getView(R.id.vote_btnBack));
        solo.waitForActivity("parse.starter.ViewEventActivity", 100);
        solo.assertCurrentActivity("The activity should be the ViewEventActivity", ViewEventActivity.class);
        try{
            Thread.sleep(1000);
        }
        catch(InterruptedException ex){
            Thread.currentThread().interrupt();
        }

        solo.clickOnView(solo.getView(R.id.viewEvent_btnBack));
        solo.waitForActivity("com.parse.starter.HubActivity", 100);
        solo.assertCurrentActivity("The activity should be the HubActivity", HubActivity.class);

        solo.clickOnButton("Log Out");
        solo.waitForActivity("parse.starter.LogIn", 100);
        solo.assertCurrentActivity("The activity should be the LogIn", LogIn.class);
    }
}
