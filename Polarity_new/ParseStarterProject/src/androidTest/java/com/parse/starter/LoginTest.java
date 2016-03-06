package com.parse.starter;

import com.parse.starter.LogIn;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.robotium.solo.Solo;

/**
 * Created by PTRAN on 3/5/2016.
 */
public class LogInTest extends ActivityInstrumentationTestCase2<LogIn>{
    private Solo solo;

    public LogInTest(){
        super(LogIn.class);
    }

    @Override
    protected void setUp() throws Exception{
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testAccessForgotPassword(){
        solo.assertCurrentActivity("Are we on the login page?", LogIn.class);
        solo.clickOnButton("Forgot Password");
        solo.waitForActivity("com.parse.starter.ForgotPassword", 100);
        solo.assertCurrentActivity("The activity should be the ForgotPassword", ForgotPassword.class);
        solo.goBack();
        solo.waitForActivity("com.parse.starter.LogIn", 100);
        solo.assertCurrentActivity("The activity should be the LogIn", LogIn.class);
    }

    public void testAccessSignUp(){
        solo.assertCurrentActivity("Are we on the login page?", LogIn.class);
        solo.clickOnButton("Create Account");
        solo.waitForActivity("com.parse.starter.SignUp", 100);
        solo.assertCurrentActivity("The activity should be the SignUp", SignUp.class);
        solo.goBack();
        solo.waitForActivity("com.parse.starter.LogIn", 100);
        solo.assertCurrentActivity("The activity should be the LogIn", LogIn.class);
    }

    public void testInvalidLogIn(){
        solo.assertCurrentActivity("Are we on the login page?", LogIn.class);
        solo.clickOnButton("Login");
        solo.searchText("invalid login parameters");
        solo.assertCurrentActivity("The activity should be the LogIn", LogIn.class);
        solo.typeText((EditText) solo.getView(R.id.login_tbUserName), "phtran");
        solo.typeText((EditText) solo.getView(R.id.login_tbPassword), "phtran");
        solo.clickOnButton("Login");
        solo.searchText("invalid login parameters");
        solo.assertCurrentActivity("Again, the activity should be the LogIn", LogIn.class);
        solo.clearEditText((EditText) solo.getView(R.id.login_tbUserName));
        solo.clearEditText((EditText) solo.getView(R.id.login_tbPassword));
        solo.typeText((EditText) solo.getView(R.id.login_tbUserName), "rxzero0079");
        solo.typeText((EditText) solo.getView(R.id.login_tbPassword), "rxzero0079");
        solo.clickOnButton("Login");
        solo.searchText("invalid login parameters");
        solo.assertCurrentActivity("Do you want me to repeat myself, it should be the LogIn", LogIn.class);
        solo.clearEditText((EditText) solo.getView(R.id.login_tbUserName));
        solo.clearEditText((EditText) solo.getView(R.id.login_tbPassword));
        solo.typeText((EditText) solo.getView(R.id.login_tbUserName), "rxzero0079");
        solo.typeText((EditText) solo.getView(R.id.login_tbPassword), "phtran");
        solo.clickOnButton("Login");
        solo.searchText("invalid login parameters");
        solo.assertCurrentActivity("Get the hell out of here, you should be in the Login", LogIn.class);
        solo.clearEditText((EditText) solo.getView(R.id.login_tbUserName));
        solo.clearEditText((EditText) solo.getView(R.id.login_tbPassword));
    }


    public void testLogin(){
        solo.assertCurrentActivity("Are we on the login page?", LogIn.class);
        solo.typeText((EditText) solo.getView(R.id.login_tbUserName), "phtran");
        solo.typeText((EditText) solo.getView(R.id.login_tbPassword), "rxzero0079");
        solo.clickOnButton("Login");
        solo.waitForActivity("com.parse.starter.HubActivity",100);
        solo.assertCurrentActivity("The activity should be the HubActivity", HubActivity.class);
        solo.clickOnButton("Log Out");
        solo.waitForActivity("com.parse.starter.LogIn", 100);
        solo.assertCurrentActivity("The activity should be the LogIn", LogIn.class);
    }
}
