package com.webblen.events.webblen;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class SetupActivity extends AppCompatActivity {

    //Firebase
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String user_id;
    private boolean user_exists;

    //Image & Name
    private CircleImageView setupProfilePic;
    private Uri mainImageURI = null;
    private Bitmap compressedImageFile;
    private EditText setupUsername;

    private boolean didChange = false;

    //Setup Progress
    private Button completeSetup;
    private ProgressBar setupProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        //** INITIALIZE

        //Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        //Other UI
        setupProfilePic = (CircleImageView) findViewById(R.id.setupProfilePic);
        setupUsername = (EditText) findViewById(R.id.setupUsername);
        completeSetup = (Button) findViewById(R.id.completeSetupBtn);
        setupProgress = (ProgressBar) findViewById(R.id.setupProgressBar);


        //** LISTENERS
        setupProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(ContextCompat.checkSelfPermission(SetupActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(SetupActivity.this, "App Does Not Have Permission to Access Phone Storage", Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(SetupActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    } else {
                        startImagePicker();
                    }
                } else {
                    startImagePicker();
                }
            }
        });

        completeSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String user_name = setupUsername.getText().toString();

                if (TextUtils.isEmpty(user_name)){
                    Toast.makeText(SetupActivity.this, "Username Required", Toast.LENGTH_LONG).show();
                } else if (user_name.length() > 20){
                    Toast.makeText(SetupActivity.this, "Username Cannot Be More Than 20 Characters Long", Toast.LENGTH_LONG).show();
                } else if (checkIfUsernameExists(user_name)){
                    Toast.makeText(SetupActivity.this, "Username Not Available", Toast.LENGTH_LONG).show();
                } else if (mainImageURI == null){
                    Toast.makeText(SetupActivity.this, "Profile Pic Required", Toast.LENGTH_LONG).show();
                } else {

                    //Compress and Upload
                    setupProgress.setVisibility(View.VISIBLE);

                    //Compress Image
                    File profileImgFile = new File(mainImageURI.getPath());
                    try {
                        compressedImageFile = new Compressor(SetupActivity.this).setQuality(85).compressToBitmap(profileImgFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] imgData = baos.toByteArray();

                    //Upload Img & Name
                    UploadTask setupUpload = storageReference.child("profile_pics").child(user_id + ".jpg").putBytes(imgData);

                    setupUpload.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri download_uri = taskSnapshot.getDownloadUrl();
                            storeToFirestore(user_name, download_uri.toString());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            String error = e.getMessage();
                            Toast.makeText(SetupActivity.this, "(Setup Error) : " + error, Toast.LENGTH_LONG).show();

                            setupProgress.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }
        });
    }

    private boolean checkIfUsernameExists(String user_name){
        user_name = user_name.toLowerCase().replaceAll("\\s+","");
        DocumentReference docRef = firebaseFirestore.collection("usernames").document(user_name);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        user_exists = true;
                    } else {
                        user_exists = false;
                    }
                } else {
                    user_exists = true;
                    Toast.makeText(SetupActivity.this, "There Was an Issue Setting Up Your Account. Please Try Again.", Toast.LENGTH_LONG).show();
                }
            }
        });

        return user_exists;
    }

    private void storeToFirestore(final String user_name, String image_uri){

        //Set Username & Profile Pic Path
        Map<String, Object> userDocData = new HashMap<>();
        userDocData.put("username", user_name);
        userDocData.put("uid", user_id);
        userDocData.put("profile_pic", image_uri);
        userDocData.put("isOver18", false);
        userDocData.put("isOver21", false);
        userDocData.put("isVerified", false);
        userDocData.put("blockedUsers", null);

        //Set all interests to true by default
        Map<String, Object> nestedUserData = new HashMap<>();
        nestedUserData.put("AMUSEMENT", true);
        nestedUserData.put("ART", true);
        nestedUserData.put("COLLEGELIFE", true);
        nestedUserData.put("COMMUNITY", true);
        nestedUserData.put("COMPETITION", true);
        nestedUserData.put("CULTURE", true);
        nestedUserData.put("EDUCATION", true);
        nestedUserData.put("ENTERTAINMENT", true);
        nestedUserData.put("FAMILY", true);
        nestedUserData.put("FOODDRINK", true);
        nestedUserData.put("GAMING", true);
        nestedUserData.put("HEALTHFITNESS", true);
        nestedUserData.put("MUSIC", true);
        nestedUserData.put("NETWORKING", true);
        nestedUserData.put("OUTDOORS", true);
        nestedUserData.put("PARTYDANCE", true);
        nestedUserData.put("SHOPPING", true);
        nestedUserData.put("SPORTS", true);
        nestedUserData.put("TECHNOLOGY", true);
        nestedUserData.put("THEATRE", true);
        nestedUserData.put("WINEBREW", true);

        userDocData.put("interests", nestedUserData);

        //Send to Firebase
        firebaseFirestore.collection("users").document(user_id)
                .set(userDocData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        // *** Add username to db
                        Map<String, Object> usernameData = new HashMap<>();
                        usernameData.put("username", user_id);

                        firebaseFirestore.collection("usernames").document(user_name)
                                .set(usernameData)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Intent mainIntent = new Intent(SetupActivity.this, MainActivity.class);
                                        startActivity(mainIntent);
                                        finish();
                                    }
                                });
                        // ***
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SetupActivity.this, "There Was an Issue Setting Up Your Account. Please Try Again.", Toast.LENGTH_LONG).show();
                    }
                });

    }

    private void startImagePicker(){
        // start picker to get image for cropping and then use the image in cropping activity
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .start(SetupActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mainImageURI = result.getUri();
                setupProfilePic.setImageURI(mainImageURI);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
