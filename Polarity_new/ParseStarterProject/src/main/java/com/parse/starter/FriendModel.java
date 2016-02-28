package com.parse.starter;

/**
 * Created by kendrick on 2/20/16.
 */
public class FriendModel extends Model {

   public enum State {
      DOT,
      CHECK,
      ADD,
      DELETE
   }

   protected String userID;
   public boolean isSelectable;
   public State state;

   public FriendModel(String username, String userID, boolean IsSelectable, State InitialState){
      this.name = username;
      this.userID = userID;
      this.isSelectable = IsSelectable;
      state = InitialState;
   }

   public String getUserID(){ return userID;}

   public void setUserID(String userID){ this.userID = userID;}
}
