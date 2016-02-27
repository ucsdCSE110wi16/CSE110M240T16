package com.parse.starter;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ViewEventActivity extends PolarityActivity {

    public static final String TAG = ViewEventActivity.class.getSimpleName();
    TextView txtTitle, txtLocation, txtDate, txtDescription, txtTime;
    Button btnBack, btnViewInviteList, btnVoteOnMovies, btnAccept, btnDeny;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);
        // region findViewById
        txtTitle = (TextView) findViewById(R.id.viewEvent_txtTitle);
        txtLocation = (TextView) findViewById(R.id.viewEvent_txtLocation);
        txtDescription = (TextView) findViewById(R.id.viewEvent_description);
        txtDate = (TextView) findViewById(R.id.viewEvent_date);
        txtTime = (TextView) findViewById(R.id.viewEvent_txtTime);
        btnBack = (Button) findViewById(R.id.addFriends_btnBack);
        btnViewInviteList = (Button) findViewById(R.id.viewEvent_btnViewInviteList);
        btnVoteOnMovies = (Button) findViewById(R.id.viewEvent_btnVoteOnMovies);
        btnAccept = (Button) findViewById(R.id.viewEvent_btnAcceptInvite);
        btnDeny = (Button) findViewById(R.id.viewEvent_btnDenyInvite);
        // endregion
        // region setText
        txtTitle.setText(com_currentEvent.getName());
        txtDescription.setText(com_currentEvent.getDescription());
        txtLocation.setText(com_currentEvent.getLocation());
        txtDate.setText(com_currentEvent.getDate().toString().substring(0, 10) + ", " +
                com_currentEvent.getDate().toString().substring(24));
        txtTime.setText(com_currentEvent.getDate().toString().substring(11, 16));
        Log.d("TAG", com_currentEvent.getDate().toString());
        // endregion
        // region setOnClickListener
        btnBack.setOnClickListener(btnBack_Click());

        btnViewInviteList.setOnClickListener(btnViewInviteList_Click());
        btnVoteOnMovies.setOnClickListener(btnVoteOnMovies_Click());
        btnAccept.setOnClickListener(btnAccept_Click());
        btnDeny.setOnClickListener(btnDeny_Click());
        // endregion
    }


    //region Button Click

    protected View.OnClickListener btnBack_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toActivity_HubActivity();
            }
        };
    } // btnBack_Click

//    protected View.OnClickListener btnHome_Click() {
//        return new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                toActivity_HubActivity();
//            }
//        };
//    } // btnHome_Click

    protected View.OnClickListener btnViewInviteList_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: link to view invite list page
            }
        };
    } // btnViewInviteList_Click

    protected View.OnClickListener btnVoteOnMovies_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toActivity_Vote();
            }
        };
    } // btnVoteOnMovies_Click

    protected View.OnClickListener btnAccept_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseObject obj;
                try {
                    v.setEnabled(false);
                    btnDeny.setEnabled(false);

                    obj = ParseQuery.getQuery("InvitedFriends").whereEqualTo("UserID", com_userID).getFirst();
                    obj.put("Confirmation", 1);
                    obj.save();
                    com_currentEvent.status = EventModel.Status.Accepted;

                } catch (ParseException e) {
                    v.setEnabled(true);
                    btnDeny.setEnabled(true);
                    Log.e(TAG, e.getMessage());
                    displayToast("Unable to process request");
                }
            }
        };
    } // btnAccept_Click

    protected View.OnClickListener btnDeny_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseObject obj;
                try {
                    v.setEnabled(false);
                    btnAccept.setEnabled(false);
                    obj = ParseQuery.getQuery("InvitedFriends").whereEqualTo("UserID", com_userID).getFirst();
                    obj.put("Confirmation", 3);
                    obj.save();
                    com_currentEvent.status = EventModel.Status.Denied;
                    com_eventModelList.remove(com_eventModelList.indexOf(com_currentEvent));
                    toActivity_HubActivity();

                } catch (ParseException e) {
                    v.setEnabled(true);
                    btnAccept.setEnabled(true);
                    Log.e(TAG, e.getMessage());
                    displayToast("Unable to process request");
                }
            }
        };
    } // btnDeny_Click

    //endregion

}
