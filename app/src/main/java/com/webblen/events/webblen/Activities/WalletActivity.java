package com.webblen.events.webblen.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
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

public class WalletActivity extends AppCompatActivity {

    //Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String user_id;

    //Image & Name
    private CircleImageView userWalletPic;
    private Uri mainImageURI = null;
    private TextView usernameWalletText;

    private ProgressBar walletProgress;

    //WBLN Options
    private ImageButton wbblnSpinner;
    private ImageButton webPowerSpinner;
    private ImageButton webDollarSpinner;
    private ImageButton savingsWblnSpinner;
    private ImageButton savingsWbDllrSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        //** INITIALIZE

        //Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();

        //Other UI
        userWalletPic = (CircleImageView) findViewById(R.id.userWalletPic);
        usernameWalletText = (TextView) findViewById(R.id.usernameWalletText);
        walletProgress = (ProgressBar) findViewById(R.id.walletProgressBar);

        wbblnSpinner = (ImageButton) findViewById(R.id.wbblnSpinner);
        webPowerSpinner = (ImageButton) findViewById(R.id.webPowerSpinner);
        webDollarSpinner = (ImageButton) findViewById(R.id.webDollarSpinner);
        savingsWblnSpinner = (ImageButton) findViewById(R.id.savingsWblnSpinner);
        savingsWbDllrSpinner = (ImageButton) findViewById(R.id.savingsWbDllrSpinner);



        loadFirestoreData();


        //Spinners
        wbblnSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WalletActivity.this, "Feature Currently Unavailable", Toast.LENGTH_LONG).show();
            }
        });
        webPowerSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WalletActivity.this, "Feature Currently Unavailable", Toast.LENGTH_LONG).show();
            }
        });
        webDollarSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WalletActivity.this, "Feature Currently Unavailable", Toast.LENGTH_LONG).show();
            }
        });
        savingsWblnSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WalletActivity.this, "Feature Currently Unavailable", Toast.LENGTH_LONG).show();
            }
        });
        savingsWbDllrSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WalletActivity.this, "Feature Currently Unavailable", Toast.LENGTH_LONG).show();
            }
        });

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
                            Intent setupIntent = new Intent(WalletActivity.this, SetupActivity.class);
                            startActivity(setupIntent);
                            finish();
                        } else {

                            mainImageURI = Uri.parse(profile_pic);
                            usernameWalletText.setText("@" + username);

                            Glide.with(WalletActivity.this).load(profile_pic).into(userWalletPic);

                            userWalletPic.setVisibility(View.VISIBLE);
                            usernameWalletText.setVisibility(View.VISIBLE);
                            walletProgress.setVisibility(View.INVISIBLE);

                        }
                    }

                } else {

                    String error = task.getException().getMessage();
                    Toast.makeText(WalletActivity.this, "Load Error: " + error, Toast.LENGTH_LONG).show();

                }

            }
        });
    }
}
