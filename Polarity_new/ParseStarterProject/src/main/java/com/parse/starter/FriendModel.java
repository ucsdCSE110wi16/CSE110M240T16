package com.parse.starter;

import java.util.Comparator;

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

// comparator for EventModel
class FriendModelComparator extends PolarityActivity implements Comparator<FriendModel> {

   public static final String TAG = EventModelComparator.class.getSimpleName();

   @Override
   public int compare(FriendModel m1, FriendModel m2) throws NullPointerException {
      if(m1 != null && m2 != null) {
         // prioritize for user id
         if(m1.getUserID().compareTo(com_userID) == 0) return -1;
         if(m2.getUserID().compareTo(com_userID) == 0) return 1;

         // prioritize for friend
         if(com_friendIdList.contains(m1.getUserID()) && !com_friendIdList.contains(m2.getUserID())) return -1;
         if(!com_friendIdList.contains(m1.getUserID()) && com_friendIdList.contains(m2.getUserID())) return 1;

         return 0;
      }
      throw new NullPointerException();
   }
}