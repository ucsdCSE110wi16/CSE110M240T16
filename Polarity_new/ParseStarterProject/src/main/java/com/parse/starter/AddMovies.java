package com.parse.starter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import java.util.ArrayList;


public class AddMovies extends AppCompatActivity {

    ListView listOfMovies;
    EditText movieToAdd;
    ArrayList<Model> movieList;
    CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movies);

        listOfMovies = (ListView) findViewById(R.id.listOfMovies);
        movieToAdd = (EditText) findViewById(R.id.movieToAdd);


    }
    // Add movie titles to the list
    public void addMovieName(View view){

    }

    public void toActivityCreateEvent(View view){
        Intent intent = new Intent(this, CreateEvent.class);
        startActivity(intent);
    }

    public void toHubActivity(View view){
        Intent intent = new Intent(this, HubActivity.class);
        startActivity(intent);
    }
}
