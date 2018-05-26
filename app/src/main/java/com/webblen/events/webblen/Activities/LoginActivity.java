package com.webblen.events.webblen.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.TwitterAuthProvider;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.webblen.events.webblen.R;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {


    //Firebase
    private FirebaseAuth mAuth;

    //Facebook
    private Button fbLoginBtn;
    private CallbackManager fbCallbackManager;

    //Twitter
    private TwitterAuthClient mTwitterAuthClient;
    private Button twitterLoginBtn;

    //UI Actions
    private Button loginBtn;
    private EditText email;
    private EditText password;
    private TextView termsAndConditions;
    private Button registerIntentBtn;
    private ProgressBar loginProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Configure Twitter SDK
        TwitterAuthConfig authConfig = new TwitterAuthConfig(
                getString(R.string.twitter_key),
                getString(R.string.twitter_secret));

        TwitterConfig twitterConfig = new TwitterConfig.Builder(this)
                .twitterAuthConfig(authConfig)
                .build();

        Twitter.initialize(twitterConfig);


        setContentView(R.layout.activity_login);

        //** INITIALIZE

        //Firebase
        mAuth = FirebaseAuth.getInstance();

        //Facebook
        fbLoginBtn = (Button) findViewById(R.id.fbLoginBtn);
        fbCallbackManager = CallbackManager.Factory.create();

        //Twitter
        mTwitterAuthClient = new TwitterAuthClient();
        twitterLoginBtn = (Button) findViewById(R.id.twitterLoginBtn);

        //UI
        loginProgressBar = (ProgressBar) findViewById(R.id.loginProgressBar);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        email = (EditText) findViewById(R.id.loginEmail);
        password = (EditText) findViewById(R.id.registerPassword);
        termsAndConditions = (TextView) findViewById(R.id.termsConditionsView);
        registerIntentBtn = (Button) findViewById(R.id.registerIntentBtn);

        //** LISTENERS & CALLBACKS

        //Email Login
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String loginEmail = email.getText().toString();
                String loginPassword = password.getText().toString();

                if (formIsValid()){
                    loginProgressBar.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(loginEmail, loginPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){
                                userIsLoggedIn();
                            } else {
                                String loginErrorMessage = task.getException().getMessage();
                                Toast.makeText(LoginActivity.this, "Login Error: " + loginErrorMessage, Toast.LENGTH_LONG).show();
                            }

                            loginProgressBar.setVisibility(View.INVISIBLE);

                        }
                    });
                }
            }
        });

        //Facebook
        fbLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginProgressBar.setVisibility(View.VISIBLE);
                fbLoginBtn.setEnabled(false);
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email", "public_profile"));
                LoginManager.getInstance().registerCallback(fbCallbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("FBLog", "facebook:onSuccess:" + loginResult);
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Log.d("FBLog", "facebook:onCancel");
                        // ...
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d("FBLog", "facebook:onError", error);
                        loginProgressBar.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });

        //Twitter
        twitterLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginProgressBar.setVisibility(View.VISIBLE);
                mTwitterAuthClient.authorize(LoginActivity.this, new com.twitter.sdk.android.core.Callback<TwitterSession>(){
                    @Override
                    public void success(Result<TwitterSession> twitterSessionResult) {
                        handleTwitterSession(twitterSessionResult.data);
                    }

                    @Override
                    public void failure(TwitterException e) {
                        e.printStackTrace();
                        loginProgressBar.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });

        //Terms & Conditions
        termsAndConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent termsConditionsIntent = new Intent(LoginActivity.this, TermsConditionsActivity.class);
                startActivity(termsConditionsIntent);
            }
        });

        //Register Intent
        registerIntentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
                finish();
            }
        });

    }

    //** Authentication Methods and Handling
    @Override
    public void onStart() {
        super.onStart();
        // Check if user_profile is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null){
            userIsLoggedIn();
        }
    }

    private void userIsLoggedIn(){
        Intent loginIntent = new Intent(LoginActivity.this, DashboardActivity.class);
        startActivity(loginIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        fbCallbackManager.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result to the Twitter login button.
        mTwitterAuthClient.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("FBLog", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user_profile's information
                            Log.d("FBLog", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            fbLoginBtn.setEnabled(true);
                            userIsLoggedIn();
                        } else {
                            // If sign in fails, display a message to the user_profile.
                            Log.w("FBLOG", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            fbLoginBtn.setEnabled(true);
                        }

                    }
                });
    }

    private void handleTwitterSession(TwitterSession session) {
        Log.d("Twitter Login", "handleTwitterSession:" + session);

        AuthCredential credential = TwitterAuthProvider.getCredential(
                session.getAuthToken().token,
                session.getAuthToken().secret);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Twitter Login", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            userIsLoggedIn();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Twitter Login", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    //FORM VALIDATION
    private boolean formIsValid() {

        boolean valid = true;

        //EMAIL
        String emailInput = email.getText().toString();
        if (TextUtils.isEmpty(emailInput)) {
            email.setError("Required.");
            valid = false;
        } else {
            email.setError(null);
        }

        //PASSWORD
        String passwordInput = password.getText().toString();

        if (TextUtils.isEmpty(passwordInput)) {
            password.setError("Required.");
            valid = false;
        } else {
            password.setError(null);
        }
        return valid;
    }

}
