package com.parse.starter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class AddMovies extends Activity {

    ListView lv;
    EditText et;

    ArrayList<Model> modelList;
    CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movies);

//        lv = (ListView) findViewById(R.id.searchImageView);
        et = (EditText) findViewById(R.id.searchField);

        modelList = new ArrayList<Model>();
        adapter = new CustomAdapter(getApplicationContext(), modelList);
        lv.setAdapter(adapter);
    }

    public void toActivityCreateEvent(View view){
        Intent intent = new Intent(this, CreateEvent.class);
        startActivity(intent);
    }

    public void toHubActivity(View view){
        Intent intent = new Intent(this, HubActivity.class);
        startActivity(intent);
    }

    public void addbtn(View v){
        String name = et.getText().toString();
        // if name field is empty, show toast notification
        if (name.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Plz enter Values",
                    Toast.LENGTH_SHORT).show();
        }
        // add string to the list
        else {
            Model md = new Model(name);
            modelList.add(md);
            adapter.notifyDataSetChanged();
            // reset input field
            et.setText("");
        }
    }
}
