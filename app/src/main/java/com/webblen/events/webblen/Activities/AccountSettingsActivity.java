package com.webblen.events.webblen.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.webblen.events.webblen.R;

public class AccountSettingsActivity extends AppCompatActivity {

    //Button Actions
    private Button verifyAccountBtn;
    private Button accountInfoBtn;
    private Button blockedUsersBtn;
    private Button termsConditionsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        //UI Buttons
        verifyAccountBtn = (Button) findViewById(R.id.verifyAccountBtn);
        accountInfoBtn = (Button) findViewById(R.id.accountInfoBtn);
        blockedUsersBtn = (Button) findViewById(R.id.blockedUsersBtn);
        termsConditionsBtn = (Button) findViewById(R.id.termsAndConditionsBtn);
    }

}
