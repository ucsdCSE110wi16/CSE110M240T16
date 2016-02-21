package com.parse.starter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.LinkedList;
import java.util.List;

public class VoteActivity extends PolarityActivity {

    //region Variables

    public static final String TAG = VoteActivity.class.getSimpleName();

    Button btnBack, btnHome, btnVote, btnCancel;
    ListView lvMovieList;

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        btnBack = (Button) findViewById(R.id.vote_btnBack);
        btnCancel = (Button) findViewById(R.id.vote_btnCancel);
        btnHome = (Button) findViewById(R.id.vote_btnHome);
        btnVote = (Button) findViewById(R.id.vote_btnVote);
        lvMovieList = (ListView) findViewById(R.id.vote_lvMovies);

        btnBack.setOnClickListener(btnBack_Click());
        btnCancel.setOnClickListener(btnCancel_Click());
        btnHome.setOnClickListener(btnHome_Click());
        btnVote.setOnClickListener(btnVote_Click());

        fetchMovies();
    }

    //region Button Clicks

    protected View.OnClickListener btnBack_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toActivity_ViewEvent();
            }
        };
    }

    protected View.OnClickListener btnCancel_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toActivity_ViewEvent();
            }
        };
    }

    protected View.OnClickListener btnHome_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toActivity_HubActivity();
            }
        };
    }

    protected View.OnClickListener btnVote_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: check if there are any votes. If so push the votes to parse

                com_currentEvent.status = EventModel.Status.AcceptedAndVoted;
                toActivity_ViewEvent();
            }
        };
    }
    //endregion

    //region Helpers

    private void fetchMovies() {
        List<ParseObject> parseMovieList;
        MovieModel model;

        try {
            parseMovieList = ParseQuery.getQuery("UserMovieInfo").whereEqualTo("userMovieQueueID",
                    com_currentEvent.getMovieQueueID()).find();

            for(ParseObject obj : parseMovieList) {
                model = new MovieModel(obj.getString("objectId"), obj.getString("userMovieQueueID"),
                        obj.getString("description"), obj.getString("title"));
                com_movieList.add(model);
            }

        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    //endregion
}
