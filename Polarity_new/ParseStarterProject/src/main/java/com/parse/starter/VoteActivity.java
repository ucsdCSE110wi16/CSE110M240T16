package com.parse.starter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

public class VoteActivity extends AppCompatActivity {

    //region Variables

    Button btnBack, btnHome, btnVote, btnCancel;
    ListView lvMovieList;


    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);
    }
}
