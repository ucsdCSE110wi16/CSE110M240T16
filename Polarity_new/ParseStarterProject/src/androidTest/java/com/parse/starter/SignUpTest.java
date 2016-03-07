package com.parse.starter;

//import com.parse.starter.LogIn;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;

import com.robotium.solo.Solo;

/**
 * Created by PTRAN on 3/6/2016.
 */
public class SignUpTest extends ActivityInstrumentationTestCase2<LogIn>{
    private Solo solo;

    public SignUpTest(){
        super(LogIn.class);
    }

    @Override
    protected void setUp() throws Exception{
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testBlankSignUp(){
        solo.assertCurrentActivity("Are we on the login page?", LogIn.class);
        solo.clickOnButton("Create Account");
        solo.waitForActivity("com.parse.starter.SignUp", 100);
        solo.assertCurrentActivity("The activity should be the SignUp", SignUp.class);
        solo.clickOnButton("Register");
        solo.assertCurrentActivity("The activity should be the SignUp", SignUp.class);
        solo.clickOnView(solo.getView(R.id.signUp_btnBack));
        solo.assertCurrentActivity("The activity should be the LogIn", LogIn.class);
    }

    public void testInvalidSignUpParameters(){
        solo.assertCurrentActivity("Are we on the login page?", LogIn.class);
        solo.clickOnButton("Create Account");
        solo.waitForActivity("com.parse.starter.SignUp", 100);
        solo.assertCurrentActivity("The activity should be the SignUp", SignUp.class);
        solo.clickOnView(solo.getView(R.id.signUp_tbLocation));
        solo.searchText("You must enter a username");
        solo.clickOnView(solo.getView(R.id.signUp_tbPassword));
        solo.searchText("You must enter a username");
        solo.clickOnView(solo.getView(R.id.signUp_tbRePassword));
        solo.searchText("You must enter a username");
        solo.typeText((EditText) solo.getView(R.id.signUp_tbName), "phtran");
        solo.clickOnView(solo.getView(R.id.signUp_tbLocation));
        solo.searchText("Username phtran is taken");
        solo.clearEditText((EditText) solo.getView(R.id.signUp_tbName));
        solo.typeText((EditText) solo.getView(R.id.signUp_tbName), "ptesting");
        solo.clickOnView(solo.getView(R.id.signUp_tbLocation));
        solo.searchText("You must enter an email");
        solo.clickOnView(solo.getView(R.id.signUp_tbPassword));
        solo.searchText("You must enter an email");
        solo.clickOnView(solo.getView(R.id.signUp_tbRePassword));
        solo.searchText("You must enter an email");
        solo.typeText((EditText) solo.getView(R.id.signUp_tbLocation), "petertran.h@gmail.com");
        solo.clickOnView(solo.getView(R.id.signUp_tbPassword));
        solo.searchText("You must enter a password");
        solo.clickOnView(solo.getView(R.id.signUp_tbRePassword));
        solo.searchText("You must enter a password");
        solo.typeText((EditText) solo.getView(R.id.signUp_tbPassword), "adol");
        solo.clickOnView(solo.getView(R.id.signUp_tbRePassword));
        solo.searchText("Passwords do not match");
        solo.clickOnView(solo.getView(R.id.signUp_btnBack));
        solo.waitForActivity("com.parse.starter.LogIn", 100);
        solo.assertCurrentActivity("The activity should be the LogIn", LogIn.class);
    }

    public void testSignUpAndLogin(){
        solo.assertCurrentActivity("Are we on the login page?", LogIn.class);
        solo.clickOnButton("Create Account");
        solo.waitForActivity("com.parse.starter.SignUp", 100);
        solo.assertCurrentActivity("The activity should be the SignUp", SignUp.class);
        solo.typeText((EditText) solo.getView(R.id.signUp_tbName), "ptesting");
        solo.typeText((EditText) solo.getView(R.id.signUp_tbLocation), "pht001@.ucsd.edu");
        solo.typeText((EditText) solo.getView(R.id.signUp_tbPassword), "adol");
        solo.typeText((EditText) solo.getView(R.id.signUp_tbRePassword), "adol");
        solo.clickOnButton("Register");
        solo.waitForActivity("com.parse.starter.HubActivity", 100);
        solo.assertCurrentActivity("The activity should be the HubActivity", HubActivity.class);
        solo.clickOnButton("Log Out");
        solo.waitForActivity("com.parse.starter.LogIn", 100);
        solo.assertCurrentActivity("The activity should be the LogIn", LogIn.class);
    }


}
