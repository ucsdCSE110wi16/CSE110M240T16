package com.parse.starter;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

/**
 * Created by lpett on 2/20/2016.
 */
public class EventModel extends Model {

    public enum Status {
        Unanswered,
        Accepted,
        AcceptedAndVoted,
        Denied;
    }

    protected Date date;
    protected Calendar cal;
    protected String hostId, description, location, eventId, movieQueueId, time;
    protected int numFriendsInvited, numFriendsAttending, numFriendsVoted;

    public boolean isHost;

    public Status status = Status.Unanswered;

    //region Constructors

    public EventModel(String HostID, String Name, String Description, String Location,
                      String EventID, String MovieQueueID, Date EventDate, String Time) {
        numFriendsInvited = 0;
        numFriendsAttending = 0;
        numFriendsVoted = 0;
        hostId = HostID;
        name = Name;
        description = Description;
        eventId = EventID;
        movieQueueId = MovieQueueID;
        date = EventDate;
        location = Location;
        time = Time;
        cal = Calendar.getInstance();
        cal.setTime(date);
        status = Status.Unanswered;
        isHost = false;
    } // EventModel

    public EventModel(String HostID, String Name, String Description, String Location,
                      String EventID, String MovieQueueID, Date EventDate, String Time,
                      int NumFriendsInvited, int NumFriendsAttending, int NumFriendsVoted) {
        numFriendsInvited = NumFriendsInvited;
        numFriendsAttending = NumFriendsAttending;
        numFriendsVoted = NumFriendsVoted;
        hostId = HostID;
        name = Name;
        description = Description;
        date = EventDate;
        location = Location;
        time = Time;
        cal = Calendar.getInstance();
        cal.setTime(date);
        eventId = EventID;
        movieQueueId = MovieQueueID;
    } // EventModel

    //endregion

    //region Getters

    public String getHostID() {return this.hostId;}
    public String getDescription() {return this.description;}
    public String getLocation() {return this.location;}
    public String getEventID() {return this.eventId;}
    public String getMovieQueueID() {return this.movieQueueId;}
    public Date getDate() {return this.date;}
    public int getMonth() {
        return cal.get(Calendar.MONTH);
    }
    public String getMonthString(){
        return Integer.toString(getMonth());
    }
    public int getDay() { return cal.get(Calendar.DATE); }
    public String getDayString(){
        return Integer.toString(getDay());
    }
    public int getYear() {
        return cal.get(Calendar.YEAR);
    }
    public String getYearString(){
        return Integer.toString(getYear());
    }
    public String getMonthName(){
        return cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
    }
    public String getDayName(){
        return cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
    }
    public String getTime() {return this.time;}
    public int getNumFriendsAttending() {return this.numFriendsAttending;}
    public int getNumFriendsInvited() {return this.numFriendsInvited;}
    public int getNumFriendsVoted() {return this.numFriendsVoted;}

    //endregion

    //region Setters

    public void setDescription(String Description) {this.description = Description;}
    public void setLocation(String Location) {this.location = Location;}
    public void setTime(String Time) {this.time = Time;}
    public void setNumFriendsInvited(int numFriendsInvited) {this.numFriendsAttending = numFriendsInvited;}
    public void setNumFriendsAttending(int numFriendsAttending) {this.numFriendsAttending = numFriendsAttending;}
    public void setNumFriendsVoted(int numFriendsVoted) {this.numFriendsVoted = numFriendsVoted;}

    //endregion

}

// comparator for EventModel
class EventModelComparator implements Comparator<EventModel> {

    public static final String TAG = EventModelComparator.class.getSimpleName();

    @Override
    public int compare(EventModel m1, EventModel m2) throws NullPointerException {
        if(m1 != null && m2 != null) {
            return m1.getDate().compareTo(m2.getDate());
        }
        throw new NullPointerException();
    }
}
