package com.webblen.events.webblen.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.webblen.events.webblen.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashboardActivity extends AppCompatActivity {

    //Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String user_id;

    //UI Elements
    private CircleImageView userWalletPic;
    private Uri mainImageURI = null;
    private TextView usernameWalletText;
    private TextView accountValText;
    private FrameLayout listEventsFrame;
    private FrameLayout mapFrame;
    private FrameLayout myInterestsFrame;
    private FrameLayout createEventFrame;
    private FrameLayout myEventsFrame;
    private FrameLayout walletFrame;
    private FrameLayout geoChatFrame;
    private FrameLayout groupsFrame;
    private FrameLayout searchFrame;
    private ImageButton settingsDashboardBtn;
    private Button logoutBtn;



    private ProgressBar walletProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //Configure Twitter SDK
        TwitterAuthConfig authConfig = new TwitterAuthConfig(
                getString(R.string.twitter_key),
                getString(R.string.twitter_secret));

        TwitterConfig twitterConfig = new TwitterConfig.Builder(this)
                .twitterAuthConfig(authConfig)
                .build();

        Twitter.initialize(twitterConfig);

        //** INITIALIZE

        //Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();


        //Other UI
        userWalletPic = (CircleImageView) findViewById(R.id.userDashboardPic);
        usernameWalletText = (TextView) findViewById(R.id.usernameDashboardTextView);
        accountValText = (TextView) findViewById(R.id.accountValueDashboardTextView);
        walletProgress = (ProgressBar) findViewById(R.id.userDashboardPicProgress);
        listEventsFrame = (FrameLayout) findViewById(R.id.listEventsDashboardBtn);
        mapFrame = (FrameLayout) findViewById(R.id.mapDashboardBtn);
        myInterestsFrame = (FrameLayout) findViewById(R.id.interestsDashboardBtn);
        createEventFrame = (FrameLayout) findViewById(R.id.createEventDashboardBtn);
        myEventsFrame = (FrameLayout) findViewById(R.id.myEventsDashboardBtn);
        walletFrame = (FrameLayout) findViewById(R.id.walletDashboardBtn);
        geoChatFrame = (FrameLayout) findViewById(R.id.geoChatDashboardBtn);
        groupsFrame = (FrameLayout) findViewById(R.id.groupDashboardBtn);
        searchFrame = (FrameLayout) findViewById(R.id.searchDashboardBtn);
        settingsDashboardBtn = (ImageButton) findViewById(R.id.settingsDashboardBtn);
        logoutBtn = (Button) findViewById(R.id.logoutDashboardBtn);

        listEventsFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent listEventIntent = new Intent(DashboardActivity.this, EventTableActivity.class);
                startActivity(listEventIntent);
            }
        });

        mapFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(DashboardActivity.this, MainActivity.class);
                startActivity(mapIntent);
            }
        });


        myInterestsFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent interestIntent = new Intent(DashboardActivity.this, InterestsActivity.class);
                startActivity(interestIntent);
            }
        });

        createEventFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createEventIntent = new Intent(DashboardActivity.this, CreateEventActivity.class);
                startActivity(createEventIntent);
            }
        });

        myEventsFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myEventsIntent = new Intent(DashboardActivity.this, MyEventsActivity.class);
                startActivity(myEventsIntent);
            }
        });

        walletFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent walletIntent = new Intent(DashboardActivity.this, WalletActivity.class);
                startActivity(walletIntent);
            }
        });

        geoChatFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent geoChatIntent = new Intent(GeoChatActivity.this, CreateEventActivity.class);
//                startActivity(geoChatIntent);
            }
        });

        groupsFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DashboardActivity.this, "This Feature is Currently Unavailable", Toast.LENGTH_SHORT).show();
//                Intent groupsIntent = new Intent(GroupsActivity.this, MyEventsActivity.class);
//                startActivity(groupsIntent);
            }
        });

        searchFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DashboardActivity.this, "This Feature is Currently Unavailable", Toast.LENGTH_SHORT).show();
//                Intent searchIntent = new Intent(SearchActivity.this, WalletActivity.class);
//                startActivity(searchIntent);
            }
        });

        settingsDashboardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DashboardActivity.this, "This Feature is Currently Unavailable", Toast.LENGTH_SHORT).show();
                Intent settingsIntent = new Intent(DashboardActivity.this, AccountSettingsActivity.class);
                startActivity(settingsIntent);
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                LoginManager.getInstance().logOut();
                TwitterCore.getInstance().getSessionManager().clearActiveSession();
                logoutUser();
            }
        });

        loadFirestoreData();

    }

    private void loadFirestoreData() {
        firebaseFirestore.collection("users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){

                    if(task.getResult().exists()){

                        String username = task.getResult().getString("username");
                        String profile_pic = task.getResult().getString("profile_pic");

                        //Set profile name & image
                        if (username == null || profile_pic == null || profile_pic.contentEquals("")){
                            Intent setupIntent = new Intent(DashboardActivity.this, SetupActivity.class);
                            startActivity(setupIntent);
                            finish();
                        } else {
                            mainImageURI = Uri.parse(profile_pic);
                            String usernameText = "@" + username;
                            usernameWalletText.setText(usernameText);

                            Glide.with(DashboardActivity.this).load(profile_pic).into(userWalletPic);

                            userWalletPic.setVisibility(View.VISIBLE);
                            usernameWalletText.setVisibility(View.VISIBLE);
                            accountValText.setVisibility(View.VISIBLE);
                            walletProgress.setVisibility(View.INVISIBLE);
                        }
                    }
                } else {

                    String error = task.getException().getMessage();
                    Toast.makeText(DashboardActivity.this, "Load Error: " + error, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //** Authentication Methods and Handling
    @Override
    public void onStart(){
        //Check if Firebase user is signed in and act accordingly
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null){
            logoutUser();
        }

    }
    private void logoutUser(){
        Intent logoutIntent = new Intent(DashboardActivity.this, LoginActivity.class);
        startActivity(logoutIntent);
        finish();
    }

}
