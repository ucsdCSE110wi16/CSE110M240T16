package com.parse.starter;

/**
 * Created by lpett on 2/20/2016.
 */
public class MovieModel extends Model{
    private String movieId;
    public boolean hasVote;

    void MovieModel(String MovieID, String MovieName) {
        movieId = MovieID;
        name = MovieName;
    }

    public String getMovieID() {return movieId;}
}
