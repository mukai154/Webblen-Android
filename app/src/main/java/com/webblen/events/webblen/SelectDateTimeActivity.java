package com.webblen.events.webblen;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SelectDateTimeActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private String selectedDate;
    private TextView dateText;
    private TimePicker timePicker;
    private String eventTime;
    private String formattedTime;
    private TextView timeText;
    private Button setStartTimeBtn;
    private Button setEndTimeBtn;
    private boolean doneEditingTime = true;

    private Button setDateTimeBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_date_time);


        //*** Initialize
        //UI
        calendarView = (CalendarView) findViewById(R.id.calendarView);
        calendarView.setMinDate(System.currentTimeMillis() - 1000);
        dateText = (TextView) findViewById(R.id.dateText);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        timePicker.setEnabled(false);
        timeText = (TextView) findViewById(R.id.timeText);
        setStartTimeBtn = (Button) findViewById(R.id.setStartBtn);
        setEndTimeBtn = (Button) findViewById(R.id.setEndBtn);
        setEndTimeBtn.setEnabled(false);
        setDateTimeBtn = (Button) findViewById(R.id.submitDateTimeBtn);


        //Listeners
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String formattedDay = null;
                String formattedMonth = null;
                if (dayOfMonth < 10) {
                    formattedDay = "0" + dayOfMonth;
                }
                if (month < 10){
                    formattedMonth = "0" + month;
                }

                if (formattedDay != null && formattedMonth == null){
                    selectedDate = month + "/" + formattedDay + "/" + year;
                } else if (formattedDay == null && formattedMonth != null){
                    selectedDate = formattedMonth + "/" + dayOfMonth + "/" + year;
                } else if (formattedDay != null && formattedMonth != null){
                    selectedDate = formattedMonth + "/" + formattedDay + "/" + year;
                } else {
                    selectedDate = month + "/" + dayOfMonth + "/" + year;
                }

                dateText.setText(selectedDate);
            }
        });

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

            }
        });

        setStartTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (doneEditingTime){
                    doneEditingTime = false;
                    setStartTimeBtn.setText("Set Start Time");
                    timePicker.setEnabled(true);
                    setEndTimeBtn.setEnabled(false);
                } else {
                    doneEditingTime = true;
                    int hour = timePicker.getHour();
                    int min = timePicker.getMinute();
                    formattedTime = formatTime(hour, min);
                    if (eventTime != null && eventTime.contains("-")){
                        eventTime = eventTime.substring(eventTime.lastIndexOf('-')+1);
                        eventTime = formattedTime + "-" + eventTime;
                    } else {
                        eventTime = formattedTime;
                    }
                    timeText.setText(eventTime);
                    timePicker.setEnabled(false);
                    setEndTimeBtn.setEnabled(true);
                    setStartTimeBtn.setText("Change Start Time");
                }

            }
        });

        setEndTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (doneEditingTime){
                    doneEditingTime = false;
                    setEndTimeBtn.setText("Set End Time");
                    timePicker.setEnabled(true);
                    setStartTimeBtn.setEnabled(false);
                } else {
                    doneEditingTime = true;
                    int hour = timePicker.getHour();
                    int min = timePicker.getMinute();
                    formattedTime = formatTime(hour, min);
                    if (eventTime.contains("-")){
                        eventTime = eventTime.split("-")[0];
                    }
                    eventTime = eventTime + "-" + formattedTime;
                    timeText.setText(eventTime);
                    timePicker.setEnabled(false);
                    setStartTimeBtn.setEnabled(true);
                    setEndTimeBtn.setText("Change End Time");
                }
            }

        });

        setDateTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = selectedDate;
                String time = eventTime;
                if (date == null){
                    Toast.makeText(SelectDateTimeActivity.this, "Please Set an Event Date", Toast.LENGTH_LONG).show();
                } else if (time == null){
                    Toast.makeText(SelectDateTimeActivity.this, "Please Set the Time", Toast.LENGTH_LONG).show();
                } else {
                    Intent returnTimeDataIntent = new Intent();
                    returnTimeDataIntent.putExtra("date", date);
                    returnTimeDataIntent.putExtra("time", time);
                    setResult(RESULT_OK, returnTimeDataIntent);
                    finish();
                }
            }
        });
    }

    private String formatTime(int hr,int min) {
        String am_pm = "";

        Calendar datetime = Calendar.getInstance();
        datetime.set(Calendar.HOUR_OF_DAY, hr);
        datetime.set(Calendar.MINUTE, min);

        if (datetime.get(Calendar.AM_PM) == Calendar.AM)
            am_pm = "AM";
        else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
            am_pm = "PM";

        String strHrsToShow = (datetime.get(Calendar.HOUR) == 0) ?"12":datetime.get(Calendar.HOUR)+"";
        String minToShow = (datetime.get(Calendar.MINUTE) == 0) ?"00":datetime.get(Calendar.MINUTE)+"";

        String formattedTime = strHrsToShow+":"+minToShow+ am_pm;
        return formattedTime;
    }
}
