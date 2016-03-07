package com.parse.starter;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ViewUsers extends PolarityActivity implements View.OnKeyListener{

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_ENTER) {
            //hide soft keyboard
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            return false;
        }
        return true;
    }

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
        FriendModelComparator friendComp = new FriendModelComparator();

        @Override
        public void done(ParseObject object, ParseException e) {
            if(e == null) {
                if(object != null) {
                    FriendModel model = new FriendModel(object.getString("username"),
                                                        object.getObjectId(),
                                                        false,
                                                        FriendModel.State.DOT);

                    if(layout == Layout.VIEW_INVITES) {
                        if(com_friendIdList.contains(model.getUserID()) || model.getUserID().compareTo(com_userID) == 0) {
                            model.state = FriendModel.State.CHECK;
                        }
                        userList.add(model);
                        displayedUserList.add(model);
                        Collections.sort(displayedUserList, friendComp);
                        adapter.notifyDataSetChanged();
                    }
                    else if(layout == Layout.INVITE_FRIENDS) {
                        if(com_invitedFriends.contains(model)) model.state = FriendModel.State.CHECK;
                        model.isSelectable = true;
                        userList.add(model);
                        displayedUserList.add(model);
                        adapter.notifyDataSetChanged();
                    }
                    else { // view friend list
                        if(model.getUserID().compareTo(com_userID) == 0) return;
                        userList.add(model);
                        displayedUserList.add(model);
                        adapter.notifyDataSetChanged();
                    }


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

    Button btnBack, btnHome, btnAction, btnAddFriends;
    CheckBox cbAddAll;
    ListView lvUsers;
    TextView txtTitle, txtInfo;
    EditText tbSearch;
    boolean skipSelectAllCheckChange;

    ArrayList<FriendModel> displayedUserList, userList, inviteList;
    FriendAdapter adapter;

    Layout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_users);

        // init vars
        skipSelectAllCheckChange = false;
        btnBack = (Button) findViewById(R.id.viewUsers_btnBack);
        btnHome = (Button) findViewById(R.id.viewUsers_btnHome);
        cbAddAll = (CheckBox) findViewById(R.id.viewUsers_cbSelectAll);
        btnAddFriends = (Button) findViewById(R.id.viewUsers_btnAddFriends);

        lvUsers = (ListView) findViewById(R.id.viewUsers_lvUsers);

        txtTitle = (TextView) findViewById(R.id.viewUsers_txtTitle);
        txtInfo = (TextView) findViewById(R.id.viewUsers_txtInfo);

        userList = new ArrayList<FriendModel>();
        displayedUserList = new ArrayList<FriendModel>();
        inviteList = new ArrayList<FriendModel>();

        btnHome.setOnClickListener(btnHome_Click());
        btnBack.setOnClickListener(btnBackOnClickListener());
        cbAddAll.setOnCheckedChangeListener(cbAddAll_CheckChanged());
        btnAddFriends.setOnClickListener(btnAddFriends_Click());

        // Select the appropreate layout
        if(com_activityHistory.peek().compareTo(ViewEventActivity.class.getSimpleName()) == 0) {
            /* Layut:
             * Title = "Invite List"
             * Large Action button
             * Large Search bar
             * btnAddAll disabled
             * btnAddFriends disabled
             *
             * Controls:
             * ListView shows users invited to active event
             * ListViewItems are NOT selectable
             * ListViewItems icon is signselecticon if user is friends with them
             * Action button text = OK -> returns to ViewEvent
             * Search bar searches invited users
             */
            // hid and disable unused stuff
            btnAction = (Button) findViewById(R.id.viewUsers_btnActionShort);
            tbSearch = (EditText) findViewById(R.id.viewUsers_tbSearchShort);
            btnAction.setVisibility(View.INVISIBLE);
            tbSearch.setVisibility(View.INVISIBLE);
            btnAddFriends.setVisibility(View.INVISIBLE);
            cbAddAll.setVisibility(View.INVISIBLE);
            btnAction.setEnabled(false);
            tbSearch.setEnabled(false);
            btnAddFriends.setEnabled(false);
            cbAddAll.setEnabled(false);

            // set up special GUI elements
            txtTitle.setText("Invite List");
            btnAction = (Button) findViewById(R.id.viewUsers_btnActionLong);
            tbSearch = (EditText) findViewById(R.id.viewUsers_tbSearchLong);
            btnAction.setText("OK");
            btnAction.setOnClickListener(btnAction_Click());
            tbSearch.addTextChangedListener(tbSearch_TextChanged());
            layout = Layout.VIEW_INVITES;

            fetchInviteList();
        }
        else if(com_activityHistory.peek().compareTo(CreateEvent.class.getSimpleName()) == 0) {
            /* Layout:
             * Title = "Invite Friends"
             * Small Action button
             * Small Search bar
             * btnAddAll enabled
             * btnAddFriends enabled
             *
             * Controls:
             * ListView shows user's friends
             * ListViewItems are selectable
             * ListViewItems icon loads with signcheckedicon if already invited
             * Search bar searches user's friends
             * btnAddAll selects all users
             * Action button text = "Invite" -> invites the selected users and returns to previous activity
             */

            // hid and disable unused stuff
            btnAction = (Button) findViewById(R.id.viewUsers_btnActionLong);
            tbSearch = (EditText) findViewById(R.id.viewUsers_tbSearchLong);
            btnAction.setVisibility(View.INVISIBLE);
            tbSearch.setVisibility(View.INVISIBLE);
            btnAction.setEnabled(false);
            tbSearch.setEnabled(false);

            // set up special GUI elements
            btnAction = (Button) findViewById(R.id.viewUsers_btnActionShort);
            tbSearch = (EditText) findViewById(R.id.viewUsers_tbSearchShort);
            btnAction.setText("Invite");
            btnAction.setOnClickListener(btnAction_Click());
            lvUsers.setOnItemClickListener(lvUsers_Click());
            layout = Layout.INVITE_FRIENDS;
            txtTitle.setText("Invite Friends");
            tbSearch.addTextChangedListener(tbSearch_TextChanged());

            fetchFriendList();
        }
        else if(com_activityHistory.peek().compareTo(HubActivity.class.getSimpleName()) == 0) {
            /* Layout:
             * Title = "Friend List"
             * Small Action button
             * Large Search bar
             * btnAddAll disabled
             * btnAddFriends enabled
             *
             * Controls:
             * ListView shows user's friends
             * ListViewItems are NOT selectable
             * ListViewItems icon is signunselectedicon
             * Search bar searches user's friends
             * Action button text = "OK" -> returns to previous activity
             */

            // hid and disable unused stuff
            btnAction = (Button) findViewById(R.id.viewUsers_btnActionLong);
            tbSearch = (EditText) findViewById(R.id.viewUsers_tbSearchShort);
            btnAction.setVisibility(View.INVISIBLE);
            tbSearch.setVisibility(View.INVISIBLE);
            cbAddAll.setVisibility(View.INVISIBLE);
            btnAction.setEnabled(false);
            tbSearch.setEnabled(false);
            cbAddAll.setEnabled(false);

            // set up special GUI elements
            btnAction = (Button) findViewById(R.id.viewUsers_btnActionShort);
            tbSearch = (EditText) findViewById(R.id.viewUsers_tbSearchLong);
            btnAction.setOnClickListener(btnAction_Click());
            btnAction.setText("OK");
            layout = Layout.VIEW_FRIENDS;
            txtTitle.setText("Friends List");
            tbSearch.addTextChangedListener(tbSearch_TextChanged());
            tbSearch.setOnKeyListener(this);

            fetchFriendList();
        }
        else {
            Log.e(TAG, "Unexpectedly entered activity " + TAG + " from " + com_activityHistory.peek());
            returnToPrevActivity();
        }

        adapter = new FriendAdapter(getApplicationContext(), displayedUserList);
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

        // View Friends or View Invites
        if(layout == Layout.VIEW_FRIENDS || layout == Layout.VIEW_INVITES) return btnBackOnClickListener();

        // invite friends
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(FriendModel friend : inviteList) {
                    com_invitedFriends.add(friend);
                }
                returnToPrevActivity();
            }
        };
    } // btnAction_Click

    protected View.OnClickListener btnAddFriends_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivity(TAG, AddFriends.class.getSimpleName());
            }
        };
    } // btnAddFriends_Click

    //endregion

    //region Checkbox CheckChanged

    protected CompoundButton.OnCheckedChangeListener cbAddAll_CheckChanged() {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // check if need to skip
                if(skipSelectAllCheckChange) {
                    skipSelectAllCheckChange = false;
                    return;
                }

                // otherwise select/deselect all
                if(!isChecked) {
                    for(FriendModel model : displayedUserList) {
                        model.state = FriendModel.State.DOT;
                        inviteList.remove(model);
                    }
                }
                else {
                    for (FriendModel model : displayedUserList) {
                        model.state = FriendModel.State.CHECK;
                        if (!inviteList.contains(model)) inviteList.add(model);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        };
    } // cbAddAll_CheckChanged


    //endregion

    // region ListView Click

    private AdapterView.OnItemClickListener lvUsers_Click() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                FriendModel selected = ((FriendModel) lvUsers.getItemAtPosition(position));

                // if isSelectable
                if(selected.isSelectable) {
                    // revert the selection and add/remove FriendModel if needed
                    if(selected.state == FriendModel.State.DOT) {
                        selected.state = FriendModel.State.CHECK;
                        inviteList.add(selected);

                        if(inviteList.size() == com_friendIdList.size()) {
                            skipSelectAllCheckChange = true;
                            cbAddAll.setChecked(true);
                        }

                        Log.d(TAG, "User[" + selected.getUserID()
                                + "] Removed");
                    }
                    else {
                        //selected.isSelectable = true;
                        selected.state = FriendModel.State.DOT;
                        inviteList.remove(selected);

                        if(cbAddAll.isChecked()) {
                            skipSelectAllCheckChange = true;
                            cbAddAll.setChecked(false);
                        }

                        Log.d(TAG, "User[" + selected.getUserID()
                                + "] Added");
                    }

                    // tell adapter to update
                    adapter.notifyDataSetChanged();
                    Log.d(TAG, "friendsToAdd.size() = " + inviteList.size());
                }


            }
        };
    } // lvUserList_Click

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

    protected TextWatcher tbSearch_TextChanged(){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count > 0) {
                    displayedUserList.clear();
                    for(FriendModel friend : userList) {
                        if(friend.getName().startsWith(s.toString())) {
                            displayedUserList.add(friend);
                        }
                    }
                    if(layout == Layout.VIEW_INVITES) {
                        Collections.sort(displayedUserList, new FriendModelComparator());
                    }
                    adapter.notifyDataSetChanged();
                }
                else{
                    displayedUserList.clear();
                    displayedUserList.addAll(userList);
                    if(layout == Layout.VIEW_INVITES) {
                        Collections.sort(displayedUserList, new FriendModelComparator());
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
    } // tbTextChanged

    //endregion


}

