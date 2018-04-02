package com.webblen.events.webblen;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class EventInfoActivity extends AppCompatActivity {

    //Firebase
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String user_id;

    //Event UI
    private ImageButton eventOptions;
    private ImageView eventPhoto;
    private TextView eventAuthName;
    private CircleImageView eventAuthImg;
    private TextView eventTitle;
    private TextView eventDesc;
    private TextView eventMonth;
    private TextView eventDayofMonth;
    private TextView eventYear;
    private TextView eventAddress;
    private TextView eventTime;
    private TextView eventViews;
    private int views;
    private ProgressBar eventInfoImgProgress;
    private ProgressBar eventAuthImgProgress;

    //Data
    private WebblenEvent selectedEvent;
    private String eventAuthor;
    private Uri authImgUri;
    private Uri eventImgUri;

    private boolean isEditing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);

        //Get Extras
        selectedEvent = (WebblenEvent)getIntent().getSerializableExtra("selectedEvent");
        eventAuthor = selectedEvent.getAuthor();

        //Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        //Other UI
        eventOptions = (ImageButton) findViewById(R.id.moreOptionsBtn);
        eventPhoto = (ImageView) findViewById(R.id.eventInfoImg);
        eventAuthName = (TextView) findViewById(R.id.eventInfoUsername);
        eventAuthImg = (CircleImageView) findViewById(R.id.eventInfoUserImg);
        eventTitle = (TextView) findViewById(R.id.eventInfoTitle);
        eventDesc = (TextView) findViewById(R.id.eventInfoDescription);
        eventMonth = (TextView) findViewById(R.id.eventInfoMonth);
        eventDayofMonth = (TextView) findViewById(R.id.eventInfoDay);
        eventYear = (TextView) findViewById(R.id.eventInfoYear);
        eventAddress = (TextView) findViewById(R.id.eventAddress);
        eventTime = (TextView) findViewById(R.id.eventTime);
        eventViews = (TextView) findViewById(R.id.eventViewsText);
        eventInfoImgProgress = (ProgressBar) findViewById(R.id.infoImgProgress);
        eventAuthImgProgress = (ProgressBar) findViewById(R.id.infoAuthImgProgress);

        //Set UI
        setEventImg(selectedEvent.getPathToImage());
        getEventAuthorFirestoreData(selectedEvent.getAuthor());
        setTextUIViews();
        addViewToEvent();

    }

    private void addViewToEvent(){
        views = selectedEvent.getViews();
        views += 1;
        firebaseFirestore.collection("events").document(selectedEvent.getEventKey())
                .update("views", views)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    private void getEventAuthorFirestoreData(String author){
        firebaseFirestore.collection("usernames").document(author).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    String author_uid = task.getResult().getString("uid");

                    firebaseFirestore.collection("users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            if(task.isSuccessful()){

                                if(task.getResult().exists()){

                                    String username = task.getResult().getString("username");
                                    String profile_pic = task.getResult().getString("profile_pic");

                                    //pic is null...
                                    if (profile_pic == null){

                                        eventAuthImg.setMaxWidth(1);
                                        eventAuthImg.setVisibility(View.INVISIBLE);

                                    } else {

                                        setAuthImg(profile_pic);

                                    }
                                }
                            } else {
                                String error = task.getException().getMessage();
                                Toast.makeText(EventInfoActivity.this, "Load Error: " + error, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(EventInfoActivity.this, "Load Error: " + error, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setEventImg(String pathToImage){
        if (pathToImage.isEmpty() || pathToImage.contentEquals("") || pathToImage == null){
            ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) eventPhoto.getLayoutParams();
            params.height = 1;
            eventPhoto.setLayoutParams(params);
            eventPhoto.setVisibility(View.INVISIBLE);
            eventInfoImgProgress.setVisibility(View.INVISIBLE);
        } else {
            Glide.with(EventInfoActivity.this).load(pathToImage).into(eventPhoto);
            eventInfoImgProgress.setVisibility(View.INVISIBLE);
        }
    }

    private void setAuthImg(String pathToImage){
        Glide.with(EventInfoActivity.this).load(pathToImage).into(eventAuthImg);
        eventAuthImg.setVisibility(View.VISIBLE);
        eventAuthImgProgress.setVisibility(View.INVISIBLE);
    }

    private void setTextUIViews(){
        String author = "@" + selectedEvent.getAuthor();
        eventAuthName.setText(author);
        eventAuthName.setBackgroundResource(0);

        eventTitle.setText(selectedEvent.getTitle());
        eventTitle.setBackgroundResource(0);

        eventDesc.setText(selectedEvent.getDescription());
        eventDesc.setBackgroundResource(0);

        String date = selectedEvent.getDate();
        String[] dateParts = date.split("/");

        eventMonth.setText(Utilities.getMonth(dateParts[0]));
        eventMonth.setBackgroundResource(0);

        eventDayofMonth.setText(dateParts[1]);
        eventDayofMonth.setBackgroundResource(0);

        eventYear.setText(dateParts[2]);
        eventYear.setBackgroundResource(0);

        String address = "Address: " + selectedEvent.getAddress();
        address = address.replace(", USA", "");
        eventAddress.setText(address);
        eventAddress.setBackgroundResource(0);

        String time = "Time: " + selectedEvent.getTime();
        eventTime.setText(time);
        eventTime.setBackgroundResource(0);

        String views = String.valueOf(selectedEvent.getViews());
        eventViews.setText(views);
        eventViews.setBackgroundResource(0);
    }

}
