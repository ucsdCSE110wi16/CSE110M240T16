package com.parse.starter;

import java.util.Comparator;

/**
 * Created by lpett on 2/20/2016.
 */
public class MovieModel extends Model{
    private String movieId, movieEventId, description;
    private int num_votes, num_maxPossibleVotes;
    public boolean hasVote;


    MovieModel(String MovieID, String MovieEventID, String Description, String MovieName, int MaxPossibleVotes) {
        movieId = MovieID;
        movieEventId = MovieEventID;
        description = Description;
        name = MovieName;
        num_votes = 0;
        num_maxPossibleVotes = MaxPossibleVotes;
        hasVote = false;
    }

    public String getMovieID() {return movieId;}
    public String getMovieEventID() {return movieEventId;}
    public String getDescription() {return description;}
    public int getNumVotes() {return num_votes;}
    public int getNumMaxPossibleVotes() {return num_maxPossibleVotes;}

    public void setNumVotes(int NumVotes) {
        num_votes = NumVotes;
    }
    public void setNumMaxPossibleVotes(int MaxVotes) { num_maxPossibleVotes = MaxVotes; }
}

// comparator for MovieModel
class MovieModelComparator implements Comparator<MovieModel> {

    public static final String TAG = MovieModelComparator.class.getSimpleName();

    @Override
    public int compare(MovieModel m1, MovieModel m2) throws NullPointerException {
        if(m1 != null && m2 != null) {
            return m2.getNumVotes() - m1.getNumVotes();
        }
        throw new NullPointerException();
    };
}