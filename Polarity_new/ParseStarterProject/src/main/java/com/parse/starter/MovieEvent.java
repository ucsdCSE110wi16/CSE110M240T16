package com.parse.starter;

import java.util.Date;

/**
 * Holds movie event info
 * Created by lucas pettit on 2/17/2016.
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
}