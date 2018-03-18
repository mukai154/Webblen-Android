package com.webblen.events.webblen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    //Firebase
    private FirebaseAuth mAuth;

    //UI
    private EditText registerEmail;
    private EditText registerPassword;
    private EditText confirmRegisterPassword;
    private Button sendRegisterData;
    private TextView termsAndConditionsPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //INITIALIZE

        registerEmail = (EditText) findViewById(R.id.registerEmail);
        registerPassword = (EditText) findViewById(R.id.registerPassword);
        confirmRegisterPassword = (EditText) findViewById(R.id.confirmRegisterPassword);
        termsAndConditionsPath = (TextView) findViewById(R.id.termsConditionsPath);


    }
}
