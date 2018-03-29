package com.webblen.events.webblen;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.net.URI;

import de.hdodenhof.circleimageview.CircleImageView;

public class PurchaseEventActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler{

    //Webblen Event
    private WebblenEvent newEvent;
    private Uri mainImgURI;
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
        String username = "@" + newEvent.getAuthor();
        previewUsername.setText(username);
        previewCat = (ImageView) findViewById(R.id.previewCat);
        String interestImgName = newEvent.getCategories().get(0).toUpperCase();
        previewCat.setImageDrawable(getDrawable(categoryToDrawable(interestImgName)));
        previewTitle = (TextView) findViewById(R.id.previewTitle);
        previewTitle.setText(newEvent.getTitle());
        previewDesc = (TextView) findViewById(R.id.previewDescription);
        previewDesc.setText(newEvent.getDescription());

        //Dates
        String[] dateParts = newEvent.getDate().split("/");
        String month = getMonth(dateParts[2]);
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
        String price = "Total: $" + getPrice(eventRadius);
        eventPrice.setText(price);
        purchaseProgressBar = (ProgressBar) findViewById(R.id.purchaseProgressBar);
        confirmPurchaseBtn = (Button) findViewById(R.id.confirmPurchaseBtn);

        confirmPurchaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (eventRadius == 250){
                    //bp.purchase(PurchaseEventActivity.this, "android.test.purchased");
                } else if (eventRadius > 250 && eventRadius < 400){
                    //bp.purchase(PurchaseEventActivity.this, "android.test.purchased");
                } else if (eventRadius > 375 && eventRadius < 600){
                    //bp.purchase(PurchaseEventActivity.this, "android.test.purchased");
                } else if (eventRadius > 375 && eventRadius < 600){
                    //bp.purchase(PurchaseEventActivity.this, "android.test.purchased");
                } else if (eventRadius > 375 && eventRadius < 600){
                    //bp.purchase(PurchaseEventActivity.this, "android.test.purchased");
                } else if (eventRadius > 375 && eventRadius < 600){
                    //bp.purchase(PurchaseEventActivity.this, "android.test.purchased");
                } else if (eventRadius > 375 && eventRadius < 600){
                    //bp.purchase(PurchaseEventActivity.this, "android.test.purchased");
                } else if (eventRadius > 375 && eventRadius < 600){
                    //bp.purchase(PurchaseEventActivity.this, "android.test.purchased");
                } else if (eventRadius > 375 && eventRadius < 600){
                    //bp.purchase(PurchaseEventActivity.this, "android.test.purchased");
                } else {
                    bp.purchase(PurchaseEventActivity.this, "android.test.purchased");
                }
            }
        });
    }

    private String getPrice(int eventRadius) {
        if (eventRadius == 250){
            return "2.99";
        } else if (eventRadius > 250 && eventRadius < 400){
            return "4.99";
        } else if (eventRadius > 375 && eventRadius < 600){
            return "9.99";
        } else if (eventRadius > 375 && eventRadius < 600){
            return "14.99";
        } else if (eventRadius > 375 && eventRadius < 600){
            return "19.99";
        } else if (eventRadius > 375 && eventRadius < 600){
            return "24.99";
        } else if (eventRadius > 375 && eventRadius < 600){
            return "26.99";
        } else if (eventRadius > 375 && eventRadius < 600){
            return "29.99";
        } else if (eventRadius > 375 && eventRadius < 600){
            return "34.99";
        } else {
            return "0.00";
        }
    }

    private String getMonth(String datePart) {
        switch (datePart){
            case "01":
                return "JAN";
            case "02":
                return "FEB";
            case "03":
                return "MAR";
            case "04":
                return "APR";
            case "05":
                return "MAY";
            case "06":
                return "JUN";
            case "07":
                return "JUL";
            case "08":
                return "AUG";
            case "09":
                return "SEP";
            case "10":
                return "OCT";
            case "11":
                return "NOV";
            case "12":
                return "DEC";
            default:
                return "MAR";
        }
    }

    //Get Drawable from Name
    public static int categoryToDrawable(String category){

        switch (category){
            case "ART":
                return R.drawable.art;
            case "MUSIC":
                return R.drawable.music;
            case "FOODDRINK":
                return R.drawable.fooddrink;
            case "SPORTS":
                return R.drawable.sports;
            case "COMMUNITY":
                return R.drawable.community;
            case "HEALTHFITNESS":
                return R.drawable.healthfitness;
            case "FAMILY":
                return R.drawable.family;
            case "ENTERTAINMENT":
                return R.drawable.entertainment;
            case "TECHNOLOGY":
                return R.drawable.technology;
            case "OUTDOORS":
                return R.drawable.outdoors;
            case "GAMING":
                return R.drawable.gaming;
            case "COMPETITION":
                return R.drawable.competition;
            case "NETWORKING":
                return R.drawable.networking;
            case "THEATRE":
                return R.drawable.theatre;
            case "CULTURE":
                return R.drawable.culture;
            case "SHOPPING":
                return R.drawable.shopping;
            case "AMUSEMENT":
                return R.drawable.amusement;
            case "EDUCATION":
                return R.drawable.education;
            case "COLLEGELIFE":
                return R.drawable.collegelife;
            case "WINEBREW":
                return R.drawable.winebrew;
            case "PARTYDANCE":
                return R.drawable.partydance;
            default:
                return R.drawable.amusement;
        }

    }

    // IBillingHandler implementation

    @Override
    public void onBillingInitialized() {
    /*
    * Called when BillingProcessor was initialized and it's ready to purchase
    */
    }

    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
    /*
    * Called when requested PRODUCT ID was successfully purchased
    */
    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
    /*
    * Called when some error occurred. See Constants class for more details
    *
    * Note - this includes handling the case where the user canceled the buy dialog:
    * errorCode = Constants.BILLING_RESPONSE_RESULT_USER_CANCELED
    */
    }

    @Override
    public void onPurchaseHistoryRestored() {
    /*
    * Called when purchase history was restored and the list of all owned PRODUCT ID's
    * was loaded from Google Play
    */
    }

}
