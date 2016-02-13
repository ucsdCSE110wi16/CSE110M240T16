package com.parse.starter;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.parse.ParseException;
import com.parse.ParseUser;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.parse.starter.R.id.main_tbUserName;

/**
 * Created by PTRAN on 2/10/2016.
 */

@RunWith(AndroidJUnit4.class)
public class LoginTest {
    @Rule
    public ActivityTestRule<LogIn> loginActivityTestRule = new ActivityTestRule(LogIn.class);

    @Test
    public void dummy_login(){
        onView(withId(R.id.main_tbUserName)).perform(typeText("rustyshackleford"));
        onView(withId(R.id.main_tbPassword)).perform(typeText("beer"));

        onView(withId(R.id.main_btnLogin)).perform(click());
    }
}
