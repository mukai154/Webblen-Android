package com.webblen.events.webblen.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.webblen.events.webblen.Adapters.WebblenEventAdapter;
import com.webblen.events.webblen.R;
import com.webblen.events.webblen.Recycler_Views.EventViewHolder;
import com.webblen.events.webblen.Classes.WebblenEvent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MyEventsActivity extends AppCompatActivity {

    //Firebase
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    //--
    private String user_id;
    private ArrayList<String> userData;
    private List<String> userInterests = new ArrayList<>();
    private List<WebblenEvent> eventList = new ArrayList<>();
//    private List<WebblenEvent> todayEventList = new ArrayList<>();
//    private List<WebblenEvent> tomorrowEventList = new ArrayList<>();
//    private List<WebblenEvent> thisWeekEventList = new ArrayList<>();
//    private List<WebblenEvent> thisMonthEventList = new ArrayList<>();
//    private List<WebblenEvent> laterEventList = new ArrayList<>();
    private DateFormat sourceFormat = new SimpleDateFormat("MM/dd/yyyy");
    private Date eventDate;
    private Date currentDate = new Date();

    //Recycler
    RecyclerView eventRecyclerView;
    WebblenEventAdapter eventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events);


        //** INITIALIZE

        //Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        //UI
//        noResultsImgView = (ImageView) findViewById(R.id.noMyEventsResultsImgView);
//        noResultsTextView = (TextView) findViewById(R.id.noMyEventsResultsTextView);
        eventRecyclerView = (RecyclerView) findViewById(R.id.myEventsRecyclerView);
        eventRecyclerView.setHasFixedSize(true);
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventAdapter = new WebblenEventAdapter(this, eventList);
        eventRecyclerView.setAdapter(eventAdapter);

        loadFirestoreData();
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
                            Intent setupIntent = new Intent(MyEventsActivity.this, SetupActivity.class);
                            startActivity(setupIntent);
                            finish();
                        }

                        userData = (ArrayList<String>) task.getResult().getData().get("interests");
                        Log.d("USER DATA: ", userData.toString());

                        Log.d("USER DATA: ",userInterests.toString());
                        firebaseFirestore.collection("events").whereEqualTo("author", username).addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                                    if (doc.getType() == DocumentChange.Type.ADDED) {
                                        String auth_pic = doc.getDocument().getString("author_pic");
                                        WebblenEvent webblenEvent = doc.getDocument().toObject(WebblenEvent.class);
                                        webblenEvent.setAuthor_Pic(auth_pic);
                                        eventList.add(webblenEvent);
                                    }

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
                        Toast.makeText(MyEventsActivity.this, "Load Error: " + error, Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
    }

    //Recycler View Thingies
    private void updateRecyclerViewData(){
        eventAdapter.webblenEventList = eventList;
        eventAdapter.notifyDataSetChanged();
    }
    //Set RecylcerView Visibility
    private void setRecyclerViewVisibility(){
        if (eventList.size() > 0) {
            eventRecyclerView.setVisibility(View.VISIBLE);
        } else {
            eventRecyclerView.setVisibility(View.INVISIBLE);
        }
    }

}
