package com.webblen.events.webblen.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.webblen.events.webblen.R;

import java.util.ArrayList;
import java.util.List;

public class InterestsActivity extends AppCompatActivity {

    //Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String user_id;
    private List<String> userInterests = new ArrayList<>();
    private List<String> newList = new ArrayList<>();

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

        addInterestsToList();


        //Send to Firebase
        firebaseFirestore.collection("users").document(user_id)
                .update("interests", newList)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(InterestsActivity.this, "There Was an Issue Setting Up Your Interests. Please Try Again.", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void loadUserInterests() {

        firebaseFirestore.collection("users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){

                    if(task.getResult().exists()){
                        userInterests = (ArrayList<String>) task.getResult().getData().get("interests");
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
        if (userInterests.contains("AMUSEMENT")){
            amusement = true;
            amusementBtn.setBackground(getDrawable(R.drawable.border_orange));
        }
        if (userInterests.contains("ART")){
            art = true;
            artBtn.setBackground(getDrawable(R.drawable.border_orange));
        }
        if (userInterests.contains("COLLEGELIFE")){
            collegeLife = true;
            collegeLifeBtn.setBackground(getDrawable(R.drawable.border_orange));
        }
        if (userInterests.contains("COMMUNITY")){
            community = true;
            communityBtn.setBackground(getDrawable(R.drawable.border_orange));
        }
        if (userInterests.contains("COMPETITION")){
            competition = true;
            competitionBtn.setBackground(getDrawable(R.drawable.border_orange));
        }
        if (userInterests.contains("CULTURE")){
            culture = true;
            cultureBtn.setBackground(getDrawable(R.drawable.border_orange));
        }
        if (userInterests.contains("EDUCATION")){
            education = true;
            educationBtn.setBackground(getDrawable(R.drawable.border_orange));
        }
        if (userInterests.contains("ENTERTAINMENT")){
            entertainment = true;
            entertainmentBtn.setBackground(getDrawable(R.drawable.border_orange));
        }
        if (userInterests.contains("FAMILY")){
            family = true;
            familyBtn.setBackground(getDrawable(R.drawable.border_orange));
        }
        if (userInterests.contains("FOODDRINK")){
            foodDrink = true;
            foodDrinkBtn.setBackground(getDrawable(R.drawable.border_orange));
        }
        if (userInterests.contains("GAMING")){
            gaming = true;
            gamingBtn.setBackground(getDrawable(R.drawable.border_orange));
        }
        if (userInterests.contains("HEALTHFITNESS")){
            healthFitness = true;
            healthFitnessBtn.setBackground(getDrawable(R.drawable.border_orange));
        }
        if (userInterests.contains("MUSIC")){
            music = true;
            musicBtn.setBackground(getDrawable(R.drawable.border_orange));
        }
        if (userInterests.contains("NETWORKING")){
            networking = true;
            networkingBtn.setBackground(getDrawable(R.drawable.border_orange));
        }
        if (userInterests.contains("OUTDOORS")){
            outdoors = true;
            outdoorsBtn.setBackground(getDrawable(R.drawable.border_orange));
        }
        if (userInterests.contains("PARTYDANCE")){
            partyDance = true;
            partyDanceBtn.setBackground(getDrawable(R.drawable.border_orange));
        }
        if (userInterests.contains("SHOPPING")){
            shopping = true;
            shoppingBtn.setBackground(getDrawable(R.drawable.border_orange));
        }
        if (userInterests.contains("SPORTS")){
            sports = true;
            sportsBtn.setBackground(getDrawable(R.drawable.border_orange));
        }
        if (userInterests.contains("TECHNOLOGY")){
            technology = true;
            techBtn.setBackground(getDrawable(R.drawable.border_orange));
        }
        if (userInterests.contains("THEATRE")){
            theatre = true;
            theatreBtn.setBackground(getDrawable(R.drawable.border_orange));
        }
        if (userInterests.contains("WINEBREW")){
            wineBrew = true;
            wineBrewBtn.setBackground(getDrawable(R.drawable.border_orange));
        }
    }

    private void addInterestsToList(){
        if (amusement){
            newList.add("AMUSEMENT");
        }
        if (art){
            newList.add("ART");
        }
        if (collegeLife){
            newList.add("COLLEGELIFE");
        }
        if (community){
            newList.add("COMMUNITY");
        }
        if (competition){
            newList.add("COMPETITION");
        }
        if (culture){
            newList.add("CULTURE");
        }
        if (education){
            newList.add("EDUCATION");
        }
        if (entertainment){
            newList.add("ENTERTAINMENT");
        }
        if (family){
            newList.add("FAMILY");
        }
        if (foodDrink){
            newList.add("FOODDRINK");
        }
        if (gaming){
            newList.add("GAMING");
        }
        if (healthFitness){
            newList.add("HEALTHFITNESS");
        }
        if (music){
            newList.add("MUSIC");
        }
        if (networking){
            newList.add("NETWORKING");
        }
        if (outdoors){
            newList.add("OUTDOORS");
        }
        if (partyDance){
            newList.add("PARTYDANCE");
        }
        if (shopping){
            newList.add("SHOPPING");
        }
        if (sports){
            newList.add("SPORTS");
        }
        if (technology){
            newList.add("TECHNOLOGY");
        }
        if (theatre){
            newList.add("THEATRE");
        }
        if (wineBrew){
            newList.add("WINEBREW");
        }
    }
}
