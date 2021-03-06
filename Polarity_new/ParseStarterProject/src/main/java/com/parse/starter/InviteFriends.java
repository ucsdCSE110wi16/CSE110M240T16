package com.parse.starter;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class InviteFriends extends PolarityActivity {
    // region variables

    static String TAG = InviteFriends.class.getSimpleName();

    Button btnBack, btnHome, btnInviteFreinds, btnSearch;
    EditText tbSearch;
    TextView txtInfo;
    ListView lvInvitedFriends;
    ArrayList<FriendModel> friendList;
    FriendAdapter adapter;
    // endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friends);

        // region findViewById
        btnBack = (Button) findViewById(R.id.inviteFriends_btnBack);
        btnHome = (Button) findViewById(R.id.inviteFriends_btnHome);

        btnInviteFreinds = (Button) findViewById(R.id.inviteFriends_btnAddFriends);
        btnSearch = (Button) findViewById(R.id.inviteFriends_btnSearch);
        tbSearch = (EditText) findViewById(R.id.inviteFriends_tbSearch);
        txtInfo = (TextView) findViewById(R.id.inviteFriends_txtInfo);
        lvInvitedFriends = (ListView) findViewById(R.id.inviteFriends_lvSearchUsersList);
        // endregion
        // region setOnClickListeners
        btnBack.setOnClickListener(btnBackOnClickListener());
        btnHome.setOnClickListener(btnHome_Click());

        btnInviteFreinds.setOnClickListener(btnInviteFriends_Click());
        btnSearch.setOnClickListener(btnSearch_Click());
        // endregion
        // check if friends list is populated
        if(com_invitedFriends == null)
            txtInfo.setText("No friends invited :(");
        else
            txtInfo.setText("");

        friendList = com_invitedFriends;
        adapter = new FriendAdapter(getApplicationContext(), friendList);
        lvInvitedFriends.setAdapter(adapter);

    }


    //region Button Clicks

    protected View.OnClickListener btnHome_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivity(TAG, HubActivity.class.getSimpleName());
            }
        };
    } // btnHome_Click

    protected View.OnClickListener btnInviteFriends_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivity(TAG, CreateEvent.class.getSimpleName());
            }
        };
    } // btnInviteFriends_Click

    protected View.OnClickListener btnSearch_Click(){
        return new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // If the text box is empty, don't query the db
                if(!tbSearch.getText().toString().isEmpty()){
                    // This is to check if a user is already invited
                    boolean isAlreadyInvited = false;
                    //Iterates through the friendList and if a user is already in the list, isAlreadyInvited will be set to true
                    for(FriendModel f: friendList){
                        if((tbSearch.getText().toString()).equals(f.getName())){
                            displayToast("User " + tbSearch.getText().toString() + " is already invited");
                            isAlreadyInvited = true;
                            break;
                        }
                    }

                    // The app will make a call to the Parse database only if the invited user is not already invited
                    if(!isAlreadyInvited) {
                        // Since users are unique, just get the first one found
                        ParseQuery<ParseObject> userQuery = ParseQuery.getQuery("_User");
                        userQuery.whereEqualTo("username", tbSearch.getText().toString());
                        userQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                            @Override
                            public void done(ParseObject pu, ParseException e) {
                                if (e == null && pu != null) {
                                    // Add the user to the invited list
                                    friendList.add(new FriendModel(tbSearch.getText().toString(),
                                            pu.getObjectId(),
                                            false,
                                            FriendModel.State.DELETE));

                                    // Resets the text in the search bar
                                    tbSearch.setText("");

                                    // Since a user has been added, clear out the "no friends" text
                                    txtInfo.setText("");
                                }//end if
                                else {
                                    displayToast("User " + tbSearch.getText().toString() + " does not exist");
                                    // Resets the text in the search bar
                                    tbSearch.setText("");
                                }//end else

                            }//end done
                        });//end callback
                    }

                    // Resets the text in the search bar
                    else{
                        tbSearch.setText("");
                    }

                }//end if

            }//end onClick
        };//end listener
    }//end btnSearch_Click

    //endregion


}
