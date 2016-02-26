package com.parse.starter;

/**
 * Created by kendrick on 2/20/16.
 */
public class FriendModel extends Model {

   protected String userID;
   public boolean isSelectable, isDeletable, isSelected;

   public FriendModel(String username, String userID, boolean IsSelectable, boolean IsDeletable){
      this.name = username;
      this.userID = userID;
      this.isSelectable = IsSelectable;
      this.isDeletable = IsDeletable;
      isSelectable = false;
   }

   public String getUserID(){ return userID;}

   public void setUserID(String userID){ this.userID = userID;}
}
