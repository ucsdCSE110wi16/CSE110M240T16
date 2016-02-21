package com.parse.starter;

/**
 * Created by lpett on 2/20/2016.
 */
public class MovieModel extends Model{
    private String movieId, movieEventId, description;
    public boolean hasVote;

    MovieModel(String MovieID, String MovieEventID, String Description, String MovieName) {
        movieId = MovieID;
        movieEventId = MovieEventID;
        description = Description;
        name = MovieName;
    }

    public String getMovieID() {return movieId;}
}
