package com.webblen.events.webblen;

import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.twitter.sdk.android.core.models.Tweet;
import com.webblen.events.webblen.Adapter.ListItemAdapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class EventTableActivity extends AppCompatActivity {

    //Firebase
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    //--
    private String user_id;
    private Map<String, Object> userData;
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
    RecyclerView.LayoutManager layoutManager;
    ListItemAdapter adapter;


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
        layoutManager = new LinearLayoutManager(this);
        eventRecyclerView.setLayoutManager(layoutManager);
        adapter = new ListItemAdapter(currentEventList);
        eventRecyclerView.setAdapter(adapter);


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
                adapter.notifyDataSetChanged();

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
                adapter.notifyDataSetChanged();
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
                adapter.notifyDataSetChanged();
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
                adapter.notifyDataSetChanged();
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
                adapter.notifyDataSetChanged();
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

    private void loadFirestoreData() {
        //Get User Interests && Events
        firebaseFirestore.collection("users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
              if (task.isSuccessful()){
                  if (task.getResult().exists()){
                      userData = (Map<String, Object>) task.getResult().getData().get("interests");
                      //loop a Map
                      for (Map.Entry<String, Object> entry : userData.entrySet()) {
                          boolean hasInterest = (Boolean) entry.getValue();
                          if (hasInterest) {
                              userInterests.add(entry.getKey().toLowerCase());
                          }
                      }
                          firebaseFirestore.collection("events").whereEqualTo("paid", true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                              @Override
                              public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                  if (task.isSuccessful()) {
                                      for (DocumentSnapshot document : task.getResult()) {
                                          //Check if event contains interest
                                          boolean containsInterest = false;
                                          //Log.d("FIRESTORE EVENT: ", document.getId() + " => " + document.getData());
                                          ArrayList<String> categories = (ArrayList<String>) document.getData().get("categories");
                                          for (String tag : categories){
                                              String eTag = tag.toLowerCase();
                                              if (userInterests.contains(eTag)){
                                                  containsInterest = true;
                                              }
                                          }

                                          if (containsInterest) {

                                              String address = (String) document.getData().get("address");
                                              String author = (String) document.getData().get("date");
                                              String[] catArray = new String[categories.size()];
                                              catArray = categories.toArray(catArray);
                                              String date = (String) document.getData().get("date");
                                              String description = (String) document.getData().get("description");
                                              int distanceFromUser = 0;
                                              boolean event18 = (Boolean) document.getData().get("event18");
                                              boolean event21 = (Boolean) document.getData().get("event21");
                                              String key = (String) document.getData().get("eventKey");
                                              Double lat = (Double) document.getData().get("lat");
                                              Double lon = (Double) document.getData().get("lon");
                                              boolean notificationOnly = (Boolean) document.getData().get("notificationOnly");
                                              boolean paid = (Boolean) document.getData().get("paid");
                                              String pathToImage = (String) document.getData().get("pathToImage");
                                              Double radius = (Double) document.getData().get("radius");
                                              int radiusInt = radius.intValue();
                                              String time = (String) document.getData().get("time");
                                              String title = (String) document.getData().get("title");
                                              boolean verified = (Boolean) document.getData().get("verified");
                                              Long views = (Long) document.getData().get("views");
                                              int viewsInt = views.intValue();

                                              WebblenEvent event = new WebblenEvent(address, author, categories,
                                                      date, description, distanceFromUser,
                                                      event18, event21, key,
                                                      lat, lon, notificationOnly,
                                                      pathToImage, radiusInt, time,
                                                      title, viewsInt);


                                              try {
                                                  eventDate = sourceFormat.parse(date);
                                                  if (eventDate.compareTo(currentDate) < 0) {
                                                      firebaseFirestore.collection("events").document(key).delete();
                                                      Log.d("FIRESTORE EVENT: ", "DELETED");
                                                  } else if (eventDate.compareTo(currentDate) == 0) {
                                                      if (!todayEventList.contains(event)) {
                                                          todayEventList.add(event);
                                                          Log.d("TODAY EVENTS: ", todayEventList.toString());
                                                      }
                                                  } else if (getDifferenceDays(currentDate, eventDate) == 1) {
                                                      if (!tomorrowEventList.contains(event)) {
                                                          tomorrowEventList.add(event);
                                                          Log.d("TOMORROW: ", tomorrowEventList.toString());
                                                      }
                                                  } else if (getDifferenceDays(currentDate, eventDate) > 1 && getDifferenceDays(currentDate, eventDate) <= 7) {
                                                      if (!thisWeekEventList.contains(event)) {
                                                          thisWeekEventList.add(event);
                                                          Log.d("THIS WEEK: ", thisWeekEventList.toString());
                                                      }
                                                  } else if (getDifferenceDays(currentDate, eventDate) > 7 && getDifferenceDays(currentDate, eventDate) <= 30) {
                                                      if (!thisMonthEventList.contains(event)) {
                                                          thisMonthEventList.add(event);
                                                          Log.d("THIS MONTH: ", thisMonthEventList.toString());
                                                      }
                                                  } else {
                                                      if (!laterEventList.contains(event)) {
                                                          laterEventList.add(event);
                                                          Log.d("LATER: ", laterEventList.toString());
                                                      }
                                                  }
                                              } catch (ParseException e) {
                                                  e.printStackTrace();
                                              }
                                          }
                                      }

                                      // TO DO: FIND AUTH IMG
                                      currentEventList = todayEventList;
                                      adapter.notifyDataSetChanged();

                                  } else {
                                      Toast.makeText(EventTableActivity.this, "Load Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                  }
                              }
                          });
                  }
              } else {
                  Toast.makeText(EventTableActivity.this, "Load Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
              }
            }
        });
    }
}
