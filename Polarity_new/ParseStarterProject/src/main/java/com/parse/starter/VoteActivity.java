package com.parse.starter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class VoteActivity extends PolarityActivity {

    //region Variables

    public static final String TAG = VoteActivity.class.getSimpleName();

    Button btnBack, btnHome, btnVote, btnCancel;
    ListView lvMovieList;
    MovieVoteAdapter adapter;
    ArrayList<MovieModel> votedMovies;

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

        adapter = new MovieVoteAdapter(getApplicationContext(), com_movieList);
        lvMovieList.setAdapter(adapter);
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

    //region ListView Clicks

    private AdapterView.OnItemClickListener lvEventQueue_Click() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                MovieModel m = (MovieModel) lvMovieList.getItemAtPosition(position);
                if(m.hasVote) m.hasVote = false;
                else m.hasVote = true;
                adapter.notifyDataSetChanged();
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

            Log.d(TAG, "found " + parseMovieList.size() + " movies in userMovieQueue=" + com_currentEvent.getMovieQueueID());

            for(ParseObject obj : parseMovieList) {
                com_movieList.add(new MovieModel(obj.getString("objectId"),
                        obj.getString("userMovieQueueID"),
                        obj.getString("description"),
                        obj.getString("title")));
            }

        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    //endregion
}
