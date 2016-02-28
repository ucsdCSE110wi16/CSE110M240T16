package com.parse.starter;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class AddFriends extends PolarityActivity {

    //region Variables

    public static final String TAG = AddFriends.class.getSimpleName();

    int numNewFriends;

    Button btnBack, btnHome, btnAdd, btnCancel;
    EditText tbSearch;
    ListView lvUserList;
    FriendAdapter friendAdapter;
    ArrayList<FriendModel> users;
    ArrayList<FriendModel> friendsToAdd;

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);

        numNewFriends = 0;
        friendsToAdd = new ArrayList<FriendModel>();
        users = new ArrayList<FriendModel>();

        btnBack = (Button) findViewById(R.id.addFriends_btnBack);
        btnCancel = (Button) findViewById(R.id.addFriends_Cancel);
        btnHome = (Button) findViewById(R.id.addFriends_btnHome);
        btnAdd = (Button) findViewById(R.id.addFriends_btnAddFriends);
        tbSearch = (EditText) findViewById(R.id.addFriends_tbSearch);
        lvUserList = (ListView) findViewById(R.id.addFriends_lvSearchUsersList);

        btnBack.setOnClickListener(btnBack_Click());
        btnCancel.setOnClickListener(btnCancel_Click());
        btnHome.setOnClickListener(btnHome_Click());
        btnAdd.setOnClickListener(btnAdd_Click());

        tbSearch.addTextChangedListener(tbTextChanged());

        lvUserList.setOnItemClickListener(lvUserList_Click());

        friendAdapter = new FriendAdapter(getApplicationContext(), users);
        lvUserList.setAdapter(friendAdapter);

        // remove focus from tbSearch
        lvUserList.requestFocus();
        tbSearch.clearFocus();

        displayAllUsers();
    }


//region Button Clicks

    protected View.OnClickListener btnBack_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivity(TAG, com_previousActivity);
            }
        };
    } // btnBack_Click

    protected View.OnClickListener btnCancel_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivity(TAG, com_previousActivity);
            }
        };
    } // btnCancel_Click

    protected View.OnClickListener btnHome_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivity(TAG, HubActivity.class.getSimpleName());
            }
        };
    } // btnHome_Click

    protected View.OnClickListener btnAdd_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(friendsToAdd.size() > 0) {
                    addFriends();
                }
                else {
                    displayToast("You have not selected anyone to add");
                }
            }
        };
    } // btnAdd_Click

//endregion

// region ListView Click

    private AdapterView.OnItemClickListener lvUserList_Click() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                FriendModel selected = ((FriendModel) lvUserList.getItemAtPosition(position));

                // if isSelectable
                if(selected.isSelectable) {
                    // revert the selection and add/remove FriendModel if needed
                    if(selected.state == FriendModel.State.DOT) {
                        selected.state = FriendModel.State.ADD;
                        friendsToAdd.add(selected);

                        Log.d(TAG, "User[" + selected.getUserID()
                                + "] Removed");
                    }
                    else {
                        //selected.isSelectable = true;
                        selected.state = FriendModel.State.DOT;
                        friendsToAdd.remove(selected);

                        Log.d(TAG, "User[" + selected.getUserID()
                                + "] Added");
                    }

                    // tell adapter to update
                    friendAdapter.notifyDataSetChanged();
                    Log.d(TAG, "friendsToAdd.size() = " + friendsToAdd.size());
                }


            }
        };
    } // lvUserList_Click

    //endregion

//region Helpers

    protected void addFriends() {

        if(friendsToAdd.size() > 0) {
            ParseObject newFollow, users;
            List<ParseObject> newFollowList = new LinkedList<ParseObject>();
            ParseACL acl;

            for (FriendModel model : friendsToAdd) {
                acl = new ParseACL();
                acl.setPublicReadAccess(true);
                acl.setWriteAccess(com_userID, true);
                acl.setWriteAccess(model.getUserID(), true);

                newFollow = new ParseObject("FriendsFollowing");
                newFollow.put("UserID", com_userID);
                newFollow.put("FriendID", model.getUserID());
                newFollow.put("FollowRequestBlocked", false);
                newFollow.setACL(acl);
                newFollowList.add(newFollow);
            }

            users = new ParseObject("FriendsFollowing");
            users.saveAllInBackground(newFollowList, new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e == null) {
                        // update all the friends
                        for(FriendModel friend : friendsToAdd) {
                            friend.state = FriendModel.State.CHECK;
                            friend.isSelectable = false;
                            com_friendIdList.add(friend.getUserID());
                        }
                        friendAdapter.notifyDataSetChanged();
                        displayToast("Successful!");
                    }
                    else {
                        Log.e(TAG, e.getCode() + " " + e.getMessage());
                        displayToast("Unable to set friend request");
                    }
                }
            });

            numNewFriends = friendsToAdd.size();
        }
    } // addFriends

    protected TextWatcher tbTextChanged(){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count > 0){
                    ParseObject user;
                    FriendModel model;
                    List<ParseObject> autoComplete;

                    try {
                        autoComplete = ParseQuery.getQuery("_User").whereStartsWith("username", s.toString()).find();
                    } catch (ParseException e) {
                        return;
                    }

                    ParseQuery.getQuery("_User").whereStartsWith("username", s.toString()).findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if (e == null) {
                                FriendModel model;
                                users.clear();

                                // / loop through each parseobject in object
                                for (ParseObject user : objects) {
                                    // skip if the user is the active user
                                    if (user.getObjectId().compareTo(com_userID) == 0) continue;
                                    model = new FriendModel(user.getString("username"),
                                            user.getObjectId(),
                                            true,
                                            FriendModel.State.DOT);

                                    // if the person is already a friend, then set them to selected and to be unselectable
                                    if (com_friendIdList.contains(model.getUserID())) {
                                        model.isSelectable = false;
                                        model.state = FriendModel.State.CHECK;
                                    }

                                    // add model to users list
                                    users.add(model);
                                }

                                friendAdapter.notifyDataSetChanged();
                            } else {
                                Log.e(AddFriends.class.getSimpleName(), " Unable to access table _User");
                            }
                        }
                    });
                }
                else{
                    displayAllUsers();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
    } // tbTextChanged

    protected void displayAllUsers() {
        ParseQuery.getQuery("_User").findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if(e == null) {
                    FriendModel person;
                    users.clear();

                    // loop trhough all teh objects
                    for(int i=0; i<objects.size() && i<50; i++) {

                        // create FriendModel of user
                        person = new FriendModel(objects.get(i).getString("username"),
                                                objects.get(i).getObjectId(), true, FriendModel.State.DOT);

                        // skip if person is the active user
                        if(person.getUserID().compareTo(com_userID) == 0) continue;

                        // if the person is already a friend, then set them to selected and to be unselectable
                        if (com_friendIdList.contains(person.getUserID())) {
                            person.isSelectable = false;
                            person.state = FriendModel.State.CHECK;
                        }

                        // add the person to the queue
                        users.add(person);
                    }

                    friendAdapter.notifyDataSetChanged();
                }
                else {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
    }

//endregion

}
