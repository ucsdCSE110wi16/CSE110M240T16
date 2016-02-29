package com.parse.starter;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

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
        // endregion
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

                ParseObject obj;
                v.setEnabled(false);
                btnDeny.setEnabled(false);

                ParseQuery.getQuery("InvitedFriends").whereEqualTo("UserID", com_userID).getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {

                        // check if there was an error
                        if(e != null) {
                            btnAccept.setEnabled(true);
                            btnDeny.setEnabled(true);
                            Log.e(TAG, e.getMessage());
                            displayToast("Unable to process request");
                            return;
                        }

                        object.put("Confirmation", 1);
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

                                com_currentEvent.status = EventModel.Status.Accepted;
                                btnDeny.setEnabled(true);
                                //TODO: refresh screen for new Status
                            }
                        });

                    }
                });

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

    //endregion

}
