package com.parse.starter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class AddMovies extends Activity {

    //region Declare Variables

    Button btnBack, btnHome, btnImportMovieQueue, btnSearch;
    ListView lvMovieList;
    EditText tbSearch;

    ArrayList<Model> modelList;
    CustomAdapter adapter;

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

        modelList = new ArrayList<Model>();
        adapter = new CustomAdapter(getApplicationContext(), modelList);
        lvMovieList.setAdapter(adapter);


        // set catches
        btnSearch.setOnClickListener(btnSearch_Click());

    }

    //region Button Click Events

    protected View.OnClickListener btnSearch_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMovie(v);
            }
        };
    } // btnSearch_Click

    //endregion

    //region Helper Methods

    protected void addMovie(View v){
        String name = tbSearch.getText().toString();
        // if name field is empty, show toast notification
        if (name.isEmpty()) {
            Toast.makeText(getApplicationContext(), "You must enter a movie name into the text box",
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

    //region Navigation

    public void toActivityCreateEvent(View view){
        Intent intent = new Intent(this, CreateEvent.class);
        startActivity(intent);
    }

    public void toHubActivity(View view){
        Intent intent = new Intent(this, HubActivity.class);
        startActivity(intent);
    }

    //endregion
}
