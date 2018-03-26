package com.webblen.events.webblen;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.twitter.sdk.android.core.SessionManager;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;

import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    //Firebase
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String user_id;

    //Menu
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle menuToggle;
    private View menuHeader;


    //Image & Name
    private CircleImageView userMainPic;
    private Uri mainImageURI = null;
    private TextView usernameMainText;
    private TextView accountValText;

    private ProgressBar menuProgressBar;

    //Map
    private GoogleMap mMap;
    private GoogleApiClient client;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker currentLocationMarker;
    private static final int REQUEST_LOCATION_CODE = 99;
    int PROXIMITY_RADIUS = 10000;
    double latitude,longitude;

    private ProgressBar mapProgressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_action_bar);
        setSupportActionBar(toolbar);

        //Action button for going to create activity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.eventTableFAB);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent eventTableIntent = new Intent(MainActivity.this, EventTableActivity.class);
                startActivity(eventTableIntent);
            }
        });

        //** INITIALIZE

        //Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        //Menu
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        menuToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_menu, R.string.close_menu);
        menuHeader = navigationView.getHeaderView(0);
        userMainPic = (CircleImageView) menuHeader.findViewById(R.id.userMainPic);
        usernameMainText = (TextView) menuHeader.findViewById(R.id.usernameMainText);
        accountValText = (TextView) menuHeader.findViewById(R.id.accountValText);
        menuProgressBar = (ProgressBar) menuHeader.findViewById(R.id.menuProgressBar);


        drawerLayout.addDrawerListener(menuToggle);
        menuToggle.syncState();
        getSupportActionBar().setTitle("Webblen");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case(R.id.interestsMenuButton):
                        Intent interestsIntent = new Intent(MainActivity.this, InterestsActivity.class);
                        drawerLayout.closeDrawers();
                        startActivity(interestsIntent);
                        break;
                    case(R.id.createMenuButton):
                        Intent createEventIntent = new Intent(MainActivity.this, CreateEventActivity.class);
                        drawerLayout.closeDrawers();
                        startActivity(createEventIntent);
                        break;
                    case(R.id.myEventsMenuButton):
                        Intent myEventsIntent = new Intent(MainActivity.this, MyEventsActivity.class);
                        drawerLayout.closeDrawers();
                        startActivity(myEventsIntent);
                        break;
                    case(R.id.walletMenuOption):
                        Intent walletIntent = new Intent(MainActivity.this, WalletActivity.class);
                        drawerLayout.closeDrawers();
                        startActivity(walletIntent);
                        break;
                    case(R.id.settingsMenuButton):
                        Intent settingsIntent = new Intent(MainActivity.this, AccountSettingsActivity.class);
                        drawerLayout.closeDrawers();
                        startActivity(settingsIntent);
                        break;
                    case(R.id.contactMenuButton):
                        Intent intent = new Intent(MainActivity.this, ContactUsActivity.class);
                        drawerLayout.closeDrawers();
                        startActivity(intent);
                        break;
                    case(R.id.logoutMenuButton):
                        drawerLayout.closeDrawers();
                        firebaseAuth.signOut();
                        LoginManager.getInstance().logOut();
                        SessionManager<TwitterSession> sessionManager = TwitterCore.getInstance().getSessionManager();
                        if (sessionManager.getActiveSession() != null){
                            sessionManager.clearActiveSession();
                        }
                        logoutUser();
                        break;
                }
                return true;
            }
        });

        loadFirestoreData();

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//            if (checkLocationPermission()){
//
//            }
//        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    //*** ENABLE MENU
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(menuToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    //*** GOOGLE MAPS METHODS
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case REQUEST_LOCATION_CODE:
                if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) !=  PackageManager.PERMISSION_GRANTED)
                    {
                        if(client == null)
                        {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }
                else {
                    Toast.makeText(this,"Permission Denied" , Toast.LENGTH_LONG).show();
                }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Set map and find user_profile location
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));


    }

    protected synchronized void buildGoogleApiClient(){

        client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        client.connect();

    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;
        if (currentLocationMarker != null){
            currentLocationMarker.remove();
        }

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

        currentLocationMarker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(10));

        if (client != null){
            LocationServices.FusedLocationApi.removeLocationUpdates(client, this);
        }

    }

    public boolean checkLocationPermission(){

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)  != PackageManager.PERMISSION_GRANTED ) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION },REQUEST_LOCATION_CODE);
            }
            else {
                ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION },REQUEST_LOCATION_CODE);
            }
            return false;
        }
        else
            return true;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        locationRequest = new LocationRequest();

        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    //Load User Data
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
                            Intent setupIntent = new Intent(MainActivity.this, SetupActivity.class);
                            startActivity(setupIntent);
                            finish();
                        } else {

                            mainImageURI = Uri.parse(profile_pic);
                            usernameMainText.setText("@" + username);
                            Glide.with(MainActivity.this).load(profile_pic).into(userMainPic);

                            userMainPic.setVisibility(View.VISIBLE);
                            usernameMainText.setVisibility(View.VISIBLE);
                            accountValText.setVisibility(View.VISIBLE);
                            menuProgressBar.setVisibility(View.INVISIBLE);

                        }
                    }

                } else {

                    String error = task.getException().getMessage();
                    Toast.makeText(MainActivity.this, "Load Error: " + error, Toast.LENGTH_LONG).show();

                }

            }
        });
    }

    //Load Event Data
//    private void loadEventData() {
//
//        firebaseFirestore.collection("users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//
//                if(task.isSuccessful()){
//
//                    if(task.getResult().exists()){
//
//                        String username = task.getResult().getString("username");
//                        String profile_pic = task.getResult().getString("profile_pic");
//
//                        //If username or pic is null...
//                        if (username == null || profile_pic == null){
//                            Intent setupIntent = new Intent(MainActivity.this, SetupActivity.class);
//                            startActivity(setupIntent);
//                            finish();
//                        } else {
//
//                            mainImageURI = Uri.parse(profile_pic);
//                            usernameMainText.setText("@" + username);
//                            Glide.with(MainActivity.this).load(profile_pic).into(userMainPic);
//
//                            userMainPic.setVisibility(View.VISIBLE);
//                            usernameMainText.setVisibility(View.VISIBLE);
//                            menuProgressBar.setVisibility(View.INVISIBLE);
//
//                        }
//                    }
//
//                } else {
//
//                    String error = task.getException().getMessage();
//                    Toast.makeText(MainActivity.this, "Load Error: " + error, Toast.LENGTH_LONG).show();
//
//                }
//
//            }
//        });
//    }

    //** Authentication Methods and Handling
    @Override
    public void onStart(){
        //Check if Firebase user is signed in and act accordingly
        super.onStart();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser == null){
            logoutUser();
        }

    }

    private void logoutUser(){
        Intent logoutIntent = new Intent(MainActivity.this, OnboardingActivity.class);
        startActivity(logoutIntent);
        finish();
    }

}
