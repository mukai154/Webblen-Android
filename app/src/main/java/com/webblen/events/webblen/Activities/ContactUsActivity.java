package com.webblen.events.webblen.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.webblen.events.webblen.R;

public class ContactUsActivity extends AppCompatActivity {

    private EditText suggestionBox;
    private EditText emailText;
    private Button submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        //UI Fields
        suggestionBox = (EditText) findViewById(R.id.suggestionBox);
        emailText = (EditText) findViewById(R.id.emailTextBox);
        submitBtn = (Button) findViewById(R.id.submitBtn);
    }
}
