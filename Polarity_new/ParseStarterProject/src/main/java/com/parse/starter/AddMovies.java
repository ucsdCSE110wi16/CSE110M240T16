package com.parse.starter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class AddMovies extends AppCompatActivity {

    ListView lv;
    EditText et;
    ArrayList<Model> movieList;
    CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movies);

        lv = (ListView) findViewById(R.id.listOfMovies);
        et = (EditText) findViewById(R.id.movieToAdd);

        movieList = new ArrayList<Model>();
//        adapter = new CustomAdapter(getApplicationContext(), movieList);
//        listOfMovies.setAdapter(adapter);
    }
    // Add movie titles to the list
    public void addMovieName(View view){
        String name = et.getText().toString();
        if (name.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Plz enter Values",
                    Toast.LENGTH_SHORT).show();
        }
        // add string to the list
        else {
//            Model md = new Model(name);
//            modelList.add(md);
//            adapter.notifyDataSetChanged();
//            // reset input field
//            et.setText("");
        }
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
