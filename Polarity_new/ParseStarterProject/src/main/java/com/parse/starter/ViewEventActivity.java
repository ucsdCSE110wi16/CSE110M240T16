package com.parse.starter;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

public class ViewEventActivity extends PolarityActivity {

    public static final String TAG = ViewEventActivity.class.getSimpleName();
    TextView txtTitle, txtLocation, txtDate, txtDescription, txtTime;
    Button btnBack, btnViewInviteList, btnVoteOnMovies, btnAccept, btnDeny;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

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
        btnBack = (Button) findViewById(R.id.viewEvent_btnBack);
        btnViewInviteList = (Button) findViewById(R.id.viewEvent_btnViewInviteList);
        btnVoteOnMovies = (Button) findViewById(R.id.viewEvent_btnVoteOnMovies);
        btnAccept = (Button) findViewById(R.id.viewEvent_btnAcceptInvite);
        btnDeny = (Button) findViewById(R.id.viewEvent_btnDenyInvite);
        // endregion
        // region setText
        txtTitle.setText(com_currentEvent.getName());
        String descriptionFormatted = "Description:\n" + com_currentEvent.getDescription();
        txtDescription.setText(descriptionFormatted);
        String locationFormatted = "Location:  " + com_currentEvent.getLocation();
        txtLocation.setText(locationFormatted);

        //Format the string prior to passing it in to setText, because it doesn't like concatenation
        //within the call
        String eventDateFormatted = com_currentEvent.getDayName() + " " + com_currentEvent.getMonthName()
                + " " + com_currentEvent.getDayString() + ", " + com_currentEvent.getYearString();
        txtDate.setText(eventDateFormatted);
        String eventTimeFormatted = "Time:  " + com_currentEvent.getTime();
        txtTime.setText(eventTimeFormatted);
        Log.d("TAG", com_currentEvent.getDate().toString());
        // endregion
        // region setOnClickListener
        btnBack.setOnClickListener(btnBackOnClickListener());

        btnViewInviteList.setOnClickListener(btnViewInviteList_Click());
        btnVoteOnMovies.setOnClickListener(btnVoteOnMovies_Click());
        btnAccept.setOnClickListener(btnAccept_Click());
        btnDeny.setOnClickListener(btnDeny_Click());

        if (com_currentEvent.status != EventModel.Status.Unanswered) {
            btnAccept.setEnabled(false);
            btnDeny.setEnabled(false);
            btnAccept.setVisibility(View.GONE);
            btnDeny.setVisibility(View.GONE);
            btnDeny = (Button) findViewById(R.id.viewEvent_btnUnattend);
            btnDeny.setVisibility(View.VISIBLE);
            btnAccept.setEnabled(true);
            btnDeny.setOnClickListener(btnDeny_Click());

            if (com_currentEvent.status == EventModel.Status.Accepted)
                btnVoteOnMovies.setText("Vote On Movies");
            else if (com_currentEvent.status == EventModel.Status.AcceptedAndVoted)
                btnVoteOnMovies.setText("View Polls");

            if(com_currentEvent.isHost) btnDeny.setText("Cancel Event");
        }
        // endregion
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    //region Button Click

    protected View.OnClickListener btnViewInviteList_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivity(TAG, ViewUsers.class.getSimpleName());
            }
        };
    } // btnViewInviteList_Click

    protected View.OnClickListener btnVoteOnMovies_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivity(TAG, VoteActivity.class.getSimpleName());
            }
        };
    } // btnVoteOnMovies_Click

    protected View.OnClickListener btnAccept_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
                btnDeny.setEnabled(false);

                ParseObject obj;
                try {
                    obj = ParseQuery.getQuery("InvitedFriends").whereEqualTo("UserID", com_userID).whereEqualTo("EventID", com_currentEventId).getFirst();

                    Log.d(TAG, "Event{" + obj.getString("EventID") + "] UserID[" + obj.getString("UserID") + "]");

                    obj.put("Confirmation", 1);
                    obj.save();

                    EventModel model = com_eventModelList.get(com_eventModelList.indexOf(com_currentEvent));
                    model.numFriendsAttending = model.numFriendsAttending++;
                    model.status = EventModel.Status.Accepted;
                    com_eventModelList.set(com_eventModelList.indexOf(model), model);

                    btnDeny.setVisibility(View.GONE);
                    btnAccept.setVisibility(View.GONE);
                    btnDeny = (Button) findViewById(R.id.viewEvent_btnUnattend);
                    btnDeny.setEnabled(true);
                    btnDeny.setVisibility(View.VISIBLE);
                    btnDeny.setOnClickListener(btnDeny_Click());
                    btnVoteOnMovies.setText("Vote On Movies");

                    displayToast("Invite accepted");

                } catch (ParseException e) {
                    btnAccept.setEnabled(true);
                    btnDeny.setEnabled(true);
                    Log.e(TAG, e.getMessage());
                }
            }
        };
    } // btnAccept_Click

    protected View.OnClickListener btnDeny_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.setEnabled(false);
                btnAccept.setEnabled(false);
                ParseQuery.getQuery("InvitedFriends").whereEqualTo("UserID", com_userID).getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {

                        // check if there was an error
                        if (e != null) {
                            btnAccept.setEnabled(true);
                            btnDeny.setEnabled(true);
                            Log.e(TAG, e.getMessage());
                            displayToast("Unable to process request");
                            return;
                        }

                        object.put("Confirmation", 3);
                        object.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {

                                // check if there was an error
                                if (e != null) {
                                    btnAccept.setEnabled(true);
                                    btnDeny.setEnabled(true);
                                    Log.e(TAG, e.getMessage());
                                    displayToast("Unable to process request");
                                    return;
                                }

                                com_currentEvent.status = EventModel.Status.Denied;
                                com_eventModelList.remove(com_eventModelList.indexOf(com_currentEvent));
                                goToActivity(TAG, HubActivity.class.getSimpleName());
                            }
                        });
                    }
                });

            }
        };
    } // btnDeny_Click

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ViewEvent Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.parse.starter/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ViewEvent Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.parse.starter/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    //endregion

}
