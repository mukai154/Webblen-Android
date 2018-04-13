package com.webblen.events.webblen.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.webblen.events.webblen.R;
import com.webblen.events.webblen.Utilities;
import com.webblen.events.webblen.Objects.WebblenEvent;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import id.zelory.compressor.Compressor;

public class PurchaseEventActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler{

    //Webblen Event
    private WebblenEvent newEvent;
    private Uri mainImgURI;
    private Bitmap compressedImageFile;

    //Billing
    BillingProcessor bp;

    //Firebase
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String user_id;

    private ImageView previewPhoto;
    private TextView previewUsername;
    private ImageView previewCat;
    private TextView previewTitle;
    private TextView previewDesc;
    private TextView previewMonth;
    private TextView previewDayofMonth;
    private TextView previewYear;
    private TextView previewRadius;
    private int eventRadius;
    private TextView eventPrice;
    private ProgressBar purchaseProgressBar;
    private Button confirmPurchaseBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_event);

        newEvent = (WebblenEvent)getIntent().getSerializableExtra("WebblenEvent");
        mainImgURI = Uri.parse(getIntent().getStringExtra("mainImageUri"));

        //** INITIALIZE

        //Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        //Billing
        bp = new BillingProcessor(this, getString(R.string.google_license), this);

        //Other UI
        previewPhoto = (ImageView) findViewById(R.id.previewPhoto);
        previewPhoto.setImageURI(mainImgURI);
        previewUsername = (TextView) findViewById(R.id.previewUsername);
        final String username = "@" + newEvent.getAuthor();
        previewUsername.setText(username);
        previewCat = (ImageView) findViewById(R.id.previewCat);
        String interestImgName = newEvent.getCategories().get(0).toUpperCase();
        previewCat.setImageDrawable(getDrawable(Utilities.categoryToDrawable(interestImgName)));
        previewTitle = (TextView) findViewById(R.id.previewTitle);
        previewTitle.setText(newEvent.getTitle());
        previewDesc = (TextView) findViewById(R.id.previewDescription);
        previewDesc.setText(newEvent.getDescription());

        //Dates
        String[] dateParts = newEvent.getDate().split("/");
        String month = Utilities.getMonth(dateParts[2]);
        previewDayofMonth = (TextView) findViewById(R.id.previewDay);
        previewDayofMonth.setText(dateParts[1]);
        previewMonth = (TextView) findViewById(R.id.previewMonth);
        previewMonth.setText(month);
        previewYear = (TextView) findViewById(R.id.previewYear);
        previewYear.setText(dateParts[2]);

        previewRadius = (TextView) findViewById(R.id.eventRadius);
        String notifyString = "Notify Those Within " + eventRadius + " Meters";
        previewRadius.setText(notifyString);
        eventPrice = (TextView) findViewById(R.id.eventPrice);
        String price = "Total: $" + Utilities.getPrice(eventRadius);
        eventPrice.setText(price);
        purchaseProgressBar = (ProgressBar) findViewById(R.id.purchaseProgressBar);
        confirmPurchaseBtn = (Button) findViewById(R.id.confirmPurchaseBtn);

        confirmPurchaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (username.contentEquals("Webblen") || username.contentEquals("webblen") || username.contentEquals("mukaiss")){
                    postEvent();
                } else if (eventRadius == 250){
                    bp.purchase(PurchaseEventActivity.this, "webblen.event250");
                } else if (eventRadius > 250 && eventRadius < 400){
                    bp.purchase(PurchaseEventActivity.this, "webblen.event375");
                } else if (eventRadius > 375 && eventRadius < 600){
                    bp.purchase(PurchaseEventActivity.this, "webblen.event575");
                } else if (eventRadius > 375 && eventRadius < 600){
                    bp.purchase(PurchaseEventActivity.this, "webblen.event975");
                } else if (eventRadius > 375 && eventRadius < 600){
                    bp.purchase(PurchaseEventActivity.this, "webblen.event1975");
                } else if (eventRadius > 375 && eventRadius < 600){
                    bp.purchase(PurchaseEventActivity.this, "webblen.event3075");
                } else if (eventRadius > 375 && eventRadius < 600){
                    bp.purchase(PurchaseEventActivity.this, "webblen.event5975");
                } else if (eventRadius > 375 && eventRadius < 600){
                    bp.purchase(PurchaseEventActivity.this, "webblen.event8475");
                } else if (eventRadius > 375 && eventRadius < 600){
                    bp.purchase(PurchaseEventActivity.this, "webblen.event10000");
                } else {
                    Toast.makeText(PurchaseEventActivity.this, "Could Not Retrieve Product", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // IBillingHandler implementation

    @Override
    public void onBillingInitialized() {

    }

    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        postEvent();
    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
        Toast.makeText(PurchaseEventActivity.this, "Payment Cancelled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPurchaseHistoryRestored() {
    /*
    * Called when purchase history was restored and the list of all owned PRODUCT ID's
    * was loaded from Google Play
    */
    }

    private void postEvent(){
        //Set Username & Profile Pic Path
        final Map<String, Object> eventDocData = new HashMap<>();
        eventDocData.put("address", newEvent.getAddress());
        eventDocData.put("author", newEvent.getAuthor());
        eventDocData.put("categories", newEvent.getCategories());
        eventDocData.put("date", newEvent.getDate());
        eventDocData.put("description", newEvent.getDescription());
        eventDocData.put("distanceFromUser", 0);
        eventDocData.put("event18", false);
        eventDocData.put("event21", false);
        eventDocData.put("eventKey", newEvent.getEventKey());
        eventDocData.put("lat", newEvent.getLat());
        eventDocData.put("lon", newEvent.getLon());
        eventDocData.put("notificationOnly", null);
        eventDocData.put("paid", true);
        eventDocData.put("radius", null);
        eventDocData.put("time", null);

        //Compress and Upload
        purchaseProgressBar.setVisibility(View.VISIBLE);

        //Compress Image
        File profileImgFile = new File(mainImgURI.getPath());
        try {
            compressedImageFile = new Compressor(PurchaseEventActivity.this).setQuality(85).compressToBitmap(profileImgFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imgData = baos.toByteArray();

        //Upload Img & Name
        UploadTask setupUpload = storageReference.child("events").child(newEvent.getEventKey() + ".jpg").putBytes(imgData);

        setupUpload.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri download_uri = taskSnapshot.getDownloadUrl();
                eventDocData.put("pathToImage", download_uri);

                //Send to Firebase
                firebaseFirestore.collection("events").document(newEvent.getEventKey())
                        .set(eventDocData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(PurchaseEventActivity.this, "Event Posted!", Toast.LENGTH_SHORT).show();
                                Intent mainIntent = new Intent(PurchaseEventActivity.this, MainActivity.class);
                                startActivity(mainIntent);
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                purchaseProgressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(PurchaseEventActivity.this, "There Was an Issue Posting Your Event. Please Contact Support.", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String error = e.getMessage();
                Toast.makeText(PurchaseEventActivity.this, "Posting Error. Please Contact Support.", Toast.LENGTH_LONG).show();

                purchaseProgressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}
