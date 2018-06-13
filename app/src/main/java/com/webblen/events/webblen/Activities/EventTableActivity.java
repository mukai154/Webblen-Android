package com.webblen.events.webblen.Activities;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.webblen.events.webblen.Classes.WebblenEvent;
import com.webblen.events.webblen.Utilities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private DateFormat sourceFormat = new SimpleDateFormat("MM/dd/yyyy");
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

    //UI
    private ImageView noResultsImgView;
    private TextView noResultsTextView;

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
        noResultsImgView = (ImageView) findViewById(R.id.noResultsImgView);
        noResultsTextView = (TextView) findViewById(R.id.noResultsTextView);
        eventRecyclerView = (RecyclerView) findViewById(R.id.eventRecyclerView);
        eventRecyclerView.setHasFixedSize(true);
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventAdapter = new WebblenEventAdapter(this, currentEventList);
        eventRecyclerView.setAdapter(eventAdapter);

        loadFirestoreData();

        //Tab Btn Listeners
        todayTableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noResultsTextView.setText("No Events Found For Today");
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
                updateRecyclerViewData();
                setRecyclerViewVisibility();
            }
        });

        tomorrowTableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noResultsTextView.setText("No Events Found For Tomorrow");
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
                updateRecyclerViewData();
                setRecyclerViewVisibility();
            }
        });

        thisWeekTableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noResultsTextView.setText("No Events Found For This Week");
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
                Log.d("THIS WEEK:", thisWeekEventList.toString());
                updateRecyclerViewData();
                setRecyclerViewVisibility();
            }
        });

        thisMonthTableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noResultsTextView.setText("No Events Found This Month");
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
                updateRecyclerViewData();
                setRecyclerViewVisibility();
            }
        });

        laterTableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noResultsTextView.setText("No Events Found Later");
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
                updateRecyclerViewData();
                setRecyclerViewVisibility();
            }
        });
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
                                        String auth_pic = doc.getDocument().getString("author_pic");
                                        WebblenEvent webblenEvent = doc.getDocument().toObject(WebblenEvent.class);
                                        for (String interest : userData) {
                                            ArrayList<String> eventCategories = webblenEvent.getCategories();
                                            if (eventCategories.contains(interest)){
                                                webblenEvent.setAuthor_Pic(auth_pic);
                                                //Log.d("ADDING EVENT", "performOrganzieByDate!");
                                                organizeByDate(webblenEvent);
                                            }
                                        }

                                    }

                                }

                                if (eventsToday){
                                    currentEventList = todayEventList;
                                } else if (eventsTomorrow){
                                    currentEventList = tomorrowEventList;
                                } else if (eventsThisWeek){
                                    currentEventList = thisWeekEventList;
                                } else if (eventsThisMonth){
                                    currentEventList = thisMonthEventList;
                                } else if (eventsLater){
                                    currentEventList = laterEventList;
                                }
                                updateRecyclerViewData();
                                setRecyclerViewVisibility();
                            }
                        });
                       // Log.d("All Events: ",todayEventList.toString() + tomorrowEventList.toString() + thisWeekEventList.toString() + thisMonthEventList.toString() + laterEventList.toString());
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

    //Recycler View Thingies
    private void updateRecyclerViewData(){
        eventAdapter.webblenEventList = currentEventList;
        eventAdapter.notifyDataSetChanged();
    }
    //Set RecylcerView Visibility
    private void setRecyclerViewVisibility(){
        if (currentEventList.size() > 0) {
            eventRecyclerView.setVisibility(View.VISIBLE);
        } else {
            eventRecyclerView.setVisibility(View.INVISIBLE);
        }
    }
}
