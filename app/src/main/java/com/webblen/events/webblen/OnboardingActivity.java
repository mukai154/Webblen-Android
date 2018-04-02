package com.webblen.events.webblen;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class OnboardingActivity extends AppCompatActivity {

    private ViewPager slideViewPager;
    private LinearLayout dotsLayout;
    private SliderAdapter slideAdapter;

    private TextView[] dots;
    private FirebaseAuth firebaseAuth;

    private Button nextBtn;
    private Button backBtn;

    private int currentPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        //Firebase
        firebaseAuth = FirebaseAuth.getInstance();

        //Layout Initialization
        slideViewPager = (ViewPager) findViewById(R.id.slideViewPager);
        dotsLayout = (LinearLayout) findViewById(R.id.dotsLayout);
        nextBtn = (Button) findViewById(R.id.nextBtn);
        backBtn = (Button) findViewById(R.id.backBtn);

        slideAdapter = new SliderAdapter(this);

        slideViewPager.setAdapter(slideAdapter);
        addDotsIndicator(0);
        slideViewPager.addOnPageChangeListener(pageChangeListener);

        //Button Listeners
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pageCount = dots.length - 1;
                Intent loginIntent = new Intent(OnboardingActivity.this, LoginActivity.class);
                if (currentPage == pageCount){
                    startActivity(loginIntent);
                    finish();
                }
                else {
                    slideViewPager.setCurrentItem(currentPage + 1);
                }
            }
        });
        
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideViewPager.setCurrentItem(currentPage - 1);
            }
        });
    }

    public void addDotsIndicator(int position){

        dots = new TextView[5];
        dotsLayout.removeAllViews();

        for(int i = 0; i < dots.length; i++){
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(30);
            dots[i].setTextColor(getResources().getColor(R.color.colorWhite));

            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0){
            dots[position].setTextColor(getResources().getColor(R.color.colorLightGray));
        }
    }

    ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int i) {
            addDotsIndicator(i);
            currentPage = i;

            if (i == 0){
                nextBtn.setEnabled(true);
                backBtn.setEnabled(false);
                backBtn.setVisibility(View.INVISIBLE);

                nextBtn.setText("Next");
                backBtn.setText("");
            }
            else if (i == dots.length - 1){
                nextBtn.setEnabled(true);
                backBtn.setEnabled(true);
                backBtn.setVisibility(View.VISIBLE);

                nextBtn.setText("Start");
                backBtn.setText("Back");
            }
            else {
                nextBtn.setEnabled(true);
                backBtn.setEnabled(true);
                backBtn.setVisibility(View.VISIBLE);

                nextBtn.setText("Next");
                backBtn.setText("Back");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    //** Authentication Methods and Handling
    @Override
    public void onStart(){
        //Check if Firebase user is signed in and act accordingly
        super.onStart();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null){
            loginUser();
        }

    }

    private void loginUser(){
        Intent loginIntent = new Intent(OnboardingActivity.this, MainActivity.class);
        startActivity(loginIntent);
        finish();
    }

}
