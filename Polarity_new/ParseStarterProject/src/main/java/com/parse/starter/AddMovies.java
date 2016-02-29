package com.parse.starter;

import android.net.Uri;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;

import java.util.ArrayList;

public class AddMovies extends PolarityActivity implements View.OnKeyListener {

    //region Declare Variables

    public static final String TAG = AddMovies.class.getSimpleName();

    Button btnBack, btnHome, btnSearch;
    ListView lvMovieList;
    EditText tbSearch;
    TextView txtInfo;
    ArrayList<Model> modelList;
    ModelAdapter adapter;

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
        btnSearch = (Button) findViewById(R.id.addMovies_btnSearch);
        txtInfo = (TextView) findViewById(R.id.addMovies_txtInfo);
        modelList = com_modelList;

        adapter = new ModelAdapter(getApplicationContext(), modelList);
        lvMovieList.setAdapter(adapter);

        // set listeners
        btnSearch.setOnClickListener(btnSearch_Click());
        btnBack.setOnClickListener(btnBackOnClickListener());
        btnHome.setOnClickListener(btnHome_Click());
        tbSearch.setOnKeyListener(this);
        // tell the user that there's no movies in the queue
        if(modelList.size() == 0) txtInfo.setText("no movies added yet :(");
        else txtInfo.setText("");
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

                returnToPrevActivity();
            }
        };
    } // btnHome_Click

    //endregion

    //region Helper Methods

    protected void addMovie() {
        String name = tbSearch.getText().toString();
        // This is for checking if the movie is not already added
        boolean isAlreadyAdded = false;
        // Iterates through the list of movies added, if typed movie is already added, isAlreadyAdded is set to true
        for(Model m: modelList){
            if(name.equals(m.getName())){
                displayToast("Movie " + name + " is already added");
                isAlreadyAdded = true;
            }
        }
        // hide message that theres' no movies in the list
        txtInfo.setText("");
        // if name field is empty, show toast notification
        if (name.isEmpty()) {
            Toast.makeText(getApplicationContext(), "You must enter a movie name",
                    Toast.LENGTH_SHORT).show();
        }
        // add string to the list only if the movie is not already in the list
        else {
            if(!isAlreadyAdded) {
                Model md = new Model(name);
                modelList.add(md);
                adapter.notifyDataSetChanged();
            }
            // reset input field
            tbSearch.setText("");
        }
    }

    //endregion

    //region Auto-Generated API Stuff
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
    //endregion
}
