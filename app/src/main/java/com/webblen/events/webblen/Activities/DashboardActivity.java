package com.webblen.events.webblen.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.webblen.events.webblen.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashboardActivity extends AppCompatActivity {

    //Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String user_id;

    //Image & Name
    private CircleImageView userWalletPic;
    private Uri mainImageURI = null;
    private TextView usernameWalletText;
    private FrameLayout listEventsFrame;
    private FrameLayout mapFrame;
    private FrameLayout myInterestsFrame;
    private FrameLayout createEventFrame;
    private FrameLayout myEventsFrame;
    private FrameLayout walletFrame;


    private ProgressBar walletProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //** INITIALIZE

        //Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();

        //Other UI
        userWalletPic = (CircleImageView) findViewById(R.id.userDashboardPic);
        usernameWalletText = (TextView) findViewById(R.id.usernameDashboardTextView);
        walletProgress = (ProgressBar) findViewById(R.id.userDashboardPicProgress);
        listEventsFrame = (FrameLayout) findViewById(R.id.listEventsDashboardBtn);
        mapFrame = (FrameLayout) findViewById(R.id.mapDashboardBtn);
        myInterestsFrame = (FrameLayout) findViewById(R.id.interestsDashboardBtn);
        createEventFrame = (FrameLayout) findViewById(R.id.createEventDashboardBtn);
        myEventsFrame = (FrameLayout) findViewById(R.id.myEventsDashboardBtn);
        walletFrame = (FrameLayout) findViewById(R.id.walletDashboardBtn);

        createEventFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createEventIntent = new Intent(DashboardActivity.this, CreateEventActivity.class);
                startActivity(createEventIntent);
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

                        //If username or pic is null...
                        if (username == null || profile_pic == null){
                            Intent setupIntent = new Intent(DashboardActivity.this, SetupActivity.class);
                            startActivity(setupIntent);
                            finish();
                        } else {

                            mainImageURI = Uri.parse(profile_pic);
                            usernameWalletText.setText("@" + username);

                            Glide.with(DashboardActivity.this).load(profile_pic).into(userWalletPic);

                            userWalletPic.setVisibility(View.VISIBLE);
                            usernameWalletText.setVisibility(View.VISIBLE);
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

}
