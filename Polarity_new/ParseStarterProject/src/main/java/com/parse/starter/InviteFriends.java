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
    Button btnBack, btnHome, btnSelectAll, btnInviteFreinds, btnSearch;
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
        btnBack = (Button) findViewById(R.id.viewEvent_btnBack);
        btnHome = (Button) findViewById(R.id.viewEvent_btnHome);
        btnSelectAll = (Button) findViewById(R.id.inviteFriends_btnSelectAll);
        btnInviteFreinds = (Button) findViewById(R.id.inviteFriends_btnInviteFriends);
        btnSearch = (Button) findViewById(R.id.inviteFriends_btnSearch);
        tbSearch = (EditText) findViewById(R.id.inviteFriends_tbSearch);
        txtInfo = (TextView) findViewById(R.id.inviteFriends_txtInfo);
        lvInvitedFriends = (ListView) findViewById(R.id.inviteFriends_lvFriendsList);
        // endregion
        // region setOnClickListeners
        btnBack.setOnClickListener(btnBack_Click());
        btnHome.setOnClickListener(btnHome_Click());
        btnSelectAll.setOnClickListener(btnSelectAll_Click());
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

    protected View.OnClickListener btnBack_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toActivity_CreateEvent();
            }
        };
    } // btnBack_Click

    protected View.OnClickListener btnHome_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toActivity_HubActivity();
            }
        };
    } // btnHome_Click

    protected View.OnClickListener btnSelectAll_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };
    } // btnSelectAll_Click

    protected View.OnClickListener btnInviteFriends_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toActivity_CreateEvent();
            }
        };
    } // btnInviteFriends_Click

    protected View.OnClickListener btnSearch_Click(){
        return new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // If the text box is empty, don't query the db
                if(!tbSearch.getText().toString().isEmpty()){

                    // Since users are unique, just get the first one found
                    ParseQuery<ParseObject> userQuery = ParseQuery.getQuery("_User");
                    userQuery.whereEqualTo("username", tbSearch.getText().toString());
                    userQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject pu, ParseException e) {
                            if (e == null && pu != null) {
                                // Add the user to the invited list
                                friendList.add(new FriendModel(tbSearch.getText().toString(), pu.getObjectId()));
                                // Since a user has been added, clear out the "no friends" text
                                txtInfo.setText("");
                            }//end if
                            else{
                                //TODO: Give the user a Toast notification telling them that no such
                                //TODO: user was found
                            }//end else

                        }//end done
                    });//end callback

                }//end if

            }//end onClick
        };//end listener
    }//end btnSearch_Click

    //endregion


}
