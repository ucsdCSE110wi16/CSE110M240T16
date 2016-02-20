package com.parse.starter;

/**
 * Created by kendrick on 2/20/16.
 */
public class FriendModel extends Model {
   protected String userID;

   public FriendModel(String username, String userID){this.name = username; this.userID = userID;}

   public String getUserID(){ return userID;}

   public void setUserID(String userID){ this.userID = userID;}
}
