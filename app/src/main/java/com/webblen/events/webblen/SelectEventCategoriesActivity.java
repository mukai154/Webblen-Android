package com.webblen.events.webblen;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SelectEventCategoriesActivity extends AppCompatActivity {

    //Tag Buttons
    private Button amusementTagBtn;
    private Button artTagBtn;
    private Button collegeTagBtn;
    private Button communityTagBtn;
    private Button competitionTagBtn;
    private Button cultureTagBtn;
    private Button educationTagBtn;
    private Button entertainmentTagBtn;
    private Button familyTagBtn;
    private Button foodDrinkTagBtn;
    private Button gamingTagBtn;
    private Button healthTagBtn;
    private Button musicTagBtn;
    private Button networkingTagBtn;
    private Button outdoorsTagBtn;
    private Button partyDanceTagBtn;
    private Button shoppingTagBtn;
    private Button sportsTagBtn;
    private Button techTagBtn;
    private Button theatreTagBtn;
    private Button wineBrewTagBtn;

    //Selected Tags
    private List<String> selectedTags = new ArrayList<String>();

    //Set Btn
    private Button setTagsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_event_categories);

        //** INITIALIZE

        amusementTagBtn = (Button) findViewById(R.id.amusementTagBtn);
        artTagBtn = (Button) findViewById(R.id.artTagBtn);
        collegeTagBtn = (Button) findViewById(R.id.collegeTagBtn);
        communityTagBtn = (Button) findViewById(R.id.communityTagBtn);
        competitionTagBtn = (Button) findViewById(R.id.competitionTagBtn);
        cultureTagBtn = (Button) findViewById(R.id.cultureTagBtn);
        entertainmentTagBtn = (Button) findViewById(R.id.entertainmentTagBtn);
        educationTagBtn = (Button) findViewById(R.id.educationTagBtn);
        familyTagBtn = (Button) findViewById(R.id.familyTagBtn);
        foodDrinkTagBtn = (Button) findViewById(R.id.foodDrinkTagBtn);
        gamingTagBtn = (Button) findViewById(R.id.gamingTagBtn);
        healthTagBtn = (Button) findViewById(R.id.healthTagBtn);
        musicTagBtn = (Button) findViewById(R.id.musicTagBtn);
        networkingTagBtn = (Button) findViewById(R.id.networkingTagBtn);
        outdoorsTagBtn = (Button) findViewById(R.id.outdoorsTagBtn);
        partyDanceTagBtn = (Button) findViewById(R.id.partyDanceTagBtn);
        shoppingTagBtn = (Button) findViewById(R.id.shoppingTagBtn);
        sportsTagBtn = (Button) findViewById(R.id.sportsTagBtn);
        theatreTagBtn = (Button) findViewById(R.id.theatreTagBtn);
        techTagBtn = (Button) findViewById(R.id.techTagBtn);
        wineBrewTagBtn = (Button) findViewById(R.id.wineBrewTagBtn);

        setTagsBtn = (Button) findViewById(R.id.setTagsBtn);

        //Button UI Actions
        amusementTagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedTags.size() == 3 && !(selectedTags.contains("AMUSEMENT"))){
                    Toast.makeText(SelectEventCategoriesActivity.this, "Events Can Only Have a Max of 3 Tags", Toast.LENGTH_LONG).show();
                }
                else if (!(selectedTags.contains("AMUSEMENT"))){
                    selectedTags.add("AMUSEMENT");
                    amusementTagBtn.setBackgroundResource(R.drawable.btn_dark_gray);
                    amusementTagBtn.setTextColor(Color.parseColor("#ffffff"));
                }
                else {
                    selectedTags.remove("AMUSEMENT");
                    amusementTagBtn.setBackgroundResource(0);
                    amusementTagBtn.setTextColor(Color.parseColor("#424242"));
                }
            }
        });

        artTagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedTags.size() == 3 && !(selectedTags.contains("ART"))){
                    Toast.makeText(SelectEventCategoriesActivity.this, "Events Can Only Have a Max of 3 Tags", Toast.LENGTH_LONG).show();
                }
                else if (!(selectedTags.contains("ART"))){
                    selectedTags.add("ART");
                    artTagBtn.setBackgroundResource(R.drawable.btn_dark_gray);
                    artTagBtn.setTextColor(Color.parseColor("#ffffff"));
                }
                else {
                    selectedTags.remove("ART");
                    artTagBtn.setBackgroundResource(0);
                    artTagBtn.setTextColor(Color.parseColor("#424242"));
                }
            }
        });

        collegeTagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedTags.size() == 3 && !(selectedTags.contains("COLLEGELIFE"))){
                    Toast.makeText(SelectEventCategoriesActivity.this, "Events Can Only Have a Max of 3 Tags", Toast.LENGTH_LONG).show();
                }
                else if (!(selectedTags.contains("COLLEGELIFE"))){
                    selectedTags.add("COLLEGELIFE");
                    collegeTagBtn.setBackgroundResource(R.drawable.btn_dark_gray);
                    collegeTagBtn.setTextColor(Color.parseColor("#ffffff"));
                }
                else {
                    selectedTags.remove("COLLEGELIFE");
                    collegeTagBtn.setBackgroundResource(0);
                    collegeTagBtn.setTextColor(Color.parseColor("#424242"));
                }
            }
        });

        communityTagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedTags.size() == 3 && !(selectedTags.contains("COMMUNITY"))){
                    Toast.makeText(SelectEventCategoriesActivity.this, "Events Can Only Have a Max of 3 Tags", Toast.LENGTH_LONG).show();
                }
                else if (!(selectedTags.contains("COMMUNITY"))){
                    selectedTags.add("COMMUNITY");
                    communityTagBtn.setBackgroundResource(R.drawable.btn_dark_gray);
                    communityTagBtn.setTextColor(Color.parseColor("#ffffff"));
                }
                else {
                    selectedTags.remove("COMMUNITY");
                    communityTagBtn.setBackgroundResource(0);
                    communityTagBtn.setTextColor(Color.parseColor("#424242"));
                }
            }
        });

        competitionTagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedTags.size() == 3 && !(selectedTags.contains("COMPETITION"))){
                    Toast.makeText(SelectEventCategoriesActivity.this, "Events Can Only Have a Max of 3 Tags", Toast.LENGTH_LONG).show();
                }
                else if (!(selectedTags.contains("COMPETITION"))){
                    selectedTags.add("COMPETITION");
                    competitionTagBtn.setBackgroundResource(R.drawable.btn_dark_gray);
                    competitionTagBtn.setTextColor(Color.parseColor("#ffffff"));
                }
                else {
                    selectedTags.remove("COMPETITION");
                    competitionTagBtn.setBackgroundResource(0);
                    competitionTagBtn.setTextColor(Color.parseColor("#424242"));
                }
            }
        });

        cultureTagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedTags.size() == 3 && !(selectedTags.contains("CULTURE"))){
                    Toast.makeText(SelectEventCategoriesActivity.this, "Events Can Only Have a Max of 3 Tags", Toast.LENGTH_LONG).show();
                }
                else if (!(selectedTags.contains("CULTURE"))){
                    selectedTags.add("CULTURE");
                    cultureTagBtn.setBackgroundResource(R.drawable.btn_dark_gray);
                    cultureTagBtn.setTextColor(Color.parseColor("#ffffff"));
                }
                else {
                    selectedTags.remove("CULTURE");
                    cultureTagBtn.setBackgroundResource(0);
                    cultureTagBtn.setTextColor(Color.parseColor("#424242"));
                }
            }
        });

        educationTagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedTags.size() == 3 && !(selectedTags.contains("EDUCATION"))){
                    Toast.makeText(SelectEventCategoriesActivity.this, "Events Can Only Have a Max of 3 Tags", Toast.LENGTH_LONG).show();
                }
                else if (!(selectedTags.contains("EDUCATION"))){
                    selectedTags.add("EDUCATION");
                    educationTagBtn.setBackgroundResource(R.drawable.btn_dark_gray);
                    educationTagBtn.setTextColor(Color.parseColor("#ffffff"));
                }
                else {
                    selectedTags.remove("EDUCATION");
                    educationTagBtn.setBackgroundResource(0);
                    educationTagBtn.setTextColor(Color.parseColor("#424242"));
                }
            }
        });

        entertainmentTagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedTags.size() == 3 && !(selectedTags.contains("ENTERTAINMENT"))){
                    Toast.makeText(SelectEventCategoriesActivity.this, "Events Can Only Have a Max of 3 Tags", Toast.LENGTH_LONG).show();
                }
                else if (!(selectedTags.contains("ENTERTAINMENT"))){
                    selectedTags.add("ENTERTAINMENT");
                    entertainmentTagBtn.setBackgroundResource(R.drawable.btn_dark_gray);
                    entertainmentTagBtn.setTextColor(Color.parseColor("#ffffff"));
                }
                else {
                    selectedTags.remove("ENTERTAINMENT");
                    entertainmentTagBtn.setBackgroundResource(0);
                    entertainmentTagBtn.setTextColor(Color.parseColor("#424242"));
                }
            }
        });

        familyTagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedTags.size() == 3 && !(selectedTags.contains("FAMILY"))){
                    Toast.makeText(SelectEventCategoriesActivity.this, "Events Can Only Have a Max of 3 Tags", Toast.LENGTH_LONG).show();
                }
                else if (!(selectedTags.contains("FAMILY"))){
                    selectedTags.add("FAMILY");
                    familyTagBtn.setBackgroundResource(R.drawable.btn_dark_gray);
                    familyTagBtn.setTextColor(Color.parseColor("#ffffff"));
                }
                else {
                    selectedTags.remove("FAMILY");
                    familyTagBtn.setBackgroundResource(0);
                    familyTagBtn.setTextColor(Color.parseColor("#424242"));
                }
            }
        });

        foodDrinkTagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedTags.size() == 3 && !(selectedTags.contains("FOODDRINK"))){
                    Toast.makeText(SelectEventCategoriesActivity.this, "Events Can Only Have a Max of 3 Tags", Toast.LENGTH_LONG).show();
                }
                else if (!(selectedTags.contains("FOODDRINK"))){
                    selectedTags.add("FOODDRINK");
                    foodDrinkTagBtn.setBackgroundResource(R.drawable.btn_dark_gray);
                    foodDrinkTagBtn.setTextColor(Color.parseColor("#ffffff"));
                }
                else {
                    selectedTags.remove("FOODDRINK");
                    foodDrinkTagBtn.setBackgroundResource(0);
                    foodDrinkTagBtn.setTextColor(Color.parseColor("#424242"));
                }
            }
        });

        gamingTagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedTags.size() == 3 && !(selectedTags.contains("GAMING"))){
                    Toast.makeText(SelectEventCategoriesActivity.this, "Events Can Only Have a Max of 3 Tags", Toast.LENGTH_LONG).show();
                }
                else if (!(selectedTags.contains("GAMING"))){
                    selectedTags.add("GAMING");
                    gamingTagBtn.setBackgroundResource(R.drawable.btn_dark_gray);
                    gamingTagBtn.setTextColor(Color.parseColor("#ffffff"));
                }
                else {
                    selectedTags.remove("GAMING");
                    gamingTagBtn.setBackgroundResource(0);
                    gamingTagBtn.setTextColor(Color.parseColor("#424242"));
                }
            }
        });

        healthTagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedTags.size() == 3 && !(selectedTags.contains("HEALTHFITNESS"))){
                    Toast.makeText(SelectEventCategoriesActivity.this, "Events Can Only Have a Max of 3 Tags", Toast.LENGTH_LONG).show();
                }
                else if (!(selectedTags.contains("HEALTHFITNESS"))){
                    selectedTags.add("HEALTHFITNESS");
                    healthTagBtn.setBackgroundResource(R.drawable.btn_dark_gray);
                    healthTagBtn.setTextColor(Color.parseColor("#ffffff"));
                }
                else {
                    selectedTags.remove("HEALTHFITNESS");
                    healthTagBtn.setBackgroundResource(0);
                    healthTagBtn.setTextColor(Color.parseColor("#424242"));
                }
            }
        });

        musicTagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedTags.size() == 3 && !(selectedTags.contains("MUSIC"))){
                    Toast.makeText(SelectEventCategoriesActivity.this, "Events Can Only Have a Max of 3 Tags", Toast.LENGTH_LONG).show();
                }
                else if (!(selectedTags.contains("MUSIC"))){
                    selectedTags.add("MUSIC");
                    musicTagBtn.setBackgroundResource(R.drawable.btn_dark_gray);
                    musicTagBtn.setTextColor(Color.parseColor("#ffffff"));
                }
                else {
                    selectedTags.remove("MUSIC");
                    musicTagBtn.setBackgroundResource(0);
                    musicTagBtn.setTextColor(Color.parseColor("#424242"));
                }
            }
        });

        networkingTagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedTags.size() == 3 && !(selectedTags.contains("NETWORKING"))){
                    Toast.makeText(SelectEventCategoriesActivity.this, "Events Can Only Have a Max of 3 Tags", Toast.LENGTH_LONG).show();
                }
                else if (!(selectedTags.contains("NETWORKING"))){
                    selectedTags.add("NETWORKING");
                    networkingTagBtn.setBackgroundResource(R.drawable.btn_dark_gray);
                    networkingTagBtn.setTextColor(Color.parseColor("#ffffff"));
                }
                else {
                    selectedTags.remove("NETWORKING");
                    networkingTagBtn.setBackgroundResource(0);
                    networkingTagBtn.setTextColor(Color.parseColor("#424242"));
                }
            }
        });

        outdoorsTagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedTags.size() == 3 && !(selectedTags.contains("OUTDOORS"))){
                    Toast.makeText(SelectEventCategoriesActivity.this, "Events Can Only Have a Max of 3 Tags", Toast.LENGTH_LONG).show();
                }
                else if (!(selectedTags.contains("OUTDOORS"))){
                    selectedTags.add("OUTDOORS");
                    outdoorsTagBtn.setBackgroundResource(R.drawable.btn_dark_gray);
                    outdoorsTagBtn.setTextColor(Color.parseColor("#ffffff"));
                }
                else {
                    selectedTags.remove("OUTDOORS");
                    outdoorsTagBtn.setBackgroundResource(0);
                    outdoorsTagBtn.setTextColor(Color.parseColor("#424242"));
                }
            }
        });

        partyDanceTagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedTags.size() == 3 && !(selectedTags.contains("PARTYDANCE"))){
                    Toast.makeText(SelectEventCategoriesActivity.this, "Events Can Only Have a Max of 3 Tags", Toast.LENGTH_LONG).show();
                }
                else if (!(selectedTags.contains("PARTYDANCE"))){
                    selectedTags.add("PARTYDANCE");
                    partyDanceTagBtn.setBackgroundResource(R.drawable.btn_dark_gray);
                    partyDanceTagBtn.setTextColor(Color.parseColor("#ffffff"));
                }
                else {
                    selectedTags.remove("PARTYDANCE");
                    partyDanceTagBtn.setBackgroundResource(0);
                    partyDanceTagBtn.setTextColor(Color.parseColor("#424242"));
                }
            }
        });

        shoppingTagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedTags.size() == 3 && !(selectedTags.contains("SHOPPING"))){
                    Toast.makeText(SelectEventCategoriesActivity.this, "Events Can Only Have a Max of 3 Tags", Toast.LENGTH_LONG).show();
                }
                else if (!(selectedTags.contains("SHOPPING"))){
                    selectedTags.add("SHOPPING");
                    shoppingTagBtn.setBackgroundResource(R.drawable.btn_dark_gray);
                    shoppingTagBtn.setTextColor(Color.parseColor("#ffffff"));
                }
                else {
                    selectedTags.remove("SHOPPING");
                    shoppingTagBtn.setBackgroundResource(0);
                    shoppingTagBtn.setTextColor(Color.parseColor("#424242"));
                }
            }
        });

        sportsTagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedTags.size() == 3 && !(selectedTags.contains("SPORTS"))){
                    Toast.makeText(SelectEventCategoriesActivity.this, "Events Can Only Have a Max of 3 Tags", Toast.LENGTH_LONG).show();
                }
                else if (!(selectedTags.contains("SPORTS"))){
                    selectedTags.add("SPORTS");
                    sportsTagBtn.setBackgroundResource(R.drawable.btn_dark_gray);
                    sportsTagBtn.setTextColor(Color.parseColor("#ffffff"));
                }
                else {
                    selectedTags.remove("SPORTS");
                    sportsTagBtn.setBackgroundResource(0);
                    sportsTagBtn.setTextColor(Color.parseColor("#424242"));
                }
            }
        });

        techTagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedTags.size() == 3 && !(selectedTags.contains("TECHNOLOGY"))){
                    Toast.makeText(SelectEventCategoriesActivity.this, "Events Can Only Have a Max of 3 Tags", Toast.LENGTH_LONG).show();
                }
                else if (!(selectedTags.contains("TECHNOLOGY"))){
                    selectedTags.add("TECHNOLOGY");
                    techTagBtn.setBackgroundResource(R.drawable.btn_dark_gray);
                    techTagBtn.setTextColor(Color.parseColor("#ffffff"));
                }
                else {
                    selectedTags.remove("TECHNOLOGY");
                    techTagBtn.setBackgroundResource(0);
                    techTagBtn.setTextColor(Color.parseColor("#424242"));
                }
            }
        });

        theatreTagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedTags.size() == 3 && !(selectedTags.contains("THEATRE"))){
                    Toast.makeText(SelectEventCategoriesActivity.this, "Events Can Only Have a Max of 3 Tags", Toast.LENGTH_LONG).show();
                }
                else if (!(selectedTags.contains("THEATRE"))){
                    selectedTags.add("THEATRE");
                    theatreTagBtn.setBackgroundResource(R.drawable.btn_dark_gray);
                    theatreTagBtn.setTextColor(Color.parseColor("#ffffff"));
                }
                else {
                    selectedTags.remove("THEATRE");
                    theatreTagBtn.setBackgroundResource(0);
                    theatreTagBtn.setTextColor(Color.parseColor("#424242"));
                }
            }
        });

        wineBrewTagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedTags.size() == 3 && !(selectedTags.contains("WINEBREW"))){
                    Toast.makeText(SelectEventCategoriesActivity.this, "Events Can Only Have a Max of 3 Tags", Toast.LENGTH_LONG).show();
                }
                else if (!(selectedTags.contains("WINEBREW"))){
                    selectedTags.add("WINEBREW");
                    wineBrewTagBtn.setBackgroundResource(R.drawable.btn_dark_gray);
                    wineBrewTagBtn.setTextColor(Color.parseColor("#ffffff"));
                }
                else {
                    selectedTags.remove("WINEBREW");
                    wineBrewTagBtn.setBackgroundResource(0);
                    wineBrewTagBtn.setTextColor(Color.parseColor("#424242"));
                }
            }
        });

        //SEND DATA BACK
        setTagsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedTags.isEmpty()){
                    Toast.makeText(SelectEventCategoriesActivity.this, "Please Choose At Least One Tag", Toast.LENGTH_LONG).show();
                } else {
                    String [] tagArray = selectedTags.toArray(new String[selectedTags.size()]);
                    Intent returnTagDataIntent = new Intent();
                    returnTagDataIntent.putExtra("selectedTags", tagArray);
                    setResult(RESULT_OK, returnTagDataIntent);
                    finish();
                }

            }
        });

    }
}
