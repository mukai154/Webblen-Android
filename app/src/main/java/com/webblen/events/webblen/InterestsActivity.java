package com.webblen.events.webblen;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InterestsActivity extends AppCompatActivity {

    //Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String user_id;
    private Map<String, Object> userData;
    private List<String> userInterests = new ArrayList<>();

    //Interests & Btns
    private boolean amusement = false;
    private ImageButton amusementBtn;
    private boolean art = false;
    private ImageButton artBtn;
    private boolean collegeLife = false;
    private ImageButton collegeLifeBtn;
    private boolean community = false;
    private ImageButton communityBtn;
    private boolean competition = false;
    private ImageButton competitionBtn;
    private boolean culture = false;
    private ImageButton cultureBtn;
    private boolean education = false;
    private ImageButton educationBtn;
    private boolean entertainment = false;
    private ImageButton entertainmentBtn;
    private boolean family = false;
    private ImageButton familyBtn;
    private boolean foodDrink = false;
    private ImageButton foodDrinkBtn;
    private boolean gaming = false;
    private ImageButton gamingBtn;
    private boolean healthFitness = false;
    private ImageButton healthFitnessBtn;
    private boolean music = false;
    private ImageButton musicBtn;
    private boolean networking = false;
    private ImageButton networkingBtn;
    private boolean outdoors = false;
    private ImageButton outdoorsBtn;
    private boolean partyDance = false;
    private ImageButton partyDanceBtn;
    private boolean shopping = false;
    private ImageButton shoppingBtn;
    private boolean sports = false;
    private ImageButton sportsBtn;
    private boolean technology = false;
    private ImageButton techBtn;
    private boolean theatre = false;
    private ImageButton theatreBtn;
    private boolean wineBrew = false;
    private ImageButton wineBrewBtn;

    private Button submitInterestsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interests);


        //** INITIALIZE

        //Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();

        //Interests Btns
        amusementBtn = (ImageButton) findViewById(R.id.amusementBtn);
        artBtn = (ImageButton) findViewById(R.id.artBtn);
        collegeLifeBtn = (ImageButton) findViewById(R.id.collegeBtn);
        communityBtn = (ImageButton) findViewById(R.id.communityBtn);
        competitionBtn = (ImageButton) findViewById(R.id.competitionBtn);
        cultureBtn = (ImageButton) findViewById(R.id.cultureBtn);
        educationBtn = (ImageButton) findViewById(R.id.educationBtn);
        entertainmentBtn = (ImageButton) findViewById(R.id.entertainmentBtn);
        familyBtn = (ImageButton) findViewById(R.id.familyBtn);
        foodDrinkBtn = (ImageButton) findViewById(R.id.foodDrinkBtn);
        gamingBtn = (ImageButton) findViewById(R.id.gamingBtn);
        healthFitnessBtn = (ImageButton) findViewById(R.id.healthFitnessBtn);
        musicBtn = (ImageButton) findViewById(R.id.musicBtn);
        networkingBtn = (ImageButton) findViewById(R.id.networkingBtn);
        outdoorsBtn = (ImageButton) findViewById(R.id.outdoorsBtn);
        partyDanceBtn = (ImageButton) findViewById(R.id.partyDanceBtn);
        shoppingBtn = (ImageButton) findViewById(R.id.shoppingBtn);
        sportsBtn = (ImageButton) findViewById(R.id.sportsBtn);
        techBtn = (ImageButton) findViewById(R.id.techBtn);
        theatreBtn = (ImageButton) findViewById(R.id.theatreBtn);
        wineBrewBtn = (ImageButton) findViewById(R.id.wineBrewBtn);

        submitInterestsBtn = (Button) findViewById(R.id.submitInterestsBtn);

        loadUserInterests();

        //Listeners
        amusementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (amusement){
                    amusement = false;
                    amusementBtn.setBackground(null);
                } else {
                    amusement = true;
                    amusementBtn.setBackground(getDrawable(R.drawable.border_orange));
                }
            }
        });

        artBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (art){
                    art = false;
                    artBtn.setBackground(null);
                } else {
                    art = true;
                    artBtn.setBackground(getDrawable(R.drawable.border_orange));
                }
            }
        });

        collegeLifeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (collegeLife){
                    collegeLife = false;
                    collegeLifeBtn.setBackground(null);
                } else {
                    collegeLife = true;
                    collegeLifeBtn.setBackground(getDrawable(R.drawable.border_orange));
                }
            }
        });

        communityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (community){
                    community = false;
                    communityBtn.setBackground(null);
                } else {
                    community = true;
                    communityBtn.setBackground(getDrawable(R.drawable.border_orange));
                }
            }
        });

        competitionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (competition){
                    competition = false;
                    competitionBtn.setBackground(null);
                } else {
                    competition = true;
                    competitionBtn.setBackground(getDrawable(R.drawable.border_orange));
                }
            }
        });

        cultureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (culture){
                    culture = false;
                    cultureBtn.setBackground(null);
                } else {
                    culture = true;
                    cultureBtn.setBackground(getDrawable(R.drawable.border_orange));
                }
            }
        });

        educationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (education){
                    education = false;
                    educationBtn.setBackground(null);
                } else {
                    education = true;
                    educationBtn.setBackground(getDrawable(R.drawable.border_orange));
                }
            }
        });

        entertainmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (entertainment){
                    entertainment = false;
                    entertainmentBtn.setBackground(null);
                } else {
                    entertainment = true;
                    entertainmentBtn.setBackground(getDrawable(R.drawable.border_orange));
                }
            }
        });

        familyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (family){
                    family = false;
                    familyBtn.setBackground(null);
                } else {
                    family = true;
                    familyBtn.setBackground(getDrawable(R.drawable.border_orange));
                }
            }
        });

        foodDrinkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (foodDrink){
                    foodDrink = false;
                    foodDrinkBtn.setBackground(null);
                } else {
                    foodDrink = true;
                    foodDrinkBtn.setBackground(getDrawable(R.drawable.border_orange));
                }
            }
        });

        gamingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gaming){
                    gaming = false;
                    gamingBtn.setBackground(null);
                } else {
                    gaming = true;
                    gamingBtn.setBackground(getDrawable(R.drawable.border_orange));
                }
            }
        });

        healthFitnessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (healthFitness){
                    healthFitness = false;
                    healthFitnessBtn.setBackground(null);
                } else {
                    healthFitness = true;
                    healthFitnessBtn.setBackground(getDrawable(R.drawable.border_orange));
                }
            }
        });

        musicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (music){
                    music = false;
                    musicBtn.setBackground(null);
                } else {
                    music = true;
                    musicBtn.setBackground(getDrawable(R.drawable.border_orange));
                }
            }
        });

        networkingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (networking){
                    networking = false;
                    networkingBtn.setBackground(null);
                } else {
                    networking = true;
                    networkingBtn.setBackground(getDrawable(R.drawable.border_orange));
                }
            }
        });

        outdoorsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (outdoors){
                    outdoors = false;
                    outdoorsBtn.setBackground(null);
                } else {
                    outdoors = true;
                    outdoorsBtn.setBackground(getDrawable(R.drawable.border_orange));
                }
            }
        });

        partyDanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (partyDance){
                    partyDance = false;
                    partyDanceBtn.setBackground(null);
                } else {
                    partyDance = true;
                    partyDanceBtn.setBackground(getDrawable(R.drawable.border_orange));
                }
            }
        });

        shoppingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shopping){
                    shopping = false;
                    shoppingBtn.setBackground(null);
                } else {
                    shopping = true;
                    shoppingBtn.setBackground(getDrawable(R.drawable.border_orange));
                }
            }
        });

        sportsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sports){
                    sports = false;
                    sportsBtn.setBackground(null);
                } else {
                    sports = true;
                    sportsBtn.setBackground(getDrawable(R.drawable.border_orange));
                }
            }
        });

        techBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (technology){
                    technology = false;
                    techBtn.setBackground(null);
                } else {
                    technology = true;
                    techBtn.setBackground(getDrawable(R.drawable.border_orange));
                }
            }
        });

        theatreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (theatre){
                    theatre = false;
                    theatreBtn.setBackground(null);
                } else {
                    theatre = true;
                    theatreBtn.setBackground(getDrawable(R.drawable.border_orange));
                }
            }
        });

        wineBrewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (wineBrew){
                    wineBrew = false;
                    wineBrewBtn.setBackground(null);
                } else {
                    wineBrew = true;
                    wineBrewBtn.setBackground(getDrawable(R.drawable.border_orange));
                }

            }
        });

        submitInterestsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeToFirebase();
            }
        });



    }

    private void storeToFirebase(){


        //Set all interests to true by default
        Map<String, Object> nestedUserData = new HashMap<>();
        nestedUserData.put("AMUSEMENT", amusement);
        nestedUserData.put("ART", art);
        nestedUserData.put("COLLEGELIFE", collegeLife);
        nestedUserData.put("COMMUNITY", community);
        nestedUserData.put("COMPETITION", competition);
        nestedUserData.put("CULTURE", culture);
        nestedUserData.put("EDUCATION", education);
        nestedUserData.put("ENTERTAINMENT", entertainment);
        nestedUserData.put("FAMILY", family);
        nestedUserData.put("FOODDRINK", foodDrink);
        nestedUserData.put("GAMING", gaming);
        nestedUserData.put("HEALTHFITNESS", healthFitness);
        nestedUserData.put("MUSIC", music);
        nestedUserData.put("NETWORKING", networking);
        nestedUserData.put("OUTDOORS", outdoors);
        nestedUserData.put("PARTYDANCE", partyDance);
        nestedUserData.put("SHOPPING", shopping);
        nestedUserData.put("SPORTS", sports);
        nestedUserData.put("TECHNOLOGY", technology);
        nestedUserData.put("THEATRE", theatre);
        nestedUserData.put("WINEBREW", wineBrew);


        //Send to Firebase
        firebaseFirestore.collection("users").document(user_id)
                .update("interests", nestedUserData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent mainIntent = new Intent(InterestsActivity.this, MainActivity.class);
                        startActivity(mainIntent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(InterestsActivity.this, "There Was an Issue Setting Up Your Account. Please Try Again.", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void loadUserInterests() {

        firebaseFirestore.collection("users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){

                    if(task.getResult().exists()){
                        userData = (Map<String, Object>) task.getResult().getData().get("interests");
                        //loop a Map
                        for (Map.Entry<String, Object> entry : userData.entrySet()) {
                            boolean hasInterest = (Boolean) entry.getValue();
                            if (hasInterest) {
                                userInterests.add(entry.getKey().toLowerCase());
                            }
                        }
                        loadedInterests();
                    }

                } else {

                    String error = task.getException().getMessage();
                    Toast.makeText(InterestsActivity.this, "Load Error: " + error, Toast.LENGTH_LONG).show();

                }

            }
        });

    }

    private void loadedInterests(){
        if (userInterests.contains("amusement")){
            amusement = true;
            amusementBtn.setBackground(getDrawable(R.drawable.border_orange));
        }
        if (userInterests.contains("art")){
            art = true;
            artBtn.setBackground(getDrawable(R.drawable.border_orange));
        }
        if (userInterests.contains("collegelife")){
            collegeLife = true;
            collegeLifeBtn.setBackground(getDrawable(R.drawable.border_orange));
        }
        if (userInterests.contains("community")){
            community = true;
            communityBtn.setBackground(getDrawable(R.drawable.border_orange));
        }
        if (userInterests.contains("competition")){
            competition = true;
            competitionBtn.setBackground(getDrawable(R.drawable.border_orange));
        }
        if (userInterests.contains("culture")){
            culture = true;
            cultureBtn.setBackground(getDrawable(R.drawable.border_orange));
        }
        if (userInterests.contains("education")){
            education = true;
            educationBtn.setBackground(getDrawable(R.drawable.border_orange));
        }
        if (userInterests.contains("entertainment")){
            entertainment = true;
            entertainmentBtn.setBackground(getDrawable(R.drawable.border_orange));
        }
        if (userInterests.contains("family")){
            family = true;
            familyBtn.setBackground(getDrawable(R.drawable.border_orange));
        }
        if (userInterests.contains("fooddrink")){
            foodDrink = true;
            foodDrinkBtn.setBackground(getDrawable(R.drawable.border_orange));
        }
        if (userInterests.contains("gaming")){
            gaming = true;
            gamingBtn.setBackground(getDrawable(R.drawable.border_orange));
        }
        if (userInterests.contains("healthfitness")){
            healthFitness = true;
            healthFitnessBtn.setBackground(getDrawable(R.drawable.border_orange));
        }
        if (userInterests.contains("music")){
            music = true;
            musicBtn.setBackground(getDrawable(R.drawable.border_orange));
        }
        if (userInterests.contains("networking")){
            networking = true;
            networkingBtn.setBackground(getDrawable(R.drawable.border_orange));
        }
        if (userInterests.contains("outdoors")){
            outdoors = true;
            outdoorsBtn.setBackground(getDrawable(R.drawable.border_orange));
        }
        if (userInterests.contains("partydance")){
            partyDance = true;
            partyDanceBtn.setBackground(getDrawable(R.drawable.border_orange));
        }
        if (userInterests.contains("shopping")){
            shopping = true;
            shoppingBtn.setBackground(getDrawable(R.drawable.border_orange));
        }
        if (userInterests.contains("sports")){
            sports = true;
            sportsBtn.setBackground(getDrawable(R.drawable.border_orange));
        }
        if (userInterests.contains("technology")){
            technology = true;
            techBtn.setBackground(getDrawable(R.drawable.border_orange));
        }
        if (userInterests.contains("theatre")){
            theatre = true;
            theatreBtn.setBackground(getDrawable(R.drawable.border_orange));
        }
        if (userInterests.contains("winebrew")){
            wineBrew = true;
            wineBrewBtn.setBackground(getDrawable(R.drawable.border_orange));
        }
    }
}
