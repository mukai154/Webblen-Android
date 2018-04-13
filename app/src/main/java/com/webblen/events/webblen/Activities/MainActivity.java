package com.webblen.events.webblen.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
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
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
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
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.webblen.events.webblen.R;
import com.webblen.events.webblen.Utilities;
import com.webblen.events.webblen.Objects.WebblenEvent;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    //Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String user_id;
    private ArrayList<String> userData;
    private List<String> userInterests = new ArrayList<>();

    //--Events
    private boolean eventsToday = true;
    private boolean eventsTomorrow = false;
    private boolean eventsThisWeek = false;
    private boolean eventsThisMonth = false;
    private boolean eventsLater = false;
    private List<WebblenEvent> todayEventList = new ArrayList<>();
    private List<WebblenEvent> tomorrowEventList = new ArrayList<>();
    private List<WebblenEvent> thisWeekEventList = new ArrayList<>();
    private List<WebblenEvent> thisMonthEventList = new ArrayList<>();
    private List<WebblenEvent> laterEventList = new ArrayList<>();
    private DateFormat sourceFormat = new SimpleDateFormat("MM/dd/yyyy");
    private Date eventDate;
    private Date currentDate = new Date();

    //Event Tabs
    private ConstraintLayout navBarBtm;
    private ImageButton todayBtn;
    private ImageButton tomorrowBtn;
    private ImageButton thisWeekBtn;
    private ImageButton thisMnthBtn;
    private ImageButton laterBtn;

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
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private boolean locationGranted = false;
    private Location lastLocation;
    private Location currentLocation;
    private Marker currentLocationMarker;
    private static final int REQUEST_LOCATION_CODE = 99;
    int PROXIMITY_RADIUS = 10000;
    double latitude,longitude;
    private WebblenEvent closestToday;
    private WebblenEvent closestTomorrow;
    private WebblenEvent closestThisWeek;
    private WebblenEvent closestThisMonth;
    private WebblenEvent closestLater;

    private ProgressBar mapProgressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_action_bar);
        setSupportActionBar(toolbar);

        //Configure Twitter SDK
        TwitterAuthConfig authConfig = new TwitterAuthConfig(
                getString(R.string.twitter_key),
                getString(R.string.twitter_secret));

        TwitterConfig twitterConfig = new TwitterConfig.Builder(this)
                .twitterAuthConfig(authConfig)
                .build();

        Twitter.initialize(twitterConfig);

        //Action button for going to create activity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.eventTableFAB);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent eventTableIntent = new Intent(MainActivity.this, EventTableActivity.class);
                startActivity(eventTableIntent);
            }
        });

        checkLocationPermission();
        getLastUserLocation();

        //** INITIALIZE

        //Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();


        //UI
        navBarBtm = (ConstraintLayout) findViewById(R.id.navBottom);
        mapProgressBar = (ProgressBar) findViewById(R.id.mapProgress);
        //Tabs
        todayBtn = (ImageButton) findViewById(R.id.todayBtn);
        tomorrowBtn = (ImageButton) findViewById(R.id.tomorrowBtn);
        thisWeekBtn = (ImageButton) findViewById(R.id.thisWeekBtn);
        thisMnthBtn = (ImageButton) findViewById(R.id.thisMonthBtn);
        laterBtn = (ImageButton) findViewById(R.id.laterBtn);

        loadFirestoreData();

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
//                        Intent myEventsIntent = new Intent(MainActivity.this, MyEventsActivity.class);
//                        drawerLayout.closeDrawers();
//                        startActivity(myEventsIntent);
                        Toast.makeText(MainActivity.this, "Temporarily Unavailable", Toast.LENGTH_SHORT).show();
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
//                        Intent intent = new Intent(MainActivity.this, ContactUsActivity.class);
//                        drawerLayout.closeDrawers();
//                        startActivity(intent);
                        String url = "https://www.webblen.io";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                        break;
                    case(R.id.logoutMenuButton):
                        drawerLayout.closeDrawers();
                        firebaseAuth.signOut();
                        LoginManager.getInstance().logOut();
                        TwitterCore.getInstance().getSessionManager().clearActiveSession();
                        logoutUser();
                        break;
                }
                return true;
            }
        });


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

    private void initMap(){
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("CALLED REQUEST", "onRequestPermissionsResult: called.");
        locationGranted = false;

        switch(requestCode){
            case REQUEST_LOCATION_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            locationGranted = false;
                            Toast.makeText(MainActivity.this, "Access to Location Denied", Toast.LENGTH_SHORT).show();
                            //Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    //Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    locationGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        mMap = googleMap;

        if (locationGranted) {
            getLastUserLocation();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
        }
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                startActivityFromKey(marker.getSnippet());
                return false;
            }
        });
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

        if (client != null){
            LocationServices.FusedLocationApi.removeLocationUpdates(client, this);
        }

    }

    @Override
    public void onConnected(Bundle bundle){

    }

    private void getLastUserLocation(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try{
            if(locationGranted){

                final Task location = fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d("LOCATION STATUS", "onComplete: found location!");
                            currentLocation = (Location) task.getResult();
                            if (currentLocation != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude())));
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
                            }

                        }else{
                            //Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(MainActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e("ERROR", "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }

    public void checkLocationPermission(){

        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                locationGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        REQUEST_LOCATION_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    REQUEST_LOCATION_CODE);
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

        Log.d("Firestore: ", "Loading Events");
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

                        userData = (ArrayList<String>) task.getResult().getData().get("interests");
                        Log.d("USER DATA: ", userData.toString());

                        Log.d("USER DATA: ",userInterests.toString());
                        firebaseFirestore.collection("events").whereEqualTo("paid", true).addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                                    if (doc.getType() == DocumentChange.Type.ADDED) {
                                        WebblenEvent webblenEvent = doc.getDocument().toObject(WebblenEvent.class);
                                        for (String interest : userData) {
                                            ArrayList<String> eventCategories = webblenEvent.getCategories();
                                            if (eventCategories.contains(interest)){
                                                Log.d("ADDING EVENT", "performOrganzieByDate!");
                                                organizeByDate(webblenEvent);
                                            }
                                        }

                                    }
                                }
                            }
                        });
                        Log.d("All Events: ",todayEventList.toString() + tomorrowEventList.toString() + thisWeekEventList.toString() + thisMonthEventList.toString() + laterEventList.toString());
                        setTabListeners();
                        navBarBtm.setVisibility(View.VISIBLE);
                        mapProgressBar.setVisibility(View.INVISIBLE);

                    }

                } else {

                    String error = task.getException().getMessage();
                    Toast.makeText(MainActivity.this, "Load Error: " + error, Toast.LENGTH_LONG).show();

                }

            }
        });
    }


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
        Intent logoutIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(logoutIntent);
        finish();
    }

    //** Map Marker Behavior

    private void addMarkerToMap(Double lat, Double lon, String key){
        LatLng latLng = new LatLng(lat, lon);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.snippet(key);

        if (eventsToday){
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        } else if (eventsTomorrow){
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
        } else if (eventsThisWeek){
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        } else if (eventsThisMonth){
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        } else if (eventsLater){
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        } else {
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        }

        mMap.addMarker(markerOptions);
    }

    private void moveCameraToPosition(LatLng latLng){
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
    }

    //Start Activity By Key
    private void startActivityFromKey(String key){
        WebblenEvent eventClicked = findEventByKey(key);
        Intent infoIntent = new Intent(MainActivity.this, EventInfoActivity.class);
        infoIntent.putExtra("selectedEvent", eventClicked);
        startActivity(infoIntent);
    }

    //Find Event By Key
    private WebblenEvent findEventByKey(String eventKey){
        WebblenEvent selectedEvent = new WebblenEvent();
        if (eventsToday){
            for (WebblenEvent event : todayEventList){
                String key = event.getEventKey();
                if (eventKey.contains(key)){
                    selectedEvent = event;
                }
            }
        } else if (eventsTomorrow){
            for (WebblenEvent event : tomorrowEventList){
                String key = event.getEventKey();
                if (eventKey.contains(key)){
                    selectedEvent = event;
                }
            }
        } else if (eventsThisWeek){
            for (WebblenEvent event : thisWeekEventList){
                String key = event.getEventKey();
                if (eventKey.contains(key)){
                    selectedEvent = event;
                }
            }
        } else if (eventsThisMonth){
            for (WebblenEvent event : thisMonthEventList){
                String key = event.getEventKey();
                if (eventKey.contains(key)){
                    selectedEvent = event;
                }
            }
        } else if (eventsLater){
            for (WebblenEvent event : laterEventList){
                String key = event.getEventKey();
                if (eventKey.contains(key)){
                    selectedEvent = event;
                }
            }
        }

        return selectedEvent;
    }

    //Organize Event Days & Distances
    private void organizeByDate(WebblenEvent event){
        String date = event.getDate();
        try {
            eventDate = sourceFormat.parse(date);
            if (eventDate.compareTo(currentDate) < 0) {
                firebaseFirestore.collection("events").document(event.getEventKey()).delete();
            } else if (eventDate.compareTo(currentDate) == 0) {
                if (!todayEventList.contains(event)) {
                    todayEventList.add(event);
                }
            } else if (Utilities.getDifferenceDays(currentDate, eventDate) == 1) {
                if (!tomorrowEventList.contains(event)) {
                    tomorrowEventList.add(event);
                }
            } else if (Utilities.getDifferenceDays(currentDate, eventDate) > 1 && Utilities.getDifferenceDays(currentDate, eventDate) <= 7) {
                if (!thisWeekEventList.contains(event)) {
                    thisWeekEventList.add(event);
                }
            } else if (Utilities.getDifferenceDays(currentDate, eventDate) > 7 && Utilities.getDifferenceDays(currentDate, eventDate) <= 30) {
                if (!thisMonthEventList.contains(event)) {
                    thisMonthEventList.add(event);
                }
            } else {
                if (!laterEventList.contains(event)) {
                    laterEventList.add(event);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    //Set Listeners for Tabs
    private void setTabListeners(){
        //TAB LISTENERS
        todayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventsToday = true;
                eventsTomorrow = false;
                eventsThisWeek = false;
                eventsThisMonth = false;
                eventsLater = false;

                if (todayEventList.isEmpty() || todayEventList == null){
                    Toast.makeText(MainActivity.this, "No Events for Today Found", Toast.LENGTH_SHORT).show();
                } else {
                    mMap.clear();
                    float distanceFromEvent = 99999999;
                    for (WebblenEvent event : todayEventList){
                        addMarkerToMap(event.getLat(), event.getLon(), event.getEventKey());
                        if (currentLocation != null){
                            //Grab Distance
                            Location eventLocation = new Location("");
                            eventLocation.setLatitude(event.getLat());
                            eventLocation.setLongitude(event.getLon());
                            float comparedDistance = currentLocation.distanceTo(eventLocation);
                            Log.d("COMPARED DISTANCE Later: ", String.valueOf(comparedDistance));
                            if (comparedDistance < distanceFromEvent){
                                distanceFromEvent = comparedDistance;
                                closestToday = event;
                            }
                        }
                    }

                    if (closestToday != null){
                        LatLng latLng = new LatLng(closestToday.getLat(), closestToday.getLon());
                        moveCameraToPosition(latLng);
                    } else {
                        LatLng latLng = new LatLng(todayEventList.get(0).getLat(), todayEventList.get(0).getLon());
                        moveCameraToPosition(latLng);
                        Toast.makeText(MainActivity.this, "Could Not Retrieve Location", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        tomorrowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventsToday = false;
                eventsTomorrow = true;
                eventsThisWeek = false;
                eventsThisMonth = false;
                eventsLater = false;


                if (tomorrowEventList.isEmpty() || tomorrowEventList == null){
                    Toast.makeText(MainActivity.this, "No Events for Tomorrow Found", Toast.LENGTH_SHORT).show();
                } else {
                    mMap.clear();
                    float distanceFromEvent = 99999999;
                    for (WebblenEvent event : tomorrowEventList){
                        addMarkerToMap(event.getLat(), event.getLon(), event.getEventKey());
                        if (currentLocation != null){
                            //Grab Distance
                            Location eventLocation = new Location("");
                            eventLocation.setLatitude(event.getLat());
                            eventLocation.setLongitude(event.getLon());
                            float comparedDistance = currentLocation.distanceTo(eventLocation);
                            Log.d("COMPARED DISTANCE Later: ", String.valueOf(comparedDistance));
                            if (comparedDistance < distanceFromEvent){
                                distanceFromEvent = comparedDistance;
                                closestTomorrow = event;
                            }
                        }
                    }

                    if (closestTomorrow != null){
                        LatLng latLng = new LatLng(closestTomorrow.getLat(), closestTomorrow.getLon());
                        moveCameraToPosition(latLng);
                    } else {
                        LatLng latLng = new LatLng(tomorrowEventList.get(0).getLat(), tomorrowEventList.get(0).getLon());
                        moveCameraToPosition(latLng);
                        Toast.makeText(MainActivity.this, "Could Not Retrieve Location", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        thisWeekBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventsToday = false;
                eventsTomorrow = false;
                eventsThisWeek = true;
                eventsThisMonth = false;
                eventsLater = false;


                if (thisWeekEventList.isEmpty() || thisWeekEventList == null){
                    Toast.makeText(MainActivity.this, "No Events for This Week Found", Toast.LENGTH_SHORT).show();
                } else {
                    mMap.clear();
                    float distanceFromEvent = 99999999;
                    for (WebblenEvent event : thisWeekEventList){
                        addMarkerToMap(event.getLat(), event.getLon(), event.getEventKey());

                        if (currentLocation != null){
                            //Grab Distance
                            Location eventLocation = new Location("");
                            eventLocation.setLatitude(event.getLat());
                            eventLocation.setLongitude(event.getLon());
                            float comparedDistance = currentLocation.distanceTo(eventLocation);
                            Log.d("COMPARED DISTANCE Later: ", String.valueOf(comparedDistance));
                            if (comparedDistance < distanceFromEvent){
                                distanceFromEvent = comparedDistance;
                                closestThisWeek = event;
                            }
                        }

                    }

                    if (closestThisWeek != null){
                        LatLng latLng = new LatLng(closestThisWeek.getLat(), closestThisWeek.getLon());
                        moveCameraToPosition(latLng);
                    } else {
                        LatLng latLng = new LatLng(thisWeekEventList.get(0).getLat(), thisWeekEventList.get(0).getLon());
                        moveCameraToPosition(latLng);
                        Toast.makeText(MainActivity.this, "Could Not Retrieve Location", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        thisMnthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventsToday = false;
                eventsTomorrow = false;
                eventsThisWeek = false;
                eventsThisMonth = true;
                eventsLater = false;

                if (thisMonthEventList.isEmpty() ||thisMonthEventList == null){
                    Toast.makeText(MainActivity.this, "No Events for This Month Found", Toast.LENGTH_SHORT).show();
                } else {
                    mMap.clear();
                    float distanceFromEvent = 99999999;
                    for (WebblenEvent event : thisMonthEventList){
                        addMarkerToMap(event.getLat(), event.getLon(), event.getEventKey());
                        if (currentLocation != null){
                            //Grab Distance
                            Location eventLocation = new Location("");
                            eventLocation.setLatitude(event.getLat());
                            eventLocation.setLongitude(event.getLon());
                            float comparedDistance = currentLocation.distanceTo(eventLocation);
                            Log.d("COMPARED DISTANCE This Month: ", String.valueOf(comparedDistance));
                            if (comparedDistance < distanceFromEvent){
                                distanceFromEvent = comparedDistance;
                                closestThisMonth = event;
                            }
                        }
                    }
                    if (closestThisMonth != null){
                        LatLng latLng = new LatLng(closestThisMonth.getLat(), closestThisMonth.getLon());
                        moveCameraToPosition(latLng);
                    } else {
                        LatLng latLng = new LatLng(thisMonthEventList.get(0).getLat(), thisMonthEventList.get(0).getLon());
                        moveCameraToPosition(latLng);
                        Toast.makeText(MainActivity.this, "Could Not Retrieve Location", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        laterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventsToday = false;
                eventsTomorrow = false;
                eventsThisWeek = false;
                eventsThisMonth = false;
                eventsLater = true;


                if (laterEventList.isEmpty() || laterEventList == null){
                    Toast.makeText(MainActivity.this, "No Events for Later Found", Toast.LENGTH_SHORT).show();
                } else {
                    mMap.clear();
                    float distanceFromEvent = 99999999;
                    for (WebblenEvent event : laterEventList){
                        addMarkerToMap(event.getLat(), event.getLon(), event.getEventKey());

                        if (currentLocation != null){
                            //Grab Distance
                            Location eventLocation = new Location("");
                            eventLocation.setLatitude(event.getLat());
                            eventLocation.setLongitude(event.getLon());
                            float comparedDistance = currentLocation.distanceTo(eventLocation);
                            Log.d("COMPARED DISTANCE Later: ", String.valueOf(comparedDistance));
                            if (comparedDistance < distanceFromEvent){
                                distanceFromEvent = comparedDistance;
                                closestLater = event;
                            }
                        }
                    }

                    if (closestLater != null){
                        LatLng latLng = new LatLng(closestLater.getLat(), closestLater.getLon());
                        moveCameraToPosition(latLng);
                    } else {
                        LatLng latLng = new LatLng(laterEventList.get(0).getLat(), laterEventList.get(0).getLon());
                        moveCameraToPosition(latLng);
                        Toast.makeText(MainActivity.this, "Could Not Retrieve Location", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

}
