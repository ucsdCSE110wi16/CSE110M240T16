package com.parse.starter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;

import java.util.ArrayList;

public class AddMovies extends PolarityActivity implements View.OnKeyListener {

    //region Declare Variables

    public static final String TAG = SignUp.class.getSimpleName();

    Button btnBack, btnHome, btnImportMovieQueue, btnSearch;
    ListView lvMovieList;
    EditText tbSearch;

    ArrayList<Model> modelList;
    CustomAdapter adapter;

    String movieNameBuffer;

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movies);

        // initialize variables
        lvMovieList = (ListView) findViewById(R.id.addMovies_lvMovieList);
        tbSearch = (EditText) findViewById(R.id.addMovies_tbSearch);
        btnBack = (Button) findViewById(R.id.addMovies_btnBack);
        btnHome = (Button) findViewById(R.id.addMovies_btnHome);
        btnImportMovieQueue = (Button) findViewById(R.id.addMovies_ImportMovieQueue);
        btnSearch = (Button) findViewById(R.id.addMovies_btnSearch);

        modelList = com_movieList;

        adapter = new CustomAdapter(getApplicationContext(), modelList);
        lvMovieList.setAdapter(adapter);

        // set listeners
        btnSearch.setOnClickListener(btnSearch_Click());
        btnBack.setOnClickListener(btnBack_Click());
        btnHome.setOnClickListener(btnHome_Click());
        tbSearch.setOnKeyListener(this);

    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            // check if tbSearch has focus AND the ENTER key was pressed
            if (tbSearch.hasFocus() && keyCode == KeyEvent.KEYCODE_ENTER) {
                btnSearch.requestFocus();
                view.clearFocus();
                addMovie();
                return true;
            }
        }
        return false; // pass on to listeners
    }

    //region Button Click Events

    protected View.OnClickListener btnSearch_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMovie();
            }
        };
    } // btnSearch_Click

    protected View.OnClickListener btnBack_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toActivity_CreateEvent();
            }
        };
    } // btnBack_onClick

    protected View.OnClickListener btnHome_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // erase all the data if backing up to main screen
                com_eventName = "";
                com_eventLocation = "";
                com_eventTime = "";
                com_eventDescription = "";
                com_movieList.clear();
                com_invitedFriends.clear();

                toActivity_HubActivity();
            }
        };
    } // btnHome_Click

    //endregion

    //region Helper Methods

    protected void addMovie() {
        String name = tbSearch.getText().toString();
        // if name field is empty, show toast notification
        if (name.isEmpty()) {
            Toast.makeText(getApplicationContext(), "You must enter a movie name",
                    Toast.LENGTH_SHORT).show();
        }
        // add string to the list
        else {
            Model md = new Model(name);
            modelList.add(md);
            adapter.notifyDataSetChanged();
            // reset input field
            tbSearch.setText("");
        }
    }

    //endregion

    @Override
    public void onStart() {
        super.onStart();

        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "AddMovies Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.parse.starter/http/host/path")
        );
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "AddMovies Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.parse.starter/http/host/path")
        );
    }
}
