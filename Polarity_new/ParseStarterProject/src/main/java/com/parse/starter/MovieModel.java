package com.parse.starter;

import java.util.Comparator;

/**
 * Created by lpett on 2/20/2016.
 */
public class MovieModel extends Model{
    private String movieId, movieEventId, description;
    private int num_totalVotes, num_maxVotes;
    public boolean hasVote;


    MovieModel(String MovieID, String MovieEventID, String Description, String MovieName, int MaxVotes) {
        movieId = MovieID;
        movieEventId = MovieEventID;
        description = Description;
        name = MovieName;
        num_totalVotes = 0;
        num_maxVotes = MaxVotes;
        hasVote = false;
    }

    public String getMovieID() {return movieId;}
    public String getMovieEventID() {return movieEventId;}
    public String getDescription() {return description;}
    public int getNumTotalVotes() {return num_totalVotes;}
    public int getNumMaxVotes() {return num_maxVotes;}

    public void setNumTotalVotes(int NumVotes) {
        num_totalVotes = NumVotes;
    }
    public void setNumMaxVotes(int MaxVotes) { num_maxVotes = MaxVotes; }
}

// comparator for MovieModel
class MovieModelComparator implements Comparator<MovieModel> {

    public static final String TAG = MovieModelComparator.class.getSimpleName();

    @Override
    public int compare(MovieModel m1, MovieModel m2) throws NullPointerException {
        if(m1 != null && m2 != null) {
            return m2.getNumTotalVotes() - m1.getNumTotalVotes();
        }
        throw new NullPointerException();
    };
}