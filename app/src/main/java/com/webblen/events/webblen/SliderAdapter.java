package com.webblen.events.webblen;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Webblen, LLC on 2/20/18.
 */

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context){
        this.context = context;
    }

    //Arrays for Onboarding Slide Information
    public int[] slide_images = {
            R.drawable.cityscape,
            R.drawable.screen,
            R.drawable.team,
            R.drawable.coins,
            R.drawable.phone_city
    };

    public String[] slide_headings = {
            "Welcome to Webblen!",
            "Be Involved",
            "Connect with Others",
            "Earn Rewards",
            "Get Started!"
    };

    public String[] slide_descriptions = {
            "The World's First Incentivized Community Building Platform",
            "Know About the Different Events Happening Around You According to Your Interests and Location",
            "Meet and Connect With Others That Have Similar Interests as Your Own",
            "The More Involved You are, the More Money You Earn Through Cryptocurrencies",
            "What are You Waiting for? Get Involved, Make Some Money, and Watch Your Community Grow!"
    };

    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (RelativeLayout) object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position){
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.onboarding_slide_layout, container, false);

        ImageView slideImageView = (ImageView) view.findViewById(R.id.slideImage);
        TextView slideHeader = (TextView) view.findViewById(R.id.slideHeader);
        TextView slideBody = (TextView) view.findViewById(R.id.slideBody);

        slideImageView.setImageResource(slide_images[position]);
        slideHeader.setText(slide_headings[position]);
        slideBody.setText(slide_descriptions[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        container.removeView((RelativeLayout)object);
    }
}
