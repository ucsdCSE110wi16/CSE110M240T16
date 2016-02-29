package com.parse.starter;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Calendar;

public class CreateEvent extends PolarityActivity {

    public static final String TAG = CreateEvent.class.getSimpleName();

    Button btnBack, btnAddMovie, btnInviteFriends, btnCreateEvent, btnHome;
    EditText tbEventName, tbEventLocation, tbEventDate, tbEventDescription, tbEventTime;

    Calendar myCalendar;
    int year_o, month_o, day_o;
    static final int DIALOG_Date = 0; //hold the Date dialog
    int hour_o, minute_o;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        btnBack = (Button) findViewById(R.id.createEvent_btnBack);
        btnAddMovie = (Button) findViewById(R.id.createEvent_btnAddMovies);
        btnInviteFriends = (Button) findViewById(R.id.createEvent_btnAddFriends);
        btnCreateEvent = (Button) findViewById(R.id.createEvent_btnCreateEvent);

        tbEventName = (EditText) findViewById(R.id.createEvent_tbName);
        tbEventLocation = (EditText) findViewById(R.id.createEvent_tbLocation);
        tbEventDate = (EditText) findViewById(R.id.createEvent_tbDate);
        tbEventDescription = (EditText) findViewById(R.id.createEvent_tbDescription);
        tbEventTime = (EditText) findViewById(R.id.createEvent_tbTime);

        btnBack.setOnClickListener(btnBack_Click());
        btnAddMovie.setOnClickListener(btnAddMovie_Click());
        btnInviteFriends.setOnClickListener(btnInviteFriends_Click());
        btnCreateEvent.setOnClickListener(btnCreateEvent_Click());

        tbEventName.setText(com_eventName);
        tbEventLocation.setText(com_eventLocation);
        tbEventDate.setText(com_eventDate);
        tbEventTime.setText(com_eventTime);
        tbEventDescription.setText(com_eventDescription);

        //for the date picker
        showDialogOnTextFieldClick();
        showDialogHourClick();
        myCalendar = Calendar.getInstance();
        year_o = myCalendar.get(Calendar.YEAR);
        month_o = myCalendar.get(Calendar.MONTH);
        day_o = myCalendar.get(Calendar.DAY_OF_MONTH);
        //disables the keyboard from showing up for date & time picker
        tbEventDate.setInputType(InputType.TYPE_NULL);
        tbEventTime.setInputType(InputType.TYPE_NULL);
        showDialogHourClick();
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
                com_eventDate = tbEventDate.getText().toString();

                goToActivity(TAG, AddMovies.class.getSimpleName());
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
                com_eventDate = tbEventDate.getText().toString();

                goToActivity(TAG, ViewUsers.class.getSimpleName());
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
                if(tbEventDate.getText().length() == 0) {
                    displayToast("You must enter a time");
                    return;
                }
                if(com_invitedFriends.size() == 0) {
                    displayToast("You must invite friends");
                    return;
                }
                if(com_modelList.size() == 0) {
                    displayToast("You must add movies");
                    return;
                }

                // save event stuff in database
                Date date;
                ParseObject movieInfo;
                ParseObject movieQueue;
                ParseObject invitedFriends;
                ParseObject event;
                ParseACL acl = new ParseACL();
                acl.setPublicReadAccess(true);
                acl.setPublicWriteAccess(true);
                List<ParseObject> poMovieList = new ArrayList<ParseObject>();

                try {
                    date = new SimpleDateFormat("MM/dd/yyy").parse(tbEventDate.getText().toString());
                    if(date == null) Log.e(TAG, "Unable to aquire date format");
                }
                catch (java.text.ParseException e) {
                    Log.e(TAG, e.getMessage());
                    displayToast("Invalid date format");
                    return;
                }

                movieQueue = new ParseObject("UserMovieQueue");
                movieQueue.put("userId", com_userID);
                movieQueue.put("name", tbEventName.getText().toString());
                movieQueue.setACL(acl);

                try {
                    movieQueue.save();
                }catch (ParseException e) {
                    displayToast("Unable to process request");
                    Log.e(TAG, "Error saving to UserMovieQueue : " + e.getMessage());
                    return;
                }

                // insert all the movies from the movieList into the database
                movieQueueID = movieQueue.getObjectId();
                for(int i=0; i<com_modelList.size(); i++) {
                    movieInfo = new ParseObject("UserMovieInfo");
                    movieInfo.put("userMovieQueueID", movieQueueID);
                    movieInfo.put("title", com_modelList.get(i).getName());
                    movieInfo.setACL(acl);
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
                event.put("EventDate", date);
                event.setACL(acl);

                try {
                    event.save();
                } catch (ParseException e) {
                    displayToast("Unable to process request");
                    Log.e(TAG, "Error saving to Event : " + e.getMessage());
                    return;
                }

                List<ParseObject> poInvitedFriends = new ArrayList<ParseObject>();
                com_eventID = event.getObjectId();

                // add the host
                invitedFriends = new ParseObject("InvitedFriends");
                invitedFriends.put("UserID", com_userID);
                invitedFriends.put("EventID", com_eventID);
                invitedFriends.put("Confirmation", 1);
                invitedFriends.setACL(acl);
                poInvitedFriends.add(invitedFriends);

                // add all the friends
                for(int i = 0; i < com_invitedFriends.size(); i++){
                    invitedFriends = new ParseObject("InvitedFriends");
                    invitedFriends.put("UserID", com_invitedFriends.get(i).getUserID());
                    invitedFriends.put("EventID", com_eventID);
                    invitedFriends.put("Confirmation", 0);
                    invitedFriends.setACL(acl);
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

                EventModel m = new EventModel(com_userID, tbEventName.getText().toString(),
                        tbEventDescription.getText().toString(), tbEventLocation.getText().toString(),
                        event.getObjectId(), movieQueueID, date, tbEventTime.getText().toString(),
                        com_invitedFriends.size(), 0, 0);

                // host is automatically set to attending.
                m.status = EventModel.Status.Accepted;

                com_eventModelList.add(m);
                Collections.sort(com_eventModelList, new EventModelComparator());
                goToActivity(TAG, HubActivity.class.getSimpleName());
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

                returnToPrevActivity();
            }
        };
    } // btnBack_Click

    //endregion

    //region DateTime

    //for the date picker
    public void showDialogOnTextFieldClick() {

        tbEventDate = (EditText) findViewById(R.id.createEvent_tbDate);

        tbEventDate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialog(DIALOG_Date);
                    }
                }
        );
    }

    @Override
    protected Dialog onCreateDialog(int in_id) {

        if (in_id == DIALOG_Date) {
            DatePickerDialog dpd = new DatePickerDialog(this, datePickerListener, year_o, month_o, day_o);
            dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            return dpd;
        }//end if
        else return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            year_o = year;
            month_o = monthOfYear + 1;
            day_o = dayOfMonth;

            tbEventDate = (EditText) findViewById(R.id.createEvent_tbDate);
            tbEventDate.setText(month_o + "/" + day_o + "/" + year_o);
        }
    };

    //for the date picker
    public void showDialogHourClick() {

        tbEventTime = (EditText) findViewById(R.id.createEvent_tbTime);

        tbEventTime.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new TimePickerDialog(CreateEvent.this, timePickerListener, hour_o, minute_o, true).show();
                    }
                }
        );
    }

    TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hour, int minute) {

            hour_o = hour;
            minute_o = minute;
            String formattedTime = hour_o + ":" + minute_o;

            tbEventTime = (EditText) findViewById(R.id.createEvent_tbTime);
            //Int truncates minutes less than 10 to a single digit, which looks wrong, so this
            //fixes it
            if(minute_o < 10){
                formattedTime = hour_o + ":" + "0" + minute_o;
                tbEventTime.setText(formattedTime);
            }//end if
            else{
                tbEventTime.setText(formattedTime);
            }//end else
        }
    };
    //endregion

}
