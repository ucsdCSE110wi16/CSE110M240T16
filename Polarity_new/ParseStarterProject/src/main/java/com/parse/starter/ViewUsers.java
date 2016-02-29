package com.parse.starter;

import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class ViewUsers extends PolarityActivity {

    private enum Layout {
        VIEW_INVITES,
        VIEW_FRIENDS,
        INVITE_FRIENDS
    }

    static String TAG = ViewUsers.class.getSimpleName();

    //region FindCallBack
    protected FindCallback<ParseObject> FindInvitedCallBack = new FindCallback<ParseObject>() {
        @Override
        public void done(List<ParseObject> objects, ParseException e) {
            if(e == null) {
                if(objects != null) {
                    ArrayList<String> invitedUserIdList = new ArrayList<String>();
                    for(ParseObject obj : objects) {
                        invitedUserIdList.add(obj.getString("UserID"));
                    }
                    populateListViewFromIds(invitedUserIdList);
                }
                else {
                    Log.e(TAG, " Invited List unexpectedly returned NULL");
                }
            }
            else {
                Log.e(TAG, e.getMessage());
            }

        }
    }; // FindInvitedCallBack
    //endregion

    //region GetUserCallBack
    protected GetCallback<ParseObject> GetUserCallBack = new GetCallback<ParseObject>() {
        @Override
        public void done(ParseObject object, ParseException e) {
            if(e == null) {
                if(object != null) {
                    FriendModel model = new FriendModel(object.getString("username"),
                                                        object.getObjectId(),
                                                        false,
                                                        FriendModel.State.DOT);

                    if(layout == Layout.VIEW_INVITES && com_friendIdList.contains(model.getUserID())) {
                        model.state = FriendModel.State.CHECK;
                    }

                    userList.add(model);
                    adapter.notifyDataSetChanged();
                }
                else {
                    Log.e(TAG, " User object unexpectedly returned NULL");
                }
            }
            else {
                Log.e(TAG, e.getMessage());
            }
        }
    };
    //endregion

    Button btnBack, btnHome, btnAddAll, btnAction, btnAddFriends;
    ListView lvUsers;
    TextView txtTitle, txtInfo;
    EditText tbSearch;
    boolean allSelected;

    ArrayList<FriendModel> userList, inviteList;
    FriendAdapter adapter;

    Layout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_users);

        // init vars
        allSelected = false;
        btnBack = (Button) findViewById(R.id.viewUsers_btnBack);
        btnHome = (Button) findViewById(R.id.viewUsers_btnHome);
        btnAddAll = (Button) findViewById(R.id.viewUsers_btnAddAll);
        btnAction = (Button) findViewById(R.id.viewUsers_btnAction);
        btnAddFriends = (Button) findViewById(R.id.viewUsers_btnAddFriends);

        tbSearch = (EditText) findViewById(R.id.viewUsers_tbSearch);

        lvUsers = (ListView) findViewById(R.id.viewUsers_lvUsers);

        txtTitle = (TextView) findViewById(R.id.viewUsers_txtTitle);
        txtInfo = (TextView) findViewById(R.id.viewUsers_txtInfo);

        userList = new ArrayList<FriendModel>();
        inviteList = new ArrayList<FriendModel>();

        btnHome.setOnClickListener(btnHome_Click());
        btnBack.setOnClickListener(btnBackOnClickListener());
        btnAddAll.setOnClickListener(btnAddAll_Click());
        btnAction.setOnClickListener(btnAction_Click());
        btnAddFriends.setOnClickListener(btnAddFriends_Click());

        // Select the appropreate layout
        if(com_activityHistory.peek().compareTo(ViewEventActivity.class.getSimpleName()) == 0) {
            layout = Layout.VIEW_INVITES;
            btnAddFriends.setVisibility(View.INVISIBLE);
            btnAddAll.setEnabled(false);

            // dynamically resize the button width to fill the parent container
            Display display = getWindowManager().getDefaultDisplay();
            btnAction.setWidth(display.getWidth() - 32);

            btnAction.setText("OK");
            txtTitle.setText("Invite List");
            fetchInviteList();
        }
        else if(com_activityHistory.peek().compareTo(CreateEvent.class.getSimpleName()) == 0) {
            layout = Layout.INVITE_FRIENDS;
            txtTitle.setText("Invite Friends");
            btnAction.setText("INVITE");
            fetchFriendList();
        }
        else if(com_activityHistory.peek().compareTo(HubActivity.class.getSimpleName()) == 0) {
            layout = Layout.VIEW_FRIENDS;
            btnAction.setText("OK");
            btnAddAll.setEnabled(false);
            txtTitle.setText("Friend List");
            fetchFriendList();
        }
        else {
            Log.e(TAG, "Unexpectedly entered activity " + TAG + " from " + com_activityHistory.peek());
            returnToPrevActivity();
        }

        adapter = new FriendAdapter(getApplicationContext(), userList);
        lvUsers.setAdapter(adapter);
    }

    //region Button Clicks

    protected View.OnClickListener btnHome_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivity(HubActivity.class.getSimpleName(), HubActivity.class.getSimpleName());
            }
        };
    } // btnHome_Click

    protected View.OnClickListener btnAction_Click() {

        // View Friends
        if(layout == Layout.VIEW_FRIENDS) return btnHome_Click();

        // invite friends
        if(layout == Layout.INVITE_FRIENDS) return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(FriendModel friend : inviteList) {
                    com_invitedFriends.add(friend);
                }
            }
        };

        // view invited list
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivity(HubActivity.class.getSimpleName(), ViewEventActivity.class.getSimpleName());
            }
        };
    } // btnAction_Click

    protected View.OnClickListener btnAddAll_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(allSelected) {
                    allSelected = false;
                    btnAddAll.setBackgroundResource(R.drawable.signuncheckicon);
                    for(FriendModel model : userList) {
                        model.state = FriendModel.State.DOT;
                        inviteList.remove(model);
                    }
                }
                else {
                    allSelected = true;
                    btnAddAll.setBackgroundResource(R.drawable.signcheckicon);
                    for (FriendModel model : userList) {
                        model.state = FriendModel.State.CHECK;
                        if (!inviteList.contains(model)) inviteList.add(model);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        };
    } // btnAddAll_Click

    protected View.OnClickListener btnAddFriends_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivity(TAG, AddFriends.class.getSimpleName());
            }
        };
    } // btnAddFriends_Click

    //endregion

    //region Helpers

    protected void fetchInviteList() {
        ParseQuery.getQuery("InvitedFriends").whereEqualTo("EventID", com_currentEventId).findInBackground(FindInvitedCallBack);
    } // fetchInviteList

    protected void fetchFriendList() {
        populateListViewFromIds(com_friendIdList);
    } // fetchFreindList

    protected void populateListViewFromIds(ArrayList<String> idList) {
        for(String id : idList) {
            ParseQuery.getQuery("_User").whereEqualTo("objectId", id).getFirstInBackground(GetUserCallBack);
        }
    } // populateListViewFromIds

    //endregion


}

