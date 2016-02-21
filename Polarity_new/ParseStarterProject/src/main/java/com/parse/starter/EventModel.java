package com.parse.starter;

import com.parse.starter.Model;

import java.util.Comparator;
import java.util.Date;

/**
 * Created by lpett on 2/20/2016.
 */
public class EventModel extends Model {

    protected Date date;
    protected String hostId, description, eventId, movieQueueId;
    protected int numFriendsInvited, numFriendsAttending, numFriendsVoted;

    //region Constructors

    public EventModel(String HostID, String Name, String Description, String EventID,
                      String MovieQueueID, Date EventDate) {
        numFriendsInvited = 0;
        numFriendsAttending = 0;
        numFriendsVoted = 0;
        hostId = HostID;
        name = Name;
        description = Description;
        eventId = EventID;
        movieQueueId = MovieQueueID;
        date = EventDate;
    } // EventModel

    public EventModel(String HostID, String Name, String Description, String EventID,
                      String MovieQueueID, Date EventDate, int NumFriendsInvited,
                      int NumFriendsAttending, int NumFriendsVoted) {
        numFriendsInvited = NumFriendsInvited;
        numFriendsAttending = NumFriendsAttending;
        numFriendsVoted = NumFriendsVoted;
        hostId = HostID;
        name = Name;
        description = Description;
        date = EventDate;
    } // EventModel

    //endregion

    //region Getters

    public String getHostID() {return hostId;}
    public String getDescription() {return description;}
    public String getEventID() {return eventId;}
    public String getMovieQueueID() {return movieQueueId;}
    public Date getDate() {return date;}
    public int getNumFriendsAttending() {return numFriendsAttending;}
    public int getNumFriendsInvited() {return numFriendsInvited;}
    public int getNumFriendsVoted() {return numFriendsVoted;}

    //endregion

    //region Setters

    public void setDescription(String Description) {description = Description;}
    public void setNumFriendsInvited(int numFriendsInvited) {this.numFriendsAttending = numFriendsInvited;}
    public void setNumFriendsAttending(int numFriendsAttending) {this.numFriendsAttending = numFriendsAttending;}
    public void setNumFriendsVoted(int numFriendsVoted) {this.numFriendsVoted = numFriendsVoted;}

    //endregion

}

// comparator for EventModel
class CompareEventModel implements Comparator<EventModel> {
    @Override
    public int compare(EventModel m1, EventModel m2) throws NullPointerException {
        if(m1 != null && m2 != null) {
            return m1.getDate().compareTo(m2.getDate());
        }
        throw new NullPointerException();
    }
}
