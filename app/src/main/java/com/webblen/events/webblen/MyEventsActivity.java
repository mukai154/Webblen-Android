package com.webblen.events.webblen;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.webblen.events.webblen.Adapter.ListItemAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MyEventsActivity extends AppCompatActivity {

    //Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String user_id;
    private Map<String, Object> userData;
    private Query userEventsQuery;
    private List<String> userInterests = new ArrayList<>();

    private RecyclerView myEventsRecycler;

    //--Events
    private boolean eventsToday = false;
    private boolean eventsTomorrow = false;
    private boolean eventsThisWeek = false;
    private boolean eventsThisMonth = false;
    private boolean eventsLater = true;
    private List<WebblenEvent> eventList = new ArrayList<>();
    private List<WebblenEvent> todayEventList = new ArrayList<>();
    private List<WebblenEvent> tomorrowEventList = new ArrayList<>();
    private List<WebblenEvent> thisWeekEventList = new ArrayList<>();
    private List<WebblenEvent> thisMonthEventList = new ArrayList<>();
    private List<WebblenEvent> laterEventList = new ArrayList<>();
    private DateFormat sourceFormat = new SimpleDateFormat("dd/MM/yyyy");
    private Date eventDate;
    private Date currentDate = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events);


        //** INITIALIZE

        //Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userEventsQuery = firebaseFirestore.collection("events").whereEqualTo("author", "Test Users").orderBy("date");

        myEventsRecycler = (RecyclerView) findViewById(R.id.myEventsRecyclerView);
        loadFirestoreData();
    }

    private void loadFirestoreData() {

        userEventsQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot snapshot,
                                FirebaseFirestoreException e) {
                if (e != null) {
                    // Handle error
                    //...
                    return;
                }

                // Convert query snapshot to a list of chats
                List<WebblenEvent> events = snapshot.toObjects(WebblenEvent.class);

                // Update UI
                // ...
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        attachRecyclerViewAdapter();
    }


    private void attachRecyclerViewAdapter() {
        final RecyclerView.Adapter adapter = newAdapter();
        myEventsRecycler.setAdapter(adapter);
    }

    protected RecyclerView.Adapter newAdapter() {
        FirestoreRecyclerOptions<WebblenEvent> options =
                new FirestoreRecyclerOptions.Builder<WebblenEvent>()
                        .setQuery(userEventsQuery, WebblenEvent.class)
                        .setLifecycleOwner(this)
                        .build();

        return new FirestoreRecyclerAdapter<WebblenEvent, EventViewHolder>(options) {
            @Override
            public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new EventViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.event_list_item, parent, false));
            }

            @Override
            protected void onBindViewHolder(@NonNull EventViewHolder holder, int position, @NonNull WebblenEvent model) {

                holder.setEventCardAuthName(model.getAuthor());
                holder.setEventCardTitle(model.getTitle());

            }

            @Override
            public void onDataChanged() {

            }
        };
    }

}
