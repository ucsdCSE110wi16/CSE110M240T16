package com.parse.starter;

import java.util.Date;

/**
<<<<<<< HEAD
 * Holds movie event info
 * Created by lpett on 2/17/2016.
 */
public class MovieEvent {

    private Date eventdate;
    private String hostId, title, description;
    private int friendsInvited, friendsAttending, friendsVoted;

    /* Constructor */
    public void MovieEvent(String HostID, String Title, String Description, Date EventDate,
                           int FriendsInvited, int FriendsAttending, int FriendsVoted) {
        hostId = HostID;
        title = Title;
        description = Description;
        eventdate = EventDate;
        friendsInvited = FriendsInvited;
        friendsAttending = FriendsAttending;
        friendsVoted = FriendsVoted;
    }

    //region Setters

    public void Title(String newTitle) {title = newTitle;}
    public void Description(String newDescription) {description = newDescription;}
    public void FriendsAttending(int numFriendsAttending) {friendsAttending = numFriendsAttending;}
    public void FriendsVoted(int numFriendsVoted) {friendsVoted = numFriendsVoted;}

    //endregion

    //region Getters

    public String HostID() {return hostId;}
    public String Title() {return title;}
    public String Description() {return description;}
    public Date Date() {return eventdate;}
    public int FriendsAttending() {return friendsAttending;}
    public int FriendsInvited() {return friendsInvited;}
    public int FriendsVoted() {return friendsVoted;}

    //endregion
=======
 * This class holds Movie Event data so that we
 * will not need to re-access the database each time
 * we need some info
 * Created by Lucas Pettit on 2/17/2016.
 */
public class MovieEvent {
    private Date eventTime;
    private String eventTitle, eventDescription, hostID;
    private int friendsInvited, friendsAttending, friendsVoted;

    /* Constructor */
    public void MovieEvent(String HostID, String Title, String Description, Date Time,
                           int FriendsInvited, int FriendsAttending, int FriendsVoted) {
        this.hostID = HostID;
        this.eventTitle = Title;
        this.eventDescription = Description;
        this.eventTime = Time;
        this.friendsInvited = FriendsInvited;
        this.friendsAttending = FriendsAttending;
        this.friendsVoted = FriendsVoted;
    }

    //region Getters

    public String HostID() {return hostID; }
    public String Title() {return eventTitle;}
    public String Description() {return eventDescription;}
    public Date Time() {return eventTime;}
    public int FriendsInvited() {return friendsInvited;}
    public int FriendsAttending() {return friendsAttending;}
    public int FriendsVoted() {return friendsVoted;}

    //endregion

    //region Setters

    public void Title(String Title) {this.eventTitle = Title;}
    public void Description(String Description) {this.eventDescription = Description;}
    public void FriendsAttending(int FriendsAttending) {this.friendsAttending = FriendsAttending;}
    public void FriendsVoted(int FriendsVoted) {this.friendsVoted = FriendsVoted;}

    //endregion

>>>>>>> c9dae742489d815aa079fc2ced64f3c381613499
}