package com.webblen.events.webblen;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class EventTableActivity extends AppCompatActivity {

    //Firebase
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String user_id;

    //Tabs
    private TextView todayTableBtn;
    private boolean eventsToday = true;

    private TextView tomorrowTableBtn;
    private boolean eventsTomorrow = false;

    private TextView thisWeekTableBtn;
    private boolean eventsThisWeek = false;

    private TextView thisMonthTableBtn;
    private boolean eventsThisMonth = false;

    private TextView laterTableBtn;
    private boolean eventsLater = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_table);

        //** INITIALIZE

        //Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        //UI
        todayTableBtn = (TextView) findViewById(R.id.todayTableBtn);
        tomorrowTableBtn = (TextView) findViewById(R.id.tomorrowTableBtn);
        thisWeekTableBtn = (TextView) findViewById(R.id.thisWeekTableBtn);
        thisMonthTableBtn = (TextView) findViewById(R.id.thisMonthTableBtn);
        laterTableBtn = (TextView) findViewById(R.id.laterTableBtn);


        //Tab Btn Listeners
        todayTableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventsToday = true;
                eventsTomorrow = false;
                eventsThisWeek = false;
                eventsThisMonth = false;
                eventsLater = false;

                todayTableBtn.setBackgroundResource(R.drawable.border_orange);
                todayTableBtn.setTextColor(getResources().getColor(R.color.colorPrimary));
                tomorrowTableBtn.setBackgroundResource(R.drawable.border_light_gray);
                tomorrowTableBtn.setTextColor(getResources().getColor(R.color.colorLightGray));
                thisWeekTableBtn.setBackgroundResource(R.drawable.border_light_gray);
                thisWeekTableBtn.setTextColor(getResources().getColor(R.color.colorLightGray));
                thisMonthTableBtn.setBackgroundResource(R.drawable.border_light_gray);
                thisMonthTableBtn.setTextColor(getResources().getColor(R.color.colorLightGray));
                laterTableBtn.setBackgroundResource(R.drawable.border_light_gray);
                laterTableBtn.setTextColor(getResources().getColor(R.color.colorLightGray));
            }
        });

        tomorrowTableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventsToday = false;
                eventsTomorrow = true;
                eventsThisWeek = false;
                eventsThisMonth = false;
                eventsLater = false;

                todayTableBtn.setBackgroundResource(R.drawable.border_light_gray);
                todayTableBtn.setTextColor(getResources().getColor(R.color.colorLightGray));
                tomorrowTableBtn.setBackgroundResource(R.drawable.border_orange);
                tomorrowTableBtn.setTextColor(getResources().getColor(R.color.colorPrimary));
                thisWeekTableBtn.setBackgroundResource(R.drawable.border_light_gray);
                thisWeekTableBtn.setTextColor(getResources().getColor(R.color.colorLightGray));
                thisMonthTableBtn.setBackgroundResource(R.drawable.border_light_gray);
                thisMonthTableBtn.setTextColor(getResources().getColor(R.color.colorLightGray));
                laterTableBtn.setBackgroundResource(R.drawable.border_light_gray);
                laterTableBtn.setTextColor(getResources().getColor(R.color.colorLightGray));
            }
        });

        thisWeekTableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventsToday = false;
                eventsTomorrow = false;
                eventsThisWeek = true;
                eventsThisMonth = false;
                eventsLater = false;

                todayTableBtn.setBackgroundResource(R.drawable.border_light_gray);
                todayTableBtn.setTextColor(getResources().getColor(R.color.colorLightGray));
                tomorrowTableBtn.setBackgroundResource(R.drawable.border_light_gray);
                tomorrowTableBtn.setTextColor(getResources().getColor(R.color.colorLightGray));
                thisWeekTableBtn.setBackgroundResource(R.drawable.border_orange);
                thisWeekTableBtn.setTextColor(getResources().getColor(R.color.colorPrimary));
                thisMonthTableBtn.setBackgroundResource(R.drawable.border_light_gray);
                thisMonthTableBtn.setTextColor(getResources().getColor(R.color.colorLightGray));
                laterTableBtn.setBackgroundResource(R.drawable.border_light_gray);
                laterTableBtn.setTextColor(getResources().getColor(R.color.colorLightGray));
            }
        });

        thisMonthTableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventsToday = false;
                eventsTomorrow = false;
                eventsThisWeek = false;
                eventsThisMonth = true;
                eventsLater = false;

                todayTableBtn.setBackgroundResource(R.drawable.border_light_gray);
                todayTableBtn.setTextColor(getResources().getColor(R.color.colorLightGray));
                tomorrowTableBtn.setBackgroundResource(R.drawable.border_light_gray);
                tomorrowTableBtn.setTextColor(getResources().getColor(R.color.colorLightGray));
                thisWeekTableBtn.setBackgroundResource(R.drawable.border_light_gray);
                thisWeekTableBtn.setTextColor(getResources().getColor(R.color.colorLightGray));
                thisMonthTableBtn.setBackgroundResource(R.drawable.border_orange);
                thisMonthTableBtn.setTextColor(getResources().getColor(R.color.colorPrimary));
                laterTableBtn.setBackgroundResource(R.drawable.border_light_gray);
                laterTableBtn.setTextColor(getResources().getColor(R.color.colorLightGray));
            }
        });

        laterTableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventsToday = false;
                eventsTomorrow = false;
                eventsThisWeek = false;
                eventsThisMonth = false;
                eventsLater = true;

                todayTableBtn.setBackgroundResource(R.drawable.border_light_gray);
                todayTableBtn.setTextColor(getResources().getColor(R.color.colorLightGray));
                tomorrowTableBtn.setBackgroundResource(R.drawable.border_light_gray);
                tomorrowTableBtn.setTextColor(getResources().getColor(R.color.colorLightGray));
                thisWeekTableBtn.setBackgroundResource(R.drawable.border_light_gray);
                thisWeekTableBtn.setTextColor(getResources().getColor(R.color.colorLightGray));
                thisMonthTableBtn.setBackgroundResource(R.drawable.border_light_gray);
                thisMonthTableBtn.setTextColor(getResources().getColor(R.color.colorLightGray));
                laterTableBtn.setBackgroundResource(R.drawable.border_orange);
                laterTableBtn.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        });
    }
}
