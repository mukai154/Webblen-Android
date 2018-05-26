package com.webblen.events.webblen.Recycler_Views;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.webblen.events.webblen.R;
import com.webblen.events.webblen.Utilities;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by codelation on 3/31/18.
 */

public class EventViewHolder extends RecyclerView.ViewHolder {

    private View mView;

    private ImageView eventCardImg;
    private CircleImageView eventCardAuthorImg;
    private TextView eventCardAuthName;
    private TextView eventCardTitle;
    private TextView eventCardDay;
    private TextView eventCardMonth;
    private TextView eventCardYear;
    private TextView eventCardDescription;
    private ImageView eventCardInt1;
    private ImageView eventCardInt2;
    private ImageView eventCardInt3;
    private TextView eventCardViews;



    public EventViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
    }

    public void setEventCardImg(String pathToImage) {
        eventCardImg = mView.findViewById(R.id.eventCardImg);
        Uri imageUri = Uri.parse(pathToImage);
        //Glide.with(context).load(pathToImage).into(eventCardImg);
    }

    public void setEventCardAuthorImg(String profilePic) {
        eventCardAuthorImg = mView.findViewById(R.id.eventCardAuthorImg);
        Uri imageUri = Uri.parse(profilePic);
        //Glide.with(context).load(imageUri).into(eventCardAuthorImg);
    }

    public void setEventCardAuthName(String eventAuthor) {
        eventCardAuthName = mView.findViewById(R.id.eventCardAuthName);
        eventCardAuthName.setText(eventAuthor);
    }

    public void setEventCardTitle(String eventTitle) {
        eventCardTitle = mView.findViewById(R.id.eventCardTitle);
        eventCardTitle.setText(eventTitle);
    }


    public void setEventCardDescription(String descriptionText) {
        eventCardDescription = mView.findViewById(R.id.eventCardDescription);
        eventCardDescription.setText(descriptionText);
    }

    public void setEventCardInt1(String interest1Image) {
        eventCardInt1 = mView.findViewById(R.id.eventCardInt1);
        eventCardInt1.setImageResource(Utilities.categoryToDrawable(interest1Image.toUpperCase()));
    }

    public void setEventCardInt2(String interest2Image) {
        eventCardInt2 = mView.findViewById(R.id.eventCardInt2);
        eventCardInt2.setImageResource(Utilities.categoryToDrawable(interest2Image.toUpperCase()));
    }

    public void setEventCardInt3(String interest3Image) {
        eventCardInt3 =  mView.findViewById(R.id.eventCardInt3);
        eventCardInt3.setImageResource(Utilities.categoryToDrawable(interest3Image.toUpperCase()));

    }

    public void setEventCardViews(int eventViews) {
        eventCardViews = mView.findViewById(R.id.eventCardViews);
        eventCardViews.setText(String.valueOf(eventViews));
    }
}
