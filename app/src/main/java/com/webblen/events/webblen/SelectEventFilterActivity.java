package com.webblen.events.webblen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class SelectEventFilterActivity extends AppCompatActivity {

    private CheckBox event18Check;
    private boolean event18;
    private CheckBox event21Check;
    private boolean event21;
    private CheckBox notifCheck;
    private boolean notifOnly;

    private Button setFilterBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_event_filter);

        //*** INITIALIZE
        event18Check = (CheckBox) findViewById(R.id.event18Check);
        event21Check = (CheckBox) findViewById(R.id.event21Check);
        notifCheck = (CheckBox) findViewById(R.id.notificationOnlyCheck);
        setFilterBtn = (Button) findViewById(R.id.setFilterBtn);

        setFilterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (event18Check.isChecked()){
                    event18 = true;
                } else {
                    event18 = false;
                }

                if (event21Check.isChecked()){
                    event21= true;
                } else {
                    event21 = false;
                }

                if (notifCheck.isChecked()){
                    notifOnly = true;
                } else {
                    notifOnly = false;
                }
                Intent returnTagDataIntent = new Intent();
                returnTagDataIntent.putExtra("event18", event18);
                returnTagDataIntent.putExtra("event21", event21);
                returnTagDataIntent.putExtra("notifOnly", notifOnly);
                setResult(RESULT_OK, returnTagDataIntent);
                finish();
            }
        });
    }
}
