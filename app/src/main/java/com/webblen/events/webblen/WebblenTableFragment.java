package com.webblen.events.webblen;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class WebblenTableFragment extends android.support.v4.app.Fragment {

    //Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String user_id;
    private Map<String, Object> userData;
    private List<String> userInterests = new ArrayList<>();

    //Recycler
    private RecyclerView event_list_view;
    private List<WebblenEvent> webblenEventList;
    private WebblenEventRecyclerAdapter webblenEventRecyclerAdapter;
    //--
    private boolean eventsToday = true;
    private boolean eventsTomorrow = false;
    private boolean eventsThisWeek = false;
    private boolean eventsThisMonth = false;
    private boolean eventsLater = false;
    private List<WebblenEvent> todayEventList = new ArrayList<>();
    private List<WebblenEvent> tomorrowEventList = new ArrayList<>();
    private List<WebblenEvent> thisWeekEventList = new ArrayList<>();
    private List<WebblenEvent> thisMonthEventList = new ArrayList<>();
    private List<WebblenEvent> laterEventList = new ArrayList<>();
    private DateFormat sourceFormat = new SimpleDateFormat("dd/MM/yyyy");
    private Date eventDate;
    private Date currentDate = new Date();


    /**
     * A simple {@link Fragment} subclass.
     */
    public WebblenTableFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        //** INITIALIZE

        //Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();

        View view = inflater.inflate(R.layout.fragment_webblen_table, container, false);
        webblenEventList = new ArrayList<>();
        event_list_view = view.findViewById(R.id.event_list_view);

        webblenEventRecyclerAdapter = new WebblenEventRecyclerAdapter(webblenEventList);
        event_list_view.setLayoutManager(new LinearLayoutManager(container.getContext()));
        event_list_view.setAdapter(webblenEventRecyclerAdapter);

        //****** RETRIEVE INTERESTS & POSTS
        //Get User Interests && Events
        firebaseFirestore.collection("users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        userData = (Map<String, Object>) task.getResult().getData().get("interests");
                        //loop a Map
                        for (Map.Entry<String, Object> entry : userData.entrySet()) {
                            boolean hasInterest = (Boolean) entry.getValue();
                            if (hasInterest) {
                                userInterests.add(entry.getKey());
                            }
                        }

                        firebaseFirestore.collection("events").whereEqualTo("paid", true).addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                                    if (doc.getType() == DocumentChange.Type.ADDED) {
                                        WebblenEvent webblenEvent = doc.getDocument().toObject(WebblenEvent.class);
                                        for (String interest : userInterests) {
                                            ArrayList<String> eventCategories = webblenEvent.getCategories();
                                            if (eventCategories.contains(interest)){
                                                organizeByDate(webblenEvent);
                                            }
                                        }

                                    }
                                }

                            }
                        });
                    } else {

                    }
                }
            }

        });
        return view;
    }

    private int getDifferenceDays(Date d1, Date d2) {
        int daysdiff = 0;
        long diff = d2.getTime() - d1.getTime();
        long diffDays = diff / (24 * 60 * 60 * 1000) + 1;
        daysdiff = (int) diffDays;
        return daysdiff;
    }

    public void changeLists(String list){
        if (list.contains("today")){
            eventsToday = true;
            eventsTomorrow = false;
            eventsThisWeek = false;
            eventsThisMonth = false;
            eventsLater = false;

            webblenEventList = todayEventList;
            Log.d("Today List: ", webblenEventList.toString());
            webblenEventRecyclerAdapter.notifyDataSetChanged();

        } else if (list.contains("tomorrow")){
            eventsToday = false;
            eventsTomorrow = true;
            eventsThisWeek = false;
            eventsThisMonth = false;
            eventsLater = false;

            webblenEventList = tomorrowEventList;
            Log.d("Tomorrow List: ", webblenEventList.toString());
            webblenEventRecyclerAdapter.notifyDataSetChanged();

        } else if (list.contains("thisWeek")){
            eventsToday = false;
            eventsTomorrow = false;
            eventsThisWeek = true;
            eventsThisMonth = false;
            eventsLater = false;

            webblenEventList = thisWeekEventList;
            Log.d("Week List: ", webblenEventList.toString());
            webblenEventRecyclerAdapter.notifyDataSetChanged();

        } else if (list.contains("thisMonth")){
            eventsToday = false;
            eventsTomorrow = false;
            eventsThisWeek = false;
            eventsThisMonth = true;
            eventsLater = false;

            webblenEventList = thisMonthEventList;
            Log.d("Month List: ", webblenEventList.toString());
            webblenEventRecyclerAdapter.notifyDataSetChanged();

        } else if (list.contains("later")){
            eventsToday = false;
            eventsTomorrow = false;
            eventsThisWeek = false;
            eventsThisMonth = false;
            eventsLater = true;

            webblenEventList = laterEventList;
            Log.d("Later List: ", webblenEventList.toString());
            webblenEventRecyclerAdapter.notifyDataSetChanged();
        }
    }

    private void organizeByDate(WebblenEvent event){
        String date = event.getDate();
        try {
            eventDate = sourceFormat.parse(date);
            if (eventDate.compareTo(currentDate) < 0) {
                firebaseFirestore.collection("events").document(event.getEventKey()).delete();
            } else if (eventDate.compareTo(currentDate) == 0) {
                if (!todayEventList.contains(event)) {
                    todayEventList.add(event);
                    if (eventsToday) {
                        webblenEventRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            } else if (getDifferenceDays(currentDate, eventDate) == 1) {
                if (!tomorrowEventList.contains(event)) {
                    tomorrowEventList.add(event);
                    if (eventsTomorrow) {
                        webblenEventRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            } else if (getDifferenceDays(currentDate, eventDate) > 1 && getDifferenceDays(currentDate, eventDate) <= 7) {
                if (!thisWeekEventList.contains(event)) {
                    thisWeekEventList.add(event);
                    if (eventsThisWeek) {
                        webblenEventRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            } else if (getDifferenceDays(currentDate, eventDate) > 7 && getDifferenceDays(currentDate, eventDate) <= 30) {
                if (!thisMonthEventList.contains(event)) {
                    thisMonthEventList.add(event);
                    if (eventsThisMonth) {
                        webblenEventRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            } else {
                if (!laterEventList.contains(event)) {
                    laterEventList.add(event);
                    if (eventsLater) {
                        webblenEventRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
