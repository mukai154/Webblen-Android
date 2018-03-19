package com.webblen.events.webblen;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    //Firebase
    private FirebaseAuth mAuth;

    //UI
    private ProgressBar registerProgressBar;
    private EditText registerEmail;
    private EditText registerPassword;
    private EditText confirmRegisterPassword;
    private Button sendRegisterData;
    private TextView termsAndConditionsPath;
    private Button loginIntentBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //** INITIALIZE

        //Firebase
        mAuth = FirebaseAuth.getInstance();

        //UI
        registerProgressBar = (ProgressBar) findViewById(R.id.registerProgressBar);
        registerEmail = (EditText) findViewById(R.id.registerEmail);
        registerPassword = (EditText) findViewById(R.id.registerPassword);
        confirmRegisterPassword = (EditText) findViewById(R.id.confirmRegisterPassword);
        termsAndConditionsPath = (TextView) findViewById(R.id.termsConditionsPath);
        sendRegisterData = (Button) findViewById(R.id.registerBtn);
        loginIntentBtn = (Button) findViewById(R.id.loginIntentBtn);

        //Send Registration
        sendRegisterData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newEmail = registerEmail.getText().toString();
                String newPassword = registerPassword.getText().toString();

                if (formIsValid()){
                    registerProgressBar.setVisibility(View.VISIBLE);
                    mAuth.createUserWithEmailAndPassword(newEmail, newPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){
                                Intent setupIntent = new Intent(RegisterActivity.this, SetupActivity.class);
                                startActivity(setupIntent);
                                finish();
                            } else {
                                String errorMessage = task.getException().getMessage();
                                Toast.makeText(RegisterActivity.this, "Error : " + errorMessage, Toast.LENGTH_LONG).show();
                            }

                            registerProgressBar.setVisibility(View.INVISIBLE);

                        }
                    });
                }
            }
        });

        //Terms & Conditions
        termsAndConditionsPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent termsConditionsIntent = new Intent(RegisterActivity.this, TermsConditionsActivity.class);
                startActivity(termsConditionsIntent);
            }
        });

        //Login Path
        loginIntentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }
        });

    }

    //FORM VALIDATION
    private boolean formIsValid() {

        boolean valid = true;

        //EMAIL
        String emailInput = registerEmail.getText().toString();
        if (TextUtils.isEmpty(emailInput)) {
            registerEmail.setError("Required.");
            valid = false;
        } else {
            registerEmail.setError(null);
        }

        //PASSWORD
        String passwordInput = registerPassword.getText().toString();

        if (TextUtils.isEmpty(passwordInput)) {
            registerPassword.setError("Required.");
            valid = false;
        } else {
            registerPassword.setError(null);
        }

        if (passwordInput.length() < 8 || passwordInput.length() > 15){
            registerPassword.setError("Password Must Be 8-15 Characters");
            valid = false ;
        } else {
            registerPassword.setError(null);
        }

        //CONFIRM PASSWORD
        String confirmPassInput = confirmRegisterPassword.getText().toString();

        if (TextUtils.isEmpty(confirmPassInput)) {
            confirmRegisterPassword.setError("Required.");
            valid = false;
        } else {
            confirmRegisterPassword.setError(null);
        }

        if (confirmPassInput.length() < 8 || confirmPassInput.length() > 15){
            confirmRegisterPassword.setError("Password Must Be 8-15 Characters");
            valid = false ;
        } else {
            confirmRegisterPassword.setError(null);
        }

        //PASSWORDS MATCH
        if(!passwordInput.equals(confirmPassInput)){
            registerPassword.setError("Passwords Do Not Match");
            confirmRegisterPassword.setError("Passwords Do Not Match");
            valid = false;
        } else {
            registerPassword.setError(null);
            confirmRegisterPassword.setError(null);
        }

        return valid;
    }
}
