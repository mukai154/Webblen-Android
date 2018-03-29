package com.webblen.events.webblen;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import id.zelory.compressor.Compressor;

public class CreateEventActivity extends AppCompatActivity {


    //Firebase
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    //--user data
    private String user_id;
    private String current_username;
    private boolean isVerified;

    //Image
    private Uri mainImageURI = null;
    private static final int MAX_LENGTH = 100;
    private Compressor imgCompressor;

    //New Event Form
    private EditText eventTitleText;
    private String eventTitle;
    private EditText eventDescriptionText;
    private String eventDescription;
    private ImageButton addImgBtn;
    private boolean didAddImg = false;
    private Button dateTimeBtn;
    private String eventDate;
    private String eventTime;
    private Button locationBtn;
    private String eventAddress;
    private Double eventLat;
    private Double eventLon;
    private Integer radius;
    private Button filterBtn;
    private Boolean event18 = false;
    private Boolean event21 = false;
    private Boolean notificationOnly = false;
    private Button catBtn;
    private ArrayList<String> eventCategories;
    private String listCategories;
    private TextView eventPriceText;
    private String eventPrice;
    private ImageButton priceInfoBtn;

    private Button reviewEventBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        //*** Initialize

        //Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();

        //UI
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

        loadUserData();

        //Btn Click Listeners
        addImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startImagePicker();
            }
        });

        dateTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dateTimeIntent = new Intent(CreateEventActivity.this, SelectDateTimeActivity.class);
                startActivityForResult(dateTimeIntent, 1);
            }
        });

        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent locationIntent = new Intent(CreateEventActivity.this, SelectLocationActivity.class);
                startActivityForResult(locationIntent, 2);
            }
        });

        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent filterIntent = new Intent(CreateEventActivity.this, SelectEventFilterActivity.class);
                startActivityForResult(filterIntent, 3);
            }
        });

        catBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent categoriesIntent = new Intent(CreateEventActivity.this, SelectEventCategoriesActivity.class);
                startActivityForResult(categoriesIntent, 4);
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

                String address = locationBtn.getText().toString();
                String author = current_username;
                ArrayList<String> categories = eventCategories;
                String date = eventDate;
                String description = eventDescriptionText.getText().toString();
                Integer distanceFromUser = 0;
                String eventKey = UUID.randomUUID().toString();
                Double lat = eventLat;
                Double lon = eventLon;
                boolean paid = false;
                String pathToImage = "";
                String title = eventTitleText.getText().toString();
                Integer views = 0;

                if (address.toLowerCase().contains("location")){
                    Toast.makeText(CreateEventActivity.this, "Please Set a Location", Toast.LENGTH_LONG).show();
                } else if (categories == null){
                    Toast.makeText(CreateEventActivity.this, "Please Set Categories", Toast.LENGTH_LONG).show();
                } else if (eventDate == null){
                    Toast.makeText(CreateEventActivity.this, "Please Set Date", Toast.LENGTH_LONG).show();
                } else if (description == null || description.isEmpty()){
                    Toast.makeText(CreateEventActivity.this, "Please Set Description", Toast.LENGTH_LONG).show();
                } else if (description.length() < 20){
                    Toast.makeText(CreateEventActivity.this, "Please Be More Descriptive About the Event", Toast.LENGTH_LONG).show();
                } else if (lat == null || lon == null){
                    Toast.makeText(CreateEventActivity.this, "Please Set a Location", Toast.LENGTH_LONG).show();
                } else if (title == null || title.isEmpty()){
                    Toast.makeText(CreateEventActivity.this, "Please Set a Title", Toast.LENGTH_LONG).show();
                } else if (!didAddImg){
                    Toast.makeText(CreateEventActivity.this, "Please Add an Image", Toast.LENGTH_LONG).show();
                } else {

                    WebblenEvent newEvent = new WebblenEvent(address, author, categories,
                            date, description, distanceFromUser,
                            event18, event21, eventKey,
                            lat, lon, notificationOnly,
                            pathToImage, radius, eventTime,
                            title, views);

                    Intent reviewIntent = new Intent(CreateEventActivity.this, PurchaseEventActivity.class);
                    reviewIntent.putExtra("mainImageUri", mainImageURI.toString());
                    reviewIntent.putExtra("WebblenEvent", newEvent);
                    startActivity(reviewIntent);
                }
            }
        });
    }

    private void loadUserData() {
        firebaseFirestore.collection("users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){

                    if(task.getResult().exists()){

                        current_username = task.getResult().getString("username");
                        isVerified = task.getResult().getBoolean("isVerified");
                        //If username or pic is null...
                        if (current_username == null){
                            Intent setupIntent = new Intent(CreateEventActivity.this, SetupActivity.class);
                            startActivity(setupIntent);
                            finish();
                        }
                    }
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(CreateEventActivity.this, "Load Error: " + error, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void startImagePicker(){
        // start picker to get image for cropping and then use the image in cropping activity
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .start(CreateEventActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mainImageURI = result.getUri();
                addImgBtn.setImageURI(mainImageURI);
                didAddImg = true;
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                eventDate = data.getStringExtra("date");
                eventTime = data.getStringExtra("time");
                dateTimeBtn.setText(eventDate + " | " + eventTime);
            }
        }

        if (requestCode == 2) {
            if(resultCode == RESULT_OK) {
                eventAddress = data.getStringExtra("address");
                radius = data.getIntExtra("radius", 0);
                eventLat = data.getDoubleExtra("lat", 0);
                eventLon = data.getDoubleExtra("lon", 0);
                locationBtn.setText(eventAddress + " | " + String.valueOf(radius) + " meters");
            }
        }

        if (requestCode == 3) {
            if(resultCode == RESULT_OK) {
                event18 = data.getBooleanExtra("event18", false);
                event21 = data.getBooleanExtra("event21", false);
                notificationOnly = data.getBooleanExtra("notifOnly", false);
                String filters;
                if (event18 && !event21 && !notificationOnly){
                    filters = "18+ Event";
                } else if (event18 && !event21 && notificationOnly){
                    filters = "18+ Event, Notifications Only";
                } else if (event21 && !notificationOnly){
                    filters = "21+ Event";
                } else if (event21 && notificationOnly){
                    filters = "21+ Event, Notifications Only";
                } else {
                    filters = "Filter Settings";
                }

                filterBtn.setText(filters);
            }
        }

        if (requestCode == 4) {
            if(resultCode == RESULT_OK) {
                eventCategories = data.getStringArrayListExtra("selectedTags");
                int catLength = eventCategories.size();
                for (int i = 0; i < catLength; i++) {
                    String indexCat = eventCategories.get(i);
                    if (indexCat.contains("COLLEGELIFE")){
                        indexCat = "COLLEGE LIFE";
                    } else if (indexCat.contains("FOODDRINK")){
                        indexCat = "FOOD & DRINK";
                    } else if (indexCat.contains("HEALTHFITNESS")){
                        indexCat = "HEALTH & FITNESS";
                    } else if (indexCat.contains("PARTYDANCE")){
                        indexCat = "PARTY/DANCE";
                    } else if (indexCat.contains("WINEBREW")){
                        indexCat = "WINE & BREW";
                    }

                    if (i == 0){
                        listCategories = indexCat;
                    } else {
                        listCategories = listCategories + ", " + indexCat;
                    }
                }

                catBtn.setText(listCategories);

            }
        }
    }

    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(MAX_LENGTH);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }
}
