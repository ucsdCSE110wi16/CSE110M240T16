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

import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Collections;
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
    ArrayList<String> friendsToAdd;

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);

        numNewFriends = 0;
        friendsToAdd = new ArrayList<String>();
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
                if(((FriendModel) lvUserList.getItemAtPosition(position)).isSelected) {
                    ((FriendModel) lvUserList.getItemAtPosition(position)).isSelectable = true;
                    ((FriendModel) lvUserList.getItemAtPosition(position)).isSelected = false;
                    friendsToAdd.remove(((FriendModel) lvUserList.getItemAtPosition(position)).getUserID());
                }
                else {
                    ((FriendModel) lvUserList.getItemAtPosition(position)).isSelectable = true;
                    ((FriendModel) lvUserList.getItemAtPosition(position)).isSelected = true;
                    friendsToAdd.add(((FriendModel) lvUserList.getItemAtPosition(position)).getUserID());
                }

                friendAdapter.notifyDataSetChanged();
                Log.d(TAG, "friendsToAdd.size() = " + friendsToAdd.size());

            }
        };
    } // lvUserList_Click

    //endregion

//region Helpers

    protected void addFriends() {

        if(friendsToAdd.size() > 0) {
            ParseQuery<ParseObject> pq;
            ParseObject user;
            ParseObject newFollow;
            List<ParseObject> newFollowList = new ArrayList<ParseObject>();

            pq = ParseQuery.getQuery("_User");

            for (String id : friendsToAdd) {
                try {
                    user = pq.whereEqualTo("objectId", id).getFirst();
                    newFollow = new ParseObject("FriendsFollowing");
                    newFollow.add("UserID", com_userID);
                    newFollow.add("FriendID", user.getString("FriendID"));
                    newFollow.add("FollowRequestBlocked", false);
                    newFollowList.add(newFollow);
                } catch (ParseException e) {
                    Log.e(TAG, "Unable to get ParseQuery");
                    numNewFriends = 0;
                    return;
                }

            }

            numNewFriends = friendsToAdd.size();
            user = new ParseObject("FriendsFallowing");
            user.saveAllInBackground(newFollowList, new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e != null) {
                        Log.e(TAG, e.getMessage());
                        displayToast(e.getMessage());
                    } else {
                        displayToast(numNewFriends + " new friends added!");
                    }
                }
            });
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
                        Log.e(AddFriends.class.getSimpleName(), " Unable to access table _User");
                        return;
                    }

                    users.clear();

                    for(int i=0; i<autoComplete.size(); i++) {
                        user = autoComplete.get(i);
                        if(user.getObjectId().compareTo(com_userID) == 0) continue;
                        model = new FriendModel(user.getString("username"), user.getObjectId(), true, false);
                        users.add(model);
                    }

                    friendAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
    } // tbTextChanged

//endregion

}