package com.webblen.events.webblen;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class CreateEventActivity extends AppCompatActivity {

    //Data Base

    //New Event Form
    private EditText eventTitleText;
    private String eventTitle;
    private EditText eventDescriptionText;
    private String eventDescription;
    private ImageButton addImgBtn;
    private Image eventPhoto;
    private Button dateTimeBtn;
    private String eventDateTime;
    private Button locationBtn;
    private String eventAddress;
    private Button filterBtn;
    private Boolean event18 = false;
    private Boolean event21 = false;
    private Boolean notificationOnly = false;
    private Button catBtn;
    private String[] eventCategories;
    private TextView eventPriceText;
    private String eventPrice;
    private ImageButton priceInfoBtn;

    private Button reviewEventBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        //Initialize
        eventTitleText = (EditText) findViewById(R.id.eventTitleText);
        eventDescriptionText = (EditText) findViewById(R.id.eventDescriptionText);
        addImgBtn = (ImageButton) findViewById(R.id.addImgBtn);
        dateTimeBtn = (Button) findViewById(R.id.dateTimeBtn);
        locationBtn = (Button) findViewById(R.id.locationBtn);
        filterBtn = (Button) findViewById(R.id.filterBtn);
        catBtn = (Button) findViewById(R.id.catBtn);
        eventPriceText = (TextView) findViewById(R.id.eventPriceText);
        priceInfoBtn = (ImageButton) findViewById(R.id.priceInfoBtn);
        reviewEventBtn = (Button) findViewById(R.id.reviewEventBtn);

        //Btn Click Listeners
        addImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        dateTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dateTimeIntent = new Intent(CreateEventActivity.this, SelectDateTimeActivity.class);
                startActivity(dateTimeIntent);
            }
        });

        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent locationIntent = new Intent(CreateEventActivity.this, SelectLocationActivity.class);
                startActivity(locationIntent);
            }
        });

        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent filterIntent = new Intent(CreateEventActivity.this, SelectEventFilterActivity.class);
                startActivity(filterIntent);
            }
        });

        catBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent categoriesIntent = new Intent(CreateEventActivity.this, SelectEventCategoriesActivity.class);
                startActivity(categoriesIntent);
            }
        });

        priceInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent priceInfoIntent = new Intent(CreateEventActivity.this, EventPricingActivity.class);
                startActivity(priceInfoIntent);
            }
        });

        reviewEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reviewIntent = new Intent(CreateEventActivity.this, PurchaseEventActivity.class);
                startActivity(reviewIntent);
            }
        });
    }
}
