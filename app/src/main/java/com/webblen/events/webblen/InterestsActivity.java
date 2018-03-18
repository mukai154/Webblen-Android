package com.webblen.events.webblen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

public class InterestsActivity extends AppCompatActivity {

    //Firebase

    //Interests & Btns
    private boolean amusement;
    private ImageButton amusementBtn;
    private boolean art;
    private ImageButton artBtn;
    private boolean collegeLife;
    private ImageButton collegeLifeBtn;
    private boolean community;
    private ImageButton communityBtn;
    private boolean competition;
    private ImageButton competitionBtn;
    private boolean culture;
    private ImageButton cultureBtn;
    private boolean education;
    private ImageButton educationBtn;
    private boolean entertainment;
    private ImageButton entertainmentBtn;
    private boolean family;
    private ImageButton familyBtn;
    private boolean foodDrink;
    private ImageButton foodDrinkBtn;
    private boolean gaming;
    private ImageButton gamingBtn;
    private boolean healthFitness;
    private ImageButton healthFitnessBtn;
    private boolean music;
    private ImageButton musicBtn;
    private boolean networking;
    private ImageButton networkingBtn;
    private boolean outdoors;
    private ImageButton outdoorsBtn;
    private boolean partyDance;
    private ImageButton partyDanceBtn;
    private boolean shopping;
    private ImageButton shoppingBtn;
    private boolean sports;
    private ImageButton sportsBtn;
    private boolean technology;
    private ImageButton techBtn;
    private boolean theatre;
    private ImageButton theatreBtn;
    private boolean wineBrew;
    private ImageButton wineBrewBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interests);


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


    }
}
