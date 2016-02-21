package com.parse.starter;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.CalendarView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;

public class CreateEvent extends PolarityActivity {

    public static final String TAG = CreateEvent.class.getSimpleName();

    Button btnBack, btnAddMovie, btnInviteFriends, btnCreateEvent, btnHome;
    EditText tbEventName, tbEventLocation, tbEventTime, tbEventDescription;

    DatePicker datePicker;
    Calendar myCalendar;
    int year_o, month_o, day_o;
    static final int DIALOG_ID = 0; //hold the dialog id


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        btnBack = (Button) findViewById(R.id.inviteFriends_btnBack);
        btnAddMovie = (Button) findViewById(R.id.inviteFriends_btnSelectAll);
        btnInviteFriends = (Button) findViewById(R.id.inviteFriends_btnInviteFriends);
        btnCreateEvent = (Button) findViewById(R.id.createEvent_btnCreateEvent);

        tbEventName = (EditText) findViewById(R.id.createEvent_tbName);
        tbEventLocation = (EditText) findViewById(R.id.createEvent_tbLocation);
        tbEventTime = (EditText) findViewById(R.id.createEvent_tbTime);
        tbEventDescription = (EditText) findViewById(R.id.createEvent_tbDescription);

        btnBack.setOnClickListener(btnBack_Click());
        btnAddMovie.setOnClickListener(btnAddMovie_Click());
        btnInviteFriends.setOnClickListener(btnInviteFriends_Click());
        btnCreateEvent.setOnClickListener(btnCreateEvent_Click());

        tbEventName.setText(com_eventName);
        tbEventLocation.setText(com_eventLocation);
        tbEventTime.setText(com_eventTime);
        tbEventDescription.setText(com_eventDescription);

        //for the date picker
        showDialogOnTextFieldClick();
        myCalendar = Calendar.getInstance();
        year_o = myCalendar.get(Calendar.YEAR);
        month_o = myCalendar.get(Calendar.MONTH);
        day_o = myCalendar.get(Calendar.DAY_OF_MONTH);
    }

    //region Button Clicks

    protected View.OnClickListener btnAddMovie_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com_eventName = tbEventName.getText().toString();
                com_eventLocation = tbEventLocation.getText().toString();
                com_eventTime = tbEventTime.getText().toString();
                com_eventDescription = tbEventDescription.getText().toString();

                toActivity_AddMovies();
            }
        };
    } // btnAddMovie_Click

    protected View.OnClickListener btnInviteFriends_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com_eventName = tbEventName.getText().toString();
                com_eventLocation = tbEventLocation.getText().toString();
                com_eventTime = tbEventTime.getText().toString();
                com_eventDescription = tbEventDescription.getText().toString();

                toActivity_InviteFriends();
            }
        };
    } // btnInviteFriends_Click

    protected View.OnClickListener btnCreateEvent_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String movieQueueID = "";
                String friendQueueID = "";

                // check conditions
                if(tbEventName.getText().length() == 0) {
                    displayToast("You must enter an event name");
                    return;
                }
                if(tbEventLocation.getText().length() == 0) {
                    displayToast("You must enter a location");
                    return;
                }
                if(tbEventTime.getText().length() == 0) {
                    displayToast("You must enter a time");
                    return;
                }
                //if(com_invitedFriends.size() == 0) {
                //   displayToast("You must invite friends");
                //    return;
                //}
                if(com_movieList.size() == 0) {
                    displayToast("You must add movies");
                    return;
                }

                // save event stuff in database
                ParseObject movieInfo;
                ParseObject movieQueue;
                ParseObject invitedFriends;
                ParseObject event;
                List<ParseObject> poMovieList = new ArrayList<ParseObject>();

                movieQueue = new ParseObject("UserMovieQueue");
                movieQueue.put("userId", com_userID);
                movieQueue.put("name", tbEventName.getText().toString());

                try {
                    movieQueue.save();
                }catch (ParseException e) {
                    displayToast("Unable to process request");
                    Log.e(TAG, "Error saving to UserMovieQueue : " + e.getMessage());
                    return;
                }

                // insert all the movies from the movieList into the database
                movieQueueID = movieQueue.getObjectId();
                for(int i=0; i<com_movieList.size(); i++) {
                    movieInfo = new ParseObject("UserMovieInfo");
                    movieInfo.put("userMovieQueueID", movieQueueID);
                    movieInfo.put("title", com_movieList.get(i).getName());
                    poMovieList.add(movieInfo);
                }

                // workaround for some odd parse bug.
                // wont capture last movieInfo unless u make another dummy one after.
                movieInfo = new ParseObject("UserMovieInfo");

                ParseObject p = new ParseObject("UserMovieInfo");
                p.saveAllInBackground(poMovieList);

                // need some way of adding friends too

                event = new ParseObject("Event");
                event.put("EventName", tbEventName.getText().toString());
                event.put("EventLocation", tbEventLocation.getText().toString());
                event.put("EventStartTime", tbEventTime.getText().toString());
                event.put("EventDiscription", tbEventDescription.getText().toString());
                event.put("UserID", com_userID);
                event.put("MovieQueueID", movieQueueID);

                try {
                    event.save();
                } catch (ParseException e) {
                    displayToast("Unable to process request");
                    Log.e(TAG, "Error saving to Event : " + e.getMessage());
                    return;
                }

                List<ParseObject> poInvitedFriends = new ArrayList<ParseObject>();
                com_eventID = event.getObjectId();
                if(com_invitedFriends.size() > 0){
                    for(int i = 0; i < com_invitedFriends.size(); i++){

                        invitedFriends = new ParseObject("InvitedFriends");
                        invitedFriends.put("UserID", com_invitedFriends.get(i).getUserID());
                        invitedFriends.put("EventID", com_eventID);
                        invitedFriends.put("Confirmation", 0);
                        invitedFriends.put("HasVoted",false);
                        poInvitedFriends.add(invitedFriends);

                    }//end for

                    p = new ParseObject("InvitedFriends");
                    p.saveAllInBackground(poInvitedFriends, new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) {
                                Log.e(TAG, e.getMessage());
                            }
                        }
                    });

                }//end if

                toActivity_HubActivity();
            }
        };
    } // btnCreateEvent_Click

    protected View.OnClickListener btnBack_Click() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // erase all the data if backing up to main screen
                com_eventName = "";
                com_eventLocation = "";
                com_eventTime = "";
                com_eventDescription = "";
                com_movieList.clear();
                com_invitedFriends.clear();

                toActivity_HubActivity();
            }
        };
    } // btnBack_Click

    //for the date picker
    public void showDialogOnTextFieldClick() {

        tbEventTime = (EditText) findViewById(R.id.createEvent_tbTime);

        tbEventTime.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialog(DIALOG_ID);
                    }
                }
        );
    }

    @Override
    protected Dialog onCreateDialog(int in_id) {

        if (in_id == DIALOG_ID)
            return new DatePickerDialog(this, datePickerListener, year_o, month_o, day_o);
        else return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            year_o = year;
            month_o = monthOfYear + 1;
            day_o = dayOfMonth;

            Toast.makeText(CreateEvent.this, month_o + "/" + day_o + "/" + year_o, Toast.LENGTH_LONG).show();
            tbEventTime = (EditText) findViewById(R.id.createEvent_tbTime);
            tbEventTime.setText(month_o + "/" + day_o + "/" + year_o);
        }
    };

    //endregion

}
