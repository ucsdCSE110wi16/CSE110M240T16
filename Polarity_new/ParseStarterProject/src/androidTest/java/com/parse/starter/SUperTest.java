package com.parse.starter;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.robotium.solo.Solo;

import java.io.PrintWriter;
import java.util.Random;

/**
 * Created by PTRAN on 3/15/2016.
 */
public class SUperTest extends ActivityInstrumentationTestCase2<LogIn>{
    private Solo solo;

    public static String username;
    private boolean isInitialized = false;

    public SUperTest(Class type){
        super(type);
    }

    @Override
    protected void setUp() throws Exception{
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
    }
}
