package com.parse.starter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class VoteActivity extends PolarityActivity {

    //region Variables

    public static final String TAG = VoteActivity.class.getSimpleName();

    Button btnBack, btnHome, btnVote, btnCancel;
    ListView lvMovieList;
    MovieVoteAdapter movieVoteAdapter;
    MoviePollAdapter moviePollsAdapter;
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

        lvMovieList.setOnItemClickListener(lvEventQueue_Click());

        com_movieList.clear();
        fetchMovies();

        // if they've already voted then display the results
        if(com_currentEvent.status == EventModel.Status.AcceptedAndVoted) {
            tallyVotes();
            Collections.sort(com_movieList, new MovieModelComparator());
            moviePollsAdapter = new MoviePollAdapter(getApplicationContext(), com_movieList);
            lvMovieList.setAdapter(moviePollsAdapter);
        }
        else { // otherwise display the movie list
            movieVoteAdapter = new MovieVoteAdapter(getApplicationContext(), com_movieList);
            lvMovieList.setAdapter(movieVoteAdapter);
        }
    }

    //region Button Clicks

    protected View.OnClickListener btnBack_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearVotes();
                returnToPrevActivity();
            }
        };
    } // btnBack_Click

    protected View.OnClickListener btnCancel_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearVotes();
                returnToPrevActivity();
            }
        };
    } // btnCancel_Click

    protected View.OnClickListener btnHome_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearVotes();
                goToActivity(TAG, HubActivity.class.getSimpleName());
            }
        };
    } // btnHome_Click

    protected View.OnClickListener btnVote_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(com_currentEvent.status != EventModel.Status.Accepted) return;

                boolean hasVoted = false;
                ParseObject movieObj, user;
                List<ParseObject> parseList = new ArrayList<ParseObject>();
                ParseACL acl = new ParseACL();
                acl.setPublicReadAccess(true);
                acl.setPublicWriteAccess(true);

                // send all votes to the database
                for(MovieModel movie : com_movieList) {
                    if(movie.hasVote) {
                        hasVoted = true;
                        movieObj = new ParseObject("MovieVotes");
                        movieObj.put("UserID", com_userID);
                        movieObj.put("EventID", com_currentEvent.getEventID());
                        movieObj.put("MovieInfoID", movie.getMovieID());
                        movieObj.setACL(acl);
                        parseList.add(movieObj);
                    }
                }

                // display worning message and return if no votes
                if(!hasVoted) {
                    displayToast("You must vote an at leat ONE movie");
                    return;
                }

                new ParseObject("MovieVotes").saveAllInBackground(parseList, new SaveCallback() {
                    @Override
                    public void done(ParseException e) {

                        if (e != null) {
                            Log.e(TAG, e.getMessage());
                            displayToast("Unable to record votes");
                            return;
                        }

                        // update user Confirmation Status
                        ParseQuery.getQuery("InvitedFriends").whereEqualTo("UserID", com_userID).getFirstInBackground(new GetCallback<ParseObject>() {
                            @Override
                            public void done(ParseObject object, ParseException e) {
                                if (e != null) {
                                    Log.e(TAG, e.getMessage());
                                    displayToast("Unable to update user vote status");
                                    return;
                                }

                                object.put("Confirmation", 2);
                                object.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e != null) {
                                            Log.e(TAG, e.getMessage());
                                            displayToast("Unable to save user vote status");
                                            return;
                                        }
                                        com_currentEvent.status = EventModel.Status.AcceptedAndVoted;
                                        goToActivity(TAG, ViewEventActivity.class.getSimpleName());
                                    }
                                });
                            }
                        });
                    }
                });
            }
        };
    }
    //endregion

    //region ListView Clicks

    private AdapterView.OnItemClickListener lvEventQueue_Click() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                Log.d(TAG, "User Status = " + com_currentEvent.status);

                switch(com_currentEvent.status) {
                    case Accepted:
                        handleVoteEvent(position);
                        break;
                    case AcceptedAndVoted:
                        //TODO: display discription maybe?
                        break;
                    default:
                        //do nothing
                }
            }
        };
    }

    //endregion

    //region Helpers

    private void handleVoteEvent(int position) {
        if (((MovieModel) lvMovieList.getItemAtPosition(position)).hasVote) {
            ((MovieModel) lvMovieList.getItemAtPosition(position)).hasVote = false;

        } else{
            ((MovieModel) lvMovieList.getItemAtPosition(position)).hasVote = true;
        }

        MovieModel m = (MovieModel) lvMovieList.getItemAtPosition(position);
        Log.d(TAG, "MovieModel [" + m.getName() + "] hasVote=" + m.hasVote);

        movieVoteAdapter.notifyDataSetChanged();
    } // handleVoteEvent

    private void clearVotes() {
        for(MovieModel movie : com_movieList) {
            movie.hasVote = false;
        }
    } // clearVotes

    private void fetchMovies() {
        List<ParseObject> parseMovieList;
        MovieModel model;

        try {
            parseMovieList = ParseQuery.getQuery("UserMovieInfo").whereEqualTo("userMovieQueueID",
                    com_currentEvent.getMovieQueueID()).find();

            Log.d(TAG, "found " + parseMovieList.size() + " movies in userMovieQueue=" + com_currentEvent.getMovieQueueID());

            for(ParseObject obj : parseMovieList) {
                com_movieList.add(new MovieModel(obj.getObjectId(),
                        obj.getString("userMovieQueueID"),
                        obj.getString("description"),
                        obj.getString("title"),
                        com_currentEvent.getNumFriendsAttending()));
            }

        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
        }
    } // fetchMovies

    private void tallyVotes() {

        ParseQuery<ParseObject> master = ParseQuery.getQuery("MovieVotes");
        master = master.whereEqualTo("EventID", com_currentEvent.getEventID());
        ParseQuery<ParseObject> subset;

        for(MovieModel movie : com_movieList) {
            subset = master.whereEqualTo("MovieInfoID", movie.getMovieID());

            try {
                movie.setNumTotalVotes(subset.count());

            } catch (ParseException e) {
                Log.d(TAG, e.getMessage());
                movie.setNumTotalVotes(0);
            }
        }
    } // tallyVotes

    //endregion
}
