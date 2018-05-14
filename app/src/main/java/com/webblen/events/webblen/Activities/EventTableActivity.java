package com.webblen.events.webblen.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.webblen.events.webblen.Adapters.WebblenEventAdapter;
import com.webblen.events.webblen.R;
import com.webblen.events.webblen.Objects.WebblenEvent;
import com.webblen.events.webblen.Utilities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class EventTableActivity extends AppCompatActivity {

    //Firebase
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    //--
    private String user_id;
    private ArrayList<String> userData;
    private List<String> userInterests = new ArrayList<>();
    private List<WebblenEvent> currentEventList = new ArrayList<>();
    private List<WebblenEvent> todayEventList = new ArrayList<>();
    private List<WebblenEvent> tomorrowEventList = new ArrayList<>();
    private List<WebblenEvent> thisWeekEventList = new ArrayList<>();
    private List<WebblenEvent> thisMonthEventList = new ArrayList<>();
    private List<WebblenEvent> laterEventList = new ArrayList<>();
    private DateFormat sourceFormat = new SimpleDateFormat("dd/MM/yyyy");
    private Date eventDate;
    private Date currentDate = new Date();


    //Recycler
    RecyclerView eventRecyclerView;
    WebblenEventAdapter eventAdapter;


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
        eventRecyclerView = (RecyclerView) findViewById(R.id.eventRecyclerView);
        eventRecyclerView.setHasFixedSize(true);
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        WebblenEvent testEvent = new WebblenEvent();
        currentEventList.add(testEvent);

        eventAdapter = new WebblenEventAdapter(this, currentEventList);
        eventRecyclerView.setAdapter(eventAdapter);

        loadFirestoreData();

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

                currentEventList = todayEventList;
                eventAdapter.webblenEventList = currentEventList;
                eventAdapter.notifyDataSetChanged();

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

                currentEventList = tomorrowEventList;
                eventAdapter.webblenEventList = currentEventList;
                eventAdapter.notifyDataSetChanged();
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

                currentEventList = thisWeekEventList;
                eventAdapter.webblenEventList = currentEventList;
                eventAdapter.notifyDataSetChanged();
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

                currentEventList = thisMonthEventList;
                eventAdapter.webblenEventList = currentEventList;
                eventAdapter.notifyDataSetChanged();
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

                currentEventList = laterEventList;
                eventAdapter.webblenEventList = currentEventList;
                eventAdapter.notifyDataSetChanged();
            }
        });



    }


    private int getDifferenceDays(Date d1, Date d2) {
        int daysdiff = 0;
        long diff = d2.getTime() - d1.getTime();
        long diffDays = diff / (24 * 60 * 60 * 1000) + 1;
        daysdiff = (int) diffDays;
        return daysdiff;
    }

    //Load User Data
    private void loadFirestoreData() {

        Log.d("Firestore: ", "Loading Events");
        firebaseFirestore.collection("users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){

                    if(task.getResult().exists()){

                        String username = task.getResult().getString("username");
                        String profile_pic = task.getResult().getString("profile_pic");

                        //If username or pic is null...
                        if (username == null || profile_pic == null){
                            Intent setupIntent = new Intent(EventTableActivity.this, SetupActivity.class);
                            startActivity(setupIntent);
                            finish();
                        }

                        userData = (ArrayList<String>) task.getResult().getData().get("interests");
                        Log.d("USER DATA: ", userData.toString());

                        Log.d("USER DATA: ",userInterests.toString());
                        firebaseFirestore.collection("events").whereEqualTo("paid", true).addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                                    if (doc.getType() == DocumentChange.Type.ADDED) {
                                        WebblenEvent webblenEvent = doc.getDocument().toObject(WebblenEvent.class);
                                        for (String interest : userData) {
                                            ArrayList<String> eventCategories = webblenEvent.getCategories();
                                            if (eventCategories.contains(interest)){
                                                Log.d("ADDING EVENT", "performOrganzieByDate!");
                                                organizeByDate(webblenEvent);
                                            }
                                        }

                                    }
                                }
                            }
                        });
                        Log.d("All Events: ",todayEventList.toString() + tomorrowEventList.toString() + thisWeekEventList.toString() + thisMonthEventList.toString() + laterEventList.toString());
                    }

                } else {
                    String error = task.getException().getMessage();
                    if (error != null) {
                        Toast.makeText(EventTableActivity.this, "Load Error: " + error, Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
    }

    //Organize Event Days & Distances
    private void organizeByDate(WebblenEvent event){
        String date = event.getDate();
        try {
            eventDate = sourceFormat.parse(date);
            if (eventDate.compareTo(currentDate) < 0) {
                firebaseFirestore.collection("events").document(event.getEventKey()).delete();
            } else if (eventDate.compareTo(currentDate) == 0) {
                if (!todayEventList.contains(event)) {
                    todayEventList.add(event);
                }
            } else if (Utilities.getDifferenceDays(currentDate, eventDate) == 1) {
                if (!tomorrowEventList.contains(event)) {
                    tomorrowEventList.add(event);
                }
            } else if (Utilities.getDifferenceDays(currentDate, eventDate) > 1 && Utilities.getDifferenceDays(currentDate, eventDate) <= 7) {
                if (!thisWeekEventList.contains(event)) {
                    thisWeekEventList.add(event);
                }
            } else if (Utilities.getDifferenceDays(currentDate, eventDate) > 7 && Utilities.getDifferenceDays(currentDate, eventDate) <= 30) {
                if (!thisMonthEventList.contains(event)) {
                    thisMonthEventList.add(event);
                }
            } else {
                if (!laterEventList.contains(event)) {
                    laterEventList.add(event);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
