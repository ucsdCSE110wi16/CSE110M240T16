package com.parse.starter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import android.util.Log;
import android.widget.Toast;

public class InviteFriends extends PolarityActivity {
    // region variables
    Button btnBack, btnHome, btnSelectAll, btnInviteFreinds;
    EditText tbSearch;
    TextView txtInfo;
    ListView lvInvitedFriends;
    ArrayList<Model> friendList;
    CustomAdapter adapter;
    // endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friends);

        // region findViewById
        btnBack = (Button) findViewById(R.id.inviteFriends_btnBack);
        btnHome = (Button) findViewById(R.id.inviteFriends_btnHome);
        btnSelectAll = (Button) findViewById(R.id.inviteFriends_btnSelectAll);
        btnInviteFreinds = (Button) findViewById(R.id.inviteFriends_btnInviteFriends);
        tbSearch = (EditText) findViewById(R.id.inviteFriends_tbSearch);
        txtInfo = (TextView) findViewById(R.id.inviteFriends_txtInfo);
        lvInvitedFriends = (ListView) findViewById(R.id.inviteFriends_lvFriendsList);
        // endregion
        // region setOnClickListeners
        btnBack.setOnClickListener(btnBack_Click());
        btnHome.setOnClickListener(btnHome_Click());
        btnSelectAll.setOnClickListener(btnSelectAll_Click());
        btnInviteFreinds.setOnClickListener(btnInviteFriends_Click());
        // endregion
        // check if friends list is populated
        if(com_invitedFriends.size() == 0)
            txtInfo.setText("No friends invited :(");
        else
            txtInfo.setText("");

        friendList = com_invitedFriends;
        adapter = new CustomAdapter(getApplicationContext(), friendList);
        lvInvitedFriends.setAdapter(adapter);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
        // TODO: get all usernames from the username column and print them out to log
        // I've tried doing this but it didn't work
//        query.findInBackground(new FindCallback<ParseObject>() {
//            @Override
//            public void done(List<ParseObject> objects, ParseException e) {
//                if(e == null){
//                    for(int i = 0; i < objects.size(); i++){
//                        objects.getString("username");
//                    }
//                }
//                else{
//                    Log.d("USERNAMES", "Error: " + e.getMessage());
//                }
//            }
//        });
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

    //endregion


}
